
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

    private class TestTreeBuilder extends TreeBuilder {
        @Override
        protected boolean process(Token token) {
            // implement custom process method for test purposes
            return false;
        }
    }

    @Test
    public void testInitialiseParse() {
        String input = "test";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        TestTreeBuilder treeBuilder = new TestTreeBuilder();
        treeBuilder.initialiseParse(input, baseUri, errors);

        // Verify that the TreeBuilder properties are initialized correctly
        Assert.assertNotNull(treeBuilder.doc);
        Assert.assertNotNull(treeBuilder.reader);
        Assert.assertNotNull(treeBuilder.tokeniser);
        Assert.assertEquals(baseUri, treeBuilder.baseUri);
        Assert.assertNotNull(treeBuilder.stack);
        Assert.assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        TestTreeBuilder treeBuilder = new TestTreeBuilder();
        Document doc = treeBuilder.parse(input, baseUri, errors);

        // Verify that the parse method returns a non-null Document object
        Assert.assertNotNull(doc);
        // Verify that the Document object has the correct base URI
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testRunParser() {
        TestTreeBuilder treeBuilder = new TestTreeBuilder();

        // Add Token.EOF to the end of the tokeniser's read sequence
        Token.TokenType[] tokenTypes = {Token.TokenType.EOF};
        treeBuilder.tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public Token read() {
                Token.TokenType type = tokenTypes[0];
                tokenTypes[0] = null;
                return new Token(type);
            }
        };

        // Call the runParser method
        treeBuilder.runParser();

        // Verify that the process method was called with Token.EOF
        Assert.assertTrue(treeBuilder.processCalled);
    }

    @Test
    public void testProcessStartTag() {
        TestTreeBuilder treeBuilder = new TestTreeBuilder();

        // Add Token.TokenType.StartTag to the end of the tokeniser's read sequence
        Token.TokenType[] tokenTypes = {Token.TokenType.StartTag};
        treeBuilder.tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public Token read() {
                Token.TokenType type = tokenTypes[0];
                tokenTypes[0] = null;
                return new Token(type);
            }
        };

        // Call the processStartTag method
        boolean result = treeBuilder.processStartTag("tag");

        // Verify that the process method was called with a StartTag token
        Assert.assertTrue(treeBuilder.processCalled);
        // Verify that the processStartTag method returned the correct result
        Assert.assertTrue(result);
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        TestTreeBuilder treeBuilder = new TestTreeBuilder();

        // Add Token.TokenType.StartTag to the end of the tokeniser's read sequence
        Token.TokenType[] tokenTypes = {Token.TokenType.StartTag};
        treeBuilder.tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public Token read() {
                Token.TokenType type = tokenTypes[0];
                tokenTypes[0] = null;
                return new Token(type);
            }
        };

        // Call the processStartTag method
        boolean result = treeBuilder.processStartTag("tag", new Attributes());

        // Verify that the process method was called with a StartTag token
        Assert.assertTrue(treeBuilder.processCalled);
        // Verify that the processStartTag method returned the correct result
        Assert.assertTrue(result);
    }

    @Test
    public void testProcessEndTag() {
        TestTreeBuilder treeBuilder = new TestTreeBuilder();

        // Add Token.TokenType.EndTag to the end of the tokeniser's read sequence
        Token.TokenType[] tokenTypes = {Token.TokenType.EndTag};
        treeBuilder.tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()) {
            @Override
            public Token read() {
                Token.TokenType type = tokenTypes[0];
                tokenTypes[0] = null;
                return new Token(type);
            }
        };

        // Call the processEndTag method
        boolean result = treeBuilder.processEndTag("tag");

        // Verify that the process method was called with an EndTag token
        Assert.assertTrue(treeBuilder.processCalled);
        // Verify that the processEndTag method returned the correct result
        Assert.assertTrue(result);
    }

    @Test
    public void testCurrentElement() {
        TestTreeBuilder treeBuilder = new TestTreeBuilder();
        treeBuilder.stack = new ArrayList<>();

        // Add a test Element to the stack
        Element element = new Element("test");
        treeBuilder.stack.add(element);

        // Call the currentElement method
        Element currentElement = treeBuilder.currentElement();

        // Verify that the currentElement method returned the correct Element
        Assert.assertEquals(element, currentElement);
    }
}

