package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

public class TreeBuilderTest {

    @Mock
    private CharacterReader reader;

    @Mock
    private Tokeniser tokeniser;

    @Mock
    private Document doc;

    @Mock
    private Element element;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "dummy input";
        String baseUri = "dummy baseUri";
        ParseErrorList errors = null;

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.errors);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertEquals(baseUri, treeBuilder.baseUri);
    }

    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "dummy input";
        String baseUri = "dummy baseUri";

        Document document = treeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
    }

    @Test
    public void testParseWithErrors() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "dummy input";
        String baseUri = "dummy baseUri";
        ParseErrorList errors = new ParseErrorList();

        Document document = treeBuilder.parse(input, baseUri, errors);
        
        assertNotNull(document);
        assertNotNull(errors);
    }

    @Test
    public void testRunParser() {
        TreeBuilder treeBuilder = new TreeBuilder();
        when(tokeniser.read()).thenReturn(new Token(), new Token(), new Token(), new Token()).thenReturn(new Token(Token.TokenType.EOF));

        treeBuilder.tokeniser = tokeniser;
        treeBuilder.runParser();

        verify(tokeniser, times(5)).read();
    }

    @Test
    public void testProcess() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Token token = new Token();

        boolean result = treeBuilder.process(token);

        assertTrue(result);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String name = "dummy name";

        boolean result = treeBuilder.processStartTag(name);

        assertTrue(result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String name = "dummy name";
        Attributes attrs = new Attributes();

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertTrue(result);
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String name = "dummy name";

        boolean result = treeBuilder.processEndTag(name);

        assertTrue(result);
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.stack = new ArrayList<Element>();
        treeBuilder.stack.add(element);

        Element currentElement = treeBuilder.currentElement();

        assertNotNull(currentElement);
        assertEquals(element, currentElement);
    }
}