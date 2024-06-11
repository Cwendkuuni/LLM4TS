
import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        TableMeta tableMeta = new TableMeta(null);
        assertEquals(null,tableMeta.getName());
    }
    
    @Test
    public void testEmptyComments() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.getComments();
        assertEquals(null,tableMeta.getComments());
    }
    
    @Test
    public void testNullRemoteCatalog() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.getRemoteCatalog();
        assertEquals(null,tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testNullRemoteSchema() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.getRemoteSchema();
        assertEquals(null,tableMeta.getRemoteSchema());
    }
}