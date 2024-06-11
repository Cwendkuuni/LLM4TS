import static org.junit.Assert.*;
import org.junit.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    // Test for the children method
    
    @Test
    public void testChildren_typicalCase() {
        // Create a test Document
        Document document = createDocument();
        Node rootNode = document.createElement("root");
        document.appendChild(rootNode);

        // Add child nodes to the root node
        Node child1 = document.createElement("child1");
        Node child2 = document.createElement("child2");
        rootNode.appendChild(child1);
        rootNode.appendChild(child2);

        // Call the children method
        List<Node> children = operationsHelper.children(rootNode);

        // Check if the children list contains the expected child nodes
        assertEquals(2, children.size());
        assertSame(child1, children.get(0));
        assertSame(child2, children.get(1));
    }

    @Test
    public void testChildren_emptyNode() {
        // Create a test Document
        Document document = createDocument();
        Node rootNode = document.createElement("root");
        document.appendChild(rootNode);
        
        // Call the children method
        List<Node> children = operationsHelper.children(rootNode);

        // Check if the children list is empty
        assertTrue(children.isEmpty());
    }

    // Test for the find method
    
    @Test
    public void testFind_typicalCase() {
        // Create a test Document
        Document document = createDocument();
        Node rootNode = document.createElement("root");
        document.appendChild(rootNode);
        
        // Add child nodes to the root node
        Node child1 = document.createElement("child1");
        Node child2 = document.createElement("child2");
        rootNode.appendChild(child1);
        rootNode.appendChild(child2);
        
        // Call the find method
        Node foundNode = operationsHelper.find(document, "root");

        // Check if the found node is the root node
        assertSame(rootNode, foundNode);
    }

    @Test
    public void testFind_invalidPath() {
        // Create a test Document
        Document document = createDocument();
        Node rootNode = document.createElement("root");
        document.appendChild(rootNode);
        
        // Call the find method with an invalid path
        Node foundNode = operationsHelper.find(document, "invalidPath");

        // Check if the found node is null
        assertNull(foundNode);
    }

    // Test for the nodeWithName method
    
    @Test
    public void testNodeWithName_typicalCase() {
        // Create a test Document
        Document document = createDocument();
        Node rootNode = document.createElement("root");
        document.appendChild(rootNode);
        
        // Add child nodes to the root node
        Node node1 = document.createElement("node");
        node1.appendChild(document.createTextNode("text1"));
        Node node2 = document.createElement("node");
        rootNode.appendChild(node1);
        rootNode.appendChild(node2);
        
        // Call the nodeWithName method
        Node foundNode = operationsHelper.nodeWithName(operationsHelper.children(rootNode), "node");

        // Check if the found node is the first node with the specified name
        assertSame(node1, foundNode);
    }

    @Test
    public void testNodeWithName_invalidNodeName() {
        // Create a test Document
        Document document = createDocument();
        Node rootNode = document.createElement("root");
        document.appendChild(rootNode);
        
        // Call the nodeWithName method with an invalid node name
        Node foundNode = operationsHelper.nodeWithName(operationsHelper.children(rootNode), "invalidNode");

        // Check if the found node is null
        assertNull(foundNode);
    }

    // Test for the sublistWithName method
    
    @Test
    public void testSublistWithName_typicalCase() {
        // Create a test Document
        Document document = createDocument();
        Node rootNode = document.createElement("root");
        document.appendChild(rootNode);
        
        // Add child nodes to the root node
        Node node1 = document.createElement("node");
        node1.appendChild(document.createTextNode("text1"));
        Node node2 = document.createElement("node");
        rootNode.appendChild(node1);
        rootNode.appendChild(node2);
        
        // Call the sublistWithName method
        List<Node> sublist = operationsHelper.sublistWithName(operationsHelper.children(rootNode), "node");

        // Check if the sublist contains the nodes with the specified name
        assertEquals(2, sublist.size());
        assertSame(node1, sublist.get(0));
        assertSame(node2, sublist.get(1));
    }

    @Test
    public void testSublistWithName_invalidNodeName() {
        // Create a test Document
        Document document = createDocument();
        Node rootNode = document.createElement("root");
        document.appendChild(rootNode);
        
        // Call the sublistWithName method with an invalid node name
        List<Node> sublist = operationsHelper.sublistWithName(operationsHelper.children(rootNode), "invalidNode");

        // Check if the sublist is empty
        assertTrue(sublist.isEmpty());
    }

    // Helper method to create a test Document
    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return builder.newDocument();
    }

}