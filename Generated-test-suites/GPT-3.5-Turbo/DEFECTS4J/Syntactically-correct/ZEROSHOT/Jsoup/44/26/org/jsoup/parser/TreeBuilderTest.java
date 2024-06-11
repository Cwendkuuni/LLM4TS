import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = treeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertNotNull(document.title());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        Document document = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertNotNull(document.title());
    }

    @Test
    public void testRunParser() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        assertEquals(Token.TokenType.EOF, treeBuilder.currentToken.type);
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";
        boolean result = treeBuilder.processStartTag(name);

        assertTrue(result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "myDiv");
        attrs.put("class", "content");
        boolean result = treeBuilder.processStartTag(name, attrs);

        assertTrue(result);
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";
        boolean result = treeBuilder.processEndTag(name);

        assertTrue(result);
    }

    @Test
    public void testCurrentElement() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals("html", treeBuilder.currentElement().tagName());
    }
}