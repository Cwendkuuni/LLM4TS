import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";

        Document result = Parser.parse(html, baseUri);

        assertEquals("Test", result.title());
        assertEquals("Hello", result.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "http://example.com";

        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("Hello", result.body().select("h1").text());
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<html><!--This is a comment--></html>", "http://example.com", false);
        Document document = parser.parse();
        Comment comment = (Comment) document.childNode(0).childNode(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html></html>", "http://example.com", false);
        Document document = parser.parse();
        XmlDeclaration xmlDeclaration = (XmlDeclaration) document.childNode(0);
        assertEquals("1.0", xmlDeclaration.attr("version"));
        assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://example.com", false);
        Document document = parser.parse();
        parser.parseEndTag();
        assertEquals(Tag.valueOf("body"), parser.last().tag());
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://example.com", false);
        Document document = parser.parse();
        parser.parseStartTag();
        assertEquals(Tag.valueOf("h1"), parser.last().tag());
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("<html><h1>Hello, world!</h1></html>", "http://example.com", false);
        Document document = parser.parse();
        parser.parseTextNode();
        assertEquals("Hello, world!", ((TextNode) parser.last().childNode(0)).text());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<![CDATA[<h1>Hello, world!</h1>]]>", "http://example.com", false);
        Document document = parser.parse();
        TextNode cdata = (TextNode) document.childNode(0);
        assertEquals("<h1>Hello, world!</h1>", cdata.getWholeText());
    }

    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("<html><body></body></html>", "http://example.com", false);
        Document document = parser.parse();
        Element child = new Element(Tag.valueOf("h1"), "http://example.com");
        Element parent = parser.addChildToParent(child, false);
        assertEquals(Tag.valueOf("body"), parent.tag());
        assertEquals(1, parent.children().size());
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://example.com", false);
        Document document = parser.parse();
        Tag childTag = Tag.valueOf("h1");
        boolean result = parser.stackHasValidParent(childTag);
        assertTrue(result);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://example.com", false);
        Document document = parser.parse();
        Element child = new Element(Tag.valueOf("h1"), "http://example.com");
        Element parent = parser.popStackToSuitableContainer(child.tag());
        assertEquals(Tag.valueOf("html"), parent.tag());
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://example.com", false);
        Document document = parser.parse();
        Tag tag = Tag.valueOf("head");
        Element result = parser.popStackToClose(tag);
        assertNotNull(result);
        assertEquals(Tag.valueOf("head"), result.tag());
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "http://example.com", false);
        Document document = parser.parse();
        Element last = parser.last();
        assertEquals(Tag.valueOf("h1"), last.tag());
    }

}