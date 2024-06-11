import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;

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
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertNotNull(columns);
        assertTrue(columns.isEmpty());
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