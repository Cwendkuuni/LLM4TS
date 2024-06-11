import static org.junit.Assert.*;
import org.junit.Test;

public class TableMetaTest {

    @Test
    public void testName() {
        TableMeta tableMeta = new TableMeta();
        assertEquals("Test Name", tableMeta.getName()); 
    }
    
    @Test
    public void testComments() {
        TableMeta tableMeta = new TableMeta();
        assertNull("Test Comments", tableMeta.getComments()); 
    }
    
    @Test
    public void testColumns() {
        TableMeta tableMeta = new TableMeta();
        assertNotNull("Test Columns", tableMeta.getColumns()); 
    }
    
    @Test
    public void testRemoteCatalog() {
        TableMeta tableMeta = new TableMeta();
        assertNull("Test Remote Catalog", tableMeta.getRemoteCatalog()); 
    }
    
    @Test
    public void testRemoteSchema() {
        TableMeta tableMeta = new TableMeta();
        assertNull("Test Remote Schema", tableMeta.getRemoteSchema()); 
    }
  
}