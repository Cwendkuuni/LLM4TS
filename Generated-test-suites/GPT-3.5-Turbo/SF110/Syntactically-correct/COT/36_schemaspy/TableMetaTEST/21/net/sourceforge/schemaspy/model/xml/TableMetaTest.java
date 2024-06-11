
import org.junit.Test;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta table = new TableMeta(null);
        String expected = null;
        String actual = table.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta(null);
        String expected = null;
        String actual = table.getComments();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta(null);
        List<TableColumnMeta> expected = new ArrayList<TableColumnMeta>();
        List<TableColumnMeta> actual = table.getColumns();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta(null);
        String expected = null;
        String actual = table.getRemoteCatalog();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta(null);
        String expected = null;
        String actual = table.getRemoteSchema();
        assertEquals(expected, actual);
    }
}
