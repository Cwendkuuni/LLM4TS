import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    private Node node;

    @Before
    public void setup() {
        node = new Node("http://www.example.com", new Attributes());
    }

    @Test
    public void testAttr() {
        node.attr("test", "value");
        assertEquals("value", node.attr("test"));
    }

    @Test
    public void testAttributes() {
        assertEquals(0, node.attributes().size());

        node.attr("test", "value");
        assertEquals(1, node.attributes().size());
    }

    @Test
    public void testHasAttr() {
        assertFalse(node.hasAttr("test"));

        node.attr("test", "value");
        assertTrue(node.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("test", "value");
        assertTrue(node.hasAttr("test"));

        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("http://www.example.com", node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        node.setBaseUri("http://www.example.org");
        assertEquals("http://www.example.org", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        node.attr("href", "/link");
        assertEquals("http://www.example.com/link", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node childNode = new Node("http://www.example.com", new Attributes());
        node.addChildren(childNode);

        assertEquals(childNode, node.childNode(0));
    }

    @Test
    public void testChildNodes() {
        Node child1 = new Node("http://www.example.com", new Attributes());
        Node child2 = new Node("http://www.example.com", new Attributes());
        node.addChildren(child1, child2);

        List<Node> children = node.childNodes();
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testParent() {
        Node parent = new Node("http://www.example.com", new Attributes());
        parent.addChildren(node);

        assertEquals(parent, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document("http://www.example.com");
        document.addNode(node);

        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Node("http://www.example.com", new Attributes());
        parent.addChildren(node);

        assertEquals(parent, node.parent());

        node.remove();
        assertNull(node.parent());
    }

    @Test
    public void testReplaceWith() {
        Node newChild = new Node("http://www.example.com", new Attributes());

        Node parent = new Node("http://www.example.com", new Attributes());
        parent.addChildren(node);

        assertEquals(parent, node.parent());

        node.replaceWith(newChild);
        assertNull(node.parent());
        assertEquals(newChild, parent.childNode(0));
    }

    @Test
    public void testSiblingNodes() {
        Node sibling1 = new Node("http://www.example.com", new Attributes());
        Node sibling2 = new Node("http://www.example.com", new Attributes());

        Node parent = new Node("http://www.example.com", new Attributes());
        parent.addChildren(sibling1, node, sibling2);

        List<Node> siblings = node.siblingNodes();
        assertEquals(sibling1, siblings.get(0));
        assertEquals(sibling2, siblings.get(1));
    }

    @Test
    public void testNextSibling() {
        Node sibling1 = new Node("http://www.example.com", new Attributes());
        Node sibling2 = new Node("http://www.example.com", new Attributes());

        Node parent = new Node("http://www.example.com", new Attributes());
        parent.addChildren(sibling1, node, sibling2);

        assertEquals(sibling2, node.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node sibling1 = new Node("http://www.example.com", new Attributes());
        Node sibling2 = new Node("http://www.example.com", new Attributes());

        Node parent = new Node("http://www.example.com", new Attributes());
        parent.addChildren(sibling1, node, sibling2);

        assertEquals(sibling1, node.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        Node sibling1 = new Node("http://www.example.com", new Attributes());
        Node sibling2 = new Node("http://www.example.com", new Attributes());

        Node parent = new Node("http://www.example.com", new Attributes());
        parent.addChildren(sibling1, node, sibling2);

        assertEquals(1, node.siblingIndex().intValue());
    }

    @Test
    public void testOuterHtml() {
        node.attr("test", "value");
        String expectedHtml = "<node test=\"value\"></node>";

        assertEquals(expectedHtml, node.outerHtml());
    }
}