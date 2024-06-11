import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();

        // Test when node has no children
        Node node1 = createNode();
        List<Node> children1 = helper.children(node1);
        assertEquals(0, children1.size());

        // Test when node has children
        Node node2 = createNode();
        Node child1 = createNode();
        Node child2 = createNode();
        node2.appendChild(child1);
        node2.appendChild(child2);

        List<Node> children2 = helper.children(node2);
        assertEquals(2, children2.size());
        assertTrue(children2.contains(child1));
        assertTrue(children2.contains(child2));
    }

    @Test
    public void testFind() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createDocument();

        // Test when path is empty
        Node emptyNode = helper.find(document, "");
        assertNull(emptyNode);

        // Test when path does not exist
        Node nonExistentNode = helper.find(document, "nonexistent");
        assertNull(nonExistentNode);

        // Test when path exists
        Node foundNode = helper.find(document, "root/child/grandchild");
        assertNotNull(foundNode);
        assertEquals("grandchild", foundNode.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        List<Node> nodes = new ArrayList<>();
        Node node1 = createNode();
        node1.setNodeName("node1");
        Node node2 = createNode();
        node2.setNodeName("node2");
        nodes.add(node1);
        nodes.add(node2);

        // Test when node name does not exist in nodes
        Node nonExistentNode = helper.nodeWithName(nodes, "nonexistent");
        assertNull(nonExistentNode);

        // Test when node name exists in nodes
        Node existingNode = helper.nodeWithName(nodes, "node1");
        assertNotNull(existingNode);
        assertEquals("node1", existingNode.getNodeName());
    }

    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        List<Node> nodes = new ArrayList<>();
        Node node1 = createNode();
        node1.setNodeName("node1");
        nodes.add(node1);
        Node node2 = createNode();
        node2.setNodeName("node2");
        nodes.add(node2);
        Node node3 = createNode();
        node3.setNodeName("node1");
        nodes.add(node3);

        // Test when node name does not exist in nodes
        List<Node> nonExistentList = helper.sublistWithName(nodes, "nonexistent");
        assertEquals(0, nonExistentList.size());

        // Test when node name exists in nodes
        List<Node> existingList = helper.sublistWithName(nodes, "node1");
        assertEquals(2, existingList.size());
        assertTrue(existingList.contains(node1));
        assertTrue(existingList.contains(node3));
    }

    // Helper method to create a node
    private Node createNode() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.newDocument().createElement("test");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to create a document with a structure
    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("root");
            Element child = document.createElement("child");
            Element grandchild = document.createElement("grandchild");

            child.appendChild(grandchild);
            root.appendChild(child);
            document.appendChild(root);

            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}