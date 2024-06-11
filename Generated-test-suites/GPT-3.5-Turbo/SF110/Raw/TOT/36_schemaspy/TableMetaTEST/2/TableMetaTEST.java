
import org.junit.*;
import static org.junit.Assert.*;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        
        assertEquals(null, tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        
        assertEquals(null, tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        
        assertEquals(0, tableMeta.getColumns().size());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        
        assertEquals(null, tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        
        assertEquals(null, tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testTableMetaConstructor() {
        Node tableNode = null;
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertNotNull(tableMeta);
    }
    
    @Test
    public void testTableMetaConstructorColumnName() {
        Node tableNode = null;
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals(null, tableMeta.getName());
    }
    
    @Test
    public void testTableMetaConstructorColumnComments() {
        Node tableNode = null;
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals(null, tableMeta.getComments());
    }
    
    @Test
    public void testTableMetaConstructorColumnRemoteCatalog() {
        Node tableNode = null;
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals(null, tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testTableMetaConstructorColumnRemoteSchema() {
        Node tableNode = null;
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals(null, tableMeta.getRemoteSchema());
    }
}
