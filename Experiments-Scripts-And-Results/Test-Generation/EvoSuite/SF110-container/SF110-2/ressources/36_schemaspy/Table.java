package net.sourceforge.schemaspy.model;

import net.sourceforge.schemaspy.util.*;
import net.sourceforge.schemaspy.*;
import java.util.regex.*;
import java.sql.*;
import java.util.*;
import net.sourceforge.schemaspy.model.xml.*;
import java.util.logging.*;

public class Table implements Comparable<Table>
{
    private final String catalog;
    private final String schema;
    private final String name;
    private final String fullName;
    private final String container;
    protected final CaseInsensitiveMap<TableColumn> columns;
    private final List<TableColumn> primaryKeys;
    private final CaseInsensitiveMap<ForeignKeyConstraint> foreignKeys;
    private final CaseInsensitiveMap<TableIndex> indexes;
    private Object id;
    private final Map<String, String> checkConstraints;
    private Long numRows;
    protected final Database db;
    private String comments;
    private int maxChildren;
    private int maxParents;
    private static final Logger logger;
    private static final boolean fineEnabled;
    private static final boolean finerEnabled;
    
    public Table(final Database db, final String catalog, final String schema, final String name, final String comments) throws SQLException {
        this.columns = new CaseInsensitiveMap<TableColumn>();
        this.primaryKeys = new ArrayList<TableColumn>();
        this.foreignKeys = new CaseInsensitiveMap<ForeignKeyConstraint>();
        this.indexes = new CaseInsensitiveMap<TableIndex>();
        this.checkConstraints = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        this.db = db;
        this.catalog = catalog;
        this.schema = schema;
        this.container = ((schema != null) ? schema : ((catalog != null) ? catalog : db.getName()));
        this.name = name;
        this.fullName = getFullName(db.getName(), catalog, schema, name);
        if (Table.fineEnabled) {
            Table.logger.fine("Creating " + this.getClass().getSimpleName() + " " + this.fullName);
        }
        this.setComments(comments);
        this.initColumns();
        this.initIndexes();
        this.initPrimaryKeys();
    }
    
    public void connectForeignKeys(final Map<String, Table> tables) throws SQLException {
        if (Table.finerEnabled) {
            Table.logger.finer("Connecting foreign keys to " + this.getFullName());
        }
        ResultSet rs = null;
        try {
            rs = this.db.getMetaData().getImportedKeys(this.getCatalog(), this.getSchema(), this.getName());
            while (rs.next()) {
                this.addForeignKey(rs.getString("FK_NAME"), rs.getString("FKCOLUMN_NAME"), rs.getString("PKTABLE_CAT"), rs.getString("PKTABLE_SCHEM"), rs.getString("PKTABLE_NAME"), rs.getString("PKCOLUMN_NAME"), rs.getInt("UPDATE_RULE"), rs.getInt("DELETE_RULE"), tables);
            }
        }
        finally {
            if (rs != null) {
                rs.close();
            }
        }
        if (this.getSchema() == null) {
            if (this.getCatalog() == null) {
                return;
            }
        }
        try {
            rs = this.db.getMetaData().getExportedKeys(this.getCatalog(), this.getSchema(), this.getName());
            while (rs.next()) {
                final String otherCatalog = rs.getString("FKTABLE_CAT");
                final String otherSchema = rs.getString("FKTABLE_SCHEM");
                if (!String.valueOf(this.getSchema()).equals(String.valueOf(otherSchema)) || !String.valueOf(this.getCatalog()).equals(String.valueOf(otherCatalog))) {
                    this.db.addRemoteTable(otherCatalog, otherSchema, rs.getString("FKTABLE_NAME"), this.getSchema(), false);
                }
            }
        }
        finally {
            if (rs != null) {
                rs.close();
            }
        }
    }
    
    public Collection<ForeignKeyConstraint> getForeignKeys() {
        return Collections.unmodifiableCollection(this.foreignKeys.values());
    }
    
    public void addCheckConstraint(final String constraintName, final String text) {
        this.checkConstraints.put(constraintName, text);
    }
    
