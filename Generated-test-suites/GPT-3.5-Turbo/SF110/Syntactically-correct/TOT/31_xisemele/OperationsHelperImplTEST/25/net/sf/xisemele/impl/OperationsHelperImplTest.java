import net.sf.xisemele.impl.OperationsHelperImpl;
import org.junit.Test;
import org.mockito.Mock;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren_WithEmptyNode_ReturnsEmptyList() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = mock(Document.class);
        Element element = mock(Element.class);
        NodeList emptyNodeList = mock(NodeList.class);
        when(element.getChildNodes()).thenReturn(emptyNodeList);
        when(document.getDocumentElement()).thenReturn(element);

        // Act
        List<Node> result = operationsHelper.children(document);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void testChildren_WithSingleNode_ReturnsListWithSingleNode() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = mock(Document.class);
        Element element = mock(Element.class);
        NodeList nodeList = mock(NodeList.class);
        Node node = mock(Node.class);
        when(element.getChildNodes()).thenReturn(nodeList);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(nodeList.getLength()).thenReturn(1);
        when(nodeList.item(0)).thenReturn(node);
        when(document.getDocumentElement()).thenReturn(element);

        // Act
        List<Node> result = operationsHelper.children(document);

        // Assert
        assertEquals(1, result.size());
        assertEquals(node, result.get(0));
    }

    @Test
    public void testFind_WithEmptyDocument_ReturnsNull() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = mock(Document.class);
        when(document.getDocumentElement()).thenReturn(null);

        // Act
        Node result = operationsHelper.find(document, "/path");

        // Assert
        assertNull(result);
    }

    @Test
    public void testFind_WithNonExistingPath_ReturnsNull() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = mock(Document.class);
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        NodeList nodeList = mock(NodeList.class);
        when(element.getChildNodes()).thenReturn(nodeList);
        when(nodeList.item(0)).thenReturn(node);
        when(document.getDocumentElement()).thenReturn(element);

        // Act
        Node result = operationsHelper.find(document, "/path");

        // Assert
        assertNull(result);
    }

    @Test
    public void testFind_WithValidPath_ReturnsNode() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Document document = mock(Document.class);
        Element element = mock(Element.class);
        Element childElement = mock(Element.class);
        Node node = mock(Node.class);
        Node childNode = mock(Node.class);
        NodeList nodeList = mock(NodeList.class);
        NodeList childNodeList = mock(NodeList.class);
        when(element.getNodeName()).thenReturn("root");
        when(element.getChildNodes()).thenReturn(nodeList);
        when(node.getNodeName()).thenReturn("node");
        when(childElement.getNodeName()).thenReturn("node");
        when(nodeList.item(0)).thenReturn(childNode);
        when(childNode.getChildNodes()).thenReturn(childNodeList);
        when(childNodeList.getLength()).thenReturn(1);
        when(childNodeList.item(0)).thenReturn(childElement);
        when(document.getDocumentElement()).thenReturn(element);

        // Act
        Node result = operationsHelper.find(document, "/root/node");

        // Assert
        assertNotNull(result);
        assertEquals(childElement, result);
    }

    @Test
    public void testNodeWithName_WithEmptyList_ReturnsNull() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        List<Node> nodes = Arrays.asList();

        // Act
        Node result = operationsHelper.nodeWithName(nodes, "test");

        // Assert
        assertNull(result);
    }

    @Test
    public void testNodeWithName_WithMatchingNode_ReturnsNode() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        List<Node> nodes = Arrays.asList(node, element);
        when(node.getNodeName()).thenReturn("test");

        // Act
        Node result = operationsHelper.nodeWithName(nodes, "test");

        // Assert
        assertNotNull(result);
        assertEquals(node, result);
    }

    @Test
    public void testNodeWithName_WithNoMatchingNode_ReturnsNull() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Element element = mock(Element.class);
        List<Node> nodes = Arrays.asList(element);
        when(element.getNodeName()).thenReturn("test");

        // Act
        Node result = operationsHelper.nodeWithName(nodes, "notFound");

        // Assert
        assertNull(result);
    }

    @Test
    public void testSublistWithName_WithEmptyList_ReturnsEmptyList() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        List<Node> nodes = Arrays.asList();

        // Act
        List<Node> result = operationsHelper.sublistWithName(nodes, "test");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSublistWithName_WithMatchingNodes_ReturnsSublist() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);
        List<Node> nodes = Arrays.asList(element1, element2);
        when(element1.getNodeName()).thenReturn("test");
        when(element2.getNodeName()).thenReturn("test");

        // Act
        List<Node> result = operationsHelper.sublistWithName(nodes, "test");

        // Assert
        assertEquals(2, result.size());
        assertEquals(element1, result.get(0));
        assertEquals(element2, result.get(1));
    }

    @Test
    public void testSublistWithName_WithNoMatchingNodes_ReturnsEmptyList() {
        // Arrange
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        Element element1 = mock(Element.class);
        List<Node> nodes = Arrays.asList(element1);
        when(element1.getNodeName()).thenReturn("test");

        // Act
        List<Node> result = operationsHelper.sublistWithName(nodes, "notFound");

        // Assert
        assertTrue(result.isEmpty());
    }
}