package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    private Parser parser;
    private String html;
    private Document doc;

    @Before
    public void setUp() {
        html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1>" +
                "<p>Test Paragraph <a href=\"http://example.com\">Link</a>.</p>" +
                "</body></html>";
        parser = new Parser(html, "http://example.com", false);
        doc = parser.parse();
    }

    @After
    public void tearDown() {
        parser = null;
        doc = null;
    }

    @Test
    public void testParse() {
        assertNotNull(doc);
        assertEquals(htmlTag, doc.child(0));
        assertEquals(headTag, doc.child(0).child(0));
        assertEquals(titleTag, doc.child(0).child(0).child(0));
        assertEquals("Test Title", doc.child(0).child(0).child(0).child(0).text());

        assertEquals(bodyTag, doc.child(1));
        assertEquals(h1Tag, doc.child(1).child(0));
        assertEquals("Test Heading", doc.child(1).child(0).text());
        assertEquals(pTag, doc.child(1).child(1));
        assertEquals(aTag, doc.child(1).child(1).child(0));
        assertEquals("Link", doc.child(1).child(1).child(0).child(0).text());
        assertEquals("http://example.com", doc.child(1).child(1).child(0).absUrl("href"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Paragraph <a href=\"http://example.com\">Link</a></p>";
        Document bodyDoc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertNotNull(bodyDoc);
        assertEquals(htmlTag, bodyDoc.child(0));
        assertEquals(bodyTag, bodyDoc.child(1));
        assertEquals(pTag, bodyDoc.child(1).child(0));
        assertEquals("Test Paragraph", bodyDoc.child(1).child(0).text());
        assertEquals(aTag, bodyDoc.child(1).child(0).child(0));
        assertEquals("Link", bodyDoc.child(1).child(0).child(0).child(0).text());
        assertEquals("http://example.com", bodyDoc.child(1).child(0).child(0).absUrl("href"));
    }

    @Test
    public void testParseInvalidHtml() {
        String invalidHtml = "<html><body><script><!--<![CDATA[//]]>--></script>";

        try {
            Parser parser = new Parser(invalidHtml, "http://example.com", false);
            parser.parse();
            fail("Expected an exception");
        } catch (RuntimeException e) {
            // expected
        }
    }
}