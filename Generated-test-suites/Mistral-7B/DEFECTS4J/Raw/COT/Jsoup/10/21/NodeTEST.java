import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import static org.jsoup.helper.Jsoup.*;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        Node node = new Node("http://example.com", new Attributes());
        assertNotNull(node);
        assertEquals("http://example.com", node.baseUri());
        assertTrue(node.childNodes().isEmpty());
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testNodeConstructorWithBaseUri() {
        Node node = new Node("http://example.com");
        assertNotNull(node);
        assertEquals("http://example.com", node.baseUri());
        assertTrue(node.childNodes().isEmpty());
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();
        assertNotNull(node);
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeNodeName() {
        Node element = new Element("div");
        assertEquals("div", element.nodeName());

        Node textNode = new TextNode("Hello");
        assertEquals("#text", textNode.nodeName());
    }

    @Test
    public void testGetAttr() {
        Element element = new Element("a").attr("href", "http://example.com");
        assertEquals("http://example.com", element.attr("href"));
        assertEquals("", element.attr("nonExistentAttr"));
    }

    @Test
    public void testAttributes() {
        Element element = new Element("a").attr("href", "http://example.com")
                .attr("class", "exampleClass");
        Attributes attributes = element.attributes();
        assertEquals("href=\"http://example.com\" class=\"exampleClass\"", attributes.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testGetAttrWithNullAttributeKey() {
        Element element = new Element("a");
        element.attr("href", "http://example.com");
        element.getAttr(null);
    }

    @Test
    public void testHasAttr() {
        Element element = new Element("a").attr("href", "http://example.com");
        assertTrue(element.hasAttr("href"));
        assertFalse(element.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        Element element = new Element("a").attr("href", "http://example.com");
        element.removeAttr("href");
        assertTrue(element.attributes().isEmpty());
    }

    @Test
    public void testGetBaseUri() {
        Element element = new Element("a").attr("href", "http://example.com");
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        Element element = new Element("a");
        element.setBaseUri("http://example.com");
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Element aElement = new Element("a").attr("abs:href", "relative.html");
        assertEquals("relative.html", aElement.absUrl("href"));
        assertEquals("http://example.com/relative.html", aElement.absUrl("abs:href"));
    }

    @Test
    public void testChildNode() {
        Document document = new Document();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        root.appendChild(child1);

        Element child2 = document.createElement("child2");
        root.appendChild(child2);

        Node node = root.childNode(1);
        assertEquals(child2, node);
    }

    @Test
    public void testChildren() {
        Document document = new Document();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        root.appendChild(child1);

        Element child2 = document.createElement("child2");
        root.appendChild(child2);

        List<Node> children = root.childNodes();
        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
    }

    @Test
    public void testGetParent() {
        Document document = new Document();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        root.appendChild(child1);

        assertEquals(root, child1.parent());
        assertNull(child1.parent(null));
    }

    @Test
    public void testGetOwnerDocument() {
        Document document = new Document();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        root.appendChild(child1);

        assertEquals(document, child1.ownerDocument());
    }

    @Test
    public void testRemove() {
        Document document = new Document();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        root.appendChild(child1);

        root.removeChild(child1);
        assertTrue(root.childNodes().isEmpty());
        assertNull(child1.parentNode());
    }

    @Test
    public void testReplaceWith() {
        Document document = new Document();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        root.appendChild(child1);

        Element child2 = document.createElement("child2");
        child1.replaceWith(child2);

        assertEquals(child2, root.childNode(0));
        assertEquals(child2, child1.parentNode());
        assertEquals(child1, root.childNode(1));
    }

    @Test
    public void testSetParentNode() {
        Document document = new Document();
        Element parent = document.createElement("parent");
        document.appendChild(parent);

        Element child = document.createElement("child");
        child.setParentNode(parent);

        assertEquals(parent, child.parentNode());
    }

    @Test
    public void testReplaceChild() {
        Document document = new Document();
        Element parent = document.createElement("parent");
        document.appendChild(parent);

        Element child1 = document.createElement("child1");
        parent.appendChild(child1);

        Element child2 = document.createElement("child2");
        parent.replaceChild(child1, child2);

        assertEquals(child2, parent.childNode(0));
        assertEquals(child2, child1.parentNode());
        assertEquals(child1, parent.childNode(1));
    }

    @Test
    public void testRemoveChild() {
        Document document = new Document();
        Element parent = document.createElement("parent");
        document.appendChild(parent);

        Element child1 = document.createElement("child1");
        parent.appendChild(child1);

        parent.removeChild(child1);
        assertTrue(parent.childNodes().isEmpty());
        assertNull(child1.parentNode());
    }

    @Test
    public void testAddChildren() {
        Document document = new Document();
        Element parent = document.createElement("parent");
        document.appendChild(parent);

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");

        parent.addChildren(child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertTrue(parent.childNodes().contains(child1));
        assertTrue(parent.childNodes().contains(child2));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Document document = new Document();
        Element parent = document.createElement("parent");
        document.appendChild(parent);

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");

        parent.addChildren(1, child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertTrue(parent.childNodes().get(0) == child2);
        assertTrue(parent.childNodes().get(1) == child1);
    }
}