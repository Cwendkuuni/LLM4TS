import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren_shouldReturnListWithAllChildNodes() {
        // Test case with a node having multiple child nodes
        Document document = createDocumentWithChildren();
        Node parentNode = document.getDocumentElement();
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        
        List<Node> children = operationsHelper.children(parentNode);
        
        assertEquals(2, children.size());
        assertEquals("Child1", children.get(0).getNodeName());
        assertEquals("Child2", children.get(1).getNodeName());
    }

    @Test
    public void testChildren_shouldReturnEmptyListForNodeWithoutChildren() {
        // Test case with a node having no child nodes
        Document document = createDocumentWithoutChildren();
        Node parentNode = document.getDocumentElement();
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        List<Node> children = operationsHelper.children(parentNode);

        assertEquals(0, children.size());
    }

    @Test
    public void testChildren_shouldReturnEmptyListForNullInputNode() {
        // Test case with a null input node
        Node parentNode = null;
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        List<Node> children = operationsHelper.children(parentNode);

        assertEquals(0, children.size());
    }
    
    @Test
    public void testFind_shouldReturnNodeWithGivenPath() {
        // Test case with a valid path
        Document document = createDocumentWithChildren();
        String path = "Parent/Child1";
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        Node foundNode = operationsHelper.find(document, path);

        assertNotNull(foundNode);
        assertEquals("Child1", foundNode.getNodeName());
    }

    @Test
    public void testFind_shouldReturnNullForInvalidPath() {
        // Test case with an invalid path
        Document document = createDocumentWithChildren();
        String path = "Parent/InvalidChild";
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        Node foundNode = operationsHelper.find(document, path);

        assertNull(foundNode);
    }

    @Test
    public void testFind_shouldReturnNullForNullDocument() {
        // Test case with a null document
        Document document = null;
        String path = "Parent/Child1";
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        Node foundNode = operationsHelper.find(document, path);

        assertNull(foundNode);
    }
    
    @Test
    public void testNodeWithName_shouldReturnNodeWithGivenName() {
        // Test case with a node having the desired name
        List<Node> nodes = createListOfNodes();
        String name = "Child2";
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        Node foundNode = operationsHelper.nodeWithName(nodes, name);

        assertNotNull(foundNode);
        assertEquals("Child2", foundNode.getNodeName());
    }

    @Test
    public void testNodeWithName_shouldReturnNullForNodeWithoutDesiredName() {
        // Test case with a node not having the desired name
        List<Node> nodes = createListOfNodes();
        String name = "Child3";
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        Node foundNode = operationsHelper.nodeWithName(nodes, name);

        assertNull(foundNode);
    }

    @Test
    public void testNodeWithName_shouldReturnNullForEmptyListOfNodes() {
        // Test case with an empty list of nodes
        List<Node> nodes = new ArrayList<>();
        String name = "Child1";
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        Node foundNode = operationsHelper.nodeWithName(nodes, name);

        assertNull(foundNode);
    }
    
    @Test
    public void testSublistWithName_shouldReturnListWithNodesHavingGivenName() {
        // Test case with multiple nodes having the desired name
        List<Node> nodes = createListOfNodes();
        String name = "Child1";
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        List<Node> sublist = operationsHelper.sublistWithName(nodes, name);

        assertEquals(2, sublist.size());
        assertEquals("Child1", sublist.get(0).getNodeName());
        assertEquals("Child1", sublist.get(1).getNodeName());
    }

    @Test
    public void testSublistWithName_shouldReturnEmptyListForNoNodesWithDesiredName() {
        // Test case with no nodes having the desired name
        List<Node> nodes = createListOfNodes();
        String name = "Child3";
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        List<Node> sublist = operationsHelper.sublistWithName(nodes, name);

        assertEquals(0, sublist.size());
    }

    @Test
    public void testSublistWithName_shouldReturnEmptyListForEmptyListOfNodes() {
        // Test case with an empty list of nodes
        List<Node> nodes = new ArrayList<>();
        String name = "Child1";
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();

        List<Node> sublist = operationsHelper.sublistWithName(nodes, name);

        assertEquals(0, sublist.size());
    }
    
    // Helper methods for creating test data
    private Document createDocumentWithChildren() {
        Document document = createEmptyDocument();
        
        Element parentElement = document.createElement("Parent");
        Element childElement1 = document.createElement("Child1");
        Element childElement2 = document.createElement("Child2");
        
        parentElement.appendChild(childElement1);
        parentElement.appendChild(childElement2);

        document.appendChild(parentElement);

        return document;
    }

    private Document createDocumentWithoutChildren() {
        return createEmptyDocument();
    }

    private Document createEmptyDocument() {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }

    private List<Node> createListOfNodes() {
        List<Node> nodes = new ArrayList<>();
        
        Document document = createDocumentWithChildren();
        Node parentNode = document.getDocumentElement();
        
        nodes.add(parentNode);
        nodes.add(parentNode); // Add duplicate node for testing
        
        return nodes;
    }
}