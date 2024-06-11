import org.jsoup.parser.*;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        treeBuilder.initialiseParse("test input", "http://example.com", null);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertEquals("http://example.com", treeBuilder.baseUri);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testProcessStartTagWithName() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        assertTrue(treeBuilder.processStartTag("div"));
    }

    @Test
    public void testProcessStartTagWithNameAndAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        Attributes attrs = new Attributes();
        attrs.put("id", "test-id");

        assertTrue(treeBuilder.processStartTag("div", attrs));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        assertTrue(treeBuilder.processEndTag("div"));
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        assertNull(treeBuilder.currentElement());

        // Adding a dummy element to the stack
        treeBuilder.stack.add(new Element("test"));
        assertNotNull(treeBuilder.currentElement());
    }
}