import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    // Test case for parse method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        Element h1 = doc.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello World", h1.text());
    }

    // Test case for parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Test Fragment</h2><p>This is a fragment of HTML.</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        Element h2 = doc.selectFirst("h2");
        assertNotNull(h2);
        assertEquals("Test Fragment", h2.text());
        Element p = doc.selectFirst("p");
        assertNotNull(p);
        assertEquals("This is a fragment of HTML.", p.text());
    }

    // Test case for parseComment method
    @Test
    public void testParseComment() {
        Parser parser = new Parser("", "https://www.example.com", false);
        parser.parseComment();

        Document doc = parser.parse();
        assertEquals(1, doc.childNodes().size());
        Node commentNode = doc.childNodes().get(0);
        assertTrue(commentNode instanceof Comment);
        Comment comment = (Comment) commentNode;
        assertEquals("", comment.getData());
        assertEquals("https://www.example.com", comment.baseUri());
    }

    // Test case for parseXmlDecl method
    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("", "https://www.example.com", false);
        parser.parseXmlDecl();

        Document doc = parser.parse();
        assertEquals(1, doc.childNodes().size());
        Node declNode = doc.childNodes().get(0);
        assertTrue(declNode instanceof XmlDeclaration);
        XmlDeclaration decl = (XmlDeclaration) declNode;
        assertEquals("", decl.getData());
        assertEquals("https://www.example.com", decl.baseUri());
        assertFalse(decl.isProcessingInstruction());
    }

    // Test case for parseEndTag method
    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("", "https://www.example.com", false);
        parser.parseEndTag();

        Document doc = parser.parse();
        assertEquals(0, doc.childNodes().size());
    }

    // Test case for parseStartTag method
    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("", "https://www.example.com", false);
        parser.parseStartTag();

        Document doc = parser.parse();
        assertEquals(0, doc.childNodes().size());
    }

    // Test case for parseTextNode method
    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("", "https://www.example.com", false);
        parser.parseTextNode();

        Document doc = parser.parse();
        assertEquals(1, doc.childNodes().size());
        Node textNode = doc.childNodes().get(0);
        assertTrue(textNode instanceof TextNode);
        TextNode text = (TextNode) textNode;
        assertEquals("", text.text());
        assertEquals("https://www.example.com", text.baseUri());
    }

    // Test case for parseCdata method
    @Test
    public void testParseCdata() {
        Parser parser = new Parser("", "https://www.example.com", false);
        parser.parseCdata();

        Document doc = parser.parse();
        assertEquals(1, doc.childNodes().size());
        Node cdataNode = doc.childNodes().get(0);
        assertTrue(cdataNode instanceof TextNode);
        TextNode cdata = (TextNode) cdataNode;
        assertEquals("", cdata.text());
        assertEquals("https://www.example.com", cdata.baseUri());
    }

    // Test case for addChildToParent method
    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("", "https://www.example.com", false);
        Element parent = new Element(Tag.valueOf("div"), "https://www.example.com");
        Element child = new Element(Tag.valueOf("p"), "https://www.example.com");
        parser.addChildToParent(child, false);

        assertEquals(1, parent.childNodes().size());
        Node childNode = parent.childNodes().get(0);
        assertTrue(childNode instanceof Element);
        Element childElement = (Element) childNode;
        assertEquals(child, childElement);
    }

    // Test case for stackHasValidParent method
    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "https://www.example.com", false);
        Element parent = new Element(Tag.valueOf("html"), "https://www.example.com");
        parser.stack.add(parent);
        Tag childTag = Tag.valueOf("head");

        assertTrue(parser.stackHasValidParent(childTag));
    }

    // Test case for popStackToSuitableContainer method
    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "https://www.example.com", false);
        Element div = new Element(Tag.valueOf("div"), "https://www.example.com");
        parser.stack.add(div);
        Tag tag = Tag.valueOf("p");

        Element container = parser.popStackToSuitableContainer(tag);
        assertEquals(div, container);
    }

    // Test case for popStackToClose method
    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "https://www.example.com", false);
        Element div = new Element(Tag.valueOf("div"), "https://www.example.com");
        parser.stack.add(div);
        Tag tag = Tag.valueOf("div");

        Element closedElement = parser.popStackToClose(tag);
        assertEquals(div, closedElement);
        assertEquals(0, parser.stack.size());
    }

    // Test case for last method
    @Test
    public void testLast() {
        Parser parser = new Parser("", "https://www.example.com", false);
        Element div = new Element(Tag.valueOf("div"), "https://www.example.com");
        parser.stack.add(div);

        Element last = parser.last();
        assertEquals(div, last);
    }

}