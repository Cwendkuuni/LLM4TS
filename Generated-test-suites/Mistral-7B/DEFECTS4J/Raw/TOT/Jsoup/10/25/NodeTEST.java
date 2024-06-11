package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for {@link Node} abstract class.
 */
public class NodeTest {

    private Node rootNode;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("class", "example");
        rootNode = new Element("html", baseUri, attributes);

        Element headElement = new Element("head", baseUri, attributes);
        Element titleElement = new Element("title", baseUri, attributes);
        titleElement.text("Example Page");
        headElement.appendText("\n");
        headElement.appendChild(titleElement);
        rootNode.appendChild(headElement);

        Element bodyElement = new Element("body", baseUri, attributes);
        Element pElement = new Element("p", baseUri, attributes);
        pElement.text("This is an example page.");
        bodyElement.appendChild(pElement);
        rootNode.appendChild(bodyElement);
    }

    /**
     * Test getting the node name.
     */
    @Test
    public void testNodeName() {
        assertEquals("html", rootNode.nodeName());
        assertEquals("head", headElement.nodeName());
        assertEquals("title", titleElement.nodeName());
        assertEquals("body", bodyElement.nodeName());
        assertEquals("p", pElement.nodeName());
    }

    /**
     * Test getting attributes.
     */
    @Test
    public void testGetAttribute() {
        assertEquals("example", rootNode.attr("class"));
        assertEquals("http://example.com", rootNode.baseUri());
        assertEquals("", rootNode.attr("nonExistentAttribute"));

        Element linkElement = new Element("a", "http://google.com", attributes());
        linkElement.attr("href", "http://example.org");
        rootNode.appendChild(linkElement);

        assertEquals("http://example.org", rootNode.absUrl("a[href]"));
    }

    /**
     * Test setting attributes.
     */
    @Test
    public void testSetAttribute() {
        Element divElement = new Element("div", baseUri, attributes());
        divElement.attr("id", "exampleDiv");
        assertEquals("exampleDiv", divElement.attr("id"));

        divElement.attr("class", "newClass");
        List<String> classes = new ArrayList<>();
        classes.add("example");
        classes.add("newClass");
        assertEquals(classes, divElement.classes());
    }

    /**
     * Test removing attributes.
     */
    @Test
    public void testRemoveAttribute() {
        Element divElement = new Element("div", baseUri, attributes());
        divElement.attr("class", "example example2");
        divElement.removeAttr("class");
        assertTrue(divElement.isEmpty());

        divElement.attr("class", "example");
        List<String> classes = new ArrayList<>();
        classes.add("example");
        assertEquals(classes, divElement.classes());
    }

    /**
     * Test getting base URI.
     */
    @Test
    public void testBaseUri() {
        assertEquals("http://example.com", rootNode.baseUri());

        Element anchorElement = new Element("a", "http://google.com", attributes());
        rootNode.appendChild(anchorElement);

        assertEquals("http://example.com", rootNode.baseUri());
    }

    /**
     * Test setting base URI.
     */
    @Test
    public void testSetBaseUri() {
        Element anchorElement = new Element("a", "http://google.com", attributes());
        rootNode.appendChild(anchorElement);

        rootNode.setBaseUri("http://example.org");
        assertEquals("http://example.org", rootNode.baseUri());
        assertEquals("http://example.org/google.com", anchorElement.absUrl("href"));
    }

    /**
     * Test getting child nodes.
     */
    @Test
    public void testChildNodes() {
        Element headElement = (Element) rootNode.childNodes().get(0);
        assertEquals(1, headElement.childNodes().size());
        assertEquals("title", headElement.childNodes().get(0).nodeName());

        Element titleElement = (Element) headElement.childNodes().get(0);
        assertEquals("Example Page", titleElement.text());
        assertEquals(0, titleElement.childNodes().size());

        Element bodyElement = (Element) rootNode.childNodes().get(1);
        assertEquals(1, bodyElement.childNodes().size());
        assertEquals("p", bodyElement.childNodes().get(0).nodeName());

        Element pElement = (Element) bodyElement.childNodes().get(0);
        assertEquals("This is an example page.", pElement.text());
        assertEquals(0, pElement.childNodes().size());
    }

    /**
     * Test getting the parent node.
     */
    @Test
    public void testParentNode() {
        Element headElement = (Element) rootNode.childNodes().get(0);
        assertEquals(rootNode, headElement.parent());

        Element titleElement = (Element) headElement.childNodes().get(0);
        assertEquals(headElement, titleElement.parent());

        Element bodyElement = (Element) rootNode.childNodes().get(1);
        assertEquals(rootNode, bodyElement.parent());

        Element pElement = (Element) bodyElement.childNodes().get(0);
        assertEquals(bodyElement, pElement.parent());
    }

    /**
     * Test getting the document associated with this node.
     */
    @Test
    public void testOwnerDocument() {
        assertEquals(rootNode, rootNode.ownerDocument());

        Document document = new Document();
        Element newRootNode = new Element("html", "", attributes());
        document.appendChild(newRootNode);

        Element newAnchorElement = new Element("a", "", attributes());
        newRootNode.appendChild(newAnchorElement);

        assertEquals(document, newAnchorElement.ownerDocument());
    }

    /**
     * Test removing a node from the DOM tree.
     */
    @Test
    public void testRemove() {
        Element headElement = (Element) rootNode.childNodes().get(0);
        Element titleElement = (Element) headElement.childNodes().get(0);

        rootNode.removeChild(headElement);
        assertEquals(1, rootNode.childNodes().size());
        assertEquals("body", rootNode.childNodes().get(0).nodeName());

        rootNode.remove();
        assertTrue(rootNode.parentNode() == null);
    }

    /**
     * Test replacing a node in the DOM tree.
     */
    @Test
    public void testReplaceWith() {
        Element pElement = (Element) bodyElement.childNodes().get(0);
        Element newPElement = new Element("p", baseUri, attributes());
        newPElement.text("This is a new paragraph.");

        rootNode.replaceChild(pElement, newPElement);
        assertEquals(1, bodyElement.childNodes().size());
        assertEquals("p", bodyElement.childNodes().get(0).nodeName());
        assertEquals("This is a new paragraph.", bodyElement.childNodes().get(0).text());
    }

    /**
     * Test adding children to a node.
     */
    @Test
    public void testAddChildren() {
        Element newPElement1 = new Element("p", baseUri, attributes());
        newPElement1.text("This is a new paragraph 1.");

        Element newPElement2 = new Element("p", baseUri, attributes());
        newPElement2.text("This is a new paragraph 2.");

        bodyElement.addChildren(newPElement1, newPElement2);
        assertEquals(3, bodyElement.childNodes().size());
        assertEquals("p", bodyElement.childNodes().get(1).nodeName());
        assertEquals("p", bodyElement.childNodes().get(2).nodeName());

        Element newDivElement = new Element("div", baseUri, attributes());
        newDivElement.appendChild(newPElement1);
        newDivElement.appendChild(newPElement2);
        bodyElement.replaceChild(bodyElement.childNodes().get(1), newDivElement);

        assertEquals(2, bodyElement.childNodes().size());
        assertEquals("div", bodyElement.childNodes().get(1).nodeName());
    }

    /**
     * Test sibling nodes.
     */
    @Test
    public void testSiblingNodes() {
        Element pElement1 = new Element("p", baseUri, attributes());
        pElement1.text("This is a new paragraph 1.");

        Element pElement2 = new Element("p", baseUri, attributes());
        pElement2.text("This is a new paragraph 2.");
        bodyElement.appendChild(pElement2);

        Element pElement3 = new Element("p", baseUri, attributes());
        pElement3.text("This is a new paragraph 3.");
        bodyElement.appendChild(pElement3);
        bodyElement.appendChild(pElement1);

        Element pElement4 = new Element("p", baseUri, attributes());
        pElement4.text("This is a new paragraph 4.");
        bodyElement.appendChild(pElement4);

        Element nextSibling = pElement1.nextSibling();
        assertEquals(pElement3, nextSibling);

        Element previousSibling = pElement1.previousSibling();
        assertEquals(pElement2, previousSibling);

        Element firstSibling = pElement2.previousSibling();
        assertEquals(null, firstSibling);

        Element lastSibling = pElement3.nextSibling();
        assertEquals(pElement4, lastSibling);

        Element siblingWithSameParent = pElement2;
        Element siblingWithDifferentParent = new Element("div", baseUri, attributes());
        assertNotSame(siblingWithSameParent, siblingWithDifferentParent);
    }

    /**
     * Test getting text from a node.
     */
    @Test
    public void testText() {
        Element pElement = new Element("p", baseUri, attributes());
        pElement.text("This is a test.");

        Element textNode = pElement.childNodes().get(0);
        assertEquals("This is a test.", textNode.text());
        assertEquals(1, textNode.childNodes().size());
        assertEquals(0, textNode.attributes().size());

        assertEquals("This is a test.", pElement.text());
        assertEquals(1, pElement.childNodes().size());

        textNode = new TextNode("This is a test.");
        pElement.appendChild(textNode);
        assertEquals("This is a test.", pElement.text());
        assertEquals(1, pElement.childNodes().size());
    }

    /**
     * Test the toString() method.
     */
    @Test
    public void testToString() {
        Element pElement = new Element("p", baseUri, attributes());
        pElement.text("This is a test.");

        assertEquals("<p></p>", pElement.toString());

        pElement.attr("class", "test");
        assertEquals("<p class=\"test\"></p>", pElement.toString());

        pElement.attr("id", "test");
        assertEquals("<p class=\"test\" id=\"test\"></p>", pElement.toString());

        pElement.attr("href", "test.html");
        assertEquals("<p class=\"test\" id=\"test\" href=\"test.html\"></p>", pElement.toString());
    }

    /**
     * Test cloning a node.
     */
    @Test
    public void testClone() {
        Element pElement = new Element("p", baseUri, attributes());
        pElement.text("This is a test.");

        Element clonedElement = pElement.clone();
        assertEquals("This is a test.", clonedElement.text());

        pElement.attr("class", "test");
        clonedElement.attr("class", "clonedTest");
        assertEquals("clonedTest", clonedElement.attr("class"));

        pElement.appendChild(new TextNode("This is a child."));
        clonedElement.appendChild(new TextNode("This is a cloned child."));
        assertEquals("This is a cloned child.", clonedElement.childNodes().get(0).text());

        Element clonedPElement = (Element) clonedElement.clone();
        assertEquals("This is a cloned child.", clonedPElement.childNodes().get(0).text());
    }
}