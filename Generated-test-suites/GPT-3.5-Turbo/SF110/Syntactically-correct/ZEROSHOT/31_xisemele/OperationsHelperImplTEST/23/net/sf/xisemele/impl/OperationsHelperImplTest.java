import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.w3c.dom.*;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl helper;
    private Document document;
    
    @Before
    public void setUp() {
        helper = new OperationsHelperImpl();
        document = documentBuilder.newDocument();
    }
    
    @Test
    public void testChildren() {
        // Test case 1: Empty node
        Node node = document.createElement("test");
        List<Node> children = helper.children(node);
        assertEquals(0, children.size());
        
        // Test case 2: Node with children
        Node child1 = document.createElement("child1");
        Node child2 = document.createElement("child2");
        node.appendChild(child1);
        node.appendChild(child2);
        children = helper.children(node);
        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
    }
    
    @Test
    public void testFind() {
        // Test case 1: Empty document
        assertNull(helper.find(document, "test"));
        
        // Test case 2: Node not found
        Element root = document.createElement("root");
        document.appendChild(root);
        Element child1 = document.createElement("child1");
        root.appendChild(child1);
        assertNull(helper.find(document, "test"));
        
        // Test case 3: Node found
        Element child2 = document.createElement("child2");
        root.appendChild(child2);
        Node foundNode = helper.find(document, "root/child2");
        assertEquals(child2, foundNode);
    }
    
    @Test
    public void testNodeWithName() {
        // Test case 1: Empty list
        List<Node> nodes = new ArrayList<Node>();
        assertNull(helper.nodeWithName(nodes, "test"));
        
        // Test case 2: Node not found
        Element node1 = document.createElement("node1");
        nodes.add(node1);
        assertNull(helper.nodeWithName(nodes, "test"));
        
        // Test case 3: Node found
        Element node2 = document.createElement("node2");
        nodes.add(node2);
        Node foundNode = helper.nodeWithName(nodes, "node2");
        assertEquals(node2, foundNode);
    }
    
    @Test
    public void testSublistWithName() {
        // Test case 1: Empty list
        List<Node> nodes = new ArrayList<Node>();
        List<Node> sublist = helper.sublistWithName(nodes, "test");
        assertEquals(0, sublist.size());
        
        // Test case 2: Node not found
        Element node1 = document.createElement("node1");
        nodes.add(node1);
        sublist = helper.sublistWithName(nodes, "test");
        assertEquals(0, sublist.size());
        
        // Test case 3: Node found
        Element node2 = document.createElement("node2");
        nodes.add(node2);
        sublist = helper.sublistWithName(nodes, "node2");
        assertEquals(1, sublist.size());
        assertEquals(node2, sublist.get(0));
    }
}