import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseComment() {
        String commentHtml = "<!-- This is a comment -->";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(commentHtml, baseUri, false);
        parser.parseComment();
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("This is a comment", doc.body().text());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDeclHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(xmlDeclHtml, baseUri, false);
        parser.parseXmlDecl();
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseEndTag() {
        String endTagHtml = "</h1>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(endTagHtml, baseUri, false);
        parser.parseEndTag();
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testParseStartTag() {
        String startTagHtml = "<h1>Test</h1>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(startTagHtml, baseUri, false);
        parser.parseStartTag();
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Test", doc.body().select("h1").text());
    }

    @Test
    public void testParseTextNode() {
        String textNodeHtml = "Hello World";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(textNodeHtml, baseUri, false);
        parser.parseTextNode();
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseCdata() {
        String cdataHtml = "<![CDATA[This is a CDATA section]]>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(cdataHtml, baseUri, false);
        parser.parseCdata();
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("This is a CDATA section", doc.body().text());
    }

    @Test
    public void testParseAttribute() {
        String attributeHtml = "<div id=\"test\"></div>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(attributeHtml, baseUri, false);
        Attribute attribute = parser.parseAttribute();
        assertNotNull(attribute);
        assertEquals("id", attribute.getKey());
        assertEquals("test", attribute.getValue());
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("test", doc.select("div").attr("id"));
    }

    @Test
    public void testAddChildToParent() {
        Element child = new Element(Tag.valueOf("div"), "http://www.example.com");
        Element parent = new Element(Tag.valueOf("body"), "http://www.example.com");
        Parser parser = new Parser("", "http://www.example.com", false);
        Element result = parser.addChildToParent(child, false);
        assertNotNull(result);
        assertEquals("http://www.example.com", result.baseUri());
        assertEquals("div", result.tagName());
        assertTrue(result.parent() == parent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Element child = new Element(Tag.valueOf("div"), "http://www.example.com");
        Element parent = new Element(Tag.valueOf("body"), "http://www.example.com");
        Parser parser = new Parser("", "http://www.example.com", false);
        parser.stack.addLast(parent);
        Element result = parser.popStackToSuitableContainer(child.tag());
        assertNotNull(result);
        assertEquals(parent, result);
    }

    @Test
    public void testStackHasValidParent() {
        Element child = new Element(Tag.valueOf("div"), "http://www.example.com");
        Element parent = new Element(Tag.valueOf("body"), "http://www.example.com");
        parent.tag().addValidChild(child.tag());
        Parser parser = new Parser("", "http://www.example.com", false);
        parser.stack.addLast(parent);
        boolean result = parser.stackHasValidParent(child.tag());
        assertTrue(result);
    }

    @Test
    public void testPopStackToClose() {
        Tag tag = Tag.valueOf("div");
        Element elToClose = new Element(tag, "http://www.example.com");
        Parser parser = new Parser("", "http://www.example.com", false);
        parser.stack.addLast(new Element(Tag.valueOf("body"), "http://www.example.com"));
        parser.stack.addLast(elToClose);
        Element result = parser.popStackToClose(tag);
        assertNotNull(result);
        assertEquals(elToClose, result);
        assertTrue(parser.stack.isEmpty());
    }

    @Test
    public void testLast() {
        Element el = new Element(Tag.valueOf("div"), "http://www.example.com");
        Parser parser = new Parser("", "http://www.example.com", false);
        parser.stack.addLast(el);
        Element result = parser.last();
        assertNotNull(result);
        assertEquals(el, result);
    }
}