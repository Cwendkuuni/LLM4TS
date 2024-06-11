package net.sourceforge.schemaspy.model.xml;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        TableMeta table = new TableMeta(null);
        table.getName();
        assertEquals(table.getName(), null);
    }
    
    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta(null);
        table.getComments();
        assertEquals(table.getComments(), null);
    }
    
    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta(null);
        List<TableColumnMeta> columns = table.getColumns();
        assertEquals(columns.size(), 0);
    }
    
    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta(null);
        table.getRemoteCatalog();
        assertEquals(table.getRemoteCatalog(), null);
    }
    
    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta(null);
        table.getRemoteSchema();
        assertEquals(table.getRemoteSchema(), null);
    }
}