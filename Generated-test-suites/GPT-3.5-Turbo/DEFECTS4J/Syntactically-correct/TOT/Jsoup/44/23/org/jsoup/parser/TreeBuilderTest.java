
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false; // implement later
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html></html>";
        String baseUri = "https://www.example.com";

        Document doc = treeBuilder.parse(input, baseUri);

        assertNotNull(doc);
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        Document doc = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(doc);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testRunParser() {
        // Implement later
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";

        boolean result = treeBuilder.processStartTag(name);

        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "myDiv");

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertFalse(result);
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";

        boolean result = treeBuilder.processEndTag(name);

        assertFalse(result);
    }

    @Test
    public void testCurrentElementWithEmptyStack() {
        Element result = treeBuilder.currentElement();

        assertNull(result);
    }

    @Test
    public void testCurrentElementWithStack() {
        Element element = new Element("div");

        treeBuilder.stack.add(element);

        Element result = treeBuilder.currentElement();

        assertSame(element, result);
    }
}
