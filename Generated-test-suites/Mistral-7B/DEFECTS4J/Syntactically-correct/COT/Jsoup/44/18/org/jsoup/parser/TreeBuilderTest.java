package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the TreeBuilder abstract class
 */
public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private Document doc;
    private Element rootElement;
    private Tokeniser tokeniser;
    private Token currentToken;
    private ParseErrorList errors;
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        treeBuilder = mock(TreeBuilder.class);
        doc = mock(Document.class);
        rootElement = mock(Element.class);
        tokeniser = mock(Tokeniser.class);
        currentToken = mock(Token.class);
        errors = mock(ParseErrorList.class);
        characterReader = mock(CharacterReader.class);

        when(treeBuilder.doc).thenReturn(doc);
        when(treeBuilder.stack.size()).thenReturn(0);
        when(treeBuilder.errors).thenReturn(errors);
        when(treeBuilder.tokeniser).thenReturn(tokeniser);
        when(treeBuilder.reader).thenReturn(characterReader);
    }

    @Test
    public void testInitialiseParse() {
        treeBuilder.initialiseParse("input", "baseUri", errors);

        Validate.notNull(treeBuilder.doc, "Document must not be null after initialiseParse");
        Validate.notNull(treeBuilder.reader, "CharacterReader must not be null after initialiseParse");
        Validate.notNull(treeBuilder.tokeniser, "Tokeniser must not be null after initialiseParse");
        Validate.notNull(treeBuilder.stack, "Stack must not be null after initialiseParse");
        Validate.notNull(treeBuilder.baseUri, "BaseUri must not be null after initialiseParse");
        Validate.notNull(treeBuilder.currentToken, "Current token must not be null after initialiseParse");
        Validate.notNull(treeBuilder.errors, "Error list must not be null after initialiseParse");
    }

    @Test
    public void testParseWithNullInput() {
        treeBuilder.parse(null, "baseUri");

        Validate.assertNull(treeBuilder.doc, "Document must be null when input is null");
    }

    @Test
    public void testParseWithNullBaseUri() {
        treeBuilder.parse("input", null);

        Validate.assertNull(treeBuilder.doc, "Document must be null when baseUri is null");
    }

    @Test
    public void testParseWithNullErrors() {
        treeBuilder.parse("input", "baseUri", null);

        Validate.assertNotNull(treeBuilder.errors, "Error list must not be null after parse");
    }

    @Test
    public void testRunParser_ProcessStartTag_RootElement() {
        when(tokeniser.read()).thenReturn(createStartTagToken("html"));

        treeBuilder.runParser();

        Element root = treeBuilder.currentElement();
        assertSame(rootElement, root, "Root element should be equal to the mocked one");
    }

    @Test
    public void testRunParser_ProcessEndTag_RootElementRemoved() {
        when(tokeniser.read()).thenReturn(createStartTagToken("html"));
        when(tokeniser.read()).thenReturn(createEndTagToken("html"));

        treeBuilder.runParser();

        Element root = treeBuilder.currentElement();
        assertNull(root, "Root element should be null after end tag is processed");
    }

    @Test
    public void testRunParser_ProcessOtherToken_Ignore() {
        when(tokeniser.read()).thenReturn(createTextToken("text"));

        treeBuilder.runParser();

        Element root = treeBuilder.currentElement();
        assertSame(rootElement, root, "Root element should be unchanged after text token is processed");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTag_NullName() {
        treeBuilder.processStartTag(null, new Attributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTag_EmptyName() {
        treeBuilder.processStartTag("", new Attributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessStartTag_NullAttributes() {
        treeBuilder.processStartTag("name", null);
    }

    @Test
    public void testProcessStartTag_ElementAddedToStack() {
        Element element = mock(Element.class);
        when(tokeniser.read()).thenReturn(createStartTagToken("tagName"));

        treeBuilder.process(start);

        ArrayList<Element> stack = treeBuilder.stack;
        assertFalse(stack.isEmpty(), "Stack should not be empty after processing a start tag");
        assertSame(element, stack.get(stack.size()-1), "Added element should be the last one in the stack");
    }

    @Test
    public void testProcessEndTag_ElementRemovedFromStack() {
        Element element = mock(Element.class);
        when(tokeniser.read()).thenReturn(createEndTagToken("tagName"));

        treeBuilder.stack.add(element);
        treeBuilder.process(end);

        ArrayList<Element> stack = treeBuilder.stack;
        assertFalse(stack.contains(element), "Removed element should no longer be in the stack");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessEndTag_NullName() {
        treeBuilder.process(end.reset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessEndTag_ElementNotFoundInStack() {
        treeBuilder.process(end.reset());

        ArrayList<Element> stack = treeBuilder.stack;
        assertFalse(stack.isEmpty(), "Stack should not be empty after processing an end tag when no element is found");
    }

    @Test
    public void testProcessText_TextAddedToElement() {
        Element element = mock(Element.class);
        when(tokeniser.read()).thenReturn(createTextToken("text"));

        treeBuilder.stack.add(element);
        treeBuilder.process(currentToken);

        Attributes attributes = element.attributes();
        String text = attributes.get("text");

        assertSame("text", text, "Text should be equal to the mocked one");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcess_NullToken() {
        treeBuilder.process(null);
    }

    private Token createStartTagToken(String name) {
        StartTag startTag = new StartTag();
        startTag.name(name);
        return startTag;
    }

    private Token createEndTagToken(String name) {
        EndTag endTag = new EndTag();
        endTag.name(name);
        return endTag;
    }

    private Token createTextToken(String text) {
        Token textToken = mock(Token.class);
        when(textToken.type).thenReturn(Token.TokenType.TEXT);
        when(textToken.text()).thenReturn(text);
        return textToken;
    }
}