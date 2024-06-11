import org.jsoup.nodes.Document;
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
                // Mock implementation for testing
                return false;
            }
        };
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";
        Document doc = treeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testProcessStartTag() {
        assertFalse(treeBuilder.processStartTag("div"));
        assertFalse(treeBuilder.processStartTag("a", new Attributes()));
    }

    @Test
    public void testProcessEndTag() {
        assertFalse(treeBuilder.processEndTag("div"));
    }

    @Test
    public void testCurrentElement() {
        assertNull(treeBuilder.currentElement());

        Element element = new Element("div");
        treeBuilder.stack.add(element);
        
        assertEquals(element, treeBuilder.currentElement());
    }
}