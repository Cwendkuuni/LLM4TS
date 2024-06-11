import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser("Test HTML", "http://example.com", false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull("Document should not be null", doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        Element element = parser.last();
        assertNotNull("Element should not be null", element);
        assertTrue("Element should be Comment", element instanceof Comment);
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        Element element = parser.last();
        assertNotNull("Element should not be null", element);
        assertTrue("Element should be XmlDeclaration", element instanceof XmlDeclaration);
    }

    @Test
    public void testParseEndTag() {
        parser.parseEndTag();
        Element element = parser.last();
        assertNull("Element should be null", element);
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
        Element element = parser.last();
        assertNotNull("Element should not be null", element);
        assertTrue("Element should be Element", element instanceof Element);
    }

    @Test
    public void testParseAttribute() {
        Attribute attribute = parser.parseAttribute();
        assertNotNull("Attribute should not be null", attribute);
        assertFalse("Attribute key should not be empty", attribute.getKey().isEmpty());
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
        Element element = parser.last();
        assertNotNull("Element should not be null", element);
        assertTrue("Element should be TextNode", element instanceof TextNode);
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
        Element element = parser.last();
        assertNotNull("Element should not be null", element);
        assertTrue("Element should be TextNode", element instanceof TextNode);
    }

    @Test
    public void testAddChildToParent() {
        Element child = new Element(Tag.valueOf("div"), "http://example.com");
        boolean isEmptyElement = true;
        Element parent = parser.addChildToParent(child, isEmptyElement);
        assertNotNull("Parent element should not be null", parent);
        assertTrue("Parent element should contain child", parent.children().contains(child));
    }

    @Test
    public void testStackHasValidParent() {
        Tag childTag = Tag.valueOf("div");
        boolean hasValidParent = parser.stackHasValidParent(childTag);
        assertFalse("Stack should not have valid parent", hasValidParent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Tag tag = Tag.valueOf("div");
        Element container = parser.popStackToSuitableContainer(tag);
        assertNull("Container should be null", container);
    }

    @Test
    public void testPopStackToClose() {
        Tag tag = Tag.valueOf("div");
        Element elementToClose = parser.popStackToClose(tag);
        assertNull("Element to close should be null", elementToClose);
    }

    @Test
    public void testLast() {
        Element element = parser.last();
        assertNull("Element should be null", element);
    }
}