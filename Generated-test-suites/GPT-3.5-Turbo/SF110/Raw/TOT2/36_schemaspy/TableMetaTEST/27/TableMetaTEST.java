import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.w3c.dom.*;
import net.sourceforge.schemaspy.model.xml.TableMeta;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        Node testNode = mock(Node.class);
        when(testNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(testNode.getAttributes().getNamedItem("name").getNodeValue()).thenReturn("testName");
        
        TableMeta tableMeta = new TableMeta(testNode);
        
        assertEquals("testName", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        Node testNode = mock(Node.class);
        when(testNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        
        TableMeta tableMeta = new TableMeta(testNode);
        
        assertNull(tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        Node testNode = mock(Node.class);
        when(testNode.getChildNodes()).thenReturn(mock(NodeList.class));
        when(((Element)testNode.getChildNodes()).getElementsByTagName("column")).thenReturn(mock(NodeList.class));
        
        TableMeta tableMeta = new TableMeta(testNode);
        
        assertEquals(0, tableMeta.getColumns().size());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        Node testNode = mock(Node.class);
        when(testNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        
        TableMeta tableMeta = new TableMeta(testNode);
        
        assertNull(tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        Node testNode = mock(Node.class);
        when(testNode.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        
        TableMeta tableMeta = new TableMeta(testNode);
        
        assertNull(tableMeta.getRemoteSchema());
    }
}