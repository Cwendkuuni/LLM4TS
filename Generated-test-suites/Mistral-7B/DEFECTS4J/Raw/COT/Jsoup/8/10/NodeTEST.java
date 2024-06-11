package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Node class.
 */
public class NodeTest {

    private Node textNode;
    private Node elementNode;
    private Document document;

    @Before
    public void setUp() {
        document = new Document();
        textNode = new TextNode("Text Node", document);
        elementNode = new Element("TestElement", document) {{
            attributes().put("test", "attribute");
        }};
        document.body().appendChild(elementNode);
        elementNode.appendChild(textNode);
    }

    

    @Test
    public void testNodeName() {
        assertEquals("Text Node", textNode.nodeName());
        assertEquals("TestElement", elementNode.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("attribute", elementNode.attr("test"));
        assertNull(elementNode.attr("nonExistingAttr"));
        assertEquals("", elementNode.attr(""));
    }

    @Test
    public void testAttributes() {
        Attributes attributes = elementNode.attributes();
        assertEquals("attribute", attributes.get("test"));
        assertEquals(1, attributes.size());
    }

    @Test
    public void testHasAttr() {
        assertTrue(elementNode.hasAttr("test"));
        assertFalse(elementNode.hasAttr("nonExistingAttr"));
    }

    @Test
    public void testSetAttr() {
        elementNode.attr("newTest", "value");
        assertEquals("value", elementNode.attr("newTest"));
    }

    @Test
    public void testRemoveAttr() {
        elementNode.removeAttr("test");
        assertFalse(elementNode.hasAttr("test"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", textNode.baseUri());
        String baseUri = "http://example.com";
        document.setBaseUri(new URL(baseUri));
        assertEquals(baseUri, textNode.baseUri());
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlThrowsException() {
        textNode.absUrl("nonExistingAttribute");
    }

    @Test
    public void testAbsUrlRelative() {
        String absUrl = textNode.absUrl("href");
        assertTrue(absUrl.contains("href"));
        assertEquals("", absUrl.substring(0, baseUri.length()));
    }

    @Test
    public void testAbsUrlAbsolute() {
        String absUrl = textNode.absUrl("http://example.com/path");
        assertEquals("http://example.com/path", absUrl);
    }

    @Test
    public void testChildNode() {
        Node child = textNode.childNode(0);
        assertSame(textNode, child.parentNode());
        assertSame(elementNode, textNode.parentNode());
    }

    @Test
    public void testChildNodes() {
        List<Node> expectedChildren = new ArrayList<Node>() {{
            add(textNode);
        }};
        List<Node> actualChildren = elementNode.childNodes();
        assertEquals(1, actualChildren.size());
        assertEquals(expectedChildren, actualChildren);
    }

    @Test
    public void testParent() {
        assertSame(elementNode, textNode.parentNode());
        assertNull(textNode.parentNode());
    }

    @Test
    public void testOwnerDocument() {
        assertSame(document, textNode.ownerDocument());
        assertSame(document, elementNode.ownerDocument());
    }

    @Test
    public void testRemove() {
        List<Node> childrenBeforeRemoval = new ArrayList<Node>() {{
            addAll(elementNode.childNodes());
        }};
        elementNode.remove();
        List<Node> childrenAfterRemoval = new ArrayList<Node>() {{
            addAll(document.childNodes());
        }};
        assertEquals(0, childrenAfterRemoval.size());
        assertTrue(childrenBeforeRemoval.contains(elementNode));
        assertFalse(childrenAfterRemoval.contains(elementNode));
    }

    @Test
    public void testReplaceWith() {
        Node replacement = new Element("TestReplacement", document);
        elementNode.replaceWith(replacement);
        List<Node> childrenBeforeReplacement = new ArrayList<Node>() {{
            addAll(elementNode.siblingNodes());
        }};
        List<Node> childrenAfterReplacement = new ArrayList<Node>() {{
            addAll(replacement.siblingNodes());
        }};
        assertEquals(childrenBeforeReplacement, childrenAfterReplacement);
    }

    @Test
    public void testSetParentNode() {
        Node newParent = new Element("NewParent", document);
        textNode.setParentNode(newParent);
        assertEquals(newParent, textNode.parentNode());
    }

    @Test
    public void testReplaceChild() {
        Node replacedNode = new Element("ReplacedNode", document);
        elementNode.replaceChild(textNode, replacedNode);
        List<Node> childrenBeforeReplacement = new ArrayList<Node>() {{
            addAll(elementNode.childNodes());
        }};
        List<Node> childrenAfterReplacement = new ArrayList<Node>() {{
            addAll(elementNode.childNodes());
        }};
        childrenAfterReplacement.remove(textNode);
        childrenAfterReplacement.add(0, replacedNode);
        assertEquals(childrenAfterReplacement, childrenBeforeReplacement);
    }

    @Test
    public void testRemoveChild() {
        List<Node> childrenBeforeRemoval = new ArrayList<Node>() {{
            addAll(elementNode.childNodes());
        }};
        elementNode.removeChild(textNode);
        List<Node> childrenAfterRemoval = new ArrayList<Node>() {{
            addAll(elementNode.childNodes());
        }};
        assertEquals(childrenAfterRemoval, childrenBeforeRemoval);
        assertFalse(childrenAfterRemoval.contains(textNode));
    }

    @Test
    public void testAddChildren() {
        Node newChild = new Element("NewChild", document);
        elementNode.addChildren(newChild);
        List<Node> childrenBeforeAddition = new ArrayList<Node>() {{
            addAll(elementNode.childNodes());
        }};
        List<Node> childrenAfterAddition = new ArrayList<Node>() {{
            addAll(elementNode.childNodes());
        }};
        childrenAfterAddition.add(newChild);
        assertEquals(childrenAfterAddition, childrenBeforeAddition);
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node newChild = new Element("NewChild", document);
        int index = 0;
        elementNode.addChildren(index, newChild);
        List<Node> childrenBeforeAddition = new ArrayList<Node>() {{
            addAll(elementNode.childNodes());
        }};
        List<Node> childrenAfterAddition = new ArrayList<Node>() {{
            addAll(elementNode.childNodes());
        }};
        childrenAfterAddition.set(index, newChild);
        assertEquals(childrenAfterAddition, childrenBeforeAddition);
    }

    @Test
    public void testOuterHtml() {
        StringBuilder accum = new StringBuilder(128);
        textNode.outerHtml(accum);
        assertEquals("Text Node", accum.toString());
        elementNode.outerHtml(accum);
        assertEquals("<TestElement test=\"attribute\">Text Node</TestElement>", accum.toString());
    }

    
}