import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {

    // Test case for initialiseParse method
    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse("input", "baseUri", errors);

        assertEquals("baseUri", treeBuilder.baseUri);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.errors);
    }

    // Test case for parse method
    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse("input", "baseUri", errors);

        assertNotNull(doc);
        assertEquals("baseUri", doc.baseUri());
    }

    // Test case for runParser method
    @Test
    public void testRunParser() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse("input", "baseUri", errors);
        treeBuilder.runParser();

        assertNotNull(treeBuilder.tokeniser.currentToken());
        assertEquals(Token.TokenType.EOF, treeBuilder.tokeniser.currentToken().type);
    }

    // Test case for processStartTag method
    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        assertTrue(treeBuilder.processStartTag("name"));
    }

    // Test case for processStartTag method with attributes
    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        Attributes attrs = new Attributes();
        attrs.put("attribute1", "value1");
        attrs.put("attribute2", "value2");

        assertTrue(treeBuilder.processStartTag("name", attrs));
    }

    // Test case for processEndTag method
    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        assertTrue(treeBuilder.processEndTag("name"));
    }

    // Test case for currentElement method
    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };

        Element element = new Element("div");
        treeBuilder.stack.add(element);

        assertEquals("div", treeBuilder.currentElement().tagName());
    }

}