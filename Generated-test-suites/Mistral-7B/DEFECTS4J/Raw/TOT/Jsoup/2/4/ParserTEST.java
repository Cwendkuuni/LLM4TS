import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();
        Assert.assertEquals(0, root.childSize());
    }

    @Test
    public void testParseSimpleHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();
        Element title = root.select("title").first();
        Assert.assertEquals("Test", title.text());
        Assert.assertEquals(1, root.childSize());
        Assert.assertEquals(1, root.child(0).childSize());
        Assert.assertEquals("Hello World", root.child(0).tagName());
        Assert.assertEquals("h1", root.child(0).tagName());
    }

    @Test
    public void testParseHtmlWithComments() {
        String html = "<html><!-- This is a comment --><head><title>Test</title></head><body><h1>Hello World</h1><!-- This is another comment --></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.body();
        List<Element> commentList = root.select("script,style"); // comments are treated as scripts and styles
        Assert.assertEquals(2, commentList.size());
        Assert.assertEquals("This is a comment", commentList.get(0).text());
        Assert.assertEquals("This is another comment", commentList.get(1).text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");
        Element root = doc.head();
        XmlDeclaration xmlDecl = (XmlDeclaration) root.child(0);
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", xmlDecl.xmlns("xsi"));
        Assert.assertEquals("en", xmlDecl.xmlLang());
    }

    @Test
    public void testParseSelfClosedElement() {
        String html = "<img src=\"image.jpg\" alt=\"Image\" />";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        Element root = doc.body();
        Element img = root.select("img").first();
        Assert.assertEquals("img", img.tagName());
        Assert.assertEquals(0, img.childSize());
        Assert.assertEquals("image.jpg", img.absUrl("src"));
        Assert.assertEquals("Image", img.attr("alt"));
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<br />";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        Element root = doc.body();
        Element br = root.select("br").first();
        Assert.assertEquals("br", br.tagName());
        Assert.assertEquals(0, br.childSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html<title>";
        Parser.parse(html, "http://example.com");
    }
}