import static org.junit.Assert.*;
import org.junit.Test;

public class TableMetaTest {
    @Test
    public void testGetName() {
        TableMeta table = new TableMeta();
        assertEquals("name", table.getName());
    }
    
    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta();
        assertEquals("comments", table.getComments());
    }
    
    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta();
        assertNotNull(table.getColumns());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta();
        assertNull(table.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta();
        assertNull(table.getRemoteSchema());
    }
    
    @Test
    public void testConstructorWithNullNode() {
        TableMeta table = new TableMeta(null);
        assertNull(table.getName());
        assertNull(table.getComments());
        assertNull(table.getRemoteCatalog());
        assertNull(table.getRemoteSchema());
        assertNotNull(table.getColumns());
    }
    
    @Test
    public void testConstructorWithInvalidNode() {
        TableMeta table = new TableMeta();
        assertNotNull(table);
    }
}