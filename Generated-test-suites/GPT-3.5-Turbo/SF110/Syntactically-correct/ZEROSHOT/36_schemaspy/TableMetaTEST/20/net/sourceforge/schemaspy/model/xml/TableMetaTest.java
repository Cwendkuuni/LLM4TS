import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

    private Node createTableNode(String name, String comments, String remoteSchema, String remoteCatalog) {
        Element tableNode = new Element() {
            @Override
            public String getTagName() {
                return "table";
            }
        };
        tableNode.setAttribute("name", name);
        if (comments != null) {
            tableNode.setAttribute("comments", comments);
        }
        if (remoteSchema != null) {
            tableNode.setAttribute("remoteSchema", remoteSchema);
        }
        if (remoteCatalog != null) {
            tableNode.setAttribute("remoteCatalog", remoteCatalog);
        }
        return tableNode;
    }
    
    @Test
    public void testConstructor() {
        Node tableNode = createTableNode("TestTable", "Test comments", "TestSchema", "TestCatalog");
        TableMeta tableMeta = new TableMeta(tableNode);

        assertEquals("TestTable", tableMeta.getName());
        assertEquals("Test comments", tableMeta.getComments());
        assertEquals("TestSchema", tableMeta.getRemoteSchema());
        assertEquals("TestCatalog", tableMeta.getRemoteCatalog());

        assertEquals(0, tableMeta.getColumns().size());
    }

    @Test
    public void testConstructor_WithoutComments() {
        Node tableNode = createTableNode("TestTable", null, null, null);
        TableMeta tableMeta = new TableMeta(tableNode);

        assertEquals("TestTable", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertNull(tableMeta.getRemoteSchema());
        assertNull(tableMeta.getRemoteCatalog());

        assertEquals(0, tableMeta.getColumns().size());
    }

    @Test
    public void testGetSetName() {
        TableMeta tableMeta = new TableMeta(createTableNode("TestTable", null, null, null));

        tableMeta.setName("NewTable");

        assertEquals("NewTable", tableMeta.getName());
    }

    @Test
    public void testGetSetComments() {
        TableMeta tableMeta = new TableMeta(createTableNode("TestTable", null, null, null));

        tableMeta.setComments("New comments");

        assertEquals("New comments", tableMeta.getComments());
    }

    @Test
    public void testGetSetColumns() {
        TableMeta tableMeta = new TableMeta(createTableNode("TestTable", null, null, null));

        List<TableColumnMeta> columns = new ArrayList<>();
        columns.add(new TableColumnMeta());
        columns.add(new TableColumnMeta());

        tableMeta.setColumns(columns);

        assertEquals(2, tableMeta.getColumns().size());
        assertSame(columns, tableMeta.getColumns());
    }

    @Test
    public void testGetSetRemoteCatalog() {
        TableMeta tableMeta = new TableMeta(createTableNode("TestTable", null, null, null));

        tableMeta.setRemoteCatalog("NewCatalog");

        assertEquals("NewCatalog", tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetSetRemoteSchema() {
        TableMeta tableMeta = new TableMeta(createTableNode("TestTable", null, null, null));

        tableMeta.setRemoteSchema("NewSchema");

        assertEquals("NewSchema", tableMeta.getRemoteSchema());
    }
}