package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Page</title></head><body><p><textarea id='textarea'>Hello World</textarea></p></body></html>";
        String baseUri = "http://example.com/";
        parser = new Parser(html, baseUri, false);
    }

    @After
    public void tearDown() {
        parser = null;
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertTrue(doc.body().hasText());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p><textarea id='textarea'>Hello World</textarea></p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertTrue(doc.body().hasText());
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        assertNotNull(parser.last().child(0));
        assertTrue(parser.last().child(0).nodeName().equals("#comment"));
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        assertNotNull(parser.last().child(0));
        assertTrue(parser.last().child(0).nodeName().equals("xml-declaration"));
    }

    @Test
    public void testParseEndTag() {
        parser.parseStartTag();
        parser.parseEndTag();
        assertNotNull(parser.last().previousSibling());
        assertTrue(parser.last().previousSibling().nodeName().equals(parser.last().tag().tagName()));
    }

    @Test
    public void testParseStartTag() {
        String tagName = "html";
        parser.parseStartTag();
        assertNotNull(parser.last());
        assertTrue(parser.last().tagName().equals(tagName));
    }

    @Test
    public void testParseAttribute() {
        parser.parseStartTag();
        Attribute attribute = parser.parseAttribute();
        assertTrue(attribute != null);
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
        assertNotNull(parser.last().child(0));
        assertTrue(parser.last().child(0).nodeName().equals("#text"));
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
        assertNotNull(parser.last().child(0));
        assertTrue(parser.last().child(0).nodeName().equals("#cdata"));
    }

    @Test
    public void testAddChildToParent() {
        String tagName = "p";
        Element child = new Element(Tag.valueOf(tagName), baseUri);
        Element parent = new Element(Tag.valueOf("body"), baseUri);
        Element root = parser.addChildToParent(child, false);
        assertNotNull(root);
        assertTrue(root.child(0).nodeName().equals(tagName));
    }

    @Test
    public void testStackHasValidParent() {
        String tagName1 = "p";
        String tagName2 = "div";
        String tagName3 = "html";

        List<Element> stack = new LinkedList<>();
        stack.add(new Element(Tag.valueOf(tagName3), baseUri));
        stack.add(new Element(Tag.valueOf(tagName2), baseUri));

        boolean validParent = parser.stackHasValidParent(Tag.valueOf(tagName1));
        assertFalse(validParent);

        stack.add(new Element(Tag.valueOf(tagName1), baseUri));
        validParent = parser.stackHasValidParent(Tag.valueOf(tagName1));
        assertTrue(validParent);

        stack.remove(stack.size() - 1);
        validParent = parser.stackHasValidParent(Tag.valueOf(tagName1));
        assertFalse(validParent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        String tagName1 = "div";
        String tagName2 = "p";
        String tagName3 = "body";
        String tagName4 = "html";

        List<Element> stack = new LinkedList<>();
        stack.add(new Element(Tag.valueOf(tagName4), baseUri));
        stack.add(new Element(Tag.valueOf(tagName3), baseUri));
        stack.add(new Element(Tag.valueOf(tagName2), baseUri));
        stack.add(new Element(Tag.valueOf(tagName1), baseUri));

        Element container = parser.popStackToSuitableContainer(Tag.valueOf(tagName2));
        assertTrue(container != null);
        assertTrue(container.tagName().equals(tagName3));

        container = parser.popStackToSuitableContainer(Tag.valueOf(tagName3));
        assertTrue(container != null);
        assertTrue(container.tagName().equals(tagName4));

        container = parser.popStackToSuitableContainer(Tag.valueOf(tagName1));
        assertTrue(container != null);
        assertTrue(container.tagName().equals(tagName4));

        container = parser.popStackToSuitableContainer(Tag.valueOf(tagName4));
        assertNull(container);
    }

    @Test
    public void testPopStackToClose() {
        String tagName1 = "body";
        String tagName2 = "html";

        List<Element> stack = new LinkedList<>();
        stack.add(new Element(Tag.valueOf(tagName1), baseUri));
        stack.add(new Element(Tag.valueOf(tagName2), baseUri));

        Element elToClose = parser.popStackToClose(Tag.valueOf(tagName1));
        assertTrue(elToClose != null);
        assertTrue(elToClose.tagName().equals(tagName1));

        stack.remove(stack.size() - 1);
        elToClose = parser.popStackToClose(Tag.valueOf(tagName1));
        assertNull(elToClose);
    }
}