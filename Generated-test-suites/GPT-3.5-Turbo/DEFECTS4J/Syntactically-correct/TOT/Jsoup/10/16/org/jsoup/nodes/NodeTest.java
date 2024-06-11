import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    private Node testNode;

    @Before
    public void setUp() {
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        testNode = new Element("tag", baseUri, attributes);
    }

    @Test
    public void testNodeConstructor() {
        assertNotNull(testNode);
        assertEquals("tag", testNode.nodeName());
        assertEquals("https://www.example.com", testNode.baseUri());
        Attributes attributes = testNode.attributes();
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testAttr() {
        assertEquals("value1", testNode.attr("key1"));
        assertEquals("value2", testNode.attr("key2"));
        assertEquals("", testNode.attr("key3"));
        assertEquals("", testNode.attr("abs:key1"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(testNode.hasAttr("key1"));
        assertTrue(testNode.hasAttr("key2"));
        assertFalse(testNode.hasAttr("key3"));
        assertFalse(testNode.hasAttr("abs:key1"));
    }

    @Test
    public void testRemoveAttr() {
        assertEquals("value1", testNode.attr("key1"));
        testNode.removeAttr("key1");
        assertEquals("", testNode.attr("key1"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("https://www.example.com", testNode.baseUri());
        testNode.setBaseUri("https://www.example.org");
        assertEquals("https://www.example.org", testNode.baseUri());
    }

    @Test
    public void testAbsUrl() {
        assertEquals("", testNode.absUrl("href"));
        assertEquals("", testNode.absUrl("src"));
    }

    @Test
    public void testChildNode() {
        assertNull(testNode.childNode(0));
    }

    @Test
    public void testChildNodes() {
        List<Node> childNodes = testNode.childNodes();
        assertNotNull(childNodes);
        assertEquals(0, childNodes.size());
    }

    @Test
    public void testParent() {
        assertNull(testNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertNull(testNode.ownerDocument());
    }

    @Test
    public void testRemove() {
        Validate.notNull(testNode.parent());
        testNode.remove();
        assertNull(testNode.parent());
    }

    @Test
    public void testReplaceWith() {
        Node newNode = new Element("newTag");
        assertNotNull(testNode.parent());
        testNode.replaceWith(newNode);
        assertNull(testNode.parent());
        assertEquals(newNode, newNode.parent());
    }

    @Test
    public void testSiblingNodes() {
        List<Node> siblingNodes = testNode.siblingNodes();
        assertNotNull(siblingNodes);
        assertEquals(0, siblingNodes.size());
    }

    @Test
    public void testNextSibling() {
        assertNull(testNode.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        assertNull(testNode.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        assertNull(testNode.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("<tag key1=\"value1\" key2=\"value2\"></tag>", testNode.outerHtml());
    }
}