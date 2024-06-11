package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    private Node root;
    private Node element;
    private Node attribute;

    @Before
    public void setUp() throws Exception {
        String baseUri = "http://example.com";
        root = new Document(baseUri);
        element = new Element("div", baseUri, new Attributes());
        attribute = new Attribute("class", "example", baseUri);
        element.attributes().put("id", "myId");
        element.attr("abs:href", "/page2.html");
        root.appendChild(element);
        element.appendChild(attribute);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testGetAttribute() {
        assertEquals("myId", element.attr("id"));
        assertEquals("/page2.html", element.absUrl("href"));
    }

    @Test
    public void testGetAttributes() {
        Attributes attributes = element.attributes();
        assertNotNull(attributes);
        assertEquals("class=example id=myId abs:href=/page2.html", attributes.toString());
    }

    @Test
    public void testSetAttribute() {
        element.attr("title", "test");
        assertEquals("test", element.attr("title"));
    }

    @Test
    public void testHasAttribute() {
        assertTrue(element.hasAttr("id"));
        assertFalse(element.hasAttr("nonexistent"));
    }

    @Test
    public void testRemoveAttribute() {
        element.removeAttr("id");
        assertFalse(element.hasAttr("id"));
    }

    @Test
    public void testGetBaseUri() {
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String newBaseUri = "http://example2.com";
        element.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, element.baseUri());
    }

    @Test
    public void testAbsUrl() {
        assertEquals("http://example.com/page2.html", element.absUrl("href"));
        assertEquals("http://example.com/page2.html", element.absUrl("abs:href"));
    }

    @Test
    public void testGetChildNode() {
        Node child = element.childNode(0);
        assertEquals(attribute, child);
    }

    @Test
    public void testGetChildren() {
        List<Node> children = new ArrayList<>();
        children.add(element);
        children.add(attribute);
        List<Node> childNodes = root.childNodes();
        assertEquals(children, childNodes);
    }

    @Test
    public void testChildNodesAsArray() {
        Node[] childNodesAsArray = root.childNodesAsArray();
        assertArrayEquals(new Node[]{element, attribute}, childNodesAsArray);
    }

    @Test
    public void testParent() {
        assertEquals(root, element.parent());
        assertEquals(null, attribute.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertEquals(root, element.ownerDocument());
    }

    @Test
    public void testRemove() {
        element.remove();
        assertFalse(root.childNodes().contains(element));
    }

    @Test
    public void testReplaceWith() {
        Node newElement = new Element("p", baseUri, new Attributes());
        element.replaceWith(newElement);
        assertEquals(newElement, root.childNodes().get(0));
    }

    @Test
    public void testSetParentNode() {
        Node newParent = new Element("body", baseUri, new Attributes());
        element.setParentNode(newParent);
        assertEquals(newParent, element.parent());
    }

    @Test
    public void testReplaceChild() {
        Node newChild = new Element("span", baseUri, new Attributes());
        element.replaceChild(attribute, newChild);
        assertEquals(newChild, element.childNodes().get(0));
    }

    @Test
    public void testRemoveChild() {
        element.removeChild(attribute);
        assertFalse(element.childNodes().contains(attribute));
    }

    @Test
    public void testAddChildren() {
        Node newChild1 = new Element("p", baseUri, new Attributes());
        Node newChild2 = new Element("span", baseUri, new Attributes());
        element.addChildren(newChild1, newChild2);
        List<Node> children = element.childNodes();
        List<Node> expected = new ArrayList<>();
        expected.add(newChild1);
        expected.add(newChild2);
        expected.add(attribute); // unchanged
        assertEquals(expected, children);
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node newChild1 = new Element("p", baseUri, new Attributes());
        Node newChild2 = new Element("span", baseUri, new Attributes());
        element.addChildren(0, newChild1, newChild2);
        List<Node> children = element.childNodes();
        List<Node> expected = new ArrayList<>();
        expected.add(newChild1);
        expected.add(newChild2);
        expected.add(attribute);
        assertEquals(expected, children);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenNull() {
        element.addChildren(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenNullElements() {
        Node newChild = new Node();
        element.addChildren(newChild, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveChildNegativeIndex() {
        element.removeChild(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveChildIndexOutOfBounds() {
        element.removeChild(childNodes().size());
    }

    @Test
    public void testAddChildrenEmptyList() {
        element.addChildren(Arrays.asList());
        List<Node> children = element.childNodes();
        assertTrue(children.isEmpty());
    }

    @Test
    public void testAddChildrenNullList() {
        List<Node> nullList = null;
        element.addChildren(nullList);
        List<Node> children = element.childNodes();
        assertTrue(children.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveChildNull() {
        element.removeChild(null);
    }

    @Test
    public void testReplaceChildNull() {
        Node newChild = null;
        element.replaceChild(attribute, newChild);
        assertEquals(attribute, element.childNodes().get(0));
    }

    @Test
    public void testReplaceChildWithSelf() {
        Node newChild = element;
        try {
            element.replaceChild(attribute, newChild);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceChildWithParent() {
        Node parent = root;
        try {
            element.replaceChild(attribute, parent);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceChildWithSibling() {
        Node sibling = root.childNodes().get(1);
        try {
            element.replaceChild(attribute, sibling);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReplaceChildWithSelfSibling() {
        Node sibling = element;
        try {
            element.replaceChild(attribute, sibling);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        assertEquals("<html><head></head><body><div id='myId' class='example' abs:href='/page2.html'></div></body></html>", element.toString());
    }

    @Test
    public void testEquals() {
        Node otherElement = new Element("div", baseUri, new Attributes());
        otherElement.attributes().put("id", "myId");
        otherElement.attr("abs:href", "/page2.html");
        assertTrue(element.equals(otherElement));

        Node otherElement2 = new Element("p", baseUri, new Attributes());
        assertFalse(element.equals(otherElement2));
    }

    @Test
    public void testHashCode() {
        Node otherElement = new Element("div", baseUri, new Attributes());
        otherElement.attributes().put("id", "myId");
        otherElement.attr("abs:href", "/page2.html");
        int elementHashCode = element.hashCode();
        int otherElementHashCode = otherElement.hashCode();
        assertEquals(elementHashCode, otherElementHashCode);

        Node otherElement3 = new Element("div", baseUri, new Attributes());
        otherElement3.attributes().put("id", "otherId");
        otherElement3.attr("abs:href", "/page3.html");
        int otherElement3HashCode = otherElement3.hashCode();
        assertNotEquals(elementHashCode, otherElement3HashCode);

        Node otherElement4 = new Element("div", baseUri, new Attributes());
        otherElement4.attributes().put("id", null);
        otherElement4.attr("abs:href", null);
        int otherElement4HashCode = otherElement4.hashCode();
        assertNotEquals(elementHashCode, otherElement4HashCode);
    }
}