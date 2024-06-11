import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ParserTest {

    private final String html = "<html>" +
            "<head>" +
            "<title>Test Page</title>" +
            "</head>" +
            "<body>" +
            "<h1>Welcome to the Test Page!</h1>" +
            "<p>This is a test page for testing the Parser class.</p>" +
            "</body>" +
            "</html>";

    private final String baseUri = "http://example.com/";

    private final Parser parser = new Parser(html, baseUri, false);

    @Test
    public void testParse() {
        Document document = parser.parse();
        Assert.assertNotNull(document);
        Assert.assertEquals("html", document.tagName());
    }

    @Test
    public void testParseWithValidHtml() {
        Document document = parser.parse();
        Assert.assertNotNull(document);
        Assert.assertEquals("Test Page", document.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Test Heading</h2>" +
                "<p>This is a test heading for parsing body fragments.</p>";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("h2", body.child(0).tagName());
        Assert.assertEquals("p", body.child(1).tagName());
    }

    @Test
    public void testParseComment() {
        String commentHtml = "<!-- This is a test comment -->";
        Document document = Parser.parse(commentHtml, baseUri);
        Assert.assertNotNull(document);
        Element comment = document.body().child(0);
        Assert.assertEquals("<!-- This is a test comment -->", comment.toString());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDeclHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document document = Parser.parse(xmlDeclHtml, baseUri);
        Assert.assertNotNull(document);
        Element xmlDecl = document.child(0);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDecl.toString());
    }

    @Test
    public void testParseEndTag() {
        String endTagHtml = "<p>This is a test paragraph</p>";
        Document document = Parser.parse(endTagHtml, baseUri);
        Assert.assertNotNull(document);
        Element paragraph = document.body().child(0);
        Assert.assertEquals("p", paragraph.tagName());
    }

    @Test
    public void testParseStartTag() {
        String startTagHtml = "<h1>This is a test heading</h1>";
        Document document = Parser.parse(startTagHtml, baseUri);
        Assert.assertNotNull(document);
        Element heading = document.body().child(0);
        Assert.assertEquals("h1", heading.tagName());
    }

    @Test
    public void testParseTextNode() {
        String textNodeHtml = "<p>This is a test paragraph</p>";
        Document document = Parser.parse(textNodeHtml, baseUri);
        Assert.assertNotNull(document);
        Element paragraph = document.body().child(0);
        Assert.assertEquals("This is a test paragraph", paragraph.text());
    }

    @Test
    public void testParseCdata() {
        String cdataHtml = "<script><![CDATA[function test() { console.log('This is a test'); }]]></script>";
        Document document = Parser.parse(cdataHtml, baseUri);
        Assert.assertNotNull(document);
        Element script = document.body().child(0);
        Assert.assertEquals("function test() { console.log('This is a test'); }", script.data());
    }

    @Test
    public void testParseAttribute() {
        String attributeHtml = "<a href=\"http://example.com\">Test Link</a>";
        Document document = Parser.parse(attributeHtml, baseUri);
        Assert.assertNotNull(document);
        Element link = document.body().child(0);
        Assert.assertEquals("http://example.com", link.attr("href"));
    }

    @Test
    public void testStackHasValidParent() {
        boolean validParent = parser.stackHasValidParent(Parser.bodyTag);
        Assert.assertTrue(validParent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Element element = new Element(Parser.titleTag, baseUri);
        parser.stack.addLast(element);
        Element parent = parser.popStackToSuitableContainer(Parser.headTag);
        Assert.assertNotNull(parent);
        Assert.assertEquals("head", parent.tagName());
    }

    @Test
    public void testPopStackToClose() {
        Element element = new Element(Parser.bodyTag, baseUri);
        parser.stack.addLast(element);
        Element parent = parser.popStackToClose(Parser.bodyTag);
        Assert.assertNotNull(parent);
        Assert.assertEquals("body", parent.tagName());
    }

    @Test
    public void testLast() {
        Element element = new Element(Parser.titleTag, baseUri);
        parser.stack.addLast(element);
        Element last = parser.last();
        Assert.assertNotNull(last);
        Assert.assertEquals("title", last.tagName());
    }
}