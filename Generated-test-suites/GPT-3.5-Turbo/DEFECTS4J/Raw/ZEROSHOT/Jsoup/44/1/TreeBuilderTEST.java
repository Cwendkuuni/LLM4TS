import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
    public void initialiseParseShouldSetDocAndReaderAndErrors() {
        String input = "<html><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.errors);
    }

    @Test(expected = NullPointerException.class)
    public void initialiseParseShouldThrowExceptionWhenInputIsNull() {
        String input = null;
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);
    }

    @Test(expected = NullPointerException.class)
    public void initialiseParseShouldThrowExceptionWhenBaseUriIsNull() {
        String input = "<html><body></body></html>";
        String baseUri = null;
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);
    }

    @Test
    public void parseShouldReturnDocumentObject() {
        String input = "<html><body></body></html>";
        String baseUri = "http://example.com";

        Document document = treeBuilder.parse(input, baseUri);

        assertNotNull(document);
    }

    @Test
    public void parseWithErrorsShouldReturnDocumentObject() {
        String input = "<html><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        Document document = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(document);
    }

    @Test
    public void runParserShouldProcessTokens() {
        // Test logic for runParser() method
        // ...
    }

    @Test
    public void processStartTagShouldProcessStartTagToken() {
        String tagName = "div";

        boolean result = treeBuilder.processStartTag(tagName);

        assertTrue(result);
    }

    @Test
    public void processStartTagWithAttrsShouldProcessStartTagTokenWithAttributes() {
        String tagName = "div";
        Attributes attrs = new Attributes();

        boolean result = treeBuilder.processStartTag(tagName, attrs);

        assertTrue(result);
    }

    @Test
    public void processEndTagShouldProcessEndTagToken() {
        String tagName = "div";

        boolean result = treeBuilder.processEndTag(tagName);

        assertTrue(result);
    }

    @Test
    public void currentElementShouldReturnCurrentElementFromStack() {
        Element element = new Element("div");
        treeBuilder.stack.add(element);

        Element current = treeBuilder.currentElement();

        assertSame(element, current);
    }
}