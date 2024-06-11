
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        // Typical use case: Node has children
        Node mockNode = mock(Node.class);
        NodeList mockNodeList = mock(NodeList.class);
        when(mockNode.getChildNodes()).thenReturn(mockNodeList);
        when(mockNodeList.getLength()).thenReturn(3);
        when(mockNodeList.item(0)).thenReturn(mock(Node.class));
        when(mockNodeList.item(1)).thenReturn(mock(Node.class));
        when(mockNodeList.item(2)).thenReturn(mock(Node.class));

        List<Node> result = new OperationsHelperImpl().children(mockNode);

        assertEquals(3, result.size());
    }

    @Test
    public void testFind() {
        // Typical use case: Node is found
        Document mockDocument = mock(Document.class);
        Element mockElement = mock(Element.class);
        when(mockDocument.getDocumentElement()).thenReturn(mockElement);
        when(mockElement.getNodeName()).thenReturn("root");
        NodeList mockNodeList1 = mock(NodeList.class);
        when(mockElement.getChildNodes()).thenReturn(mockNodeList1);
        when(mockNodeList1.getLength()).thenReturn(1);
        Element mockChildElement1 = mock(Element.class);
        when(mockChildElement1.getNodeName()).thenReturn("child1");
        when(mockNodeList1.item(0)).thenReturn(mockChildElement1);
        NodeList mockNodeList2 = mock(NodeList.class);
        when(mockChildElement1.getChildNodes()).thenReturn(mockNodeList2);
        when(mockNodeList2.getLength()).thenReturn(1);
        Element mockChildElement2 = mock(Element.class);
        when(mockChildElement2.getNodeName()).thenReturn("child2");
        when(mockNodeList2.item(0)).thenReturn(mockChildElement2);

        Node result = new OperationsHelperImpl().find(mockDocument, "root/child1/child2");

        assertEquals(mockChildElement2, result);
    }

    @Test
    public void testNodeWithName() {
        // Typical use case: Node with given name exists
        Node mockNode1 = mock(Node.class);
        when(mockNode1.getNodeName()).thenReturn("node1");
        Node mockNode2 = mock(Node.class);
        when(mockNode2.getNodeName()).thenReturn("node2");
        List<Node> nodes = new ArrayList<>();
        nodes.add(mockNode1);
        nodes.add(mockNode2);

        Node result = new OperationsHelperImpl().nodeWithName(nodes, "node2");

        assertEquals(mockNode2, result);
    }

    @Test
    public void testSublistWithName() {
        // Typical use case: Nodes with given name exist
        Node mockNode1 = mock(Node.class);
        when(mockNode1.getNodeName()).thenReturn("node1");
        Node mockNode2 = mock(Node.class);
        when(mockNode2.getNodeName()).thenReturn("node2");
        Node mockNode3 = mock(Node.class);
        when(mockNode3.getNodeName()).thenReturn("node2");
        List<Node> nodes = new ArrayList<>();
        nodes.add(mockNode1);
        nodes.add(mockNode2);
        nodes.add(mockNode3);

        List<Node> result = new OperationsHelperImpl().sublistWithName(nodes, "node2");

        assertEquals(2, result.size());
        assertTrue(result.contains(mockNode2));
        assertTrue(result.contains(mockNode3));
    }
}
