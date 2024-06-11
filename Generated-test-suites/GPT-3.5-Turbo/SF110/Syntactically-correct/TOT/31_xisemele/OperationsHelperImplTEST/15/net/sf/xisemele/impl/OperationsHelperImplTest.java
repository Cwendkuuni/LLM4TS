
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;
    private Document document;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
        document = createTestDocument();
    }

    @Test
    public void testChildren_EmptyNode_ReturnsEmptyList() {
        Node emptyNode = document.createTextNode("");
        List<Node> result = operationsHelper.children(emptyNode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testChildren_NodeWithChildren_ReturnsExpectedList() {
        Node parentNode = document.createElement("Parent");
        Node childNode = document.createTextNode("Child");
        parentNode.appendChild(childNode);
    
        List<Node> result = operationsHelper.children(parentNode);
        assertEquals(1, result.size());
        assertEquals(childNode, result.get(0));
    }

    @Test
    public void testFind_PathDoesNotExist_ReturnsNull() {
        String path = "NonExistent";
        Node result = operationsHelper.find(document, path);
        assertNull(result);
    }

    @Test
    public void testFind_EmptyPath_ReturnsDocumentElement() {
        String path = "";
        Node result = operationsHelper.find(document, path);
        assertEquals(document.getDocumentElement(), result);
    }

    @Test
    public void testFind_ValidPath_ReturnsExpectedNode() {
        String path = "Root/Child/Grandchild";
        Node result = operationsHelper.find(document, path);
        assertNotNull(result);
        assertEquals("Grandchild", result.getNodeName());
    }

    @Test
    public void testFind_ValidPathWithoutChildren_ReturnsExpectedNode() {
        String path = "Root/Child/Null";
        Node result = operationsHelper.find(document, path);
        assertNotNull(result);
        assertEquals("Null", result.getNodeName());
    }

    @Test
    public void testNodeWithName_NodeListContainsMatch_ReturnsFirstMatchingNode() {
        Node node1 = document.createElement("Node");
        Node node2 = document.createElement("Node");

        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
    
        String name = "Node";
        Node result = operationsHelper.nodeWithName(nodes, name);
        assertEquals(node1, result);
    }

    @Test
    public void testNodeWithName_NodeListContainsNoMatch_ReturnsNull() {
        Node node1 = document.createElement("Node");
        Node node2 = document.createElement("Node");

        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
    
        String name = "NonExistent";
        Node result = operationsHelper.nodeWithName(nodes, name);
        assertNull(result);
    }

    @Test
    public void testSublistWithName_NodeListContainsMatch_ReturnsExpectedSublist() {
        Node node1 = document.createElement("Node");
        Node node2 = document.createElement("Node");
        Node node3 = document.createElement("DifferentNode");
        Node node4 = document.createElement("Node");

        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
    
        String name = "Node";
        List<Node> result = operationsHelper.sublistWithName(nodes, name);
        assertEquals(3, result.size());
        assertEquals(node1, result.get(0));
        assertEquals(node2, result.get(1));
        assertEquals(node4, result.get(2));
    }

    @Test
    public void testSublistWithName_NodeListContainsNoMatch_ReturnsEmptyList() {
        Node node1 = document.createElement("Node");
        Node node2 = document.createElement("Node");

        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
    
        String name = "NonExistent";
        List<Node> result = operationsHelper.sublistWithName(nodes, name);
        assertTrue(result.isEmpty());
    }

    // Helper method to create a test document
    private Document createTestDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.newDocument();
    
            Element root = document.createElement("Root");
            document.appendChild(root);
    
            Element child = document.createElement("Child");
            root.appendChild(child);
    
            Element grandchild = document.createElement("Grandchild");
            child.appendChild(grandchild);

            Element nullElement = document.createElement("Null");
            child.appendChild(nullElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
}

