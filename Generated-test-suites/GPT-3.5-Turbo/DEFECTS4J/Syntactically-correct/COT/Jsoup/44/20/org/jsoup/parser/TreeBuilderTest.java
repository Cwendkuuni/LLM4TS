import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;

import static org.junit.Assert.*;

public class TreeBuilderTest {
    private TreeBuilder treeBuilder;
    private String testInput;
    private String baseUri;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        testInput = "<html><head><title>Test Page</title></head><body></body></html>";
        baseUri = "https://example.com";
    }

    @Test
    public void testInitialiseParseNotNullInput() {
        assertThrows(IllegalArgumentException.class, () ->
                treeBuilder.initialiseParse(null, baseUri, new ParseErrorList()));
    }

    @Test
    public void testInitialiseParseNotNullBaseUri() {
        assertThrows(IllegalArgumentException.class, () ->
                treeBuilder.initialiseParse(testInput, null, new ParseErrorList()));
    }

    @Test
    public void testParseWithoutErrors() {
        Document document = treeBuilder.parse(testInput, baseUri, ParseErrorList.noTracking());
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithErrors() {
        ParseErrorList errors = new ParseErrorList();
        Document document = treeBuilder.parse(testInput, baseUri, errors);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testProcessStartTagWithName() {
        assertTrue(treeBuilder.processStartTag("html"));
    }

    @Test
    public void testProcessStartTagWithNameAndAttrs() {
        Attributes attrs = new Attributes();
        attrs.put("lang", "en");
        assertTrue(treeBuilder.processStartTag("html", attrs));
    }

    @Test
    public void testProcessEndTag() {
        assertTrue(treeBuilder.processEndTag("body"));
    }

    @Test
    public void testCurrentElementWithEmptyStack() {
        assertNull(treeBuilder.currentElement());
    }

    @Test
    public void testCurrentElementWithNonEmptyStack() {
        Element element = new Element("div");
        treeBuilder.stack.add(element);
        assertEquals(element, treeBuilder.currentElement());
    }
}