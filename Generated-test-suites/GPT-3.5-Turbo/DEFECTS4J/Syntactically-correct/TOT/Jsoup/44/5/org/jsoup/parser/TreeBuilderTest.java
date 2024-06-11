import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder testTreeBuilder;

    @Before
    public void setUp() {
        testTreeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }

    @Test
    public void testInitialiseParseValidInput() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();

        testTreeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(testTreeBuilder.doc);
        assertNotNull(testTreeBuilder.reader);
        assertNotNull(testTreeBuilder.tokeniser);
        assertNotNull(testTreeBuilder.stack);
        assertNotNull(testTreeBuilder.baseUri);
        assertEquals(errors, testTreeBuilder.errors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialiseParseNullInput() {
        String input = null;
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();

        testTreeBuilder.initialiseParse(input, baseUri, errors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialiseParseNullBaseUri() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = null;
        ParseErrorList errors = ParseErrorList.noTracking();

        testTreeBuilder.initialiseParse(input, baseUri, errors);
    }

    @Test
    public void testParseNoErrors() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();

        Document doc = testTreeBuilder.parse(input, baseUri, errors);

        assertNotNull(doc);
        assertNull(errors.getFirst());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>";

        // Deliberately incomplete HTML causes a parse error
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.tracking(1);

        Document doc = testTreeBuilder.parse(input, baseUri, errors);

        assertNotNull(doc);
        assertNotNull(errors.getFirst());
    }

    @Test
    public void testProcessStartTagValid() {
        String name = "div";

        boolean result = testTreeBuilder.processStartTag(name);

        assertTrue(result);
    }

    @Test
    public void testProcessStartTagEmptyName() {
        String name = "";

        boolean result = testTreeBuilder.processStartTag(name);

        assertFalse(result);
    }

    @Test
    public void testProcessStartTagNullName() {
        String name = null;

        boolean result = testTreeBuilder.processStartTag(name);

        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("class", "container");

        boolean result = testTreeBuilder.processStartTag(name, attrs);

        assertTrue(result);
    }

    @Test
    public void testProcessEndTagValid() {
        String name = "div";

        boolean result = testTreeBuilder.processEndTag(name);

        assertTrue(result);
    }

    @Test
    public void testProcessEndTagEmptyName() {
        String name = "";

        boolean result = testTreeBuilder.processEndTag(name);

        assertFalse(result);
    }

    @Test
    public void testProcessEndTagNullName() {
        String name = null;

        boolean result = testTreeBuilder.processEndTag(name);

        assertFalse(result);
    }

    @Test
    public void testCurrentElementNotEmptyStack() {
        Element element = new Element("div");
        testTreeBuilder.stack.add(element);

        Element result = testTreeBuilder.currentElement();

        assertNotNull(result);
        assertEquals(element, result);
    }

    @Test
    public void testCurrentElementEmptyStack() {
        Element result = testTreeBuilder.currentElement();

        assertNull(result);
    }
}