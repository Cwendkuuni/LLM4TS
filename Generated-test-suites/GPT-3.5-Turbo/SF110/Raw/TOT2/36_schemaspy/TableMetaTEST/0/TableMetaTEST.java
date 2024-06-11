import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta table = new TableMeta(createTestNode());
        assertEquals("Table Name Test", "testTable", table.getName());
    }

    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta(createTestNode());
        assertNull("Comments Test", table.getComments());
    }

    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta(createTestNode());
        List<TableColumnMeta> columns = table.getColumns();
        assertNotNull("Columns Test", columns);
        assertEquals("Columns Size Test", 2, columns.size());
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta(createTestNode());
        assertNull("Remote Catalog Test", table.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta(createTestNode());
        assertNull("Remote Schema Test", table.getRemoteSchema());
    }

    private Node createTestNode() {
        // Create a test Node
    }

    // more edge cases and exception handling test cases can be added here

}