import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().select("h1").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Paragraph 1", doc.select("p").get(0).text());
        assertEquals("Paragraph 2", doc.select("p").get(1).text());
    }
    
    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("<!-- This is a comment -->", doc.html());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is a CDATA section]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("This is a CDATA section", doc.html());
    }
    
    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.html());
    }
    
    @Test
    public void testParseEndTag() {
        String html = "</body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("</body></html>", doc.html());
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<div><p>Paragraph</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Paragraph", doc.select("p").text());
    }
    
    @Test
    public void testParseTextNode() {
        String html = "This is a text node";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("This is a text node", doc.body().text());
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<div class=\"container\">Content</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("container", doc.select("div").attr("class"));
    }
    
    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        parser.popStackToClose(Tag.valueOf("body"));
        assertTrue(parser.stack.isEmpty());
    }
    
    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", false);
        assertTrue(parser.stackHasValidParent(Tag.valueOf("head")));
        assertFalse(parser.stackHasValidParent(Tag.valueOf("body")));
    }
    
    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false);
        assertEquals(parser.stack.getLast(), parser.last());
    }

}