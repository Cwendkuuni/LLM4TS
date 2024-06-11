import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

public class NodeTest {

    @Test
    public void testNodeConstructors() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        // Test default constructor
        Node emptyNode = new Node();
        assertNull(emptyNode.parentNode);
        assertNotNull(emptyNode.attributes);
        assertNull(emptyNode.childNodes);
        assertEquals("", emptyNode.nodeName());
        assertEquals("", emptyNode.baseUri());
        assertNull(emptyNode.attr("test"));
        assertEquals(0, emptyNode.siblingIndex());

        // Test constructor with baseUri and attributes
        Node node1 = new Node(baseUri, attributes);
        assertNotNull(node1.parentNode);
        assertNotNull(node1.attributes);
        assertNotNull(node1.childNodes);
        assertEquals("Node", node1.nodeName());
        assertEquals(baseUri, node1.baseUri());
        assertEquals("testNode", node1.attr("id"));
        assertEquals(-1, node1.siblingIndex());

        // Test constructor with only baseUri
        Node node2 = new Node(baseUri);
        assertNotNull(node2.parentNode);
        assertNotNull(node2.attributes);
        assertNotNull(node2.childNodes);
        assertEquals("Node", node2.nodeName());
        assertEquals(baseUri, node2.baseUri());
        assertEquals(0, node2.siblingIndex());
    }

    @Test
    public void testAttrMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        Node node = new Node(baseUri, attributes);

        // Test attr method with existing attribute
        String attributeValue = "testValue";
        node.attr("test", attributeValue);
        assertEquals(attributeValue, node.attr("test"));

        // Test attr method with non-existent attribute
        assertEquals("", node.attr("nonExistent"));

        // Test attr method with "abs:" prefix
        String absUrl = "http://example.com/test";
        node.attr("abs:href", absUrl);
        assertEquals(absUrl, node.absUrl("href"));

        // Test attr method with multiple attributes
        attributes.put("class", "testClass");
        node.attributes = attributes;
        assertEquals("testClass", node.attr("class"));
        assertEquals("testNode", node.attr("id"));

        // Test attr method with removed attribute
        node.removeAttr("id");
        assertEquals("", node.attr("id"));

        // Test attr method with null attributes
        node.attributes = null;
        assertEquals("", node.attr("test"));
    }

    @Test
    public void testAbsUrlMethod() {
        String baseUri = "http://example.com";
        String attributeValue = "relativeUrl";

        Node node = new Node(baseUri, new Attributes());
        node.attr("href", attributeValue);

        String absUrl = node.absUrl("href");
        assertEquals(baseUri + "/" + attributeValue, absUrl);

        // Test absUrl method with absolute url
        node.attr("abs:href", "http://another-example.com");
        absUrl = node.absUrl("abs:href");
        assertEquals("http://another-example.com", absUrl);
    }

    @Test
    public void testChildNodesMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "parentNode");

        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, new Attributes());
        Node childNode2 = new Node(baseUri, new Attributes());

        parentNode.addChildren(childNode1, childNode2);

        // Test childNodes method
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));

        // Test childNode method
        Node childNodeTest = parentNode.childNode(0);
        assertEquals(childNode1, childNodeTest);

        // Test removeChild method
        parentNode.removeChild(childNode1);
        childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode2, childNodes.get(0));
    }

    @Test
    public void testParentNodeMethods() {
        String baseUri = "http://example.com";
        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "grandParentNode");

        Node grandParentNode = new Node(baseUri, parentAttributes);
        Node parentNode = new Node(baseUri, new Attributes());
        Node node = new Node(baseUri, new Attributes());

        grandParentNode.addChildren(parentNode);
        parentNode.addChildren(node);

        // Test parent method
        Node parentTest = node.parent();
        assertEquals(parentNode, parentTest);
        assertEquals(grandParentNode, parentTest.parent());

        // Test ownerDocument method
        Document document = new Document(baseUri);
        document.setBaseUri(baseUri);
        node.setOwnerDocument(document);
        assertEquals(document, node.ownerDocument());
    }

    @Test(expected = NullPointerException.class)
    public void testNullParameterException() {
        String baseUri = null;
        try {
            new Node(baseUri, new Attributes());
        } catch (NullPointerException e) {
            assertEquals("baseUri is marked non-null but is null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        try {
            new Node(null, attributes);
        } catch (IllegalArgumentException e) {
            assertEquals("parentNode cannot be null", e.getMessage());
            throw e;
        }

        try {
            new Node(baseUri, null);
        } catch (IllegalArgumentException e) {
            assertEquals("attributes cannot be null", e.getMessage());
            throw e;
        }
    }
}