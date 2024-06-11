
import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;
    
    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }
    
    @Test
    public void testChildren() {
        // Test basic functionality with a node that has children
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("test.xml");
            List<Node> children = operationsHelper.children(document.getDocumentElement());
            assertNotNull(children);
            assertEquals(2, children.size());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        // Test functionality with a node that has no children
        Element element = document.createElement("test");
        List<Node> emptyChildren = operationsHelper.children(element);
        assertNotNull(emptyChildren);
        assertEquals(0, emptyChildren.size());
        
        // Test functionality with a null node
        List<Node> nullChildren = operationsHelper.children(null);
        assertNotNull(nullChildren);
        assertEquals(0, nullChildren.size());
    }
    
    @Test
    public void testFind() {
        // Test basic functionality with an existing path
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("test.xml");
            Node foundNode = operationsHelper.find(document, "root/child1/grandchild");
            assertNotNull(foundNode);
            assertEquals("grandchild", foundNode.getNodeName());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test functionality with a non-existing path
        Node nullNode = operationsHelper.find(document, "root/nonexisting");
        assertNull(nullNode);
        
        // Test functionality with a null document
        Node nullDocumentNode = operationsHelper.find(null, "root/child");
        assertNull(nullDocumentNode);
    }
    
    @Test
    public void testNodeWithName() {
        List<Node> nodes = new ArrayList<>();
        Element element1 = document.createElement("test");
        Element element2 = document.createElement("test");
        Element element3 = document.createElement("other");
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);
        
        // Test basic functionality with an existing node name
        Node foundNode = operationsHelper.nodeWithName(nodes, "test");
        assertNotNull(foundNode);
        assertEquals("test", foundNode.getNodeName());
        
        // Test functionality with a non-existing node name
        Node nullNode = operationsHelper.nodeWithName(nodes, "nonexisting");
        assertNull(nullNode);
        
        // Test functionality with an empty node list
        List<Node> emptyNodes = new ArrayList<>();
        Node emptyNode = operationsHelper.nodeWithName(emptyNodes, "test");
        assertNull(emptyNode);
    }
    
    @Test
    public void testSublistWithName() {
        List<Node> nodes = new ArrayList<>();
        Element element1 = document.createElement("test");
        Element element2 = document.createElement("test");
        Element element3 = document.createElement("other");
        nodes.add(element1);
        nodes.add(element2);
        nodes.add(element3);
        
        // Test basic functionality with an existing node name
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "test");
        assertNotNull(sublist);
        assertEquals(2, sublist.size());
        
        // Test functionality with a non-existing node name
        List<Node> nullSublist = operationsHelper.sublistWithName(nodes, "nonexisting");
        assertNotNull(nullSublist);
        assertEquals(0, nullSublist.size());
        
        // Test functionality with an empty node list
        List<Node> emptyNodes = new ArrayList<>();
        List<Node> emptySublist = operationsHelper.sublistWithName(emptyNodes, "test");
        assertNotNull(emptySublist);
        assertEquals(0, emptySublist.size());
    }
}

