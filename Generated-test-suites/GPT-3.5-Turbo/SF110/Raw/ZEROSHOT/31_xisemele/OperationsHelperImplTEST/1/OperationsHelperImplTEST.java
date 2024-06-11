import org.junit.Test;
import org.junit.Assert;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

    @Test
    public void testChildren() {
        // Test with empty node
        Node emptyNode = mock(Node.class);
        NodeList emptyNodeList = mock(NodeList.class);
        when(emptyNode.getChildNodes()).thenReturn(emptyNodeList);
        when(emptyNodeList.getLength()).thenReturn(0);

        List<Node> emptyResult = operationsHelper.children(emptyNode);
        Assert.assertEquals(0, emptyResult.size());

        // Test with non-empty node
        Node nonEmptyNode = mock(Node.class);
        NodeList nonEmptyNodeList = mock(NodeList.class);
        Node child1 = mock(Node.class);
        Node child2 = mock(Node.class);
        when(nonEmptyNode.getChildNodes()).thenReturn(nonEmptyNodeList);
        when(nonEmptyNodeList.getLength()).thenReturn(2);
        when(nonEmptyNodeList.item(0)).thenReturn(child1);
        when(nonEmptyNodeList.item(1)).thenReturn(child2);

        List<Node> nonEmptyResult = operationsHelper.children(nonEmptyNode);
        Assert.assertEquals(2, nonEmptyResult.size());
        Assert.assertEquals(child1, nonEmptyResult.get(0));
        Assert.assertEquals(child2, nonEmptyResult.get(1));
    }

    @Test
    public void testFind() {
        // Test with empty document
        Document emptyDocument = mock(Document.class);
        Element documentElement = mock(Element.class);
        when(emptyDocument.getDocumentElement()).thenReturn(documentElement);
        when(documentElement.getNodeName()).thenReturn("empty");

        Node nullResult = operationsHelper.find(emptyDocument, "empty");
        Assert.assertNull(nullResult);

        // Test with non-empty document
        Document nonEmptyDocument = mock(Document.class);
        Element nonEmptyDocumentElement = mock(Element.class);
        NodeList nodeList = mock(NodeList.class);
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        when(nonEmptyDocument.getDocumentElement()).thenReturn(nonEmptyDocumentElement);
        when(nonEmptyDocumentElement.getNodeName()).thenReturn("root");
        when(nonEmptyDocumentElement.getChildNodes()).thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(2);
        when(nodeList.item(0)).thenReturn(node1);
        when(node1.getNodeName()).thenReturn("node1");
        when(nodeList.item(1)).thenReturn(node2);
        when(node2.getNodeName()).thenReturn("node2");

        Node foundNode = operationsHelper.find(nonEmptyDocument, "root/node1");
        Assert.assertNotNull(foundNode);
        Assert.assertEquals(node1, foundNode);

        Node nullResult2 = operationsHelper.find(nonEmptyDocument, "root/node3");
        Assert.assertNull(nullResult2);
    }

    @Test
    public void testNodeWithName() {
        // Test with empty node list
        List<Node> emptyNodes = new ArrayList<Node>();

        Node nullResult = operationsHelper.nodeWithName(emptyNodes, "node1");
        Assert.assertNull(nullResult);

        // Test with non-empty node list
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        List<Node> nonEmptyNodes = new ArrayList<Node>();
        nonEmptyNodes.add(node1);
        nonEmptyNodes.add(node2);

        Node foundNode = operationsHelper.nodeWithName(nonEmptyNodes, "node1");
        Assert.assertNotNull(foundNode);
        Assert.assertEquals(node1, foundNode);

        Node nullResult2 = operationsHelper.nodeWithName(nonEmptyNodes, "node3");
        Assert.assertNull(nullResult2);
    }

    @Test
    public void testSublistWithName() {
        // Test with empty node list
        List<Node> emptyNodes = new ArrayList<Node>();

        List<Node> emptyResult = operationsHelper.sublistWithName(emptyNodes, "node1");
        Assert.assertEquals(0, emptyResult.size());

        // Test with non-empty node list
        Node node1 = mock(Node.class);
        Node node2 = mock(Node.class);
        Node node3 = mock(Node.class);
        when(node1.getNodeName()).thenReturn("node1");
        when(node2.getNodeName()).thenReturn("node2");
        when(node3.getNodeName()).thenReturn("node1");
        List<Node> nonEmptyNodes = new ArrayList<Node>();
        nonEmptyNodes.add(node1);
        nonEmptyNodes.add(node2);
        nonEmptyNodes.add(node3);

        List<Node> nonEmptyResult = operationsHelper.sublistWithName(nonEmptyNodes, "node1");
        Assert.assertEquals(2, nonEmptyResult.size());
        Assert.assertEquals(node1, nonEmptyResult.get(0));
        Assert.assertEquals(node3, nonEmptyResult.get(1));

        List<Node> emptyResult2 = operationsHelper.sublistWithName(nonEmptyNodes, "node4");
        Assert.assertEquals(0, emptyResult2.size());
    }
}