package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeName() {
        Node node = new Elements.Element("div");
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("p", "", "", attributes);

        assertEquals("test", node.attr("class"));
        assertEquals("", node.attr("non-existent-attr"));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("p", "", "", attributes);

        List<String> attrNames = new ArrayList<String>() {{
            add("class");
            add("non-existent-attr");
        }};
         for (String attrName : attrNames) {
             String attrValue = node.attr(attrName);
             if (attrName.equals("class")) {
                 assertEquals("test", attrValue);
             } else {
                 assertEquals("", attrValue);
             }
         }

         List<String> attrNames2 = node.attributes().keySet();
         assertEquals(1, attrNames2.size());
         assertEquals("class", attrNames2.get(0));
    }

    @Test
    public void testHasAttr() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("p", "", "", attributes);

        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non-existent-attr"));
    }

    @Test
    public void testRemoveAttr() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("p", "", "", attributes);

        node.removeAttr("class");
        List<String> attrNames = node.attributes().keySet();
        assertTrue(attrNames.isEmpty());
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://test.com";
        Node node = new Element("p", "", baseUri, null);

        assertEquals(baseUri, node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        Node node = new Element("p", null, null, null);
        node.setBaseUri(null);
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("href", "relative.html");
        Node node = new Element("a", "", baseUri, attributes);

        String absUrl = node.absUrl("href");
        assertEquals("http://test.com/relative.html", absUrl);

        String absUrlEmpty = node.absUrl("");
        assertEquals(baseUri, absUrlEmpty);
    }

    @Test
    public void testChildNode() {
        Node parent = new Element("div");
        Node child = new Element("p");
        parent.appendChild(child);

        Node retrievedChild = parent.childNode(0);
        assertEquals(child, retrievedChild);
    }

    @Test
    public void testChildren() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testParent() {
        Node child = new Element("p");
        Node parent = new Element("div");
        parent.appendChild(child);

        assertEquals(parent, child.parent());
        assertEquals(null, child.parent());
    }

    @Test
    public void testOwnerDocument() {
        Node node = new Element("p");
        Document document = new Document();
        document.appendChild(node);

        assertEquals(document, node.ownerDocument());
        assertEquals(null, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Element("div");
        Node child = new Element("p");
        parent.appendChild(child);

        parent.removeChild(child);
        List<Node> children = parent.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("p");
        parent.appendChild(child1);

        parent.replaceChild(child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertEquals(child2, children.get(0));
    }

    @Test
    public void testSetParentNode() {
        Node child = new Element("p");
        Node parent1 = new Element("div");
        Node parent2 = new Element("div");

        child.setParentNode(parent1);
        parent1.appendChild(child);

        child.setParentNode(parent2);
        parent1.removeChild(child);
        parent2.appendChild(child);

        List<Node> children = parent2.childNodes();
        assertEquals(1, children.size());
        assertEquals(child, children.get(0));
    }

    @Test
    public void testReplaceChild() {
        Node parent = new Element("div");
        Node oldChild = new Element("p");
        Node newChild = new Element("p");
        parent.appendChild(oldChild);

        parent.replaceChild(oldChild, newChild);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertEquals(newChild, children.get(0));
    }

    @Test
    public void testAddChildren() {
        Node parent = new Element("div");

        Node child1 = new Element("p");
        Node child2 = new Element("p");
        Node child3 = new Element("p");

        parent.addChildren(child1, child2, child3);
        List<Node> children = parent.childNodes();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node parent = new Element("div");

        Node child1 = new Element("p");
        Node child2 = new Element("p");
        Node child3 = new Element("p");

        parent.addChildren(1, child1, child2, child3);
        List<Node> children = parent.childNodes();
        assertEquals(3, children.size());
        assertEquals(child1, children.get(1));
        assertEquals(child2, children.get(2));
        assertEquals(child3, children.get(0));
    }
}