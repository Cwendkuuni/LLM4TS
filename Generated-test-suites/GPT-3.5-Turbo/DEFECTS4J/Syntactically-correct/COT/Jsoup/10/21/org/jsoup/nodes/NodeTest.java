
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    private Node node;

    @Before
    public void setUp() {
        node = new TestNode("http://www.example.com");
    }

    @Test
    public void testAttr() {
        assertEquals("example", node.attr("attributeKey"));
    }

    @Test
    public void testAttributes() {
        assertEquals(1, node.attributes().size());
    }

    @Test
    public void testAddAttr() {
        node.attr("newAttribute", "value");
        assertTrue(node.hasAttr("newAttribute"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("attributeKey"));
        assertFalse(node.hasAttr("nonExistentKey"));
    }

    @Test
    public void testRemoveAttr() {
        node.removeAttr("attributeKey");
        assertFalse(node.hasAttr("attributeKey"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("http://www.example.com", node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        node.setBaseUri("https://www.example.com");
        assertEquals("https://www.example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        assertEquals("http://www.example.com/path", node.absUrl("attributeKey"));
    }

    @Test
    public void testChildNode() {
        Node childNode = node.childNode(0);
        assertNotNull(childNode);
    }

    @Test
    public void testChildNodes() {
        assertEquals(1, node.childNodes().size());
    }

    @Test
    public void testParent() {
        assertNull(node.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertNotNull(node.ownerDocument());
    }

    @Test
    public void testRemove() {
        node.remove();
        assertNull(node.parent());
    }

    @Test
    public void testReplaceWith() {
        Node newNode = new TestNode("http://www.newexample.com");
        node.replaceWith(newNode);
        assertNull(node.parent());
        assertEquals("http://www.newexample.com", newNode.baseUri());
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

class TestNode extends Node {
    TestNode(String baseUri) {
        super(baseUri);
    }

    @Override
    public String nodeName() {
        return "TestNode";
    }

    @Override
    void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {

    }

    @Override
    void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {

    }
}
