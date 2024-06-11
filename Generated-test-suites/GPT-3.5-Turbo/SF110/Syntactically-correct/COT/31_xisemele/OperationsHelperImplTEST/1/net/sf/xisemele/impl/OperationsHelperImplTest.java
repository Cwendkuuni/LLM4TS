import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        // Test when node has children
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        Element child1 = document.createElement("child1");
        root.appendChild(child1);
        Element child2 = document.createElement("child2");
        root.appendChild(child2);
        List<Node> children = operationsHelper.children(root);
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        
        // Test when node has no children
        children = operationsHelper.children(child1);
        assertEquals(0, children.size());
    }

    @Test
    public void testFind() {
        // Test when document has the path
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        Element child1 = document.createElement("child1");
        root.appendChild(child1);
        Element child2 = document.createElement("child2");
        child1.appendChild(child2);
        Node found = operationsHelper.find(document, "/root/child1");
        assertEquals(child1, found);
        
        found = operationsHelper.find(document, "/root/child1/child2");
        assertEquals(child2, found);
        
        // Test when document does not have the path
        found = operationsHelper.find(document, "/root/child3");
        assertNull(found);
        
        found = operationsHelper.find(document, "/root/child1/child3");
        assertNull(found);
    }

    @Test
    public void testNodeWithName() {
        // Test when nodes list contains the node with the given name
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        Element child1 = document.createElement("child");
        root.appendChild(child1);
        Element child2 = document.createElement("child");
        root.appendChild(child2);
        Element child3 = document.createElement("child");
        root.appendChild(child3);
        
        List<Node> nodes = Arrays.asList(root, child1, child2, child3);
        Node found = operationsHelper.nodeWithName(nodes, "child");
        assertEquals(child1, found);
    }

    @Test
    public void testSublistWithName() {
        // Test when nodes list contains nodes with the given name
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        Element child1 = document.createElement("child");
        root.appendChild(child1);
        Element child2 = document.createElement("child");
        root.appendChild(child2);
        
        List<Node> nodes = Arrays.asList(root, child1, child2);
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child");
        assertEquals(2, sublist.size());
        assertEquals(child1, sublist.get(0));
        assertEquals(child2, sublist.get(1));
        
        // Test when nodes list does not contain nodes with the given name
        sublist = operationsHelper.sublistWithName(nodes, "nonexistent");
        assertEquals(0, sublist.size());
    }
}