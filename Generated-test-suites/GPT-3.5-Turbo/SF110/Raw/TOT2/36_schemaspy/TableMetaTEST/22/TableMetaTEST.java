import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TableMetaTest {
    private TableMeta tableMeta;

    @Before
    public void setUp() {
        tableMeta = new TableMeta();
    }

    @Test
    public void testGetName() {
        tableMeta = new TableMeta("sampleTable");
        assertEquals("sampleTable", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        tableMeta = new TableMeta("sampleTable", "Table comments");
        assertEquals("Table comments", tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertNotNull(columns);
    }

    @Test
    public void testGetRemoteCatalog() {
        tableMeta = new TableMeta("sampleTable", "Table comments", "catalog");
        assertEquals("catalog", tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        tableMeta = new TableMeta("sampleTable", "Table comments", "catalog", "schema");
        assertEquals("schema", tableMeta.getRemoteSchema());
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        tableMeta = new TableMeta(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullComments() {
        tableMeta = new TableMeta("sampleTable", null);
        tableMeta.getComments();
    }

    @Test
    public void testNoColumns() {
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertEquals(0, columns.size());
    }

    // Add more test cases for edge cases and exception scenarios here

}