    protected void addForeignKey(final String fkName, final String fkColName, final String pkCatalog, final String pkSchema, final String pkTableName, final String pkColName, final int updateRule, final int deleteRule, final Map<String, Table> tables) throws SQLException {
        if (fkName == null) {
            return;
        }
        final Pattern include = Config.getInstance().getTableInclusions();
        final Pattern exclude = Config.getInstance().getTableExclusions();
        if (!include.matcher(pkTableName).matches() || exclude.matcher(pkTableName).matches()) {
            if (Table.fineEnabled) {
                Table.logger.fine("Ignoring " + getFullName(this.db.getName(), pkCatalog, pkSchema, pkTableName) + " referenced by FK " + fkName);
            }
            return;
        }
        ForeignKeyConstraint foreignKey = this.foreignKeys.get(fkName);
        if (foreignKey == null) {
            foreignKey = new ForeignKeyConstraint(this, fkName, updateRule, deleteRule);
            this.foreignKeys.put(fkName, foreignKey);
        }
        final TableColumn childColumn = this.getColumn(fkColName);
        if (childColumn != null) {
            foreignKey.addChildColumn(childColumn);
            final Config config = Config.getInstance();
            Table parentTable = tables.get(pkTableName);
            final String parentContainer = (pkSchema != null) ? pkSchema : ((pkCatalog != null) ? pkCatalog : this.db.getName());
            final String baseContainer = (config.getSchema() != null) ? config.getSchema() : ((config.getCatalog() != null) ? config.getCatalog() : this.db.getName());
            if (parentTable == null || !baseContainer.equals(parentContainer)) {
                if (Table.fineEnabled) {
                    Table.logger.fine("Adding remote table " + getFullName(this.db.getName(), pkCatalog, pkSchema, pkTableName));
                }
                parentTable = this.db.addRemoteTable(pkCatalog, pkSchema, pkTableName, baseContainer, false);
            }
            if (parentTable != null) {
                final TableColumn parentColumn = parentTable.getColumn(pkColName);
                if (parentColumn != null) {
                    foreignKey.addParentColumn(parentColumn);
                    childColumn.addParent(parentColumn, foreignKey);
                    parentColumn.addChild(childColumn, foreignKey);
                }
                else {
                    Table.logger.warning("Couldn't add FK '" + foreignKey.getName() + "' to table '" + this + "' - Column '" + pkColName + "' doesn't exist in table '" + parentTable + "'");
                }
            }
            else {
                Table.logger.warning("Couldn't add FK '" + foreignKey.getName() + "' to table '" + this + "' - Unknown Referenced Table '" + pkTableName + "'");
            }
        }
        else {
            Table.logger.warning("Couldn't add FK '" + foreignKey.getName() + "' to table '" + this + "' - Column '" + fkColName + "' doesn't exist");
        }
    }
    
    private void initPrimaryKeys() throws SQLException {
        ResultSet rs = null;
        try {
            if (Table.fineEnabled) {
                Table.logger.fine("Querying primary keys for " + this.getFullName());
            }
            rs = this.db.getMetaData().getPrimaryKeys(this.getCatalog(), this.getSchema(), this.getName());
            while (rs.next()) {
                this.setPrimaryColumn(rs);
            }
        }
        catch (SQLException exc) {
            if (!this.isLogical()) {
                throw exc;
            }
        }
        finally {
            if (rs != null) {
                rs.close();
            }
        }
    }
    
    private void setPrimaryColumn(final ResultSet rs) throws SQLException {
        final String pkName = rs.getString("PK_NAME");
        if (pkName == null) {
            return;
        }
        final TableIndex index = this.getIndex(pkName);
        if (index != null) {
            index.setIsPrimaryKey(true);
        }
        final String columnName = rs.getString("COLUMN_NAME");
        this.setPrimaryColumn(this.getColumn(columnName));
    }
    
    void setPrimaryColumn(final TableColumn primaryColumn) {
        this.primaryKeys.add(primaryColumn);
    }
    
