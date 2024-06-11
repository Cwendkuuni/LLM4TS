import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attribute;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        Node node = new Element("div", baseUri, attributes);

        assertNotNull(node);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testAttributeGetters() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        attributes.put("id", "example");

        Node node = new Element("div", baseUri, attributes);

        assertEquals("example", node.attr("class"));
        assertEquals("example", node.attr("class").getValue());
        assertEquals("example", node.attr("class").toString());
        assertEquals("example", node.attr("class").get());

        assertEquals("example", node.attr("id").getValue());
        assertEquals("example", node.attr("id").toString());
        assertEquals("example", node.attr("id").get());

        assertNull(node.attr("nonexistent"));
        assertFalse(node.hasAttr("nonexistent"));

        String absUrl = node.absUrl("href");
        assertEquals("", absUrl);

        Node child = new TextNode("child");
        node.appendChild(child);
        absUrl = node.absUrl("href");
        assertEquals("https://example.com/child", absUrl);
    }

    @Test
    public void testAttributeSetters() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node node = new Element("div", baseUri, attributes);

        node.attr("class", "example");
        node.attr("class", "new-example");
        node.attr("class", Arrays.asList("old-example", "new-example"));
        assertEquals(Arrays.asList("new-example", "old-example"), node.attributes().getList("class"));

        node.attr("id", "example");
        assertEquals("example", node.attr("id"));
        assertEquals("example", node.attr("id").getValue());
        assertEquals("example", node.attr("id").toString());
        assertEquals("example", node.attr("id").get());

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
        assertNull(node.attr("id"));

        node.attr("class", null);
        assertFalse(node.hasAttr("class"));
        assertNull(node.attr("class"));

        node.attr("nonexistent", "nonexistent-value");
        assertTrue(node.hasAttr("nonexistent"));
        assertEquals("nonexistent-value", node.attr("nonexistent"));
    }

    @Test
    public void testAttributeRemoval() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");

        Node node = new Element("div", baseUri, attributes);

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
        assertNull(node.attr("class"));

        node.removeAttr("nonexistent");
        assertFalse(node.hasAttr("nonexistent"));
        assertNull(node.attr("nonexistent"));
    }

    @Test
    public void testChildNodes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri, attributes);

        Node child1 = new Element("p", baseUri, new Attributes());
        Node child2 = new Element("p", baseUri, new Attributes());

        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertSame(child1, childNodes.get(0));
        assertSame(child2, childNodes.get(1));

        Node child3 = new Element("p", baseUri, new Attributes());
        parent.insertBefore(child3, child1);

        childNodes = parent.childNodes();
        assertEquals(3, childNodes.size());
        assertSame(child3, childNodes.get(0));
        assertSame(child1, childNodes.get(1));
        assertSame(child2, childNodes.get(2));

        Node child4 = new Element("p", baseUri, new Attributes());
        parent.replaceChild(child4, child2);

        childNodes = parent.childNodes();
        assertEquals(3, childNodes.size());
        assertSame(child3, childNodes.get(0));
        assertSame(child4, childNodes.get(1));
        assertSame(child1, childNodes.get(2));

        parent.removeChild(child1);

        childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertSame(child3, childNodes.get(0));
        assertSame(child4, childNodes.get(1));
    }

    @Test
    public void testParentNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node child = new Element("p", baseUri, attributes);
        Node parent = new Element("div", baseUri, attributes);

        parent.appendChild(child);

        assertEquals(parent, child.parentNode());
        assertSame(child, parent.childNodes().get(0));

        parent.removeChild(child);
        assertNull(child.parentNode());
    }

    @Test
    public void testDocument() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node node = new Element("div", baseUri, attributes);

        Document document = new Document(baseUri);
        document.appendChild(node);

        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);

        parent.appendChild(node);

        parent.remove();

        assertEquals(null, node.parentNode());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri, attributes);
        Node oldNode = new Element("p", baseUri, attributes);
        Node newNode = new Element("p", baseUri, attributes);

        parent.appendChild(oldNode);
        parent.replaceChild(newNode, oldNode);

        assertEquals(newNode, parent.childNodes().get(0));
        assertSame(parent, newNode.parentNode());
        assertNull(oldNode.parentNode());
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node node = new Element("p", baseUri, attributes);

        Node parent1 = new Element("div", baseUri, attributes);
        Node parent2 = new Element("div", baseUri, attributes);

        node.setParentNode(parent1);
        assertEquals(parent1, node.parentNode());

        node.setParentNode(parent2);
        assertEquals(parent2, node.parentNode());
    }

    @Test
    public void testSetSiblingIndex() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri, attributes);
        Node sibling1 = new Element("p", baseUri, attributes);
        Node sibling2 = new Element("p", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);

        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);

        node.setSiblingIndex(1);

        assertEquals(sibling1, node.previousSibling());
        assertEquals(sibling2, node.nextSibling());
    }

    @Test
    public void testSiblingNodes() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Element("div", baseUri, attributes);
        Node sibling1 = new Element("p", baseUri, attributes);
        Node node = new Element("p", baseUri, attributes);
        Node sibling2 = new Element("p", baseUri, attributes);

        parent.appendChild(sibling1);
        parent.appendChild(node);
        parent.appendChild(sibling2);

        List<Node> siblings = parent.siblingNodes();

        assertEquals(3, siblings.size());
        assertSame(sibling1, siblings.get(0));
        assertSame(node, siblings.get(1));
        assertSame(sibling2, siblings.get(2));

        assertEquals(sibling1, node.previousSibling());
        assertEquals(sibling2, node.nextSibling());

        parent.removeChild(sibling1);

        siblings = parent.siblingNodes();

        assertEquals(2, siblings.size());
        assertSame(node, siblings.get(0));
        assertSame(sibling2, siblings.get(1));
    }

    @Test
    public void testClone() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();

        Node node = new Element("div", baseUri, attributes);
        Node clone = node.cloneNode(true); // deep clone

        assertEquals(node.nodeName(), clone.nodeName());
        assertEquals(node.baseUri(), clone.baseUri());
        assertEquals(node.attributes(), clone.attributes());
        assertEquals(node.childNodes(), clone.childNodes());

        Node child1 = new Element("p", baseUri, attributes);
        Node child2 = new Element("p", baseUri, attributes);

        node.appendChild(child1);
        node.appendChild(child2);

        assertEquals(node.childNodes(), node.childNodesAsArray());
        assertEquals(node.childNodes(), node.childNodesAsArray());

        Node cloneChild1 = clone.childNodes().get(0);
        Node cloneChild2 = clone.childNodes().get(1);

        assertEquals(node.childNodes(), clone.childNodes());
        assertEquals(node.childNodes(), clone.childNodes());

        assertEquals(child1, cloneChild1);
        assertEquals(child2, cloneChild2);

        Node newChild = new Element("p", baseUri, attributes);
        node.appendChild(newChild);

        assertNotSame(newChild, cloneChild1);
        assertNotSame(newChild, cloneChild2);
    }
}