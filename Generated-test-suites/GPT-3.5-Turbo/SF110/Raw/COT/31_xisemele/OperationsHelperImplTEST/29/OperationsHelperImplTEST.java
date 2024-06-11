import static org.junit.Assert.*;
import org.junit.Test;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        // Test with a node that has children
        NodeList nodeList = mock(NodeList.class);
        when(nodeList.getLength()).thenReturn(2);
        Node child1 = mock(Node.class);
        Node child2 = mock(Node.class);
        when(child1.getNodeType()).thenReturn(1);
        when(child2.getNodeType()).thenReturn(1);
        when(nodeList.item(0)).thenReturn(child1);
        when(nodeList.item(1)).thenReturn(child2);
        List<Node> expectedResult = new ArrayList<>();
        expectedResult.add(child1);
        expectedResult.add(child2);
        assertEquals(expectedResult, new OperationsHelperImpl().children(mock(Node.class)));
        
        // Test with a node that doesn't have any children
        nodeList = mock(NodeList.class);
        when(nodeList.getLength()).thenReturn(0);
        assertEquals(0, new OperationsHelperImpl().children(mock(Node.class)).size());
    }

    @Test
    public void testFind() {
        // Test with a valid path
        Document document = mock(Document.class);
        Element documentElement = mock(Element.class);
        Node child1 = mock(Node.class);
        Node child2 = mock(Node.class);
        Node child3 = mock(Node.class);
        NodeList childNodes = mock(NodeList.class);
        when(document.getDocumentElement()).thenReturn(documentElement);
        when(documentElement.getNodeName()).thenReturn("element");
        when(documentElement.getChildNodes()).thenReturn(childNodes);
        when(childNodes.getLength()).thenReturn(2);
        when(childNodes.item(0)).thenReturn(child1);
        when(childNodes.item(1)).thenReturn(child2);
        when(child1.getNodeName()).thenReturn("child1");
        when(child2.getNodeName()).thenReturn("child2");
        NodeList child1ChildNodes = mock(NodeList.class);
        when(child1.getChildNodes()).thenReturn(child1ChildNodes);
        when(child1ChildNodes.getLength()).thenReturn(1);
        when(child1ChildNodes.item(0)).thenReturn(child3);
        when(child3.getNodeName()).thenReturn("child3");
        Node expectedNode = child3;
        assertEquals(expectedNode, new OperationsHelperImpl().find(document, "element/child1/child3"));

        // Test with an invalid path
        assertNull(new OperationsHelperImpl().find(document, "element/child1/nonexistent"));
    }

    @Test
    public void testNodeWithName() {
        // Test with a list of nodes that contains the node with the specified name
        List<Node> nodes = new ArrayList<>();
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("node1");
        when(node2.getNodeName()).thenReturn("node2");
        nodes.add(node1);
        nodes.add(node2);
        Node expectedNode = node1;
        assertEquals(expectedNode, new OperationsHelperImpl().nodeWithName(nodes, "node1"));
        
        // Test with a list of nodes that doesn't contain the node with the specified name
        nodes = new ArrayList<>();
        expectedNode = null;
        assertEquals(expectedNode, new OperationsHelperImpl().nodeWithName(nodes, "nonexistent"));
    }

    @Test
    public void testSublistWithName() {
        // Test with a list of nodes that contains nodes with the specified name
        List<Node> nodes = new ArrayList<>();
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        Node node3 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("node1");
        when(node2.getNodeName()).thenReturn("node1");
        when(node3.getNodeName()).thenReturn("node2");
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        List<Node> expectedList = new ArrayList<>();
        expectedList.add(node1);
        expectedList.add(node2);
        assertEquals(expectedList, new OperationsHelperImpl().sublistWithName(nodes, "node1"));

        // Test with a list of nodes that doesn't contain nodes with the specified name
        nodes = new ArrayList<>();
        expectedList = new ArrayList<>();
        assertEquals(expectedList, new OperationsHelperImpl().sublistWithName(nodes, "nonexistent"));
    }

}