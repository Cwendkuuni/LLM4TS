import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class TableMetaTest {
    @Test
    public void testGetName() {
        TableMeta table = new TableMeta(tableNode);
        assertEquals("Name should match", "tableName", table.getName());
    }

    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta(tableNode);
        assertNull("Comments should be null", table.getComments());
    }

    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta(tableNode);
        List<TableColumnMeta> columns = table.getColumns();
        assertNotNull("Columns should not be null", columns);
        assertEquals("Number of columns should be 0", 0, columns.size());
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta(tableNode);
        assertNull("Remote Catalog should be null", table.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta(tableNode);
        assertNull("Remote Schema should be null", table.getRemoteSchema());
    }
}