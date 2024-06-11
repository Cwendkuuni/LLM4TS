import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta tableMeta = new TableMeta(null);
        assertEquals(null, tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        TableMeta tableMeta = new TableMeta(null);
        assertEquals(null, tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        TableMeta tableMeta = new TableMeta(null);
        assertEquals(0, tableMeta.getColumns().size());
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta tableMeta = new TableMeta(null);
        assertEquals(null, tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta tableMeta = new TableMeta(null);
        assertEquals(null, tableMeta.getRemoteSchema());
    }
}