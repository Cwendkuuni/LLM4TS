import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<!-- this is a comment -->", "http://example.com", false);
        parser.parseComment();
        Assert.assertEquals(" this is a comment ", parser.last().childNode(0).toString());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html></html>", "http://example.com", false);
        parser.parseXmlDecl();
        Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", parser.last().childNode(0).toString());
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("</h1>", "http://example.com", false);
        parser.parseEndTag();
        Assert.assertNull(parser.last());
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("<h1>Hello</h1>", "http://example.com", false);
        parser.parseStartTag();
        Assert.assertEquals("Hello", parser.last().childNode(0).toString());
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("Hello", "http://example.com", false);
        parser.parseTextNode();
        Assert.assertEquals("Hello", parser.last().childNode(0).toString());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<![CDATA[This is CDATA]]>", "http://example.com", false);
        parser.parseCdata();
        Assert.assertEquals("This is CDATA", parser.last().childNode(0).toString());
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("key=\"value\"", "http://example.com", false);
        Attribute attribute = parser.parseAttribute();
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }
}