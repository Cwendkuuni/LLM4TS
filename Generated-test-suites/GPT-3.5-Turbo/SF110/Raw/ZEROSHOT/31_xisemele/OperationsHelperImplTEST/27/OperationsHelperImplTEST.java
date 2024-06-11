import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;

    @Before
    public void setup() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        // Test case 1: node with no children
        Node node1 = createNode("testNode");
        List<Node> children1 = operationsHelper.children(node1);
        assertEquals(0, children1.size());

        // Test case 2: node with multiple children
        Node node2 = createNode("parent");
        Node child1 = createNode("child1");
        Node child2 = createNode("child2");
        Node child3 = createNode("child3");
        node2.appendChild(child1);
        node2.appendChild(child2);
        node2.appendChild(child3);
        List<Node> children2 = operationsHelper.children(node2);
        assertEquals(3, children2.size());

        // Test case 3: node with nested children
        Node parent = createNode("parent");
        Node child1 = createNode("child1");
        Node child2 = createNode("child2");
        Node grandchild1 = createNode("grandchild1");
        Node grandchild2 = createNode("grandchild2");
        child1.appendChild(grandchild1);
        child2.appendChild(grandchild2);
        parent.appendChild(child1);
        parent.appendChild(child2);
        List<Node> children3 = operationsHelper.children(parent);
        assertEquals(2, children3.size());
    }

    @Test
    public void testFind() {
        // Test case 1: valid path exists
        Document document1 = createDocument();
        Node node1 = operationsHelper.find(document1, "root/child1");
        assertNotNull(node1);
        assertEquals("child1", node1.getNodeName());

        // Test case 2: valid path does not exist
        Document document2 = createDocument();
        Node node2 = operationsHelper.find(document2, "root/child3");
        assertNull(node2);

        // Test case 3: empty path
        Document document3 = createDocument();
        Node node3 = operationsHelper.find(document3, "");
        assertNull(node3);

        // Test case 4: single-level path
        Document document4 = createDocument();
        Node node4 = operationsHelper.find(document4, "root");
        assertNotNull(node4);
        assertEquals("root", node4.getNodeName());

        // Test case 5: multi-level path
        Document document5 = createDocument();
        Node node5 = operationsHelper.find(document5, "root/child1/grandchild1");
        assertNotNull(node5);
        assertEquals("grandchild1", node5.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        // Test case 1: node with matching name exists
        List<Node> nodes1 = new ArrayList<>();
        Node node1 = createNode("testNode");
        nodes1.add(node1);
        Node result1 = operationsHelper.nodeWithName(nodes1, "testNode");
        assertNotNull(result1);
        assertSame(node1, result1);

        // Test case 2: node with matching name does not exist
        List<Node> nodes2 = new ArrayList<>();
        Node node2 = createNode("testNode");
        nodes2.add(node2);
        Node result2 = operationsHelper.nodeWithName(nodes2, "otherNode");
        assertNull(result2);

        // Test case 3: empty node list
        List<Node> nodes3 = new ArrayList<>();
        Node result3 = operationsHelper.nodeWithName(nodes3, "testNode");
        assertNull(result3);

        // Test case 4: multiple nodes with matching name
        List<Node> nodes4 = new ArrayList<>();
        Node node3 = createNode("testNode");
        Node node4 = createNode("testNode");
        nodes4.add(node3);
        nodes4.add(node4);
        Node result4 = operationsHelper.nodeWithName(nodes4, "testNode");
        assertNotNull(result4);
        assertSame(node3, result4);
    }

    @Test
    public void testSublistWithName() {
        // Test case 1: nodes with matching name exist
        List<Node> nodes1 = new ArrayList<>();
        Node node1 = createNode("testNode");
        Node node2 = createNode("otherNode");
        Node node3 = createNode("testNode");
        nodes1.add(node1);
        nodes1.add(node2);
        nodes1.add(node3);
        List<Node> result1 = operationsHelper.sublistWithName(nodes1, "testNode");
        assertEquals(2, result1.size());
        assertSame(node1, result1.get(0));
        assertSame(node3, result1.get(1));

        // Test case 2: no nodes with matching name exist
        List<Node> nodes2 = new ArrayList<>();
        Node node4 = createNode("testNode");
        Node node5 = createNode("otherNode");
        nodes2.add(node4);
        nodes2.add(node5);
        List<Node> result2 = operationsHelper.sublistWithName(nodes2, "unknownNode");
        assertEquals(0, result2.size());

        // Test case 3: empty node list
        List<Node> nodes3 = new ArrayList<>();
        List<Node> result3 = operationsHelper.sublistWithName(nodes3, "testNode");
        assertEquals(0, result3.size());

        // Test case 4: single node with matching name
        List<Node> nodes4 = new ArrayList<>();
        Node node6 = createNode("testNode");
        nodes4.add(node6);
        List<Node> result4 = operationsHelper.sublistWithName(nodes4, "testNode");
        assertEquals(1, result4.size());
        assertSame(node6, result4.get(0));
    }

    // Helper method to create a Node with the given name
    private Node createNode(String name) {
        Document document = createDocument();
        return document.createElement(name);
    }

    // Helper method to create a Document with a root element named "root"
    private Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        return document;
    }
}