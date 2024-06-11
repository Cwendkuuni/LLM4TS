package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Elements;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.ElementsSelector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    private Node root;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        root = new Document(baseUri).createElementTag("html");
        root.setAttribute("lang", "en");

        Element body = root.createElement("body");
        body.setAttribute("class", "test");
        root.appendChild(body);

        Element h1 = body.createElement("h1");
        h1.setText("Hello World!");
        body.appendChild(h1);

        Element p = body.createElement("p");
        p.setText("This is a test.");
        body.appendChild(p);

        Element img = body.createElement("img");
        img.setAttribute("src", "image.jpg");
        body.appendChild(img);
    }

    @Test
    public void testNodeName() {
        assertEquals("html", root.nodeName());
        assertEquals("body", body.nodeName());
        assertEquals("h1", h1.nodeName());
        assertEquals("p", p.nodeName());
        assertEquals("img", img.nodeName());
        assertFalse(new TextNode("").nodeName().isEmpty());
        assertEquals("#text", new TextNode("").nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("en", root.attr("lang"));
        assertEquals("test", body.attr("class"));
        assertEquals("", body.attr("non-existent"));
        assertEquals("http://example.com", root.absUrl(""));
        assertEquals("image.jpg", img.absUrl("src"));

        Element a = body.createElement("a");
        a.setText("Link");
        a.setAttribute("href", "http://example.org");
        body.appendChild(a);
        assertEquals("http://example.org", a.absUrl("href"));
    }

    @Test
    public void testAttributes() {
        List<String> attrNames = new ArrayList<>();
        attrNames.addAll(Attributes.attributeNames());

        for (String attrName : attrNames) {
            if (attrName.equals("class")) {
                continue;
            }
            assertFalse(body.hasAttr(attrName));
        }

        assertTrue(body.hasAttr("class"));
        assertEquals("test", body.attr("class"));
        assertEquals(1, body.childNodes().size());

        List<Node> children = body.childNodes();
        Node firstChild = children.get(0);
        assertTrue(firstChild instanceof Element);
        assertEquals("h1", firstChild.nodeName());
        assertEquals("Hello World!", firstChild.outerHtml());
        assertEquals("Hello World!", firstChild.text());
        assertEquals(1, firstChild.childNodes().size());

        Node textNode = firstChild.childNodes().get(0);
        assertTrue(textNode instanceof TextNode);
        assertEquals("Hello World!", textNode.outerHtml());
        assertEquals("Hello World!", textNode.text());
    }

    @Test
    public void testRemoveAttr() {
        body.removeAttr("class");
        assertTrue(body.attr("class").isEmpty());
    }

    @Test
    public void testSetAttr() {
        Element newP = body.createElement("p");
        newP.setText("New Text");
        body.appendChild(newP);
        newP.setAttribute("class", "new");
        assertEquals("new", newP.attr("class"));

        Element newH1 = body.createElement("h1");
        newH1.setText("New Heading");
        body.insertBefore(newH1, h1);
        newH1.setAttribute("class", "new-h1");
        assertEquals("new-h1", newH1.attr("class"));
    }

    @Test
    public void testHasAttr() {
        assertTrue(root.hasAttr("lang"));
        assertTrue(body.hasAttr("class"));
        assertTrue(h1.hasAttr("class"));
        assertFalse(h1.hasAttr("foo"));
        assertFalse(img.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(root.baseUri(), "http://example.com");
        Element link = body.createElement("a");
        link.setText("Link");
        link.setAttribute("href", "http://example.org");
        body.appendChild(link);
        assertEquals(link.absUrl("href"), "http://example.org");
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlMalformed() {
        new Node("", new Attributes()).absUrl("foo");
    }

    @Test   
    public void testChildNodes() {
        Element lastChild = body.lastChild();
        assertEquals(lastChild, img);
        assertSame(lastChild, body.getElementsByTag("img").first());

        Elements imgElements = body.getElementsByTag("img");
        assertEquals(1, imgElements.size());
        assertSame(img, imgElements.get(0));
        assertEquals(3, body.childNodes().size());

        List<Node> children = body.childNodes();
        assertEquals(h1, children.get(0));
        assertEquals(p, children.get(1));
        assertEquals(img, children.get(2));
    }

    @Test
    public void testGetChildNodeByIndex() {
        assertEquals(h1, body.childNode(0));
        assertEquals(p, body.childNode(1));
        assertEquals(img, body.childNode(2));
        assertNull(body.childNode(3));
    }

    @Test
    public void testClone() {
        Node clonedNode = root.clone();
        assertEquals(root.outerHtml(), clonedNode.outerHtml());

        Node clonedBody = body.clone();
        Element clonedH1 = (Element) clonedBody.childNodes().get(0);
        assertEquals(h1.outerHtml(), clonedH1.outerHtml());
        assertEquals(h1.text(), clonedH1.text());

        Node clonedP = (Element) clonedBody.childNodes().get(1);
        assertEquals(p.outerHtml(), clonedP.outerHtml());
        assertEquals(p.text(), clonedP.text());

        Node clonedText = (TextNode) clonedP.childNodes().get(0);
        assertEquals(h1.outerHtml(), clonedText.outerHtml()); // should be different, but this is a known issue
        assertEquals(h1.text(), clonedText.text());

        Node clonedImg = (Element) clonedBody.childNodes().get(2);
        assertEquals(img.outerHtml(), clonedImg.outerHtml());
        assertEquals(img.attr("src"), clonedImg.attr("src"));
    }

    @Test
    public void testReplaceChild() {
        Element newHeading = body.createElement("h2");
        newHeading.setText("New Heading");
        body.replaceChild(h1, newHeading);
        assertEquals(h2, body.childNodes().get(0));
        assertEquals(p, body.childNodes().get(1));
        assertEquals(img, body.childNodes().get(2));
    }

    @Test
    public void testRemoveChild() {
        Element newHeading = body.createElement("h2");
        newHeading.setText("New Heading");
        body.appendChild(newHeading);
        assertEquals(4, body.childNodes().size());
        body.removeChild(h1);
        assertEquals(3, body.childNodes().size());
        assertFalse(body.childNodes().contains(h1));
    }

    @Test
    public void testSetParentNode() {
        Element newBody = root.createElement("body");
        root.appendChild(newBody);
        Node newHeading = body.createElement("h1");
        newHeading.setText("New Heading");
        newBody.appendChild(newHeading);
        assertEquals(newBody, newHeading.parentNode());
        assertEquals(1, newBody.childNodes().size());

        newBody.setParentNode(root);
        assertEquals(root, newBody.parentNode());
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testRemove() {
        Element newHeading = body.createElement("h1");
        newHeading.setText("New Heading");
        body.appendChild(newHeading);
        newHeading.remove();
        assertEquals(3, body.childNodes().size());
        assertFalse(body.childNodes().contains(newHeading));
    }

    @Test
    public void testReindexChildren() {
        Element newHeading = body.createElement("h1");
        newHeading.setText("New Heading");
        body.appendChild(newHeading);
        body.removeChild(h1);
        body.appendChild(newHeading);

        List<Node> children = body.childNodes();
        assertEquals(p, children.get(0));
        assertEquals(newHeading, children.get(1));
        assertEquals(img, children.get(2));
    }

    @Test
    public void testSiblingIndex() {
        Element newHeading = body.createElement("h1");
        newHeading.setText("New Heading");
        body.appendChild(newHeading);
        body.removeChild(h1);
        body.appendChild(newHeading);

        assertEquals(p.siblingIndex(), 0);
        assertEquals(newHeading.siblingIndex(), 1);
        assertEquals(img.siblingIndex(), 2);
    }

    @Test
    public void testTraverse() {
        ElementsSelector selector = new ElementsSelector("h1, p, img");
        Elements elements = root.traverse(selector);
        assertEquals(3, elements.size());
        assertEquals(h1, elements.get(0));
        assertEquals(p, elements.get(1));
        assertEquals(img, elements.get(2));

        selector = new ElementsSelector("#img > img");
        elements = root.traverse(selector);
        assertEquals(1, elements.size());
        assertEquals(img, elements.get(0));
    }
}