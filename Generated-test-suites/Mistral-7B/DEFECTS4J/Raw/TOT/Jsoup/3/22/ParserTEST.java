package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.jsoup.parse.Parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        String html = "<html><head><title>Test Parser</title></head><body>" +
                "<p>This is a test <b>paragraph</b> with an <a href=\"test.html\">anchor</a>.</p>" +
                "<img src=\"image.jpg\" alt=\"Image\">" +
                "<form method=\"post\">" +
                "<input type=\"text\" name=\"username\">" +
                "<input type=\"password\" name=\"password\">" +
                "<button type=\"submit\">Submit</button>" +
                "</form>" +
                "</body></html>";
        String baseUri = "http://example.com";
        this.parser = new Parser(html, baseUri, false);
    }

    @Test
    public void testParseBasicHtml() {
        Document doc = parser.parse();
        assertEquals("Test Parser", doc.title());
        Element body = doc.body();
        assertEquals(14, body.childNodeSize());

        // Check all child types
        assertEquals(Element.Tag.HTML, body.child(0).tag());
        assertEquals(Element.Tag.HEAD, body.child(1).tag());
        assertEquals(Element.Tag.TITLE, body.child(1).child(0).tag());
        assertEquals(Element.Tag.PARAGRAPH, body.child(2).tag());
        assertEquals(Element.Tag.B, body.child(2).child(0).tag());
        assertEquals(Element.Tag.A, body.child(2).child(5).tag());
        assertEquals(Element.Tag.IMG, body.child(2).child(10).tag());
        assertEquals(Element.Tag.FORM, body.child(3).tag());
        assertEquals(Element.Tag.INPUT, body.child(3).child(0).tag());
        assertEquals(Element.Tag.INPUT, body.child(3).child(1).tag());
        assertEquals(Element.Tag.BUTTON, body.child(3).child(2).tag());

        // Test attributes
        Element anchor = body.child(2).child(5);
        assertEquals("test.html", anchor.absUrl("href"));

        // Test error cases and edge cases
        String invalidHtml = "<html><body><script>alert('Hello')</script></body></html>";
        assertThrows(RuntimeException.class, () -> new Parser(invalidHtml, baseUri, false).parse());

        String emptyHtml = "<html></html>";
        Document emptyDoc = new Parser(emptyHtml, baseUri).parse();
        assertEquals("", emptyDoc.title());
        assertEquals(1, emptyDoc.childNodeSize());

        String brokenHtml = "<html><body><script>alert('Hello')<script></body></html>";
        assertThrows(RuntimeException.class, () -> new Parser(brokenHtml, baseUri).parse());

        String invalidTagName = "<html><body><invalidTag></body></html>";
        assertThrows(RuntimeException.class, () -> new Parser(invalidTagName, baseUri).parse());
    }
}