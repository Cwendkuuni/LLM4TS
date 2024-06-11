import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TableMetaTest {

    private TableMeta tableMeta;

    @Before
    public void setup() {
        tableMeta = new TableMeta();
    }

    @Test
    public void testGetName() {
        assertNotNull(tableMeta.getName());
        assertEquals("TableMeta", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        assertNotNull(tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertNotNull(columns);
        assertEquals(0, columns.size());
    }

    @Test
    public void testGetRemoteCatalog() {
        assertNotNull(tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        assertNotNull(tableMeta.getRemoteSchema());
    }

    @Test(expected = NullPointerException.class)
    public void testTableMetaConstructorWithNullNode() {
        new TableMeta(null);
    }
}