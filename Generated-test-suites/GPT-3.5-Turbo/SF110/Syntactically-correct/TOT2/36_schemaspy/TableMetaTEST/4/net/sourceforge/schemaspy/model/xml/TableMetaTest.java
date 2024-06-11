package net.sourceforge.schemaspy.model.xml;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class TableMetaTest {

    // Test case for getName()
    @Test
    public void testGetName() {
        TableMeta table = new TableMeta(null);
        table = new TableMeta(null);
        table.getName();
        assertEquals("name", table.getName());
    }

    // Test case for getComments()
    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta(null);
        table = new TableMeta(null);
        assertNull(table.getComments());
    }

    // Test case for getColumns()
    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta(null);
        assertTrue(table.getColumns().isEmpty());
    }

    // Test case for getRemoteCatalog()
    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta(null);
        assertNull(table.getRemoteCatalog());
    }

    // Test case for getRemoteSchema()
    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta(null);
        assertNull(table.getRemoteSchema());
    }
}