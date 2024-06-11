
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser();
    }

    @Test
    public void testParse_WhenValidHtml_ReturnsDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testParse_WhenInvalidHtml_ReturnsEmptyDocument() {
        String html = "<html><head><title>Test";
        String baseUri = "http://example.com";

        Document document = parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("", document.title());
        Assert.assertEquals("", document.body().text());
    }

    @Test
    public void testParseBodyFragment_WhenValidBodyHtml_ReturnsDocumentWithBody() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";

        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment_WhenInvalidBodyHtml_ReturnsDocumentWithEmptyBody() {
        String bodyHtml = "<h1>Hello World";
        String baseUri = "http://example.com";

        Document document = parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals("", document.body().text());
    }

    @Test
    public void testParseComment_WhenValidCommentHtml_AddsCommentToLastElement() {
        parser.setup("<html><body><h1>Hello World</h1><!-- This is a comment --></body></html>");

        parser.parseComment();

        Element body = parser.last();
        Assert.assertEquals(2, body.childNodeSize());
        Assert.assertEquals("body", body.tag().getName());

        Node comment = body.childNode(1);
        Assert.assertEquals(Node.COMMENT_NODE, comment.getNodeType());
        Assert.assertEquals(" This is a comment ", comment.getData());
    }

    // ... Additional tests for other methods ...

}

