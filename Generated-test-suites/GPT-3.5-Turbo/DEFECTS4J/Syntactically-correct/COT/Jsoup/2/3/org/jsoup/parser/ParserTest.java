
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "");
        assertNotNull(doc);
        assertEquals("Test", doc.body().select("p").text());
    }

    @Test
    public void testParseWithEmptyInput() {
        Document doc = Parser.parse("", "");
        assertNotNull(doc);
        assertTrue(doc.body().children().isEmpty());
    }
    
    @Test
    public void testParseWithComment() {
        String html = "<!-- Comment -->";
        Document doc = Parser.parse(html, "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<!-- Comment -->", doc.childNode(0).outerHtml());
    }
    
    @Test
    public void testParseWithCDATA() {
        String html = "<![CDATA[ My CDATA ]]>";
        Document doc = Parser.parse(html, "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<![CDATA[ My CDATA ]]>", doc.childNode(0).outerHtml());
    }
    
    @Test
    public void testParseWithXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = Parser.parse(html, "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.childNode(0).outerHtml());
    }

    // Test edge cases and exceptions

    @Test
    public void testParseWithEmptyTagName() {
        String html = "<>";
        Document doc = Parser.parse(html, "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<>", doc.childNode(0).outerHtml());
    }
    
    @Test
    public void testParseWithInvalidAttribute() {
        String html = "<div =123>";
        Document doc = Parser.parse(html, "");
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<div =123>", doc.childNode(0).outerHtml());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullHtml() {
        Parser.parse(null, "");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullHtml() {
        Parser.parseBodyFragment(null, "");
    }

    // Add more test cases here

}