    private void initColumns() throws SQLException {
        ResultSet rs = null;
        synchronized (Table.class) {
            try {
                rs = this.db.getMetaData().getColumns(this.getCatalog(), this.getSchema(), this.getName(), "%");
                while (rs.next()) {
                    this.addColumn(rs);
                }
            }
            catch (SQLException exc) {
                if (!this.isLogical()) {
                    class ColumnInitializationFailure extends SQLException
                    {
                        private static final long serialVersionUID = 1L;
                        
                        public ColumnInitializationFailure() {
                            super("Failed to collect column details for " + (Table.this.isView() ? "view" : "table") + " '" + Table.this.getName() + "' in schema '" + Table.this.getContainer() + "'");
                            this.initCause(failure);
                        }
                    }
                    throw new ColumnInitializationFailure(exc);
                }
            }
            finally {
                if (rs != null) {
                    rs.close();
                }
            }
        }
        this.initColumnAutoUpdate(false);
    }
    
    private void initColumnAutoUpdate(final boolean forceQuotes) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        if (this.isView() || this.isRemote()) {
            return;
        }
        final StringBuilder sql = new StringBuilder("select * from ");
        if (this.getSchema() != null) {
            sql.append(this.getSchema());
            sql.append('.');
        }
        else if (this.getCatalog() != null) {
            sql.append(this.getCatalog());
            sql.append('.');
        }
        if (forceQuotes) {
            final String quote = this.db.getMetaData().getIdentifierQuoteString().trim();
            sql.append(quote + this.getName() + quote);
        }
        else {
            sql.append(this.db.getQuotedIdentifier(this.getName()));
        }
        sql.append(" where 0 = 1");
        try {
            stmt = this.db.getMetaData().getConnection().prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            final ResultSetMetaData rsMeta = rs.getMetaData();
            for (int i = rsMeta.getColumnCount(); i > 0; --i) {
                final TableColumn column = this.getColumn(rsMeta.getColumnName(i));
                column.setIsAutoUpdated(rsMeta.isAutoIncrement(i));
            }
        }
        catch (SQLException exc) {
            if (forceQuotes) {
                if (!this.isLogical()) {
                    Table.logger.warning("Failed to determine auto increment status: " + exc);
                    Table.logger.warning("SQL: " + sql.toString());
                }
            }
            else {
                this.initColumnAutoUpdate(true);
            }
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    protected void addColumn(final ResultSet rs) throws SQLException {
        final String columnName = rs.getString("COLUMN_NAME");
        if (columnName == null) {
            return;
        }
        if (this.getColumn(columnName) == null) {
            final TableColumn column = new TableColumn(this, rs);
            this.columns.put(column.getName(), column);
        }
    }
    
    protected TableColumn addColumn(final TableColumnMeta colMeta) {
        final TableColumn column = new TableColumn(this, colMeta);
        this.columns.put(column.getName(), column);
        return column;
    }
    
    private void initIndexes() throws SQLException {
        if (this.isView() || this.isRemote()) {
            return;
        }
        if (this.initIndexes(Config.getInstance().getDbProperties().getProperty("selectIndexesSql"))) {
            return;
        }
        ResultSet rs = null;
        try {
            rs = this.db.getMetaData().getIndexInfo(this.getCatalog(), this.getSchema(), this.getName(), false, true);
            while (rs.next()) {
                if (rs.getShort("TYPE") != 0) {
                    this.addIndex(rs);
                }
            }
        }
        catch (SQLException exc) {
            if (!this.isLogical()) {
                Table.logger.warning("Unable to extract index info for table '" + this.getName() + "' in schema '" + this.getContainer() + "': " + exc);
            }
        }
        finally {
            if (rs != null) {
                rs.close();
            }
        }
    }
    
    private boolean initIndexes(final String selectIndexesSql) {
        if (selectIndexesSql == null) {
            return false;
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.db.prepareStatement(selectIndexesSql, this.getName());
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getShort("TYPE") != 0) {
                    this.addIndex(rs);
                }
            }
        }
        catch (SQLException sqlException) {
            Table.logger.warning("Failed to query index information with SQL: " + selectIndexesSql);
            Table.logger.warning(sqlException.toString());
            return false;
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
        return true;
    }
    
    public TableIndex getIndex(final String indexName) {
        return this.indexes.get(indexName);
    }
    
    private void addIndex(final ResultSet rs) throws SQLException {
        final String indexName = rs.getString("INDEX_NAME");
        if (indexName == null) {
            return;
        }
        TableIndex index = this.getIndex(indexName);
        if (index == null) {
            index = new TableIndex(rs);
            this.indexes.put(index.getName(), index);
        }
        index.addColumn(this.getColumn(rs.getString("COLUMN_NAME")), rs.getString("ASC_OR_DESC"));
    }
    
