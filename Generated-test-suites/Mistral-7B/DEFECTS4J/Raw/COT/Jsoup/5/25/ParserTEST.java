package org.jsoup.parser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.LinkedList;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private static final String HTML = "<html><head><title>Test Title</title></head>" +
            "<body><h1>Test Heading</h1>" +
            "<p>Test Paragraph<a href=\"test.html\">Link</a></p>" +
            "<textarea>Test TextArea</textarea>" +
            "<script>alert('Test Script');</script>" +
            "<base href=\"test.com\"/>" +
            "</body></html>";

    private static final String BODY_HTML = "<body><p>Test Paragraph</p>" +
            "<textarea>Test TextArea</textarea>" +
            "<script>alert('Test Script');</script>" +
            "</body>";

    private static final String BODY_FRAGMENT_HTML_RELAXED = "<body>" +
            "<p>Test Paragraph</p>" +
            "<base href=\"test.com\"/>" +
            "</body>";

    private Document parsedDoc;
    private Document parsedBodyFragmentDoc;
    private Document parsedBodyFragmentDocRelaxed;
    private Parser parser;

    @Before
    public void setUp() {
        parsedDoc = Parser.parse(HTML, "http://test.com");
        parsedBodyFragmentDoc = Parser.parseBodyFragment(BODY_HTML, "http://test.com");
        parsedBodyFragmentDocRelaxed = Parser.parseBodyFragmentRelaxed(BODY_FRAGMENT_HTML_RELaxed, "http://test.com");
        parser = new Parser(HTML, "http://test.com", false);
    }

    @After
    public void tearDown() {
        parsedDoc = null;
        parsedBodyFragmentDoc = null;
        parsedBodyFragmentDocRelaxed = null;
        parser = null;
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        Assert.assertEquals(htmlTag, doc.tagName());
        Assert.assertNotNull(doc.title());
        Assert.assertEquals("Test Title", doc.title().text());

        Element h1 = doc.getElementsByTag("h1").first();
        Assert.assertEquals("Test Heading", h1.text());

        Element p = doc.getElementsByTag("p").first();
        Assert.assertEquals("Test Paragraph", p.text());

        Element link = p.getElementsByTag("a").first();
        Assert.assertEquals("test.html", link.absUrl("href"));

        Element textarea = doc.getElementsByTag(textareaTag).first();
        Assert.assertEquals("Test TextArea", textarea.text());

        Element script = doc.getElementsByTag("script").first();
        Assert.assertNotNull(script);

        Element base = doc.getElementsByTag("base").first();
        Assert.assertEquals("test.com", base.absUrl("href"));
        Assert.assertEquals(parsedDoc, doc.normalise());
    }

    @Test
    public void testParseBodyFragment() {
        Assert.assertEquals(bodyTag, parsedBodyFragmentDoc.tagName());
        Assert.assertNull(parsedBodyFragmentDoc.head());

        Element p = parsedBodyFragmentDoc.getElementsByTag("p").first();
        Assert.assertEquals("Test Paragraph", p.text());

        Element textarea = parsedBodyFragmentDoc.getElementsByTag(textareaTag).first();
        Assert.assertEquals("Test TextArea", textarea.text());

        Element script = parsedBodyFragmentDoc.getElementsByTag("script").first();
        Assert.assertNotNull(script);

        Assert.assertEquals(parsedBodyFragmentDoc, parsedBodyFragmentDoc.normalise());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Assert.assertEquals(bodyTag, parsedBodyFragmentDocRelaxed.tagName());
        Assert.assertNull(parsedBodyFragmentDocRelaxed.head());

        Element p = parsedBodyFragmentDocRelaxed.getElementsByTag("p").first();
        Assert.assertEquals("Test Paragraph", p.text());

        Element base = parsedBodyFragmentDocRelaxed.getElementsByTag("base").first();
        Assert.assertEquals("test.com", base.absUrl("href"));

        Element implicitParent = parsedBodyFragmentDocRelaxed.getElementsByTag("div").first();
        if (implicitParent != null) {
            Element pChild = new Element(pTag, "http://test.com");
            parsedBodyFragmentDocRelaxed.body().appendChild(implicitParent);
            implicitParent.appendChild(pChild);
            Assert.assertEquals(2, parsedBodyFragmentDocRelaxed.body().childCount());
        }

        Assert.assertEquals(parsedBodyFragmentDocRelaxed, parsedBodyFragmentDocRelaxed.normalise());
    }
}