import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    private Node node;

    @Before
    public void setUp() {
        node = new Node("http://example.com");
    }

    @Test
    public void testNodeName() {
        String nodeName = node.nodeName();
        assertEquals("", nodeName);
    }

    @Test
    public void testAttr() {
        String attrValue = node.attr("attributeKey");
        assertEquals("", attrValue);
    }

    @Test
    public void testAttributes() {
        Attributes attributes = node.attributes();
        assertNotNull(attributes);
    }

    @Test
    public void testAttrWithKeyAndValue() {
        Node newNode = node.attr("attributeKey", "attributeValue");
        assertNotNull(newNode);
    }

    @Test
    public void testHasAttr() {
        boolean hasAttr = node.hasAttr("attributeKey");
        assertFalse(hasAttr);
    }

    @Test
    public void testRemoveAttr() {
        Node newNode = node.removeAttr("attributeKey");
        assertNotNull(newNode);
    }

    @Test
    public void testBaseUri() {
        String baseUri = node.baseUri();
        assertEquals("http://example.com", baseUri);
    }

    @Test
    public void testSetBaseUri() {
        node.setBaseUri("http://newexample.com");
        String baseUri = node.baseUri();
        assertEquals("http://newexample.com", baseUri);
    }

    @Test
    public void testChildNode() {
        Node childNode = node.childNode(0);
        assertNull(childNode);
    }

    @Test
    public void testChildNodes() {
        List<Node> childNodes = node.childNodes();
        assertNotNull(childNodes);
        assertEquals(0, childNodes.size());
    }

    @Test
    public void testParent() {
        Node parent = node.parent();
        assertNull(parent);
    }

    @Test
    public void testOwnerDocument() {
        Document ownerDocument = node.ownerDocument();
        assertNull(ownerDocument);
    }

    @Test
    public void testRemove() {
        node.remove();
        Node parent = node.parent();
        assertNull(parent);
    }

    @Test
    public void testReplaceWith() {
        Node in = new Node("http://newexample.com");
        node.replaceWith(in);
        Node parent = node.parent();
        assertNull(parent);
    }

    @Test
    public void testSiblingNodes() {
        List<Node> siblingNodes = node.siblingNodes();
        assertNotNull(siblingNodes);
        assertEquals(0, siblingNodes.size());
    }

    @Test
    public void testNextSibling() {
        Node nextSibling = node.nextSibling();
        assertNull(nextSibling);
    }

    @Test
    public void testPreviousSibling() {
        Node previousSibling = node.previousSibling();
        assertNull(previousSibling);
    }

    @Test
    public void testSiblingIndex() {
        Integer siblingIndex = node.siblingIndex();
        assertNull(siblingIndex);
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = node.outerHtml();
        assertNotNull(outerHtml);
    }

    @Test
    public void testClone() {
        Node clone = node.clone();
        assertNotNull(clone);
    }
}