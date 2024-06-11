import static org.junit.Assert.*;
import org.junit.Test;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta tableMeta = new TableMeta(createTableNode("table1"));
        assertEquals("table1", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        TableMeta tableMeta = new TableMeta(createTableNode("table2"));
        assertNull(tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        TableMeta tableMeta = new TableMeta(createTableNode("table3"));
        assertEquals(0, tableMeta.getColumns().size());
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta tableMeta = new TableMeta(createTableNode("table4"));
        assertNull(tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta tableMeta = new TableMeta(createTableNode("table5"));
        assertNull(tableMeta.getRemoteSchema());
    }

    private Node createTableNode(String name) {
        // Create a mock table node for testing
        // You can generate mock nodes as per the requirements
        return null;
    }
}