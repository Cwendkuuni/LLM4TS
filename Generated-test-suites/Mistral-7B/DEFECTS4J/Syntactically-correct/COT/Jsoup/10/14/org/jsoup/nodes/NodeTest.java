package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NodeTest {
    private Node node;
    private String baseUri;
    private Attributes attributes;

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"http://example.com", new Attributes()},
                {"http://example.com", new Attributes().put("id", "element1")},
                {"http://example.com", new Attributes().put("id", "element1").put("class", "element2")},
                {"http://example.com", new Attributes().put("id", "element1").put("class", "element2").put("style", "color:red")},
                {null, new Attributes()}
        });
    }

    @Before
    public void setUp() {
        baseUri = "http://example.com";
        attributes = new Attributes();
        node = new Element("div", baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        node.attr("id", "element3");
        assertEquals("element3", node.attr("id"));
        assertEquals("", node.attr("nonExistentAttr"));
        assertEquals("", node.attr("abs:nonExistentAbsAttr"));
    }

    @Test
    public void testAttributes() {
        node.attr("id", "element3");
        assertEquals(attributes, node.attributes());
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("id", "element3");
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testAbsUrl() {
        node.attr("href", "/path/to/file.html");
        assertEquals("http://example.com/path/to/file.html", node.absUrl("href"));
        assertEquals("", node.absUrl("nonExistentAttr"));
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlMalformedUrl() {
        node.attr("href", "//example.com/path/to/file.html");
        node.absUrl("href");
    }

    @Test
    public void testGetChildNode() {
        Element child = new Element("p", baseUri, new Attributes());
        node.appendChild(child);
        Node childNode = node.childNode(0);
        assertSame(child, childNode);
    }

    @Test
    public void testGetChildren() {
        Element child1 = new Element("p", baseUri, new Attributes());
        Element child2 = new Element("p", baseUri, new Attributes());
        node.appendChild(child1);
        node.appendChild(child2);
        List<Node> children = node.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testGetParent() {
        Element parent = new Element("body", baseUri, attributes);
        parent.appendChild(node);
        assertEquals(parent, node.parent());
        assertNull(node.parent());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetParentNull() {
        node.setParentNode(null);
    }

    @Test
    public void testReplaceChild() {
        Element child1 = new Element("p", baseUri, new Attributes());
        Element child2 = new Element("p", baseUri, new Attributes());
        node.appendChild(child1);
        node.replaceChild(child1, child2);
        assertEquals(child2, node.childNode(0));
    }

    @Test
    public void testRemoveChild() {
        Element child = new Element("p", baseUri, new Attributes());
        node.appendChild(child);
        node.removeChild(child);
        assertFalse(node.hasChildNodes());
    }

    @Test
    public void testAddChildren() {
        Element child1 = new Element("p", baseUri, new Attributes());
        Element child2 = new Element("p", baseUri, new Attributes());
        node.addChildren(child1, child2);
        assertEquals(2, node.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenNullChild() {
        node.addChildren(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenNullChildrenArray() {
        node.addChildren(null, new Node[0]);
    }

    @Test
    public void testClone() {
        Element clone = node.clone();
        assertEquals(node.toString(), clone.toString());
        assertEquals(node.baseUri(), clone.baseUri());
        assertFalse(node.equals(clone));
        assertEquals(node.childNodes().size(), clone.childNodes().size());
        for (int i = 0; i < node.childNodes().size(); i++) {
            Node nodeChild = node.childNodes().get(i);
            Node cloneChild = clone.childNodes().get(i);
            assertEquals(nodeChild.toString(), cloneChild.toString());
            assertFalse(nodeChild.equals(cloneChild));
        }
    }
}