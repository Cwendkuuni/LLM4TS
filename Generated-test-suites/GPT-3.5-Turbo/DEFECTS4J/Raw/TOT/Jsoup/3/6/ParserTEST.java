import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertEquals("Document should have one root element", 1, doc.children().size());
        Assert.assertEquals("Root element should be html", "html", doc.tagName());
        Assert.assertNotNull("Root element should have a child", doc.child(0));
        Assert.assertEquals("Child element should be body", "body", doc.child(0).tagName());
        Assert.assertEquals("Body element should have one child", 1, doc.child(0).children().size());
        Assert.assertEquals("Child element should be h1", "h1", doc.child(0).child(0).tagName());
        Assert.assertEquals("Text content should be 'Title'", "Title", doc.child(0).child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Title</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);
        
        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertEquals("Document should have one root element", 1, doc.children().size());
        Assert.assertEquals("Root element should be body", "body", doc.tagName());
        Assert.assertEquals("Body element should have one child", 1, doc.children().size());
        Assert.assertEquals("Child element should be h1", "h1", doc.child(0).tagName());
        Assert.assertEquals("Text content should be 'Title'", "Title", doc.child(0).text());
    }

    @Test
    public void testParseBodyFragment_relaxedParsing() {
        String html = "<div><p>Paragraph</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);
        
        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertEquals("Document should have one root element", 1, doc.children().size());
        Assert.assertEquals("Root element should be body", "body", doc.tagName());
        Assert.assertEquals("Body element should have one child", 1, doc.children().size());
        Assert.assertEquals("Child element should be div", "div", doc.child(0).tagName());
        Assert.assertEquals("Div element should have one child", 1, doc.child(0).children().size());
        Assert.assertEquals("Child element should be p", "p", doc.child(0).child(0).tagName());
        Assert.assertEquals("Text content should be 'Paragraph'", "Paragraph", doc.child(0).child(0).text());
    }

    @Test
    public void testParse_comment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertEquals("Document should have one root element", 1, doc.children().size());
        Assert.assertTrue("Root element should be an instance of Comment class", doc.child(0) instanceof Comment);
        Assert.assertEquals("Comment text should be ' This is a comment '", " This is a comment ", ((Comment) doc.child(0)).getData());
    }

    @Test
    public void testParse_cdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertEquals("Document should have one root element", 1, doc.children().size());
        Assert.assertTrue("Root element should be an instance of TextNode class", doc.child(0) instanceof TextNode);
        Assert.assertEquals("Text content should be 'This is CDATA'", "This is CDATA", doc.child(0).text());
    }

    @Test
    public void testParse_xmlDecl() {
        String html = "<?xml version='1.0' encoding='UTF-8'?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertEquals("Document should have no root element", 0, doc.children().size());
    }

    @Test
    public void testParse_endTag() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull("Document should not be null", doc);
        Assert.assertEquals("Document should have one root element", 1, doc.children().size());
        Assert.assertEquals("Root element should be html", "html", doc.tagName());
        
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        String parsedHtml = doc.toString();
        Assert.assertEquals("Parsed HTML should be the same as input HTML", html, parsedHtml);
    }
}