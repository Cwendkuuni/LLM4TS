
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://www.example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParse_comment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<!-- This is a comment -->", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParse_cdata() {
        String html = "<![CDATA[This is CDATA.]]>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<![CDATA[This is CDATA.]]>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParse_xmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.childNodes().get(0).outerHtml());
    }

    // Generated test cases for potential edge cases and exception handling scenarios

    // TODO: Add test cases to cover potential edge cases and exception handling scenarios

}
