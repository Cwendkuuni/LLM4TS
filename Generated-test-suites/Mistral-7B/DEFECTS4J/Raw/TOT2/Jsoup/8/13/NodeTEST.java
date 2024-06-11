import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        // Test default constructor
        Node node = new Node();
        assertNull(node.parentNode);
        assertNotNull(node.childNodes);
        assertNull(node.attributes);
        assertNull(node.baseUri);
        assertFalse(node.hasAttr("test"));

        // Test constructor with baseUri and attributes
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Node nodeWithBaseUriAndAttributes = new Node(baseUri, attributes);
        assertEquals(baseUri, nodeWithBaseUriAndAttributes.baseUri());
        assertEquals(attributes, nodeWithBaseUriAndAttributes.attributes());
        assertTrue(nodeWithBaseUriAndAttributes.hasAttr("test"));
        assertEquals("value", nodeWithBaseUriAndAttributes.attr("test"));

        // Test constructor with only baseUri
        Node nodeWithBaseUri = new Node(baseUri);
        assertEquals(baseUri, nodeWithBaseUri.baseUri());
        assertNull(nodeWithBaseUri.attributes());
        assertNull(nodeWithBaseUri.childNodes);
    }

    @Test
    public void testAttributeMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        Node node = new Node(baseUri, attributes);

        // Test attr method with existing attribute
        assertEquals("value", node.attr("test"));

        // Test attr method with non-existent attribute
        assertEmpty(node.attr("nonExistent"));

        // Test attr method with abs: prefix for absolute URLs
        String absUrl = "http://example.com/path";
        node.attr("abs:src", absUrl);
        assertEquals(absUrl, node.absUrl("src"));
    }

    @Test
    public void testChildNodeMethods() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        parentNode.addChildren(childNode1, childNode2);

        // Test childNode method
        Node childNode = parentNode.childNode(0);
        assertEquals(childNode1, childNode);

        // Test childNodes method
        List<Node> children = parentNode.childNodes();
        assertEquals(children.size(), 2);
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));

        // Test childNodesAsArray method
        Node[] childrenArray = parentNode.childNodesAsArray();
        assertEquals(childrenArray.length, 2);
        assertEquals(childNode1, childrenArray[0]);
        assertEquals(childNode2, childrenArray[1]);
    }
}