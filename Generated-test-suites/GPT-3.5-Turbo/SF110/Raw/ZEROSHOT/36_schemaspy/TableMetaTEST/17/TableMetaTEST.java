import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TableMetaTest {

    private TableMeta tableMeta;

    @Before
    public void setUp() {
        String name = "TestTable";
        String comments = "This is a test table";
        List<TableColumnMeta> columns = new ArrayList<TableColumnMeta>();
        String remoteCatalog = "TestCatalog";
        String remoteSchema = "TestSchema";

        tableMeta = new TableMeta(name, comments, columns, remoteCatalog, remoteSchema);
    }

    @Test
    public void testNameGetter() {
        assertEquals("TestTable", tableMeta.getName());
    }

    @Test
    public void testCommentsGetter() {
        assertEquals("This is a test table", tableMeta.getComments());
    }

    @Test
    public void testColumnsGetter() {
        assertNotNull(tableMeta.getColumns());
        assertTrue(tableMeta.getColumns().isEmpty());
    }

    @Test
    public void testRemoteCatalogGetter() {
        assertEquals("TestCatalog", tableMeta.getRemoteCatalog());
    }

    @Test
    public void testRemoteSchemaGetter() {
        assertEquals("TestSchema", tableMeta.getRemoteSchema());
    }
}