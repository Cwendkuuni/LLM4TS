package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * Test cases for TreeBuilder class
 */
public class TreeBuilderTest {

    @org.junit.Before
    public void setup() {
        // Initialize mocks
        CharacterReader mockCharacterReader = mock(CharacterReader.class);
        Tokenizer mockTokenizer = mock(Tokenizer.class);
        ParseErrorList mockErrors = mock(ParseErrorList.class);
    }

    @org.junit.Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "test input";
        String baseUri = "test base uri";

        treeBuilder.initialiseParse(input, baseUri, mockErrors);

        // Check arguments validation
        VerifyThat.notNull(treeBuilder.reader, "reader should not be null");
        VerifyThat.notNull(treeBuilder.tokeniser, "tokeniser should not be null");
        VerifyThat.notNull(treeBuilder.doc, "doc should not be null");
        VerifyThat.notNull(treeBuilder.stack, "stack should not be null");
        VerifyThat.notNull(treeBuilder.baseUri, "baseUri should not be null");
        VerifyThat.isNull(treeBuilder.currentToken);
        VerifyThat.isNull(treeBuilder.errors);
    }

    @org.junit.Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "test input";
        String baseUri = "test base uri";

        Document doc = treeBuilder.parse(input, baseUri);

        // Check doc is not null
        assertNotNull(doc);
    }

    @org.junit.Test
    public void testParseWithErrors() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "test input";
        String baseUri = "test base uri";       
        ParseErrorList errors = mock(ParseErrorList.class);

        Document doc = treeBuilder.parse(input, baseUri, errors);

        // Check doc is not null
        assertNotNull(doc);
        // Check errors list is passed and contains errors if any
        verify(errors).addError(any(ParseError.class));
    }

    @org.junit.Test
    public void testRunParser() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "test input";
        String baseUri = "test base uri";
        ParseErrorList errors = mock(ParseErrorList.class);

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        // Check all tokens are processed
        verify(treeBuilder.tokeniser, times(input.length())).read();
    }

    @org.junit.Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String name = "test name";

        treeBuilder.processStartTag(name);

        // Check process method is called with correct argument
        Token startTag = new Token.StartTag();
        startTag.name(name);
        verify(treeBuilder).process(startTag);
    }

    @org.junit.Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String name = "test name";
        Attributes attrs = mock(Attributes.class);

        treeBuilder.processStartTag(name, attrs);

        // Check process method is called with correct argument
        Token startTag = new Token.StartTag();
        startTag.nameAttr(name, attrs);
        verify(treeBuilder).process(startTag);
    }

    @org.junit.Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String name = "test name";

        treeBuilder.processEndTag(name);

        // Check process method is called with correct argument
        Token endTag = new Token.EndTag();
        endTag.name(name);
        verify(treeBuilder).process(endTag);
    }

    @org.junit.Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Element element = mock(Element.class);
        ArrayList<Element> stack = new ArrayList<>();
        stack.add(element);

        treeBuilder.stack = stack;

        Element result = treeBuilder.currentElement();

        // Check result is the same as the last element in the stack
        assertEquals(element, result);
    }

    @org.junit.Test
    public void testProcessStartTagWithEmptyName() {
        TreeBuilder treeBuilder = new TreeBuilder();

        // Exception test: empty name
        try {
            treeBuilder.processStartTag("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testProcessStartTagWithNullName() {
        TreeBuilder treeBuilder = new TreeBuilder();

        // Exception test: null name
        try {
            treeBuilder.processStartTag(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testProcessStartTagWithNullAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String name = "test name";

        Attributes attrs = null;

        // Exception test: null attributes
        try {
            treeBuilder.processStartTag(name, attrs);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testProcessEndTagWithEmptyName() {
        TreeBuilder treeBuilder = new TreeBuilder();

        // Exception test: empty name
        try {
            treeBuilder.processEndTag("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testProcessEndTagWithNullName() {
        TreeBuilder treeBuilder = new TreeBuilder();

        // Exception test: null name
        try {
            treeBuilder.processEndTag(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}