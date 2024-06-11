
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {
    private TestTreeBuilder treeBuilder;

    private class TestTreeBuilder extends TreeBuilder {
        private ArrayList<Token> processedTokens;

        public TestTreeBuilder() {
            super();
            processedTokens = new ArrayList<>();
        }

        @Override
        protected boolean process(Token token) {
            processedTokens.add(token);
            return true;
        }

        public ArrayList<Token> getProcessedTokens() {
            return processedTokens;
        }
    }

    @Before
    public void setUp() {
        treeBuilder = new TestTreeBuilder();
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        Document doc = treeBuilder.parse(input, "http://www.example.com");

        assertNotNull("Parsed document is not null", doc);
        assertTrue("Processed tokens are not empty", !treeBuilder.getProcessedTokens().isEmpty());
        assertEquals("Document base URI is correct", "http://www.example.com", doc.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        String tagName = "div";
        boolean result = treeBuilder.processStartTag(tagName);

        assertTrue("Start tag processed successfully", result);
        assertEquals("Processed tokens contain the start tag", tagName, treeBuilder.getProcessedTokens().get(0).getName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String tagName = "a";
        Attributes attrs = new Attributes();
        attrs.put("href", "http://www.example.com");
        attrs.put("target", "_blank");
        boolean result = treeBuilder.processStartTag(tagName, attrs);

        assertTrue("Start tag with attributes processed successfully", result);
        assertEquals("Processed tokens contain the start tag", tagName, treeBuilder.getProcessedTokens().get(0).getName());
        assertEquals("Processed tokens contain the attributes", attrs, treeBuilder.getProcessedTokens().get(0).getAttributes());
    }

    @Test
    public void testProcessEndTag() {
        String tagName = "p";
        boolean result = treeBuilder.processEndTag(tagName);

        assertTrue("End tag processed successfully", result);
        assertEquals("Processed tokens contain the end tag", tagName, treeBuilder.getProcessedTokens().get(0).getName());
    }

    @Test
    public void testCurrentElement() {
        Element element = treeBuilder.currentElement();

        assertNull("Current element is null when stack is empty", element);

        String tagName = "div";
        treeBuilder.processStartTag(tagName);
        element = treeBuilder.currentElement();

        assertNotNull("Current element is not null after processing start tag", element);
        assertEquals("Current element has correct tag name", tagName, element.tagName());
    }
}
