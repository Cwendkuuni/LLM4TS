import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser("<html><head><title>Test</title></head><body><div class=\"container\"><h1>Hello World</h1><p>This is a test</p></div></body></html>", "https://example.com", false);
    }

    @Test
    public void testParse() {
        Document document = parser.parse();
        Assert.assertNotNull(document);
        Assert.assertEquals("https://example.com", document.baseUri());
        Assert.assertEquals("Hello World", document.select("h1").first().text());
        Assert.assertEquals("This is a test", document.select("p").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<div><span>Test</span></div>", "https://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("https://example.com", document.baseUri());
        Assert.assertEquals("Test", document.select("div span").first().text());
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        Element element = parser.last();
        Assert.assertTrue(element instanceof org.jsoup.nodes.Comment);
        Assert.assertEquals("<!-- -->", element.toString());
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        Element element = parser.last();
        Assert.assertTrue(element instanceof org.jsoup.nodes.XmlDeclaration);
        Assert.assertEquals("", element.toString());
    }

    @Test
    public void testParseEndTag() {
        parser.parseEndTag();
        Element element = parser.last();
        Assert.assertTrue(element instanceof org.jsoup.nodes.Element);
        Assert.assertEquals("</>", element.toString());
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
        Element element = parser.last();
        Assert.assertTrue(element instanceof org.jsoup.nodes.Element);
        Assert.assertEquals("<div class=\"container\"></div>", element.toString());
    }

    @Test
    public void testParseAttribute() {
        org.jsoup.parser.Attribute attribute = parser.parseAttribute();
        Assert.assertNotNull(attribute);
        Assert.assertEquals("class=\"container\"", attribute.toString());
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
        Element element = parser.last();
        Assert.assertTrue(element instanceof org.jsoup.nodes.TextNode);
        Assert.assertEquals("Hello World", element.toString());
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
        Element element = parser.last();
        Assert.assertTrue(element instanceof org.jsoup.nodes.TextNode);
        Assert.assertEquals("", element.toString());
    }

    @Test
    public void testAddChildToParent() {
        Element child = new Element(org.jsoup.parser.Tag.valueOf("div"), "https://example.com");
        Element parent = parser.addChildToParent(child, false);
        Assert.assertNotNull(parent);
        Assert.assertEquals("<div></div>", parent.toString());
    }

    @Test
    public void testStackHasValidParent() {
        org.jsoup.parser.Tag childTag = org.jsoup.parser.Tag.valueOf("div");
        boolean result = parser.stackHasValidParent(childTag);
        Assert.assertTrue(result);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        org.jsoup.parser.Tag tag = org.jsoup.parser.Tag.valueOf("div");
        Element element = parser.popStackToSuitableContainer(tag);
        Assert.assertNotNull(element);
        Assert.assertEquals("<div class=\"container\"></div>", element.toString());
    }

    @Test
    public void testPopStackToClose() {
        org.jsoup.parser.Tag tag = org.jsoup.parser.Tag.valueOf("div");
        Element element = parser.popStackToClose(tag);
        Assert.assertNotNull(element);
        Assert.assertEquals("<div class=\"container\"></div>", element.toString());
    }

    @Test
    public void testLast() {
        Element element = parser.last();
        Assert.assertNotNull(element);
        Assert.assertEquals("<html><head></head><body></body></html>", element.toString());
    }
}