import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Test positive case
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        treeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNotNull(treeBuilder.reader);
    }

    @Test
    public void testParse() {
        // Test positive case
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        Document document = treeBuilder.parse(input, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testProcessStartTag() {
        // Test positive case
        String tagName = "div";
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        assertTrue(treeBuilder.processStartTag(tagName));

        // Test edge case where tag name is null
        assertNull(treeBuilder.processStartTag(null));
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        // Test positive case
        String tagName = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "main");
        attrs.put("class", "container");
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        assertTrue(treeBuilder.processStartTag(tagName, attrs));
    }

    @Test
    public void testProcessEndTag() {
        // Test positive case
        String tagName = "div";
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        assertTrue(treeBuilder.processEndTag(tagName));

        // Test edge case where tag name is null
        assertNull(treeBuilder.processEndTag(null));
    }

    @Test
    public void testCurrentElement() {
        // Test positive case
        TreeBuilder treeBuilder = new TreeBuilderImpl();
        Element element = treeBuilder.currentElement();

        assertNull(element); // Since stack is empty
    }
}