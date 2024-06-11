
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TestTreeBuilder();
    }

    @Test
    public void testProcessStartTag() {
        boolean success = treeBuilder.processStartTag("test");
        
        assertTrue(success);
    }
    
    @Test
    public void testProcessStartTagWithAttributes() {
        boolean success = treeBuilder.processStartTag("test", new Attributes());
        
        assertTrue(success);
    }

    @Test
    public void testProcessEndTag() {
        boolean success = treeBuilder.processEndTag("test");
        
        assertTrue(success);
    }

    private class TestTreeBuilder extends TreeBuilder {
        @Override
        protected boolean process(Token token) {
            return true;
        }
    }
}
