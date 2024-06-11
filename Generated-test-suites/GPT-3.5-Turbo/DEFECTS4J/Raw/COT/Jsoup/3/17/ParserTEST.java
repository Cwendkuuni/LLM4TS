import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Heading", doc.body().select("h1").text());
        Assert.assertEquals("Paragraph", doc.body().select("p").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Heading</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Heading", doc.body().select("h1").text());
        Assert.assertEquals("Paragraph", doc.body().select("p").text());
    }
    
    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<h1>Heading</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Heading", doc.body().select("h1").text());
        Assert.assertEquals("Paragraph", doc.body().select("p").text());
    }
    
    @Test
    public void testParseWithComment() {
        String html = "<html><!-- This is a comment --><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Heading", doc.body().select("h1").text());
        Assert.assertEquals("Paragraph", doc.body().select("p").text());
        Assert.assertEquals("This is a comment", doc.body().previousSibling().toString().trim());
    }
    
    @Test
    public void testParseWithCdata() {
        String html = "<html><body><h1>Heading</h1><p><![CDATA[This is a CDATA section]]></p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Heading", doc.body().select("h1").text());
        Assert.assertEquals("This is a CDATA section", doc.body().select("p").text());
    }
    
    @Test
    public void testParseWithXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Heading", doc.body().select("h1").text());
        Assert.assertEquals("Paragraph", doc.body().select("p").text());
    }
    
    @Test
    public void testParseWithEndTag() {
        String html = "<html><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Heading", doc.body().select("h1").text());
        Assert.assertEquals("Paragraph", doc.body().select("p").text());
        Assert.assertEquals("", doc.body().select("body").text());
    }
    
    @Test
    public void testParseWithStartTag() {
        String html = "<html><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Heading", doc.body().select("h1").text());
        Assert.assertEquals("Paragraph", doc.body().select("p").text());
    }
    
    // Add more test cases for potential edge cases and exception handling scenarios
    
}