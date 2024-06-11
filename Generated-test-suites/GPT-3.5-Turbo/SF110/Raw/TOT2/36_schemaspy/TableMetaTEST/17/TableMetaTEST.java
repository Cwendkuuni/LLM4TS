import static org.junit.Assert.*;
import org.junit.Test;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta table = new TableMeta(null);
        assertEquals(null, table.getName());
    }

    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta(null);
        assertEquals(null, table.getComments());
    }

    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta(null);
        assertEquals(0, table.getColumns().size());
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta(null);
        assertEquals(null, table.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta(null);
        assertEquals(null, table.getRemoteSchema());
    }
}