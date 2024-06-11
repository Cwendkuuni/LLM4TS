import org.junit.Test;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<html></html>";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder builder = new TreeBuilder();
        builder.initialiseParse(input, baseUri, errors);

        assertEquals(baseUri, builder.baseUri);
        assertNotNull(builder.doc);
        assertNotNull(builder.reader);
        assertNotNull(builder.tokeniser);
        assertNotNull(builder.stack);
        assertNull(builder.currentToken);
        assertSame(errors, builder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html></html>";
        String baseUri = "https://example.com/";

        TreeBuilder builder = new TreeBuilder();
        Document document = builder.parse(input, baseUri);

        assertEquals(baseUri, document.baseUri());
        assertNotNull(document.children());
        assertEquals(1, document.children().size());
        assertEquals("html", document.child(0).tagName());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><br></html>";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder builder = new TreeBuilder();
        Document document = builder.parse(input, baseUri, errors);

        assertEquals(baseUri, document.baseUri());
        assertNotNull(document.children());
        assertEquals(2, document.children().size());
        assertEquals("html", document.child(0).tagName());
        assertEquals("br", document.child(1).tagName());

        assertEquals(1, errors.size());
        assertEquals("Unexpected end tag </html>", errors.get(0).getErrorMessage());
    }

    @Test
    public void testRunParser() {
        String input = "<html></html>";
        String baseUri = "https://example.com/";

        TreeBuilder builder = new TreeBuilder();
        builder.initialiseParse(input, baseUri, new ParseErrorList());
        builder.runParser();

        assertEquals(baseUri, builder.doc.baseUri());
        assertNotNull(builder.doc.children());
        assertEquals(1, builder.doc.children().size());
        assertEquals("html", builder.doc.child(0).tagName());
    }

    @Test
    public void testProcessStartTag() {
        String input = "<p></p>";
        String baseUri = "https://example.com/";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        builder.initialiseParse(input, baseUri, new ParseErrorList());
        boolean result = builder.processStartTag("p");

        assertTrue(result);
        assertEquals("p", builder.currentElement().tagName());
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        String input = "<a href='https://example.com'></a>";
        String baseUri = "https://example.com/";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        builder.initialiseParse(input, baseUri, new ParseErrorList());
        Attributes attrs = new Attributes();
        attrs.put("href", "https://example.com");
        boolean result = builder.processStartTag("a", attrs);

        assertTrue(result);
        assertEquals("a", builder.currentElement().tagName());
        assertEquals("https://example.com", builder.currentElement().attr("href"));
    }

    @Test
    public void testProcessEndTag() {
        String input = "<p></p>";
        String baseUri = "https://example.com/";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        builder.initialiseParse(input, baseUri, new ParseErrorList());
        boolean result = builder.processEndTag("p");

        assertTrue(result);
        assertEquals("p", builder.currentElement().tagName());
    }

    @Test
    public void testCurrentElement() {
        String input = "<div><p></p></div>";
        String baseUri = "https://example.com/";

        TreeBuilder builder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        builder.initialiseParse(input, baseUri, new ParseErrorList());
        builder.processStartTag("div");
        builder.processStartTag("p");
        Element element = builder.currentElement();

        assertNotNull(element);
        assertEquals("p", element.tagName());
    }
}