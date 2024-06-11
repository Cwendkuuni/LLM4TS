import static org.junit.Assert.*;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        ParseErrorList errorList = new ParseErrorList();
        treeBuilder.initialiseParse("<html><body></body></html>", "http://example.com", errorList);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertEquals("http://example.com", treeBuilder.baseUri);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        Document document = treeBuilder.parse("<html><body></body></html>", "http://example.com");

        assertNotNull(document);
    }

    @Test
    public void testRunParser() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        assertFalse(treeBuilder.process(new Token(Token.TokenType.EOF)));
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        assertFalse(treeBuilder.processStartTag("p"));
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        Attributes attrs = new Attributes();
        attrs.put("class", "test");

        assertFalse(treeBuilder.processStartTag("div", attrs));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        assertFalse(treeBuilder.processEndTag("body"));
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
    }
}