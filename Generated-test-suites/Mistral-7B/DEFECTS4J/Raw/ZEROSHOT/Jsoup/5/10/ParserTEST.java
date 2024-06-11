package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.LinkedList;

/**
 * ParserTest class to test the Parser class.
 */
public class ParserTest {

    private Parser parser;
    private Document doc;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1><p>Test Paragraph.</p>" +
                "<textarea>Test TextArea</textarea>" +
                "</body></html>";

        parser = new Parser(html, "", false);
        doc = parser.parse();
    }

    /**
     * Test the parse() method.
     */
    @Test
    public void testParse() {
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());

        Element htmlNode = doc.childNodes().get(0);
        assertEquals(headTag, htmlNode.tagName());
        assertEquals(1, htmlNode.childNodes().size());

        Element headNode = htmlNode.childNodes().get(0);
        assertEquals(titleTag, headNode.tagName());
        assertEquals("Test Title", headNode.text());

        Element bodyNode = doc.getElementsByTag("body").get(0);
        assertEquals(4, bodyNode.childNodes().size());

        Element h1Node = (Element) bodyNode.childNodes().get(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Test Heading", h1Node.text());

        Element pNode = (Element) bodyNode.childNodes().get(2);
        assertEquals("p", pNode.tagName());
        assertEquals("Test Paragraph.", pNode.text());

        Element textareaNode = (Element) bodyNode.childNodes().get(3);
        assertEquals("textarea", textareaNode.tagName());
        assertEquals("Test TextArea", textareaNode.text());
    }

    /**
     * Test the parseBodyFragment() method.
     */
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Paragraph.</p>";

        Document doc = Parser.parseBodyFragment(bodyHtml, "");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());

        Element htmlNode = doc.childNodes().get(0);
        assertEquals(bodyTag, htmlNode.tagName());
        assertEquals(1, htmlNode.childNodes().size());

        Element pNode = htmlNode.childNodes().get(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Test Paragraph.", pNode.text());
    }

    /**
     * Test the parseBodyFragmentRelaxed() method.
     */
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Test Paragraph.</p>";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());

        Element htmlNode = doc.childNodes().get(0);
        assertEquals(bodyTag, htmlNode.tagName());
        assertEquals(1, htmlNode.childNodes().size());

        Element pNode = htmlNode.childNodes().get(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Test Paragraph.", pNode.text());
    }
}