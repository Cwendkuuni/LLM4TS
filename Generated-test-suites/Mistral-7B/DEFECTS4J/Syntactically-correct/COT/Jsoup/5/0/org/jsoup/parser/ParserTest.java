package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.LinkedList;

/**
 * ParserTest class for testing Parser methods.
 */
public class ParserTest {

    private static final String HTML_FRAGMENT_1 = "<html><body><p>Hello <world/></p></body></html>";
    private static final String HTML_FRAGMENT_2 = "<html><head><title>Test</title></head><body><p>Hello <world/></p></body></html>";
    private static final String HTML_FRAGMENT_3 = "<html><body><script type=\"text/javascript\">function test() { alert('Hello World'); }</script></body></html>";
    private static final String HTML_FRAGMENT_4 = "<html><body><textarea>Hello World</textarea></body></html>";
    private static final String HTML_FRAGMENT_5 = "<html><head><base href=\"test.html\"/><body><a href=\"index.html\">Link</a></body></html>";

    @ org.junit.Test
    public void testParse() {
        Document doc = Parser.parse(HTML_FRAGMENT_1, "test.html");
        assertEquals("test.html", doc.baseUri());
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(bodyTag, doc.body());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(pTag, doc.body().child(0).tag());
        assertEquals("Hello ", doc.body().child(0).text().text());
        assertEquals("world", doc.body().child(0).child(0).tag().tagName());
    }

