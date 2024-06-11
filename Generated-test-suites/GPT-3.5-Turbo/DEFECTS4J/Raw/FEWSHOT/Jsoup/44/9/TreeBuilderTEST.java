package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.ParseErrorList;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {
    
    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // Mock process method
                return true;
            }
        };
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(32, treeBuilder.stack.size());
        assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testParseWithNoErrors() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        treeBuilder.doc = null;
        Document result = treeBuilder.parse(input, baseUri);

        assertEquals(baseUri, result.baseUri());
        assertEquals(0, treeBuilder.errors.size());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.doc = null;
        Document result = treeBuilder.parse(input, baseUri, errors);

        assertEquals(baseUri, result.baseUri());
        assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testRunParser() {
        // Test case for runParser method

        // Mocking tokeniser.read() method
        Token token1 = new Token(Token.TokenType.StartTag);
        Token token2 = new Token(Token.TokenType.EndTag);
        Token token3 = new Token(Token.TokenType.EOF);
        Tokeniser tokeniser = new Tokeniser(null, null) {
            int count = 0;

            @Override
            public Token read() {
                count++;
                switch(count) {
                    case 1: return token1;
                    case 2: return token2;
                    case 3: return token3;
                    default: return new Token(Token.TokenType.EOF);
                }
            }
        };

        treeBuilder.tokeniser = tokeniser;
        treeBuilder.process = token -> true;
        treeBuilder.runParser();

        assertEquals(1, token1.resetCount);
        assertEquals(1, token2.setupCount);
        assertEquals(0, token3.resetCount);
    }

    @Test
    public void testProcessStartTag() {
        // Test case for processStartTag(name) method

        // Mocking process method
        treeBuilder.process = token -> false;

        boolean result = treeBuilder.processStartTag("tag");

        assertEquals(true, result);
        assertEquals("tag", treeBuilder.start.name);
    }

    @Test
    public void testProcessStartTagWithNameAndAttrs() {
        // Test case for processStartTag(name, attrs) method

        // Mocking process method
        treeBuilder.process = token -> false;

        Attributes attrs = new Attributes();
        boolean result = treeBuilder.processStartTag("tag", attrs);

        assertEquals(true, result);
        assertEquals("tag", treeBuilder.start.name);
        assertEquals(attrs, treeBuilder.start.attrs);
    }

    @Test
    public void testProcessEndTag() {
        // Test case for processEndTag(name) method

        // Mocking process method
        treeBuilder.process = token -> false;

        boolean result = treeBuilder.processEndTag("tag");

        assertEquals(true, result);
        assertEquals("tag", treeBuilder.end.name);
    }

    @Test
    public void testCurrentElement() {
        // Test case for currentElement() method

        // Mocking stack
        treeBuilder.stack = new ArrayList<>();
        Element element1 = new Element("div");
        Element element2 = new Element("span");
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);

        Element result = treeBuilder.currentElement();

        assertEquals(element2, result);
    }
}