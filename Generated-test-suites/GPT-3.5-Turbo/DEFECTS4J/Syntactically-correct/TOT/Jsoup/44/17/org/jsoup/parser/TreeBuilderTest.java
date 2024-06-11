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

        String input = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = null;

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
    }

    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        String input = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = null;

        Document doc = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(doc);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
    }

    @Test
    public void testRunParser() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        String input = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = null;

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        String name = "div";

        boolean result = treeBuilder.processStartTag(name);

        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "test");

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertFalse(result);
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        String name = "div";

        boolean result = treeBuilder.processEndTag(name);

        assertFalse(result);
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        Element element = treeBuilder.currentElement();

        assertNull(element);
    }
}