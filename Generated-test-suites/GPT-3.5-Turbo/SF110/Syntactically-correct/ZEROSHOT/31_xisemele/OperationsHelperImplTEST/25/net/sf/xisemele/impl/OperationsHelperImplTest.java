import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl helper;

    @Before
    public void setup() {
        helper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        // Test when node has children
        Document document = createDocument();
        List<Node> children = helper.children(document.getDocumentElement());
        assertEquals(2, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());

        // Test when node does not have children
        Element element = document.createElement("element");
        List<Node> emptyChildren = helper.children(element);
        assertNotNull(emptyChildren);
        assertTrue(emptyChildren.isEmpty());
    }

    @Test
    public void testFind() {
        Document document = createDocument();

        // Test when path leads to a valid node
        Node node = helper.find(document, "/root/child1");
        assertNotNull(node);
        assertEquals("child1", node.getNodeName());

        // Test when path does not lead to a valid node
        Node invalidNode = helper.find(document, "/root/child3");
        assertNull(invalidNode);
    }

    @Test
    public void testNodeWithName() {
        List<Node> nodes = createNodes();

        // Test when node with specified name exists
        Node node = helper.nodeWithName(nodes, "node2");
        assertNotNull(node);
        assertEquals("node2", node.getNodeName());

        // Test when node with specified name does not exist
        Node invalidNode = helper.nodeWithName(nodes, "node3");
        assertNull(invalidNode);
    }

    @Test
    public void testSublistWithName() {
        List<Node> nodes = createNodes();

        // Test when sublist with specified name exists
        List<Node> sublist = helper.sublistWithName(nodes, "sublist2");
        assertNotNull(sublist);
        assertEquals(2, sublist.size());
        assertEquals("sublist2", sublist.get(0).getNodeName());
        assertEquals("sublist2", sublist.get(1).getNodeName());

        // Test when sublist with specified name does not exist
        List<Node> emptySublist = helper.sublistWithName(nodes, "sublist3");
        assertNotNull(emptySublist);
        assertTrue(emptySublist.isEmpty());
    }

    // Helper methods for test cases
    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Failed to create document builder", e);
        }
        Document document = builder.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);
        document.appendChild(root);
        return document;
    }

    private List<Node> createNodes() {
        List<Node> nodes = new ArrayList<>();
        Document document = createDocument();
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            nodes.add(nodeList.item(i));
        }
        return nodes;
    }
}