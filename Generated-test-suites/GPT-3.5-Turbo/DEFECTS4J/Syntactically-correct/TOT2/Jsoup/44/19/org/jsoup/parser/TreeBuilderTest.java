import static org.junit.Assert.*;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Test successful initialization
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", null);
        assertEquals("baseUri", treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
    }

    @Test
    public void testProcessStartTag() {
        // Test processing start tag
        TreeBuilder treeBuilder = new TreeBuilder();
        assertTrue(treeBuilder.processStartTag("tag"));
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        // Test processing start tag with attributes
        TreeBuilder treeBuilder = new TreeBuilder();
        assertTrue(treeBuilder.processStartTag("tag", new Attributes()));
    }

    @Test
    public void testProcessEndTag() {
        // Test processing end tag
        TreeBuilder treeBuilder = new TreeBuilder();
        assertTrue(treeBuilder.processEndTag("tag"));
    }

    @Test
    public void testCurrentElement() {
        // Test getting current element
        TreeBuilder treeBuilder = new TreeBuilder();
        assertNull(treeBuilder.currentElement());
    }
}