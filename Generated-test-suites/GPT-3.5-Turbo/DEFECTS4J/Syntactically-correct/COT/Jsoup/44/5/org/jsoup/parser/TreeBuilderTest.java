
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;

import static org.junit.Assert.*;

public class TreeBuilderTest {
    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // override process method for testing purposes
                return false;
            }
        };
    }

    @Test
    public void testParse() {
        String input = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = treeBuilder.parse(input, baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.body());
        assertEquals("<h1>Title</h1>", doc.body().html());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseNullInput() {
        String baseUri = "http://example.com";
        treeBuilder.parse(null, baseUri);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseNullBaseUri() {
        String input = "<html><body><h1>Title</h1></body></html>";
        treeBuilder.parse(input, null);
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";
        boolean result = treeBuilder.processStartTag(name);

        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "myDiv");
        attrs.put("class", "container");

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
        Element element = treeBuilder.currentElement();

        assertNull(element);
    }

    @Test
    public void testCurrentElementWithNonEmptyStack() {
        Element expectedElement = new Element("div");
        treeBuilder.stack.add(expectedElement);

        Element element = treeBuilder.currentElement();

        assertEquals(expectedElement, element);
    }
}
