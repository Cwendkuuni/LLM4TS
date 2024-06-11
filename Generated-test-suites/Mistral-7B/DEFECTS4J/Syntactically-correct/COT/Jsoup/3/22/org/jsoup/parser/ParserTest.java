package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * ParserTest.java - JUnit 4 test file for Parser class.
 */
public class ParserTest {

    private static final String HTML_WITH_COMMENT = "<html><!-- Test Comment --></html>";
    private static final String HTML_WITH_XML_DECL = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">";
    private static final String HTML_WITH_IMG_TAG = "<html><img src=\"image.png\" alt=\"Test Image\"/><body></body></html>";
    private static final String HTML_WITH_INVALID_TAG = "<html><invalidtag attr=\"value\"/></html>";
    private static final String HTML_WITH_EMPTY_TAG = "<html><img/></html>";
    private static final String HTML_WITH_SCRIPT_TAG = "<html><script src=\"script.js\"></script></html>";
    private static final String HTML_WITH_TITLE_TAG = "<html><head><title>Test Title</title></head><body></body></html>";
    private static final String HTML_WITH_BASE_TAG = "<html><base href=\"http://test.com\"/><body></body></html>";
    private static final String HTML_FRAGMENT = "<html><body></body></html>";

    private Document parsedDocument;
    private Document parsedBodyFragment;

    @Before
    public void setUp() {
        parsedDocument = Parser.parse(HTML_WITH_COMMENT, "http://test.com");
        parsedBodyFragment = Parser.parseBodyFragment(HTML_FRAGMENT, "http://test.com");
    }

    @Test
    public void testParse() {
        Document document = Parser.parse(HTML_WITH_XML_DECL, "http://test.com");
        assertNotNull(document);
        assertEquals("html", document.tagName());
    }

    @Test
    public void testParseBodyFragment() {
        assertEquals("body", parsedBodyFragment.tagName());
        assertEquals("html", parsedBodyFragment.head().tagName());
        assertEquals("html", parsedBodyFragment.getElementsByTag("html").first().tagName());
    }

    @Test
    public void testParseComment() {
        Element comment = parsedDocument.getElementsByTag("comment").first();
        assertNotNull(comment);
        assertEquals("Test Comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        XmlDeclaration xmlDeclaration = parsedDocument.getElementsByTag("xml-declaration").first();
        assertNotNull(xmlDeclaration);
        assertTrue(xmlDeclaration.isStandalone());
        assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.getNamespace());
        assertEquals("en", xmlDeclaration.getLanguage());
    }

    @Test
    public void testParseEndTag() {
        Element imgElement = parsedDocument.getElementsByTag("img").first();
        Element imgParent = imgElement.parent();

        Parser.parseEndTag(parsedDocument, "img");

        assertSame(imgParent, imgElement.parent());
        assertEquals(0, imgParent.children().size());
    }

