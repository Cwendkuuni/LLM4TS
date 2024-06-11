import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {
    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
    
    @Test
    public void testChildren() {
        Node parent = Mockito.mock(Node.class);
        Node child1 = Mockito.mock(Node.class);
        Node child2 = Mockito.mock(Node.class);
        
        Mockito.when(parent.getChildNodes()).thenReturn(Mockito.mock(NodeList.class));
        Mockito.when(parent.getChildNodes().getLength()).thenReturn(2);
        Mockito.when(parent.getChildNodes().item(0)).thenReturn(child1);
        Mockito.when(parent.getChildNodes().item(1)).thenReturn(child2);
        
        List<Node> children = operationsHelper.children(parent);
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }
    
    @Test
    public void testFind() {
        Document document = Mockito.mock(Document.class);
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        
        Mockito.when(document.getDocumentElement()).thenReturn(node1);
        Mockito.when(node1.getChildNodes()).thenReturn(Mockito.mock(NodeList.class));
        Mockito.when(node1.getChildNodes().getLength()).thenReturn(1);
        Mockito.when(node1.getChildNodes().item(0)).thenReturn(node2);
        
        Node result = operationsHelper.find(document, "element");
        assertEquals(node2, result);
    }
    
    @Test
    public void testNodeWithName() {
        List<Node> nodes = new ArrayList<Node>();
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        nodes.add(node1);
        nodes.add(node2);
        
        Node result = operationsHelper.nodeWithName(nodes, "node2");
        assertEquals(node2, result);
    }
    
    @Test
    public void testSublistWithName() {
        List<Node> nodes = new ArrayList<Node>();
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        nodes.add(node1);
        nodes.add(node2);
        
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "node2");
        assertEquals(1, sublist.size());
        assertEquals(node2, sublist.get(0));
    }

}