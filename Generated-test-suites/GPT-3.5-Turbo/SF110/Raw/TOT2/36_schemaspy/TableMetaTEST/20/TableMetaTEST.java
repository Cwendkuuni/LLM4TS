import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class TableMetaTest {
    @Test
    public void testName() {
        TableMeta table = new TableMeta(tableNode);
        assertEquals("Table name should be equal", "TestTable", table.getName());
    }
    
    @Test
    public void testComments() {
        TableMeta table = new TableMeta(tableNode);
        assertNull("Comments should be null", table.getComments());
    }
    
    @Test
    public void testColumns() {
        TableMeta table = new TableMeta(tableNode);
        List<TableColumnMeta> columns = table.getColumns();
        assertEquals("Number of columns should be 0", 0, columns.size());
    }
    
    @Test
    public void testRemoteCatalog() {
        TableMeta table = new TableMeta(tableNode);
        assertNull("Remote catalog should be null", table.getRemoteCatalog());
    }
    
    @Test
    public void testRemoteSchema() {
        TableMeta table = new TableMeta(tableNode);
        assertNull("Remote schema should be null", table.getRemoteSchema());
    }
    
    @Test
    public void testTableMetaConstructor() {
        TableMeta table = new TableMeta(tableNode);
        assertNotNull("TableMeta object should not be null", table);
    }
}