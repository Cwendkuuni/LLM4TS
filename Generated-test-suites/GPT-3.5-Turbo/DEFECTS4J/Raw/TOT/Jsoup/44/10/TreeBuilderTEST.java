import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;

import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // dummy implementation for test purposes
                return true;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        Document doc = treeBuilder.parse(input, baseUri, errors);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testRunParser() {
        String input = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        
        treeBuilder.initialiseParse(input, baseUri, new ParseErrorList());
        treeBuilder.runParser();
        
        assertTrue(treeBuilder.tokeniser.eof());
    }

    @Test
    public void testProcessStartTag() {
        String tagName = "div";
        
        boolean result = treeBuilder.processStartTag(tagName);
        
        assertTrue(result);
        assertEquals(tagName, treeBuilder.start.name());
        assertNull(treeBuilder.start.attributes());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String tagName = "div";
        Attributes attrs = new Attributes();
        
        boolean result = treeBuilder.processStartTag(tagName, attrs);
        
        assertTrue(result);
        assertEquals(tagName, treeBuilder.start.name());
        assertEquals(attrs, treeBuilder.start.attributes());
    }

    @Test
    public void testProcessEndTag() {
        String tagName = "div";
        
        boolean result = treeBuilder.processEndTag(tagName);
        
        assertTrue(result);
        assertEquals(tagName, treeBuilder.end.name());
    }

    @Test
    public void testCurrentElementWithValidStack() {
        Element element1 = new Element("div");
        Element element2 = new Element("span");
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);
        
        Element currentElement = treeBuilder.currentElement();
        
        assertEquals(element2, currentElement);
    }

    @Test
    public void testCurrentElementWithEmptyStack() {
        assertNull(treeBuilder.currentElement());
    }
}