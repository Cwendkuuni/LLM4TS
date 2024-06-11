package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.Stack;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;
    private Tokenizer tokenizer;
    private Stack<Element> stack;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
        tokenizer = new Tokenizer();
        stack = new Stack<>();
        builder.setTokenizer(tokenizer);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml><foo></foo></xml>";
        builder.initialiseParse(input, null, null);
        assertEquals(1, stack.size());
        assertEquals("xml", stack.peek().nodeName());
    }

    @Test
    public void testProcessStartTag() {
        String input = "<foo><bar></bar></foo>";
        tokenizer.reset(input);
        builder.initialiseParse(input, null, null);

        Token token;
        Element foo = null;
        Element bar = null;

        while ((token = tokenizer.next()) != null) {
            builder.process(token);
            if (token.type == Token.StartTag) {
                if (foo == null) {
                    foo = builder.insert(token.asStartTag());
                }
                if (token.name().equals("bar")) {
                    bar = builder.insert(token.asStartTag());
                    assertEquals(foo, bar.parentNode());
                }
            }
        }

        assertEquals(1, stack.size());
        assertEquals("xml", stack.peek().nodeName());
    }

    @Test
    public void testProcessEndTag() {
        String input = "<foo><bar></bar></foo>";
        tokenizer.reset(input);
        builder.initialiseParse(input, null, null);

        Token token;

        while ((token = tokenizer.next()) != null) {
            builder.process(token);
        }

        tokenizer.reset(input);

        token = tokenizer.next();
        assertEquals(Token.EndTag, token.type);
        assertEquals("bar", token.asEndTag().name());
        builder.popStackToClose(token.asEndTag());
        assertEquals(0, stack.size());
    }

    @Test
    public void testProcessComment() {
        String input = "<!-- comment -->";
        tokenizer.reset(input);
        builder.initialiseParse(input, null, null);

        Token token = tokenizer.next();
        assertEquals(Token.Comment, token.type);
        builder.process(token);

        assertEquals(0, stack.size());
    }

    @Test
    public void testProcessCharacter() {
        String input = "<foo>&#xA;<bar>";
        tokenizer.reset(input);
        builder.initialiseParse(input, null, null);

        Token token = tokenizer.next();
        assertEquals(Token.Character, token.type);
        builder.process(token);

        assertEquals(0, stack.size());
    }

    @Test
    public void testProcessDoctype() {
        String input = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"http://www.w3.org/TR/xml11/DTD/xml10.dtd\">";
        tokenizer.reset(input);
        builder.initialiseParse(input, null, null);

        Token token = tokenizer.next();
        assertEquals(Token.Doctype, token.type);
        builder.process(token);

        assertEquals(0, stack.size());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTokenType() {
        String input = "<xml>";
        tokenizer.reset(input);
        builder.initialiseParse(input, null, null);

        Token token = tokenizer.next();
        builder.process(new Token(-1, null, null, null));
    }
}