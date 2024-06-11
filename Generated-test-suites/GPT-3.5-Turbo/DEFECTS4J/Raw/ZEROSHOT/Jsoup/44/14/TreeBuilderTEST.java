import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Attributes;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;

import java.util.ArrayList;

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
        String input = "<html><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.errors);
        assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><body></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = treeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse(input, baseUri, errors);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testRunParser() {
        treeBuilder.reader = new CharacterReader("<html><body></body></html>");
        treeBuilder.tokeniser = new Tokeniser(treeBuilder.reader, treeBuilder.errors);
        treeBuilder.runParser();
        assertEquals(Token.TokenType.EOF, treeBuilder.tokeniser.currentToken().type);
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";
        assertTrue(treeBuilder.processStartTag(name));
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "container");
        assertTrue(treeBuilder.processStartTag(name, attrs));
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";
        assertTrue(treeBuilder.processEndTag(name));
    }

    @Test
    public void testCurrentElement() {
        Element element = new Element("div");
        treeBuilder.stack.add(element);
        Element current = treeBuilder.currentElement();
        assertEquals(element, current);
    }
}