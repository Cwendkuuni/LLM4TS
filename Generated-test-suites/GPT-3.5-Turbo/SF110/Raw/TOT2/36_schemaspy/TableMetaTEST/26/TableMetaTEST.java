import static org.junit.Assert.*;
import org.junit.Test;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        TableMeta table = new TableMeta();
        table.name = "TestTable";
        assertEquals("TestTable", table.getName());
    }
    
    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta();
        table.comments = "Test Comments";
        assertEquals("Test Comments", table.getComments());
    }
    
    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta();
        List<TableColumnMeta> columns = new ArrayList<>();
        TableColumnMeta column1 = new TableColumnMeta();
        columns.add(column1);
        table.columns = columns;
        assertEquals(columns, table.getColumns());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta();
        table.remoteCatalog = "TestCatalog";
        assertEquals("TestCatalog", table.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta();
        table.remoteSchema = "TestSchema";
        assertEquals("TestSchema", table.getRemoteSchema());
    }
    
    // Edge cases and exception handling scenarios
    
    @Test
    public void testGetCommentsNull() {
        TableMeta table = new TableMeta();
        table.comments = null;
        assertNull(table.getComments());
    }
    
    @Test
    public void testGetRemoteCatalogNull() {
        TableMeta table = new TableMeta();
        table.remoteCatalog = null;
        assertNull(table.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchemaNull() {
        TableMeta table = new TableMeta();
        table.remoteSchema = null;
        assertNull(table.getRemoteSchema());
    }
    
    @Test
    public void testGetColumnsEmpty() {
        TableMeta table = new TableMeta();
        table.columns = new ArrayList<>();
        assertTrue(table.getColumns().isEmpty());
    }
}