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
                return false;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        treeBuilder.initialiseParse("input", "http://example.com", null);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNotNull(treeBuilder.tokeniser);
    }

    @Test
    public void testProcessStartTag() {
        assertTrue(treeBuilder.processStartTag("div"));
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        assertTrue(treeBuilder.processStartTag("div", new Attributes()));
    }

    @Test
    public void testProcessEndTag() {
        assertTrue(treeBuilder.processEndTag("div"));
    }

    @Test
    public void testCurrentElement() {
        assertNull(treeBuilder.currentElement());
        treeBuilder.stack.add(new Element("div"));
        assertEquals("div", treeBuilder.currentElement().tagName());
    }
}