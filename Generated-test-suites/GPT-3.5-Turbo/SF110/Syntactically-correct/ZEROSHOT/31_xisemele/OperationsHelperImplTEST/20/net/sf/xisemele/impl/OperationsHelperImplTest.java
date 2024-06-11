import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.List;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
    
    @Test
    public void testChildren() {
        // Test case 1: Empty node
        Node emptyNode = createEmptyNode();
        List<Node> emptyChildren = operationsHelper.children(emptyNode);
        assertTrue(emptyChildren.isEmpty());
        
        // Test case 2: Node with children
        Node parentNode = createParentNode();
        List<Node> parentChildren = operationsHelper.children(parentNode);
        assertEquals(2, parentChildren.size());
        assertEquals("child1", parentChildren.get(0).getNodeName());
        assertEquals("child2", parentChildren.get(1).getNodeName());
    }
    
    @Test
    public void testFind() {
        // Test case 1: Non-existent path
        Document document = createDocument();
        Node foundNode1 = operationsHelper.find(document, "nonexistent/path");
        assertNull(foundNode1);
        
        // Test case 2: Path with correct elements
        Node foundNode2 = operationsHelper.find(document, "root/child1/grandchild1");
        assertNotNull(foundNode2);
        assertEquals("grandchild1", foundNode2.getNodeName());
        
        // Test case 3: Path with incorrect element
        Node foundNode3 = operationsHelper.find(document, "root/child1/wrong");
        assertNull(foundNode3);
    }
    
    @Test
    public void testNodeWithName() {
        // Test case 1: Empty node list
        List<Node> emptyList = createEmptyNodeList();
        Node resultNode1 = operationsHelper.nodeWithName(emptyList, "name");
        assertNull(resultNode1);
        
        // Test case 2: Node with matching name
        List<Node> nodeList = createNodeList();
        Node resultNode2 = operationsHelper.nodeWithName(nodeList, "name2");
        assertNotNull(resultNode2);
        assertEquals("name2", resultNode2.getNodeName());
        
        // Test case 3: Node with non-matching name
        Node resultNode3 = operationsHelper.nodeWithName(nodeList, "wrong");
        assertNull(resultNode3);
    }
    
    @Test
    public void testSublistWithName() {
        // Test case 1: Empty node list
        List<Node> emptyList = createEmptyNodeList();
        List<Node> emptySublist = operationsHelper.sublistWithName(emptyList, "name");
        assertTrue(emptySublist.isEmpty());
        
        // Test case 2: Node list with matching names
        List<Node> nodeList = createNodeList();
        List<Node> sublist = operationsHelper.sublistWithName(nodeList, "name2");
        assertEquals(2, sublist.size());
        assertEquals("name2", sublist.get(0).getNodeName());
        assertEquals("name2", sublist.get(1).getNodeName());
        
        // Test case 3: Node list with non-matching names
        List<Node> wrongSublist = operationsHelper.sublistWithName(nodeList, "wrong");
        assertTrue(wrongSublist.isEmpty());
    }
    
    // Helper methods for test data
    
    private Node createEmptyNode() {
        Document document = createDocument();
        return document.createElement("empty");
    }
    
    private Node createParentNode() {
        Document document = createDocument();
        Node parentNode = document.createElement("parent");
        Node child1 = document.createElement("child1");
        Node child2 = document.createElement("child2");
        parentNode.appendChild(child1);
        parentNode.appendChild(child2);
        return parentNode;
    }
    
    private List<Node> createEmptyNodeList() {
        return Collections.emptyList();
    }
    
    private List<Node> createNodeList() {
        Document document = createDocument();
        Node node1 = document.createElement("name1");
        Node node2 = document.createElement("name2");
        Node node3 = document.createElement("name2");
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(node1);
        nodeList.add(node2);
        nodeList.add(node3);
        return nodeList;
    }
    
    private Document createDocument() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.newDocument();
    }
}