    @ org.junit.Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment(HTML_FRAGMENT_2, "test.html");
        assertEquals("test.html", doc.baseUri());
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.child(0).tag());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(pTag, doc.body().child(0).tag());
        assertEquals("Hello ", doc.body().child(0).text().text());
        assertEquals("world", doc.body().child(0).child(0).tag().tagName());
    }

    @ org.junit.Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed(HTML_FRAGMENT_2, "test.html");
        assertEquals("test.html", doc.baseUri());
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.childNodes().size());
        assertEquals(bodyTag, doc.child(0).tag());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(pTag, doc.body().child(0).tag());
        assertEquals("Hello ", doc.body().child(0).text().text());
        assertEquals("world", doc.body().child(0).child(0).tag().tagName());
    }

    @ org.junit.Test(expected = AssertionError.class)
    public void testParseEmptyConstructor() {
        new Parser("");
    }

    @ org.junit.Test(expected = AssertionError.class)
    public void testParseNullBaseUri() {
        new Parser(null);
    }

    @ org.junit.Test
    public void testParseStartTag() {
        Parser parser = new Parser("<p id=\"test\"><i>Hello</i></p>", "test.html");
        assertEquals(1, parser.stack.size());
        assertEquals(htmlTag, parser.stack.getLast().tag());
        parser.parse();

        assertEquals(2, parser.stack.size());
        assertEquals(headTag, parser.stack.getLast().tag());
        parser.parse();

        assertEquals(3, parser.stack.size());
        assertEquals(bodyTag, parser.stack.getLast().tag());
        parser.parse();

        assertEquals(4, parser.stack.size());
        assertEquals(pTag, parser.stack.getLast().tag());
        parser.parse();

        assertEquals(5, parser.stack.size());
        assertEquals(iTag, parser.stack.getLast().tag());
        parser.parse();

        assertEquals(6, parser.stack.size());
        assertEquals(textNode, parser.stack.getLast().tag().tagName());
        parser.parse();

        assertEquals(5, parser.stack.size());
        assertTrue(parser.stack.getLast().tag().childNodes().size() > 0);
    }

    @ org.junit.Test
    public void testParseComment() {
        Parser parser = new Parser("<!DOCTYPE html><html><!-- This is a comment --><body><p>Test</p></body></html>", "test.html");
        parser.parse();
        assertEquals(1, parser.stack.size());
        assertEquals(htmlTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(2, parser.stack.size());
        assertEquals(headTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(3, parser.stack.size());
        assertEquals(bodyTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(4, parser.stack.size());
        assertEquals(commentTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(3, parser.stack.size());
    }

    @ org.junit.Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<!DOCTYPE html SYSTEM \"about:legacy-compat\">"
                + "<html><head><title>Test</title></head><body><p>Test</p></body></html>", "test.xml");
        parser.parse();
        assertEquals(1, parser.stack.size());
        assertEquals(htmlTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(2, parser.stack.size());
        assertEquals(headTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(3, parser.stack.size());
        assertEquals(bodyTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(4, parser.stack.size());
        assertEquals(xmlDeclarationTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(3, parser.stack.size());
    }

    @ org.junit.Test(expected = AssertionError.class)
    public void testParseInvalidEndTag() {
        Parser parser = new Parser("<p><i>Hello</i></p><div/>", "test.html");
        parser.parse();
        parser.parse();
        parser.parse();
        parser.parse();
        parser.parse();
        parser.parse();

        // Exception is thrown here
        parser.parse();
    }

    @ org.junit.Test(expected = AssertionError.class)
    public void testParseEndTagMissingTag() {
        Parser parser = new Parser("<p><i>Hello</i></p></div>", "test.html");
        parser.parse();
        parser.parse();
        parser.parse();
        parser.parse();
        parser.parse();
        parser.parse();

        // Exception is thrown here
        parser.parse();
    }

    @ org.junit.Test(expected = AssertionError.class)
    public void testParseUnknownTag() {
        Parser parser = new Parser("<p><unknownTag/></p>", "test.html");
        parser.parse();
        parser.parse();

        // Exception is thrown here
        parser.parse();
    }

    @ org.junit.Test(expected = AssertionError.class)
    public void testParseInvalidStartTag() {
        Parser parser = new Parser("<p><script type=\"text/javascript\">Hello</script></p>", "test.html");
        parser.parse();
        parser.parse();

        // Exception is thrown here
        parser.parse();
    }

    @ org.junit.Test
    public void testParseTextNode() {
        Parser parser = new Parser("<p>Hello World</p>", "test.html");
        parser.parse();
        assertEquals(1, parser.stack.size());
        assertEquals(htmlTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(2, parser.stack.size());
        assertEquals(headTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(3, parser.stack.size());
        assertEquals(bodyTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(4, parser.stack.size());
        assertEquals(pTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(5, parser.stack.size());
        assertEquals(TextNode.class, parser.stack.getLast().getClass());
        assertEquals("Hello World", parser.stack.getLast().toString());
        parser.parse();
        assertEquals(4, parser.stack.size());
    }

    @ org.junit.Test
    public void testParseDataNode() {
        Parser parser = new Parser("<p><textarea>Hello World</textarea></p>", "test.html");
        parser.parse();
        assertEquals(1, parser.stack.size());
        assertEquals(htmlTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(2, parser.stack.size());
        assertEquals(headTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(3, parser.stack.size());
        assertEquals(bodyTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(4, parser.stack.size());
        assertEquals(pTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(5, parser.stack.size());
        assertEquals(DataNode.class, parser.stack.getLast().getClass());
        assertEquals("Hello World", parser.stack.getLast().toString());
        parser.parse();
        assertEquals(4, parser.stack.size());
    }

    @ org.junit.Test
    public void testParseEmptyElement() {
        Parser parser = new Parser("<img src=\"image.jpg\"/>", "test.html");
        parser.parse();
        assertEquals(1, parser.stack.size());
        assertEquals(htmlTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(2, parser.stack.size());
        assertEquals(headTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(3, parser.stack.size());
        assertEquals(bodyTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(4, parser.stack.size());
        assertEquals(imgTag, parser.stack.getLast().tag());
        parser.parse();
        assertEquals(3, parser.stack.size());
    }

    @ org.junit.Test
    public void testParseBaseTag() {
        Document doc = Parser.parseBodyFragment(HTML_FRAGMENT_5, "test.html");
        assertEquals("test.html", doc.baseUri());
        assertEquals("index.html", doc.body().child(0).tag().tagName());
    }

    @ org.junit.Test
    public void testParseUnknownTagRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<html><body><unknownTag/></body></html>", "test.html");
        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals("unknownTag", doc.body().child(0).tag().tagName());
    }
}