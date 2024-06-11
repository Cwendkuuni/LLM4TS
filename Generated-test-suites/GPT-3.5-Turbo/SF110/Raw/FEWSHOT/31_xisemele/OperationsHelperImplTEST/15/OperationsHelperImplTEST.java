import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class OperationsHelperImplTest {
    @Mock
    private Document document;
    @Mock
    private NodeList nodeList;
    
    private OperationsHelperImpl operationsHelper;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        operationsHelper = new OperationsHelperImpl();
    }
    
    @Test
    public void testChildren() {
        //Setup
        Node node = mock(Node.class);
        when(node.getChildNodes()).thenReturn(nodeList);
        
        //Test
        List<Node> result = operationsHelper.children(node);
        
        //Verify
        assertEquals(ArrayList.class, result.getClass());
        verify(node, times(1)).getChildNodes();
    }
    
    @Test
    public void testFind() {
        //Setup
        String path = "";
        String[] names = path.split("/");
        Node node = mock(Node.class);
        Node found = mock(Node.class);
        when(node.getNodeName()).thenReturn(names[0]);
        when(document.getDocumentElement().getNodeName()).thenReturn(names[0]);
        when(document.getDocumentElement()).thenReturn(found);
        when(found.getChildNodes()).thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(1);
        when(nodeList.item(0)).thenReturn(found);
        
        //Test
        Node result = operationsHelper.find(document, path);
        
        //Verify
        assertEquals(found, result);
        verify(node, times(1)).getNodeName();
        verify(document, times(1)).getDocumentElement().getNodeName();
        verify(node, times(names.length - 1)).getChildNodes();
        verify(nodeList, times(1)).getLength();
        verify(nodeList, times(1)).item(0);
    }
    
    @Test
    public void testNodeWithName() {
        //Setup
        List<Node> nodes = mock(List.class);
        Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn("name");
        when(nodes.get(anyInt())).thenReturn(node);
        
        //Test
        Node result = operationsHelper.nodeWithName(nodes, "name");
        
        //Verify
        assertEquals(node, result);
        verify(nodes, atLeastOnce()).get(anyInt());
        verify(node, atLeastOnce()).getNodeName();
    }
    
    @Test
    public void testSublistWithName() {
        //Setup
        List<Node> nodes = mock(List.class);
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("name");
        when(node2.getNodeName()).thenReturn("other");
        when(nodes.get(anyInt())).thenReturn(node1, node2);
        
        //Test
        List<Node> result = operationsHelper.sublistWithName(nodes, "name");
        
        //Verify
        assertEquals(ArrayList.class, result.getClass());
        verify(nodes, atLeastOnce()).get(anyInt());
        verify(node1, atLeastOnce()).getNodeName();
        verify(node2, atLeastOnce()).getNodeName();
    }
}