    public String getCatalog() {
        return this.catalog;
    }
    
    public String getSchema() {
        return this.schema;
    }
    
    public String getContainer() {
        return this.container;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getFullName() {
        return this.fullName;
    }
    
    public static String getFullName(final String db, final String catalog, final String schema, final String table) {
        final String fullName = ((catalog == null && schema == null) ? (db + '.') : "") + ((catalog == null) ? "" : (catalog + '.')) + ((schema == null) ? "" : (schema + '.')) + table;
        return fullName;
    }
    
    public void setId(final Object id) {
        this.id = id;
    }
    
    public Object getId() {
        return this.id;
    }
    
    public Map<String, String> getCheckConstraints() {
        return this.checkConstraints;
    }
    
    public Set<TableIndex> getIndexes() {
        return new HashSet<TableIndex>(this.indexes.values());
    }
    
    public List<TableColumn> getPrimaryColumns() {
        return this.primaryKeys;
    }
    
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(final String comments) {
        String cmts = (comments == null || comments.trim().length() == 0) ? null : comments.trim();
        if (cmts != null) {
            int crapIndex = cmts.indexOf("; InnoDB free: ");
            if (crapIndex == -1) {
                crapIndex = (cmts.startsWith("InnoDB free: ") ? 0 : -1);
            }
            if (crapIndex != -1) {
                cmts = cmts.substring(0, crapIndex).trim();
                cmts = ((cmts.length() == 0) ? null : cmts);
            }
        }
        this.comments = cmts;
    }
    
    public TableColumn getColumn(final String columnName) {
        return this.columns.get(columnName);
    }
    
    public List<TableColumn> getColumns() {
        final Set<TableColumn> sorted = new TreeSet<TableColumn>(new ByColumnIdComparator());
        sorted.addAll(this.columns.values());
        return new ArrayList<TableColumn>(sorted);
    }
    
    public boolean isRoot() {
        for (final TableColumn column : this.columns.values()) {
            if (column.isForeignKey()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isLeaf() {
        for (final TableColumn column : this.columns.values()) {
            if (!column.getChildren().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public int getMaxParents() {
        return this.maxParents;
    }
    
    public void addedParent() {
        ++this.maxParents;
    }
    
    public void unlinkParents() {
        for (final TableColumn column : this.columns.values()) {
            column.unlinkParents();
        }
    }
    
    public int getMaxChildren() {
        return this.maxChildren;
    }
    
    public void addedChild() {
        ++this.maxChildren;
    }
    
    public void unlinkChildren() {
        for (final TableColumn column : this.columns.values()) {
            column.unlinkChildren();
        }
    }
    
    public ForeignKeyConstraint removeSelfReferencingConstraint() {
        return this.remove(this.getSelfReferencingConstraint());
    }
    
    private ForeignKeyConstraint remove(final ForeignKeyConstraint constraint) {
        if (constraint != null) {
            for (int i = 0; i < constraint.getChildColumns().size(); ++i) {
                final TableColumn childColumn = constraint.getChildColumns().get(i);
                final TableColumn parentColumn = constraint.getParentColumns().get(i);
                childColumn.removeParent(parentColumn);
                parentColumn.removeChild(childColumn);
            }
        }
        return constraint;
    }
    
    private ForeignKeyConstraint getSelfReferencingConstraint() {
        for (final TableColumn column : this.columns.values()) {
            for (final TableColumn parentColumn : column.getParents()) {
                if (this.compareTo(parentColumn.getTable()) == 0) {
                    return column.getParentConstraint(parentColumn);
                }
            }
        }
        return null;
    }
    
    public List<ForeignKeyConstraint> removeNonRealForeignKeys() {
        final List<ForeignKeyConstraint> nonReals = new ArrayList<ForeignKeyConstraint>();
        for (final TableColumn column : this.columns.values()) {
            for (final TableColumn parentColumn : column.getParents()) {
                final ForeignKeyConstraint constraint = column.getParentConstraint(parentColumn);
                if (constraint != null && !constraint.isReal()) {
                    nonReals.add(constraint);
                }
            }
        }
        for (final ForeignKeyConstraint constraint2 : nonReals) {
            this.remove(constraint2);
        }
        return nonReals;
    }
    
    public int getNumChildren() {
        int numChildren = 0;
        for (final TableColumn column : this.columns.values()) {
            numChildren += column.getChildren().size();
        }
        return numChildren;
    }
    
    public int getNumNonImpliedChildren() {
        int numChildren = 0;
        for (final TableColumn column : this.columns.values()) {
            for (final TableColumn childColumn : column.getChildren()) {
                if (!column.getChildConstraint(childColumn).isImplied()) {
                    ++numChildren;
                }
            }
        }
        return numChildren;
    }
    
    public int getNumParents() {
        int numParents = 0;
        for (final TableColumn column : this.columns.values()) {
            numParents += column.getParents().size();
        }
        return numParents;
    }
    
    public int getNumNonImpliedParents() {
        int numParents = 0;
        for (final TableColumn column : this.columns.values()) {
            for (final TableColumn parentColumn : column.getParents()) {
                if (!column.getParentConstraint(parentColumn).isImplied()) {
                    ++numParents;
                }
            }
        }
        return numParents;
    }
    
    public ForeignKeyConstraint removeAForeignKeyConstraint() {
        final List<TableColumn> columns = this.getColumns();
        int numParents = 0;
        int numChildren = 0;
        for (final TableColumn column : columns) {
            numParents += column.getParents().size();
            numChildren += column.getChildren().size();
        }
        for (final TableColumn column : columns) {
            ForeignKeyConstraint constraint;
            if (numParents <= numChildren) {
                constraint = column.removeAParentFKConstraint();
            }
            else {
                constraint = column.removeAChildFKConstraint();
            }
            if (constraint != null) {
                return constraint;
            }
        }
        return null;
    }
    
    public boolean isLogical() {
        return false;
    }
    
    public boolean isView() {
        return false;
    }
    
    public boolean isRemote() {
        return false;
    }
    
    public String getViewSql() {
        return null;
    }
    
    public long getNumRows() {
        if (this.numRows == null) {
            this.numRows = (Config.getInstance().isNumRowsEnabled() ? this.fetchNumRows() : -1L);
        }
        return this.numRows;
    }
    
    public void setNumRows(final long numRows) {
        this.numRows = numRows;
    }
    
    protected long fetchNumRows() {
        if (this.isView() || this.isRemote()) {
            return -1L;
        }
        SQLException originalFailure = null;
        final String sql = Config.getInstance().getDbProperties().getProperty("selectRowCountSql");
        if (sql != null) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                stmt = this.db.prepareStatement(sql, this.getName());
                rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getLong("row_count");
                }
            }
            catch (SQLException sqlException) {
                originalFailure = sqlException;
            }
            finally {
                if (rs != null) {
                    try {
                        rs.close();
                    }
                    catch (SQLException ex) {}
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    }
                    catch (SQLException ex2) {}
                }
            }
        }
        try {
            return this.fetchNumRows("count(*)", false);
        }
        catch (SQLException try2Exception) {
            try {
                return this.fetchNumRows("count(1)", false);
            }
            catch (SQLException try3Exception) {
                if (!this.isLogical()) {
                    Table.logger.warning("Unable to extract the number of rows for table " + this.getName() + ", using '-1'");
                    if (originalFailure != null) {
                        Table.logger.warning(originalFailure.toString());
                    }
                    Table.logger.warning(try2Exception.toString());
                    if (!String.valueOf(try2Exception.toString()).equals(try3Exception.toString())) {
                        Table.logger.warning(try3Exception.toString());
                    }
                }
                return -1L;
            }
        }
    }
    
    protected long fetchNumRows(final String clause, final boolean forceQuotes) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        final StringBuilder sql = new StringBuilder("select ");
        sql.append(clause);
        sql.append(" from ");
        if (this.getSchema() != null) {
            sql.append(this.getSchema());
            sql.append('.');
        }
        else if (this.getCatalog() != null) {
            sql.append(this.getCatalog());
            sql.append('.');
        }
        if (forceQuotes) {
            final String quote = this.db.getMetaData().getIdentifierQuoteString().trim();
            sql.append(quote + this.getName() + quote);
        }
        else {
            sql.append(this.db.getQuotedIdentifier(this.getName()));
        }
        try {
            if (Table.finerEnabled) {
                Table.logger.finer(sql.toString());
            }
            stmt = this.db.getConnection().prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return -1L;
        }
        catch (SQLException exc) {
            if (forceQuotes) {
                throw exc;
            }
            return this.fetchNumRows(clause, true);
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    public void update(final TableMeta tableMeta) {
        final String newComments = tableMeta.getComments();
        if (newComments != null) {
            this.comments = newComments;
        }
        for (final TableColumnMeta colMeta : tableMeta.getColumns()) {
            TableColumn col = this.getColumn(colMeta.getName());
            if (col == null) {
                col = this.addColumn(colMeta);
            }
            col.update(colMeta);
        }
    }
    
    public void connect(final TableMeta tableMeta, final Map<String, Table> tables) {
        for (final TableColumnMeta colMeta : tableMeta.getColumns()) {
            final TableColumn col = this.getColumn(colMeta.getName());
            if (col != null) {
                for (final ForeignKeyMeta fk : colMeta.getForeignKeys()) {
                    Table parent = null;
                    Label_0152: {
                        if (fk.getRemoteCatalog() == null) {
                            if (fk.getRemoteSchema() == null) {
                                parent = tables.get(fk.getTableName());
                                break Label_0152;
                            }
                        }
                        try {
                            parent = this.db.addRemoteTable(fk.getRemoteCatalog(), fk.getRemoteSchema(), fk.getTableName(), this.getContainer(), true);
                        }
                        catch (SQLException exc) {
                            parent = null;
                        }
                    }
                    if (parent != null) {
                        final TableColumn parentColumn = parent.getColumn(fk.getColumnName());
                        if (parentColumn == null) {
                            Table.logger.warning("Undefined column '" + parent.getName() + '.' + fk.getColumnName() + "' referenced by '" + col.getTable() + '.' + col + "' in XML metadata");
                        }
                        else {
                            final ForeignKeyConstraint unused = new ForeignKeyConstraint(parentColumn, col) {
                                @Override
                                public String getName() {
                                    return "Defined in XML";
                                }
                            };
                            if (parentColumn.isPrimary()) {
                                continue;
                            }
                            Table.logger.warning("Assuming " + parentColumn.getTable() + '.' + parentColumn + " is a primary key due to being referenced by " + col.getTable() + '.' + col);
                            parent.setPrimaryColumn(parentColumn);
                        }
                    }
                    else {
                        Table.logger.warning("Undefined table '" + fk.getTableName() + "' referenced by '" + this.getName() + '.' + col.getName() + "' in XML metadata");
                    }
                }
            }
            else {
                Table.logger.warning("Undefined column '" + this.getName() + '.' + colMeta.getName() + "' in XML metadata");
            }
        }
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
    public boolean isOrphan(final boolean withImpliedRelationships) {
        if (withImpliedRelationships) {
            return this.getMaxParents() == 0 && this.getMaxChildren() == 0;
        }
        for (final TableColumn column : this.columns.values()) {
            for (final TableColumn parentColumn : column.getParents()) {
                if (!column.getParentConstraint(parentColumn).isImplied()) {
                    return false;
                }
            }
            for (final TableColumn childColumn : column.getChildren()) {
                if (!column.getChildConstraint(childColumn).isImplied()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public int compareTo(final Table other) {
        if (other == this) {
            return 0;
        }
        return this.getFullName().compareToIgnoreCase(other.getFullName());
    }
    
    static {
        logger = Logger.getLogger(Table.class.getName());
        fineEnabled = Table.logger.isLoggable(Level.FINE);
        finerEnabled = Table.logger.isLoggable(Level.FINER);
    }
    
    public static class ByColumnIdComparator implements Comparator<TableColumn>
    {
        @Override
        public int compare(final TableColumn column1, final TableColumn column2) {
            final Object id1 = column1.getId();
            final Object id2 = column2.getId();
            if (id1 == null || id2 == null) {
                return column1.getName().compareToIgnoreCase(column2.getName());
            }
            if (id1 instanceof Number && id2 instanceof Number) {
                return ((Number)id1).intValue() - ((Number)id2).intValue();
            }
            return id1.toString().compareToIgnoreCase(id2.toString());
        }
    }
}
