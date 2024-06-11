package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testAbstractNodeMethods() {
        String baseUri = "https://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node1 = new Element("div", baseUri, attributes);
        Node node2 = new Element("span", baseUri, attributes);

        assertEquals("div", node1.nodeName());
        assertEquals("node1", node1.attr("id"));
        assertEquals(baseUri, node1.baseUri());

        assertNull(node1.childNodes().size());
        assertNull(node1.parent());

        node1.attr("class", "myClass");
        assertEquals("myClass", node1.attr("class"));

        node1.removeAttr("id");
        assertFalse(node1.hasAttr("id"));

        assertEquals(baseUri, node1.absUrl(""));
        assertEquals("https://example.com/class=myClass", node1.absUrl("class"));
    }

    @Test
    public void testNodeWithChildren() {
        String baseUri = "https://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "parent");

        Node parent = new Element("div", baseUri, attributes);

        Attributes childAttributes = new Attributes();
        childAttributes.put("class", "child");

        Node child = new Element("p", baseUri, childAttributes);
        child.appendText("Child text");

        parent.appendChild(child);

        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertEquals("p", children.get(0).nodeName());
        assertEquals("Child text", children.get(0).text());
        assertEquals("child", children.get(0).attr("class"));

        assertEquals(1, parent.childNodes().size());
        assertSame(child, parent.childNodes().get(0));
        assertEquals("div", parent.nodeName());
        assertEquals("parent", parent.attr("id"));
        assertEquals(baseUri, parent.baseUri());
    }

    @Test
    public void testNodeWithMultipleChildren() {
        String baseUri = "https://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "parent");

        Node parent = new Element("div", baseUri, attributes);

        Attributes child1Attributes = new Attributes();
        child1Attributes.put("class", "child1");

        Node child1 = new Element("p", baseUri, child1Attributes);
        child1.appendText("Child 1 text");

        Attributes child2Attributes = new Attributes();
        child2Attributes.put("class", "child2");

        Node child2 = new TextNode("Child 2 text");

        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals("p", children.get(0).nodeName());
        assertEquals("Child 1 text", children.get(0).text());
        assertEquals("child1", children.get(0).attr("class"));

        assertEquals("Text", children.get(1).nodeName());
        assertEquals("Child 2 text", children.get(1).text());
        assertNull(children.get(1).attributes());

        assertEquals(2, parent.childNodes().size());
        assertSame(child1, parent.childNodes().get(0));
        assertSame(child2, parent.childNodes().get(1));
        assertEquals("div", parent.nodeName());
        assertEquals("parent", parent.attr("id"));
        assertEquals(baseUri, parent.baseUri());
    }

    @Test
    public void testNodeWithParent() {
        String baseUri = "https://example.com/";
        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "parent");

        Node parent = new Element("div", baseUri, parentAttributes);

        Attributes childAttributes = new Attributes();
        childAttributes.put("class", "child");

        Node child = new Element("p", baseUri, childAttributes);
        child.appendText("Child text");

        parent.appendChild(child);

        assertEquals(1, parent.childNodes().size());
        assertSame(child, parent.childNodes().get(0));
        assertEquals("parent", parent.attr("id"));
        assertEquals(baseUri, parent.baseUri());

        assertEquals(parent, child.parent());
        assertEquals("div", child.parentNode().nodeName());
        assertEquals("parent", child.parentNode().attr("id"));
        assertEquals(baseUri, child.parentNode().baseUri());
    }

    @Test
    public void testNodeWithMultipleParents() {
        String baseUri = "https://example.com/";
        Attributes parent1Attributes = new Attributes();
        parent1Attributes.put("id", "parent1");

        Node parent1 = new Element("div", baseUri, parent1Attributes);

        Attributes parent2Attributes = new Attributes();
        parent2Attributes.put("id", "parent2");

        Node parent2 = new Element("div", baseUri, parent2Attributes);
        parent1.appendChild(parent2);

        Attributes childAttributes = new Attributes();
        childAttributes.put("class", "child");

        Node child = new Element("p", baseUri, childAttributes);
        child.appendText("Child text");

        parent2.appendChild(child);

        assertEquals(1, parent2.childNodes().size());
        assertSame(child, parent2.childNodes().get(0));
        assertEquals("parent2", parent2.attr("id"));
        assertEquals(baseUri, parent2.baseUri());

        assertEquals(1, parent1.childNodes().size());
        assertSame(parent2, parent1.childNodes().get(0));
        assertEquals("parent1", parent1.attr("id"));
        assertEquals(baseUri, parent1.baseUri());

        assertEquals(parent1, child.parentNode());
        assertEquals("div", child.parentNode().nodeName());
        assertEquals("parent1", child.parentNode().attr("id"));
        assertEquals(baseUri, child.parentNode().baseUri());
    }

    @Test
    public void testNodeWithSiblings() {
        String baseUri = "https://example.com/";
        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "parent");

        Node parent = new Element("div", baseUri, parentAttributes);

        Attributes child1Attributes = new Attributes();
        child1Attributes.put("class", "child1");

        Node child1 = new Element("p", baseUri, child1Attributes);
        child1.appendText("Child 1 text");

        Attributes child2Attributes = new Attributes();
        child2Attributes.put("class", "child2");

        Node child2 = new Element("p", baseUri, child2Attributes);
        child2.appendText("Child 2 text");

        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> siblings = child1.siblingNodes();
        assertEquals(1, siblings.size());
        assertSame(child2, siblings.get(0));

        List<Node> siblings2 = child2.siblingNodes();
        assertEquals(1, siblings2.size());
        assertSame(child1, siblings2.get(0));

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertSame(child1, children.get(0));
        assertSame(child2, children.get(1));

        assertEquals(child1, child2.previousSibling());
        assertEquals(child2, child1.nextSibling());
    }

    @Test
    public void testNodeWithNextAndPreviousSiblings() {
        String baseUri = "https://example.com/";
        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "parent");

        Node parent = new Element("div", baseUri, parentAttributes);

        Attributes child1Attributes = new Attributes();
        child1Attributes.put("class", "child1");

        Node child1 = new Element("p", baseUri, child1Attributes);
        child1.appendText("Child 1 text");

        Attributes child2Attributes = new Attributes();
        child2Attributes.put("class", "child2");

        Node child2 = new Element("p", baseUri, child2Attributes);
        child2.appendText("Child 2 text");

        Node child3 = new Element("p", baseUri, null);
        child3.appendText("Child 3 text");

        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.appendChild(child3);

        List<Node> siblings = child1.siblingNodes();
        assertEquals(2, siblings.size());
        assertSame(child2, siblings.get(0));
        assertSame(child3, siblings.get(1));

        List<Node> siblings2 = child2.siblingNodes();
        assertEquals(1, siblings2.size());
        assertSame(child3, siblings2.get(0));

        List<Node> children = parent.childNodes();
        assertEquals(3, children.size());
        assertSame(child1, children.get(0));
        assertSame(child2, children.get(1));
        assertSame(child3, children.get(2));

        assertEquals(child1, child2.previousSibling());
        assertEquals(child3, child2.nextSibling());
        assertEquals(child1, child3.previousSibling());
    }

    @Test
    public void testNodeCloning() {
        String baseUri = "https://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node1 = new Element("div", baseUri, attributes);
        Node node2 = node1.cloneNode();

        assertEquals("div", node1.nodeName());
        assertEquals("div", node2.nodeName());
        assertEquals("node1", node1.attr("id"));
        assertEquals("node1", node2.attr("id"));
        assertEquals(baseUri, node1.baseUri());
        assertEquals(baseUri, node2.baseUri());

        Attributes node2Attributes = node2.attributes();
        assertSame(attributes, node2Attributes);

        Node child1 = new Element("p", baseUri, null);
        child1.appendText("Child text");

        node1.appendChild(child1);

        assertEquals(1, node1.childNodes().size());
        assertSame(child1, node1.childNodes().get(0));

        Node clonedChild = node1.cloneNode(true);
        Node clonedChildParent = new Element("div", baseUri, null);
        clonedChildParent.appendChild(clonedChild);

        assertEquals(1, node1.childNodes().size());
        assertEquals(1, clonedChildParent.childNodes().size());
        assertSame(child1, node1.childNodes().get(0));
        assertSame(child1, clonedChildParent.childNodes().get(0));
    }

}