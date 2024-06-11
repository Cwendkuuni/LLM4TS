import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = Mockito.mock(Document.class);
        
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        
        Mockito.when(node1.getNodeType()).thenReturn(1);
        Mockito.when(node2.getNodeType()).thenReturn(2);
        
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(node1);
        nodeList.add(node2);
        
        Mockito.when(document.getChildNodes()).thenReturn(nodeList);
        
        List<Node> children = helper.children(document);
        
        assertEquals(1, children.size());
        assertEquals(node1, children.get(0));
    }

    @Test
    public void testFind() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = Mockito.mock(Document.class);
        
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        
        Mockito.when(document.getDocumentElement()).thenReturn(node1);
        Mockito.when(node1.getNodeName()).thenReturn("element1");
        Mockito.when(node2.getNodeName()).thenReturn("element2");
        
        List<Node> children = new ArrayList<>();
        children.add(node2);
        
        Mockito.when(node1.getChildNodes()).thenReturn(children);
        
        Node foundNode = helper.find(document, "element1/element2");
        
        assertEquals(node2, foundNode);

    }

    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        List<Node> nodes = new ArrayList<>();
        
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        
        Mockito.when(node1.getNodeName()).thenReturn("element1");
        Mockito.when(node2.getNodeName()).thenReturn("element2");
        
        nodes.add(node1);
        nodes.add(node2);
        
        Node resultNode = helper.nodeWithName(nodes, "element2");
        
        assertEquals(node2, resultNode);
    }

    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        List<Node> nodes = new ArrayList<>();
        
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        
        Mockito.when(node1.getNodeName()).thenReturn("element1");
        Mockito.when(node2.getNodeName()).thenReturn("element2");
        
        nodes.add(node1);
        nodes.add(node2);
        
        List<Node> sublist = helper.sublistWithName(nodes, "element2");
        
        assertEquals(1, sublist.size());
        assertEquals(node2, sublist.get(0));
    }

}