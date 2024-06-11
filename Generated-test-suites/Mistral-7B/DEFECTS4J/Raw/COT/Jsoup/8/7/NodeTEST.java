import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test-class");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals("test-class", node.attr("class"));
    }

    @Test
    public void testNodeConstructorWithBaseUriOnly() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertNull(node.attributes());
    }

    @Test
    public void testDefaultNodeConstructor() {
        Node node = new Node();

        Assert.assertEquals(Collections.EMPTY_LIST, node.childNodes());
        Assert.assertNull(node.attributes());
    }

    @Test
    public void testNodeName() {
        // Abstract class Node doesn't have a nodeName(), test subclasses instead
        // For example, Element and TextNode have their own nodeName() implementation
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test-class");
        Node node = new Node(baseUri, attributes);

        String attrValue = "test-value";
        node.attr("test-attr", attrValue);

        Assert.assertEquals(attrValue, node.attr("test-attr"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test-class");
        Node node = new Node(baseUri, attributes);

        Assert.assertTrue(node.hasAttr("class"));
        Assert.assertFalse(node.hasAttr("non-existent-attr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test-class");
        Node node = new Node(baseUri, attributes);

        node.removeAttr("class");

        Assert.assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        node.attributes().put("href", "relative-link.html");

        String absUrl = node.absUrl("href");

        Assert.assertEquals("http://example.com/relative-link.html", absUrl);
    }

    @Test
    public void testAbsUrlWithAbsoluteUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        node.attributes().put("href", "http://another-example.com/absolute-link.html");

        String absUrl = node.absUrl("href");

        Assert.assertEquals("http://another-example.com/absolute-link.html", absUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlMalformedUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        node.attributes().put("href", "malformed-url");

        node.absUrl("href");
    }

    @Test
    public void testChildNode() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri);

        parentNode.addChild(childNode);

        Assert.assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri);
        Node childNode2 = new Node(baseUri);

        parentNode.addChildren(childNode1, childNode2);

        Assert.assertEquals(2, parentNode.childNodes().size());
        Assert.assertEquals(childNode1, parentNode.childNodes().get(0));
        Assert.assertEquals(childNode2, parentNode.childNodes().get(1));
    }

    @Test
    public void testParent() {
        String baseUri1 = "http://example1.com";
        String baseUri2 = "http://example2.com";
        Node childNode = new Node(baseUri1);
        Node parentNode1 = new Node(baseUri2);

        childNode.setParentNode(parentNode1);

        Assert.assertEquals(parentNode1, childNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        Assert.assertNull(node.ownerDocument());

        Document document = new Document(baseUri);
        document.appendChild(node);

        Assert.assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri);

        parentNode.appendChild(childNode);

        parentNode.removeChild(childNode);

        Assert.assertFalse(parentNode.childNodes().contains(childNode));
        Assert.assertNull(childNode.parent());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri);
        Node childNode2 = new Node(baseUri);

        parentNode.appendChild(childNode1);

        parentNode.replaceChild(childNode1, childNode2);

        Assert.assertFalse(parentNode.childNodes().contains(childNode1));
        Assert.assertEquals(childNode2, parentNode.childNode(0));
        Assert.assertEquals(parentNode, childNode2.parent());
    }

    // Edge cases and exception handling tests:

    @Test(expected = NullPointerException.class)
    public void testParentNodeNull() {
        Node node = new Node();
        node.setParentNode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndexChildNode() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri);

        parentNode.appendChild(childNode);

        parentNode.childNode(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsChildNode() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri);
        Node childNode2 = new Node(baseUri);

        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);

        parentNode.childNode(3);
    }

    @Test(expected = NullPointerException.class)
    public void testNullChildNodesArray() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node[] children = null;

        parentNode.addChildren(children);
    }

    @Test(expected = NullPointerException.class)
    public void testNullChildInAddChildren() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node childNode = null;

        parentNode.addChildren(childNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndexAddChildren() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node[] children = {new Node(baseUri)};

        parentNode.addChildren(-1, children);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsAddChildren() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri);
        Node childNode2 = new Node(baseUri);
        Node[] children = {childNode1, childNode2};

        parentNode.addChildren(3, children);
    }

    @Test(expected = NullPointerException.class)
    public void testNullChildInReplaceChild() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node oldChild = new Node(baseUri);
        Node newChild = null;

        parentNode.replaceChild(oldChild, newChild);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndexRemoveChild() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node childNode = new Node(baseUri);

        parentNode.appendChild(childNode);

        parentNode.removeChild(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsRemoveChild() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);
        Node childNode1 = new Node(baseUri);
        Node childNode2 = new Node(baseUri);

        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);

        parentNode.removeChild(3);
    }
}