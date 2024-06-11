package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TreeBuilderTest {

    @Mock
    private CharacterReader reader;

    @Mock
    private Tokeniser tokeniser;

    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        treeBuilder = new TreeBuilderImpl(reader, tokeniser);
    }

    @Test
    public void testInitialiseParse() {
        String input = "";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(input, treeBuilder.reader.getInput());
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();

        Document document = treeBuilder.parse(input, baseUri, errors);

        assertEquals(input, treeBuilder.reader.getInput());
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testRunParser() {
        Token token1 = new Token();
        Token token2 = new Token();
        token2.type = Token.TokenType.EOF;

        tokeniser.read();
        tokeniser.read();

        treeBuilder.process(token1);
        treeBuilder.process(token2);

        assertEquals(token1, tokeniser.read());
        assertEquals(token2, tokeniser.read());
    }

    @Test
    public void testProcessStartTag() {
        String name = "tag";

        boolean result = treeBuilder.processStartTag(name);

        assertEquals(true, result);
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "tag";
        Attributes attrs = new Attributes();

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertEquals(true, result);
    }

    @Test
    public void testProcessEndTag() {
        String name = "tag";

        boolean result = treeBuilder.processEndTag(name);

        assertEquals(true, result);
    }

    @Test
    public void testCurrentElement() {
        Element element = new Element("tag");

        treeBuilder.stack.add(element);

        assertEquals(element, treeBuilder.currentElement());
    }
}