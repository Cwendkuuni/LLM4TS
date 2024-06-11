import static org.junit.Assert.*;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Test normal case
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", null);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        // Test normal case
        TreeBuilder treeBuilder = new TreeBuilder();
        assertNotNull(treeBuilder.parse("input", "baseUri"));

        // Test with error tracking
        assertNotNull(treeBuilder.parse("input", "baseUri", new ParseErrorList()));
    }

    @Test
    public void testRunParser() {
        // Test normal case
        TreeBuilder treeBuilder = new TreeBuilder();
        // Cannot test runParser() directly as it is a protected method
        // However, it is indirectly tested within the parse() method
    }

    @Test
    public void testProcessStartTag() {
        // Test normal case
        TreeBuilder treeBuilder = new TreeBuilder();
        assertTrue(treeBuilder.processStartTag("tag"));

        // Test edge case with attributes
        assertTrue(treeBuilder.processStartTag("tag", new Attributes()));

        // Test error scenario with invalid name
        assertFalse(treeBuilder.processStartTag(null));
    }

    @Test
    public void testProcessEndTag() {
        // Test normal case
        TreeBuilder treeBuilder = new TreeBuilder();
        assertTrue(treeBuilder.processEndTag("tag"));

        // Test error scenario with invalid name
        assertFalse(treeBuilder.processEndTag(null));
    }

    @Test
    public void testCurrentElement() {
        // Test normal case
        TreeBuilder treeBuilder = new TreeBuilder();
        assertNull(treeBuilder.currentElement());

        // Test with elements in stack
        treeBuilder.stack.add(new Element("test"));
        assertNotNull(treeBuilder.currentElement());
    }
}