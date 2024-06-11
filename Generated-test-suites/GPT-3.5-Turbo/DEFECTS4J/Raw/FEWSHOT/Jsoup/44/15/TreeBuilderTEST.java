package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TreeBuilderTest {

    @Mock
    CharacterReader mockReader;

    @Mock
    Tokeniser mockTokeniser;

    TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        treeBuilder = spy(TreeBuilder.class);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(errors, treeBuilder.errors);
        assertEquals(input, treeBuilder.reader.getInput());
        assertEquals(32, treeBuilder.stack.size());
    }

    @Test
    public void testParse() {
        String input = "<html><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        Document result = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(result);
        assertEquals(baseUri, result.baseUri());
    }

    @Test
    public void testRunParser() {
        when(mockTokeniser.read()).thenReturn(new Token.StartTag(), new Token.StartTag(), new Token.EndTag(), new Token.EOF());

        treeBuilder.tokeniser = mockTokeniser;
        treeBuilder.process = mock(TreeBuilder.class).process;

        doReturn(false).when(treeBuilder).process(any(Token.class));

        treeBuilder.runParser();

        verify(mockTokeniser, times(4)).read();
    }

    @Test
    public void testProcessStartTag() {
        doReturn(false).when(treeBuilder).process(any(Token.class));

        treeBuilder.start = mock(Token.StartTag.class);

        boolean result = treeBuilder.processStartTag("div");

        assertFalse(result);
        verify(treeBuilder.start).reset();
        verify(treeBuilder.start).name("div");
        verify(treeBuilder).process(any(Token.class));
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        doReturn(false).when(treeBuilder).process(any(Token.class));

        treeBuilder.start = mock(Token.StartTag.class);
        Attributes attrs = mock(Attributes.class);

        boolean result = treeBuilder.processStartTag("div", attrs);

        assertFalse(result);
        verify(treeBuilder.start).reset();
        verify(treeBuilder.start).nameAttr("div", attrs);
        verify(treeBuilder).process(any(Token.class));
    }

    @Test
    public void testProcessEndTag() {
        doReturn(false).when(treeBuilder).process(any(Token.class));

        treeBuilder.end = mock(Token.EndTag.class);

        boolean result = treeBuilder.processEndTag("div");

        assertFalse(result);
        verify(treeBuilder.end).reset();
        verify(treeBuilder.end).name("div");
        verify(treeBuilder).process(any(Token.class));
    }

    @Test
    public void testCurrentElement() {
        Element elem = mock(Element.class);
        ArrayList<Element> stack = new ArrayList<>();
        stack.add(elem);

        treeBuilder.stack = stack;

        Element result = treeBuilder.currentElement();

        assertEquals(elem, result);
    }

}