package net.sourceforge.schemaspy.model.xml;
import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        TableMeta table = new TableMeta(null);
        assertEquals("Test Name", table.getName());
    }
    
    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta(null);
        assertNull(table.getComments());
    }
    
    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta(null);
        List<TableColumnMeta> columns = table.getColumns();
        assertNotNull(columns);
        assertTrue(columns.isEmpty());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta(null);
        assertNull(table.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta(null);
        assertNull(table.getRemoteSchema());
    }
}