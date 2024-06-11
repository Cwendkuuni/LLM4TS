import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);
        assertEquals("node1", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertTrue(node.hasAttr("id"));
        assertEquals("node1", node.attr("id"));
        assertFalse(node.hasAttr("nonexistentattr"));
    }

    @Test
    public void testNodeConstructorWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertNull(node.attributes());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testNodeConstructorNullBaseUri() {
        Attributes attributes = new Attributes();
        new Node(null, attributes);
    }

    @Test(expected = NullPointerException.class)
    public void testNodeConstructorNullAttributes() {
        String baseUri = "http://example.com";
        new Node(baseUri, null);
    }

    @Test
    public void testAttrMethod() {
        String baseUri = "http://example.com";
        String attributeKey = "class";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "testclass");
        Node node = new Node(baseUri, attributes);
        assertEquals("testclass", node.attr("class"));
        assertEquals("", node.attr("nonexistentattr"));
        assertEquals("", node.attr("class").trim());
    }

    @Test
    public void testAttributesMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "testclass");
        Node node = new Node(baseUri, attributes);
        Attributes nodeAttributes = node.attributes();
        assertEquals("id", nodeAttributes.keyAt(0));
        assertEquals("node1", nodeAttributes.get("id"));
        assertEquals("class", nodeAttributes.keyAt(1));
        assertEquals("testclass", nodeAttributes.get("class"));
    }

    @Test
    public void testAttrMethodWithAbsUrl() {
        String baseUri = "http://example.com";
        String attributeKey = "abs:href";
        Attributes attributes = new Attributes();
        attributes.put("href", "relative.html");
        Node node = new Node(baseUri, attributes);
        assertEquals("http://example.com/relative.html", node.absUrl(attributeKey));
        assertEquals("", node.absUrl("nonexistentattr"));
    }

    @Test
    public void testHasAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonexistentattr"));
    }

    @Test
    public void testRemoveAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "testclass");
        Node node = new Node(baseUri, attributes);
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testSetBaseUriMethod() {
        String newBaseUri = "http://newexample.com";
        Node node = new Node("http://example.com");
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testSetBaseUriMethodNullBaseUri() {
        Node node = new Node("http://example.com");
        node.setBaseUri(null);
    }

    @Test
    public void testChildNodeMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri, attributes);
        parentNode.addChild(childNode);
        Node retrievedChildNode = parentNode.childNode(0);
        assertEquals(childNode, retrievedChildNode);
    }

    @Test
    public void testChildrenMethod() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri, attributes1);
        Node childNode2 = new Node(baseUri, attributes2);
        parentNode.addChildren(childNode1, childNode2);
        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(childNode1, children.get(0));
        assertEquals(childNode2, children.get(1));
    }

    @Test
    public void testSetParentNodeMethod() {
        String baseUri1 = "http://example1.com";
        String baseUri2 = "http://example2.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri1, attributes);
        Node parentNode1 = new Node(baseUri1);
        Node parentNode2 = new Node(baseUri2);
        childNode.setParentNode(parentNode1);
        parentNode1.addChild(childNode);
        childNode.setParentNode(parentNode2);
        List<Node> children = parentNode2.childNodes();
        assertEquals(1, children.size());
        assertEquals(childNode, children.get(0));
        assertNull(parentNode1.childNode(0));
    }

    @Test(expected = NullPointerException.class)
    public void testSetParentNodeMethodNullParentNode() {
        Node childNode = new Node();
        childNode.setParentNode(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetParentNodeMethodParentNodeWithChild() {
        String baseUri1 = "http://example1.com";
        String baseUri2 = "http://example2.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri1, attributes);
        Node parentNode1 = new Node(baseUri1);
        Node parentNode2 = new Node(baseUri2);
        parentNode1.addChild(childNode);
        childNode.setParentNode(parentNode2);
    }

    @Test
    public void testReplaceChildMethod() {
        String baseUri = "http://example.com";
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        Node parentNode = new Node(baseUri);
        Node oldChildNode = new Node(baseUri, attributes1);
        Node newChildNode = new Node(baseUri, attributes2);
        parentNode.addChild(oldChildNode);
        parentNode.replaceChild(oldChildNode, newChildNode);
        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(newChildNode, children.get(0));
        assertNull(parentNode.childNode(0));
    }

    @Test(expected = NullPointerException.class)
    public void testReplaceChildMethodNullChildNode() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node newChildNode = new Node(baseUri);
        parentNode.replaceChild(null, newChildNode);
    }

    @Test
    public void testRemoveChildMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri, attributes);
        parentNode.addChild(childNode);
        parentNode.removeChild(childNode);
        assertEquals(0, parentNode.childNodes().size());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveChildMethodNullChildNodeToRemove() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        parentNode.removeChild(null);
    }
}