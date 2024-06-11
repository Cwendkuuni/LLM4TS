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
        treeBuilder.initialiseParse("input", "baseUri", null);
        assertNotNull(treeBuilder.doc);
        assertEquals("baseUri", treeBuilder.baseUri);
    }
    
    @Test
    public void testParse() {
        assertNotNull(treeBuilder.parse("input", "baseUri"));
    }
    
    @Test
    public void testProcessStartTagWithoutAttributes() {
        assertTrue(treeBuilder.processStartTag("name"));
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        assertTrue(treeBuilder.processStartTag("name", new Attributes()));
    }

    @Test
    public void testProcessEndTag() {
        assertTrue(treeBuilder.processEndTag("name"));
    }

    @Test
    public void testCurrentElement() {
        assertNull(treeBuilder.currentElement());
    }
}