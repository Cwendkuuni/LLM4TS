
import org.junit.Test;
import static org.junit.Assert.*;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta tableMeta = new TableMeta(null);
        assertNull(tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        TableMeta tableMeta = new TableMeta(null);
        assertNull(tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        TableMeta tableMeta = new TableMeta(null);
        assertNotNull(tableMeta.getColumns());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        TableMeta tableMeta = new TableMeta(null);
        assertNull(tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        TableMeta tableMeta = new TableMeta(null);
        assertNull(tableMeta.getRemoteSchema());
    }
}
