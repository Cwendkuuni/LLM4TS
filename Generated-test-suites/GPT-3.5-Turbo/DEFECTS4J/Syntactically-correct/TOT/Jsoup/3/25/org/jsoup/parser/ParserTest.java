import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void testParse_validHTML_returnDocument() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document doc = parser.parse(html);
        assertNotNull(doc);
    }

    @Test
    public void testParse_emptyHTML_returnEmptyDocument() {
        String html = "";
        Document doc = parser.parse(html);
        assertNotNull(doc);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test
    public void testParse_invalidHTML_returnEmptyDocument() {
        String html = "<html><body><h1>Test</h2></body></html>";
        Document doc = parser.parse(html);
        assertNotNull(doc);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test
    public void testParseBodyFragment_validFragment_returnDocumentWithParsedFragment() {
        String bodyHtml = "<div>Test</div>";
        Document doc = parser.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertFalse(doc.body().children().isEmpty());
        assertEquals("div", doc.body().child(0).tagName());
    }

    @Test
    public void testParseBodyFragment_emptyFragment_returnDocumentWithEmptyBody() {
        String bodyHtml = "";
        Document doc = parser.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test
    public void testParseBodyFragment_invalidFragment_returnDocumentWithEmptyBody() {
        String bodyHtml = "<div>Test</span>";
        Document doc = parser.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test
    public void testParseBodyFragment_relaxedParsingEnabled_returnDocumentWithImplicitTags() {
        String bodyHtml = "<p>Test</p>";
        Document doc = parser.parseBodyFragmentRelaxed(bodyHtml);
        assertNotNull(doc);
        assertFalse(doc.body().children().isEmpty());
        assertEquals("body", doc.body().child(0).tagName());
    }

}