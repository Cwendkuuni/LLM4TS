import org.jsoup.nodes.Document;
import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // typical use case
        Document doc = Parser.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>", "https://example.com");
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertTrue(doc.hasText());
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParse_EmptyHtml() {
        // edge case: empty HTML
        Document doc = Parser.parse("", "https://example.com");
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertFalse(doc.hasText());
        assertNull(doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testParse_NullHtml() {
        // error scenario: null HTML
        Parser.parse(null, "https://example.com");
    }

    @Test(expected = NullPointerException.class)
    public void testParse_NullBaseUri() {
        // error scenario: null base URI
        Parser.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>", null);
    }

    @Test
    public void testParseBodyFragment() {
        // typical use case
        Document doc = Parser.parseBodyFragment("<h1>Hello World!</h1>", "https://example.com");
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertTrue(doc.hasText());
        assertFalse(doc.hasHead());
    }

    @Test
    public void testParseBodyFragment_EmptyHtml() {
        // edge case: empty HTML fragment
        Document doc = Parser.parseBodyFragment("", "https://example.com");
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertFalse(doc.hasText());
        assertFalse(doc.hasHead());
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragment_NullHtml() {
        // error scenario: null HTML fragment
        Parser.parseBodyFragment(null, "https://example.com");
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragment_NullBaseUri() {
        // error scenario: null base URI
        Parser.parseBodyFragment("<h1>Hello World!</h1>", null);
    }

    @Test
    public void testParseComment() {
        // typical use case
        Parser parser = new Parser("<!DOCTYPE html><!-- This is a comment -->", "https://example.com", false);
        parser.parseComment();
        assertEquals(1, parser.last().childNodes().size());
    }

    @Test
    public void testParseComment_CommentWithHyphen() {
        // edge case: comment with hyphen in the end
        Parser parser = new Parser("<!-- This is a comment - -->", "https://example.com", false);
        parser.parseComment();
        assertEquals(1, parser.last().childNodes().size());
    }

    @Test
    public void testParseComment_EmptyComment() {
        // edge case: empty comment
        Parser parser = new Parser("<!-- -->", "https://example.com", false);
        parser.parseComment();
        assertEquals(1, parser.last().childNodes().size());
    }

    @Test
    public void testParseXmlDecl() {
        // typical use case
        Parser parser = new Parser("<?xml version='1.0' encoding='UTF-8'?>", "https://example.com", false);
        parser.parseXmlDecl();
        assertEquals(1, parser.last().childNodes().size());
    }

    @Test
    public void testParseEndTag() {
        // typical use case
        Parser parser = new Parser("</head>", "https://example.com", false);
        parser.parseEndTag();
        assertEquals(1, parser.stack.size());
    }

    @Test
    public void testParseEndTag_EmptyTagName() {
        // edge case: empty tag name
        Parser parser = new Parser("</>", "https://example.com", false);
        parser.parseEndTag();
        assertEquals(1, parser.stack.size());
    }

    @Test
    public void testParseEndTag_TagNameWithAttributes() {
        // edge case: tag name with attributes
        Parser parser = new Parser("</head style='color:red;'>", "https://example.com", false);
        parser.parseEndTag();
        assertEquals(1, parser.stack.size());
    }

    @Test
    public void testParseStartTag() {
        // typical use case
        Parser parser = new Parser("<div class='container'>", "https://example.com", false);
        parser.parseStartTag();
        assertEquals(1, parser.last().childNodes().size());
    }

    @Test
    public void testParseStartTag_NotStartTag() {
        // edge case: not a start tag
        Parser parser = new Parser("<<div class='container'>", "https://example.com", false);
        parser.parseStartTag();
        assertEquals(1, parser.last().childNodes().size());
    }

    @Test
    public void testParseStartTag_EmptyTagName() {
        // edge case: empty tag name
        Parser parser = new Parser("<>", "https://example.com", false);
        parser.parseStartTag();
        assertEquals(1, parser.last().childNodes().size());
    }

    @Test
    public void testParseStartTag_EmptyAttribute() {
        // edge case: empty attribute value
        Parser parser = new Parser("<div class=''>", "https://example.com", false);
        parser.parseStartTag();
        assertEquals(1, parser.last().childNodes().size());
    }

    @Test
    public void testParseTextNode() {
        // typical use case
        Parser parser = new Parser("Hello World!</div>", "https://example.com", false);
        parser.parseTextNode();
        assertEquals(1, parser.last().childNodes().size());
    }

    @Test
    public void testParseCdata() {
        // typical use case
        Parser parser = new Parser("<![CDATA[This is CDATA]]>", "https://example.com", false);
        parser.parseCdata();
        assertEquals(1, parser.last().childNodes().size());
    }

    @Test
    public void testAddChildToParent() {
        // typical use case
        Parser parser = new Parser("<body><h1>Hello World!</h1></body>", "https://example.com", false);
        parser.parseStartTag();
        parser.stack.add(parser.last());
        parser.parseStartTag();
        parser.parseTextNode();
        assertEquals(2, parser.stack.size());
    }

    @Test
    public void testAddChildToParent_ImplicitParent() {
        // edge case: implicit parent creation
        Parser parser = new Parser("<div><h1>Hello World!</h1></div>", "https://example.com", false);
        parser.parseStartTag();
        parser.stack.add(parser.last());
        parser.parseStartTag();
        assertEquals(3, parser.stack.size());
    }

    @Test
    public void testPopStackToSuitableContainer() {
        // typical use case
        Parser parser = new Parser("<body><h1>Hello World!</h1></body>", "https://example.com", false);
        parser.parseStartTag();
        parser.stack.add(parser.last());
        parser.parseStartTag();
        parser.parseStartTag();
        assertEquals(3, parser.stack.size());
        parser.popStackToSuitableContainer(parser.last().tag());
        assertEquals(2, parser.stack.size());
    }

    @Test
    public void testPopStackToClose() {
        // typical use case
        Parser parser = new Parser("<body><h1>Hello World!</h1></body>", "https://example.com", false);
        parser.parseStartTag();
        parser.stack.add(parser.last());
        parser.parseStartTag();
        parser.parseEndTag();
        assertEquals(1, parser.stack.size());
    }

    @Test
    public void testLast() {
        // typical use case
        Parser parser = new Parser("<body><h1>Hello World!</h1></body>", "https://example.com", false);
        parser.parseStartTag();
        parser.stack.add(parser.last());
        assertEquals(parser.stack.getLast(), parser.last());
    }
}