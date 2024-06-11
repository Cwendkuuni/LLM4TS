
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse_ValidHtml_ReturnsDocument() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("html", document.tagName());
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParse_InvalidHtml_ReturnsNull() {
        String html = "<html><body><h1>Hello World!</h2></body></html>"; // Invalid closing tag for h1
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNull(document);
    }

    @Test
    public void testParseBodyFragment_ValidFragment_ReturnsDocumentWithBodyContent() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("html", document.tagName());
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("body", document.body().tagName());
        Assert.assertEquals(1, document.body().children().size());
        Assert.assertEquals("h1", document.body().child(0).tagName());
        Assert.assertEquals("Hello World!", document.body().child(0).text());
    }

    @Test
    public void testParseBodyFragment_EmptyFragment_ReturnsEmptyDocument() {
        String bodyHtml = "";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("html", document.tagName());
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("body", document.body().tagName());
        Assert.assertTrue(document.body().children().isEmpty());
    }

    @Test
    public void testParseBodyFragment_InvalidFragment_ReturnsNull() {
        String bodyHtml = "<h1>Hello World!";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNull(document);
    }

    @Test
    public void testParse_Comment_ReturnsDocumentWithCommentNode() {
        String html = "<p><!-- This is a comment --></p>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("html", document.tagName());
        Assert.assertEquals("http://example.com", document.baseUri());
        
        Element paragraph = document.body().child(0);
        Assert.assertEquals("p", paragraph.tagName());
        Assert.assertEquals(1, paragraph.childNodeSize());
        Assert.assertEquals("<!-- This is a comment -->", paragraph.childNode(0).outerHtml());
    }

    // Add more test cases for other methods...

}

