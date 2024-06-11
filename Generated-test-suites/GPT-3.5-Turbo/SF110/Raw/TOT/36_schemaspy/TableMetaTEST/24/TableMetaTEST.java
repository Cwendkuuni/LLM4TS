import org.junit.*;
import static org.junit.Assert.*;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta table = new TableMeta(null);
        assertNull(table.getName()); // edge case: tableNode is null
    }

    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta(null);
        assertNull(table.getComments()); // edge case: tableNode is null
    }

    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta(null);
        assertTrue(table.getColumns().isEmpty()); // edge case: tableNode is null
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta(null);
        assertNull(table.getRemoteCatalog()); // edge case: tableNode is null
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta(null);
        assertNull(table.getRemoteSchema()); // edge case: tableNode is null
    }
}