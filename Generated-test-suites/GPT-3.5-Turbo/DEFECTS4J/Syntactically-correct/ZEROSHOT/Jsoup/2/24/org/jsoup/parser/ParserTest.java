import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    private static final String HTML = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
    private static Parser parser;

    @BeforeClass
    public static void setUp() {
        parser = new Parser(HTML, "http://www.example.com");
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyFragment = "<h1>Hello World!</h1>";
        Document doc = Parser.parseBodyFragment(bodyFragment, "http://www.example.com");
        assertNotNull(doc);
        assertEquals("Hello World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseComment() {
        String comment = "<!-- This is a comment -->";
        Element element = parser.parseComment(comment);
        assertNotNull(element);
        assertEquals("#comment", element.nodeName());
        assertEquals("<!-- This is a comment -->", element.toString());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Element element = parser.parseXmlDecl(xmlDecl);
        assertNotNull(element);
        assertEquals("?xml", element.nodeName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", element.toString());
    }

    @Test
    public void testParseEndTag() {
        String endTag = "</h1>";
        Element element = parser.parseEndTag(endTag);
        assertNotNull(element);
        assertEquals("/h1", element.nodeName());
        assertEquals("</h1>", element.toString());
    }

    @Test
    public void testParseStartTag() {
        String startTag = "<h1>";
        Element element = parser.parseStartTag(startTag);
        assertNotNull(element);
        assertEquals("h1", element.nodeName());
        assertEquals("<h1>", element.toString());
    }

    @Test
    public void testParseTextNode() {
        String text = "Hello World!";
        Element element = parser.parseTextNode(text);
        assertNotNull(element);
        assertEquals("#text", element.nodeName());
        assertEquals("Hello World!", element.toString());
    }

    @Test
    public void testParseCdata() {
        String cdata = "<![CDATA[This is CDATA]]>";
        Element element = parser.parseCdata(cdata);
        assertNotNull(element);
        assertEquals("#cdata-section", element.nodeName());
        assertEquals("<![CDATA[This is CDATA]]>", element.toString());
    }

    @Test
    public void testAddChildToParent() {
        Element parent = new Element("div");
        Element child = new Element("h1");
        Element result = parser.addChildToParent(parent, child, false);
        assertNotNull(result);
        assertEquals("h1", result.nodeName());
        assertTrue(parent.children().contains(child));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Element element1 = new Element("div");
        Element element2 = new Element("span");
        parser.stack.addLast(element1);
        parser.stack.addLast(element2);
        Tag tag = Tag.valueOf("span");
        Element result = parser.popStackToSuitableContainer(tag);
        assertNotNull(result);
        assertEquals("span", result.nodeName());
        assertFalse(parser.stack.contains(element2));
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element("div");
        Element element2 = new Element("h1");
        Element element3 = new Element("h2");
        parser.stack.addLast(element1);
        parser.stack.addLast(element2);
        parser.stack.addLast(element3);
        Tag tag = Tag.valueOf("h1");
        Element result = parser.popStackToClose(tag);
        assertNotNull(result);
        assertEquals("h1", result.nodeName());
        assertFalse(parser.stack.contains(element2));
    }

    @Test
    public void testLast() {
        Element element = new Element("div");
        parser.stack.addLast(element);
        Element result = parser.last();
        assertNotNull(result);
        assertEquals("div", result.nodeName());
    }
}