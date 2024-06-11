import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertEquals(32, treeBuilder.stack.size());
        assertEquals(baseUri, treeBuilder.baseUri);
    }

    @Test
    public void testParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        Document document = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testRunParser() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        assertNotNull(treeBuilder.tokeniser.currentToken);
        assertEquals(Token.TokenType.EOF, treeBuilder.tokeniser.currentToken.type);
    }

    @Test
    public void testProcessStartTag() {
        String name = "html";
        boolean result = treeBuilder.processStartTag(name);

        assertFalse(result);
        assertNotNull(treeBuilder.currentToken);
        assertEquals(name, treeBuilder.currentToken.name());
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("class", "container");
        boolean result = treeBuilder.processStartTag(name, attrs);

        assertFalse(result);
        assertNotNull(treeBuilder.currentToken);
        assertEquals(name, treeBuilder.currentToken.name());
        assertEquals(attrs, treeBuilder.currentToken.attributes);
    }

    @Test
    public void testProcessEndTag() {
        String name = "body";
        boolean result = treeBuilder.processEndTag(name);

        assertFalse(result);
        assertNotNull(treeBuilder.currentToken);
        assertEquals(name, treeBuilder.currentToken.name());
    }

    @Test
    public void testCurrentElement() {
        Element element = treeBuilder.currentElement();

        assertNull(element);
    }
}