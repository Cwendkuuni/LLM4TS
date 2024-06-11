import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;

import java.util.ArrayList;

class TreeBuilderTest {

    // Test case for initialiseParse method
    @Test
    public void testInitialiseParse() {
        String input = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                return false;
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);

        Assertions.assertNotNull(treeBuilder.doc);
        Assertions.assertNotNull(treeBuilder.reader);
        Assertions.assertNotNull(treeBuilder.tokeniser);
        Assertions.assertNotNull(treeBuilder.stack);
        Assertions.assertEquals(treeBuilder.baseUri, baseUri);
        Assertions.assertEquals(treeBuilder.errors, errors);
    }

    // Test case for parse method
    @Test
    public void testParse() {
        String input = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                return false;
            }
        };

        Document document = treeBuilder.parse(input, baseUri, errors);

        Assertions.assertNotNull(document);
        Assertions.assertEquals(document.baseUri(), baseUri);
        Assertions.assertEquals(document.select("p").text(), "Hello, World!");
    }

    // Test case for runParser method with EOF token
    @Test
    public void testRunParserWithEOFToken() {
        String input = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                return false;
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        Assertions.assertEquals(treeBuilder.doc.select("p").text(), "Hello, World!");
        Assertions.assertEquals(treeBuilder.doc.select("body").first().tagName(), "body");
    }

    // Test case for processStartTag method
    @Test
    public void testProcessStartTag() {
        String input = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        // Custom TreeBuilder implementation for testing
        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.StartTag) {
                    Element element = currentElement();
                    if (element != null) {
                        element.appendElement(token.asStartTag().name());
                    }
                }
                return false;
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        Assertions.assertNotNull(treeBuilder.doc);
        Assertions.assertEquals(treeBuilder.doc.select("p").text(), "Hello, World!");
    }

    // Test case for processStartTag method with attributes
    @Test
    public void testProcessStartTagWithAttributes() {
        String input = "<html><body><p class=\"test\">Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        // Custom TreeBuilder implementation for testing
        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.StartTag) {
                    Element element = currentElement();
                    if (element != null) {
                        Attributes attrs = token.asStartTag().getAttributes();
                        element.appendElement(token.asStartTag().name())
                               .addClass(attrs.get("class"));
                    }
                }
                return false;
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        Assertions.assertNotNull(treeBuilder.doc);
        Assertions.assertEquals(treeBuilder.doc.select("p.test").text(), "Hello, World!");
    }

    // Test case for processEndTag method
    @Test
    public void testProcessEndTag() {
        String input = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        // Custom TreeBuilder implementation for testing
        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                if (token.type == Token.TokenType.EndTag) {
                    Element element = currentElement();
                    if (element != null) {
                        element.appendElement(token.asEndTag().name());
                    }
                }
                return false;
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        Assertions.assertNotNull(treeBuilder.doc);
        Assertions.assertEquals(treeBuilder.doc.select("p").text(), "Hello, World!");
    }

    // Test case for currentElement method when stack is empty
    @Test
    public void testCurrentElementWithEmptyStack() {
        String input = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        TreeBuilder treeBuilder = new TreeBuilder() {
            protected boolean process(Token token) {
                return false;
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        Element element = treeBuilder.currentElement();

        Assertions.assertNull(element);
    }
}