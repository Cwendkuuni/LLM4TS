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

    @Test
    public void testChildren() {
        // Create test node
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);
        document.appendChild(root);

        // Test children method
        List<Node> children = operationsHelper.children(document);
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }
    
    @Test
    public void testFind() {
        // Create test document
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        Element child = document.createElement("child");
        Element grandchild = document.createElement("grandchild");
        child.appendChild(grandchild);
        root.appendChild(child);
        document.appendChild(root);

        // Test find method
        Node found = operationsHelper.find(document, "/root/child/grandchild");
        assertEquals(grandchild, found);
    }
    
    @Test
    public void testNodeWithName() {
        // Create test nodes
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);
        document.appendChild(root);
        List<Node> nodes = Arrays.asList(child1, child2, child3);

        // Test nodeWithName method
        Node node = operationsHelper.nodeWithName(nodes, "child2");
        assertEquals(child2, node);
    }
    
    @Test
    public void testSublistWithName() {
        // Create test nodes
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child");
        Element child2 = document.createElement("child");
        Element child3 = document.createElement("child");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);
        document.appendChild(root);
        List<Node> nodes = Arrays.asList(child1, child2, child3);

        // Test sublistWithName method
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child");
        assertEquals(3, sublist.size());
        assertEquals(child1, sublist.get(0));
        assertEquals(child2, sublist.get(1));
        assertEquals(child3, sublist.get(2));
    }
}