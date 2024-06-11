
import org.junit.Test;
import static org.junit.Assert.*;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        TableMeta table = new TableMeta(null);
        table.name = "TestTable";
        assertEquals("TestTable", table.getName());
    }
    
    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta(null);
        table.comments = "Test comments";
        assertEquals("Test comments", table.getComments());
    }
    
    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta(null);
        List<TableColumnMeta> columns = new ArrayList<TableColumnMeta>();
        TableColumnMeta column = new TableColumnMeta(null);
        columns.add(column);
        table.columns = columns;
        assertEquals(columns, table.getColumns());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta(null);
        table.remoteCatalog = "TestCatalog";
        assertEquals("TestCatalog", table.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta(null);
        table.remoteSchema = "TestSchema";
        assertEquals("TestSchema", table.getRemoteSchema());
    }
}
