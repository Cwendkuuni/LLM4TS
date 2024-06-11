
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    private Node testNode;
    private Node parent;
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("class", "testClass");
        attributes.put("data-test", "test");
        
        testNode = new Element("testNode", attributes);
        parent = new Element("parent");
        parent.appendChild(testNode);
    }

    @Test
    public void testAttr() {
        assertEquals("testId", testNode.attr("id"));
        assertEquals("testClass", testNode.attr("class"));
        assertEquals("test", testNode.attr("data-test"));
        assertEquals("", testNode.attr("testAttr"));
        assertEquals("http://example.com", testNode.attr("abs:href"));
        assertEquals("", testNode.attr("abs:test"));
    }

    @Test
    public void testAttributes() {
        assertEquals(3, testNode.attributes().size());
        assertTrue(testNode.attributes().hasKey("id"));
        assertTrue(testNode.attributes().hasKey("class"));
        assertTrue(testNode.attributes().hasKey("data-test"));
        assertFalse(testNode.attributes().hasKey("testAttr"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(testNode.hasAttr("id"));
        assertTrue(testNode.hasAttr("class"));
        assertTrue(testNode.hasAttr("data-test"));
        assertFalse(testNode.hasAttr("testAttr"));
    }

    @Test
    public void testRemoveAttr() {
        assertTrue(testNode.hasAttr("id"));
        testNode.removeAttr("id");
        assertFalse(testNode.hasAttr("id"));

        assertTrue(testNode.hasAttr("class"));
        testNode.removeAttr("class");
        assertFalse(testNode.hasAttr("class"));

        assertTrue(testNode.hasAttr("data-test"));
        testNode.removeAttr("data-test");
        assertFalse(testNode.hasAttr("data-test"));
    }

    @Test
    public void testBaseUri() {
        assertNull(testNode.baseUri());
        testNode.setBaseUri("http://example.com");
        assertEquals("http://example.com", testNode.baseUri());
    }

    @Test
    public void testAbsUrl() {
        assertEquals("http://example.com", testNode.absUrl("href"));
        assertEquals("", testNode.absUrl("test"));
    }

    @Test
    public void testChildNode() {
        assertEquals(testNode, parent.childNode(0));
        assertNull(parent.childNode(1));
    }

    @Test
    public void testChildNodes() {
        List<Node> childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(testNode, childNodes.get(0));
    }

    @Test
    public void testParent() {
        assertEquals(parent, testNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertNull(testNode.ownerDocument());
        
        Document document = new Document("");
        Element root = document.appendElement("root");
        Node child = new Element("child");
        root.appendChild(child);

        assertEquals(document, child.ownerDocument());
    }

    @Test
    public void testRemove() {
        assertTrue(parent.childNodes().contains(testNode));
        testNode.remove();
        assertFalse(parent.childNodes().contains(testNode));
    }

    @Test
    public void testReplaceWith() {
        Node newNode = new Element("newNode");
        testNode.replaceWith(newNode);
        assertFalse(parent.childNodes().contains(testNode));
        assertTrue(parent.childNodes().contains(newNode));
    }

    @Test
    public void testSiblingNodes() {
        List<Node> siblingNodes = testNode.siblingNodes();
        assertEquals(1, siblingNodes.size());
        assertEquals(testNode, siblingNodes.get(0));
    }

    @Test
    public void testNextSibling() {
        assertNull(testNode.nextSibling());

        Node newSibling = new Element("newSibling");
        parent.appendChild(newSibling);

        assertEquals(newSibling, testNode.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        assertNull(testNode.previousSibling());

        Node newSibling = new Element("newSibling");
        parent.insertBefore(newSibling, testNode);

        assertEquals(newSibling, testNode.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        assertEquals(0, testNode.siblingIndex());
    }

    @Test
    public void testEquals() {
        Element clone = (Element) testNode.clone();
        assertTrue(testNode.equals(clone));
    }

    @Test
    public void testHashCode() {
        Element clone = (Element) testNode.clone();
        assertEquals(testNode.hashCode(), clone.hashCode());
    }
}
