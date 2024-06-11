import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;

import static org.junit.Assert.assertEquals;

public class TreeBuilderTest {
    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }

    // Test for initialiseParse()
    @Test
    public void testInitialiseParse() {
        String input = "<html><body><div>Hello World</div></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(errors, treeBuilder.errors);
        assertEquals(32, treeBuilder.stack.size());
        assertEquals(baseUri, treeBuilder.doc.baseUri());
    }

    // Test for parse()
    @Test
    public void testParse() {
        String input = "<html><body><div>Hello World</div></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = treeBuilder.parse(input, baseUri);

        assertEquals(baseUri, doc.baseUri());
        assertEquals("html", doc.child(0).tagName());
        assertEquals("body", doc.child(0).child(0).tagName());
        assertEquals("div", doc.child(0).child(0).child(0).tagName());
        assertEquals("Hello World", doc.child(0).child(0).child(0).text());
    }

    // Test for runParser() - process EOF
    @Test
    public void testRunParserWithEOF() {
        treeBuilder.tokeniser = new Tokeniser(null, null);
        Token token = new Token();
        token.type = Token.TokenType.EOF;
        treeBuilder.tokeniser.tokens.add(token);

        treeBuilder.runParser();
    }

    // Test for processStartTag() - typical use case
    @Test
    public void testProcessStartTag() {
        treeBuilder.start = new Token.StartTag().name("div");
        boolean result = treeBuilder.processStartTag("div");

        assertEquals(true, result);
    }

    // Test for processStartTag() - edge case
    @Test
    public void testProcessStartTagWithAttributes() {
        treeBuilder.start = new Token.StartTag().nameAttr("div", new Attributes());
        boolean result = treeBuilder.processStartTag("div");

        assertEquals(true, result);
    }

    // Test for processEndTag() - typical use case
    @Test
    public void testProcessEndTag() {
        treeBuilder.end = new Token.EndTag().name("div");
        boolean result = treeBuilder.processEndTag("div");

        assertEquals(true, result);
    }

    // Test for currentElement() - empty stack
    @Test
    public void testCurrentElementWithEmptyStack() {
        Element element = treeBuilder.currentElement();

        assertEquals(null, element);
    }

    // Test for currentElement() - non-empty stack
    @Test
    public void testCurrentElementWithNonEmptyStack() {
        Element divElement = new Element("div");
        treeBuilder.stack.add(divElement);

        Element element = treeBuilder.currentElement();

        assertEquals(divElement, element);
    }
}