    @Test
    public void testParseStartTag() {
        Element imgElement = parsedDocument.getElementsByTag("img").first();
        Element imgParent = imgElement.parent();

        Parser.parseStartTag(parsedDocument, "img", new Attributes());

        assertSame(imgParent, imgElement.parent());
        assertEquals(1, imgParent.children().size());
        assertEquals("img", imgParent.children().get(0).tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStartTagWithNullTagName() {
        Parser.parseStartTag(parsedDocument, null, new Attributes());
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStartTagWithEmptyTagName() {
        Parser.parseStartTag(parsedDocument, "", new Attributes());
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testParseAttribute() {
        Element imgElement = parsedDocument.getElementsByTag("img").first();
        Attribute attribute = Parser.parseAttribute(parsedDocument);

        assertNull(attribute);

        attribute = Parser.parseAttribute(parsedDocument, "src");
        assertNotNull(attribute);
        assertEquals("src", attribute.getKey());
    }

    @Test
    public void testParseTextNode() {
        Element body = parsedDocument.getElementsByTag("body").first();
        TextNode textNode = (TextNode) body.getElementsByClass("testText").first();
        String originalText = textNode.text();

        Parser.parseTextNode(parsedDocument);

        TextNode parsedTextNode = (TextNode) body.getElementsByClass("testText").first();
        assertEquals(originalText, parsedTextNode.text());
    }

    @Test
    public void testParseCdata() {
        Element body = parsedDocument.getElementsByTag("body").first();
        DataNode dataNode = (DataNode) body.getElementsByClass("testCdata").first();
        String originalData = dataNode.text();

        Parser.parseCdata(parsedDocument);

        DataNode parsedDataNode = (DataNode) body.getElementsByClass("testCdata").first();
        assertEquals(originalData, parsedDataNode.text());
    }

    @Test
    public void testParseEmptyElement() {
        Element imgElement = parsedDocument.getElementsByTag("img").first();
        Element imgParent = imgElement.parent();

        Parser.parseStartTag(parsedDocument, "img", new Attributes());
        Parser.parseEndTag(parsedDocument, "img");

        assertSame(imgParent, imgElement.parent());
        assertEquals(0, imgParent.children().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseInvalidTag() {
        Parser.parseStartTag(parsedDocument, "invalidtag", new Attributes());
        fail("IllegalStateException expected but not thrown");
    }

    @Test
    public void testStackHasValidParent() {
        List<Element> elements = new LinkedList<Element>() {{
            add(new Element(htmlTag, "http://test.com"));
            add(new Element(bodyTag, "http://test.com"));
            add(new Element(pTag, "http://test.com"));
            add(new Element(textareaTag, "http://test.com"));
        }};

        Stack<Element> stack = new LinkedList<Element>(Arrays.asList(elements.subList(0, 3)));
        assertFalse(Parser.stackHasValidParent(textareaTag, stack));
        assertTrue(Parser.stackHasValidParent(pTag, stack));
        assertTrue(Parser.stackHasValidParent(bodyTag, stack));
        assertTrue(Parser.stackHasValidParent(htmlTag, stack));
    }

    @Test
    public void testAddChildToParent() {
        Element parent = new Element(bodyTag, "http://test.com");
        Element child = new Element(pTag, "http://test.com");

        Element result = Parser.addChildToParent(child, false);
        assertEquals(parent, result);
        assertEquals(1, parent.children().size());

        child = new Element(textareaTag, "http://test.com");
        result = Parser.addChildToParent(child, false);
        assertEquals(parent, result);
        assertEquals(2, parent.children().size());

        result = Parser.addChildToParent(child, true);
        assertEquals(parent, result);
        assertEquals(1, parent.children().size());
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Stack<Element> stack = new LinkedList<Element>() {{
            add(new Element(bodyTag, "http://test.com"));
            add(new Element(divTag, "http://test.com"));
            add(new Element(pTag, "http://test.com"));
            add(new Element(ulTag, "http://test.com"));
        }};

        Element result = Parser.popStackToSuitableContainer(htmlTag);
        assertEquals(null, result);

        result = Parser.popStackToSuitableContainer(bodyTag);
        assertEquals(stack.get(0), result);
        assertEquals(3, stack.size());

        result = Parser.popStackToSuitableContainer(divTag);
        assertEquals(stack.get(1), result);
        assertEquals(2, stack.size());

        result = Parser.popStackToSuitableContainer(pTag);
        assertEquals(stack.get(2), result);
        assertEquals(1, stack.size());

        result = Parser.popStackToSuitableContainer(ulTag);
        assertEquals(stack.get(3), result);
        assertEquals(0, stack.size());
    }

    @Test
    public void testPopStackToClose() {
        Stack<Element> stack = new LinkedList<Element>() {{
            add(new Element(htmlTag, "http://test.com"));
            add(new Element(bodyTag, "http://test.com"));
            add(new Element(divTag, "http://test.com"));
            add(new Element(pTag, "http://test.com"));
            add(new Element(imgTag, "http://test.com"));
        }};

        Element result = Parser.popStackToClose(htmlTag);
        assertEquals(null, result);

        result = Parser.popStackToClose(imgTag);
        assertEquals(stack.get(4), result);
        assertEquals(4, stack.size());

        result = Parser.popStackToClose(divTag);
        assertEquals(stack.get(3), result);
        assertEquals(3, stack.size());

        result = Parser.popStackToClose(pTag);
        assertEquals(stack.get(2), result);
        assertEquals(2, stack.size());

        result = Parser.popStackToClose(bodyTag);
        assertEquals(stack.get(1), result);
        assertEquals(1, stack.size());

        result = Parser.popStackToClose(htmlTag);
        assertEquals(stack.get(0), result);
        assertEquals(0, stack.size());
    }

    @Test
    public void testLast() {
        Stack<Element> stack = new LinkedList<Element>() {{
            add(new Element(htmlTag, "http://test.com"));
            add(new Element(bodyTag, "http://test.com"));
            add(new Element(divTag, "http://test.com"));
            add(new Element(pTag, "http://test.com"));
        }};

        Element result = Parser.last(stack);
        assertEquals(stack.get(stack.size()-1), result);
    }
}