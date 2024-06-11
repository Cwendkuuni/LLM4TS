package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.helper.Validate;

import org.junit.Test;

public class XmlTreeBuilderTest {
    private Tokenizer tokenizer;
    private XmlTreeBuilder builder;

    @Test
    public void testXmlTreeBuilder() {
        tokenizer = new Tokenizer();
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", null, null);
        assertEquals(Document.class, builder.getDocument().getClass());
    }

    @Test
    public void testProcessStartTag() {
        tokenizer = new Tokenizer();
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", null, null);
        Token startTag = new Token.StartTag("root");

        builder.process(startTag);

        Element root = builder.getDocument().child(0);
        assertEquals("root", root.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        tokenizer = new Tokenizer();
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", null, null);
        Token startTag = new Token.StartTag("root");
        Token endTag = new Token.EndTag("root");

        builder.process(startTag);
        builder.process(endTag);

        Element root = builder.getDocument().child(0);
        assertNull(root.child(0));
    }

    @Test
    public void testProcessComment() {
        tokenizer = new Tokenizer();
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", null, null);
        Token comment = new Token.Comment("this is a comment");

        builder.process(comment);

        Comment commentNode = builder.getDocument().child(0).child(0);
        assertEquals("this is a comment", commentNode.text());
    }

    @Test
    public void testProcessCharacter() {
        tokenizer = new Tokenizer();
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", null, null);
        Token character = new Token.Character('a');

        builder.process(character);

        TextNode textNode = (TextNode) builder.getDocument().child(0).child(0);
        assertEquals("a", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        tokenizer = new Tokenizer();
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", null, null);
        Token doctype = new Token.Doctype("html", null, null, null);

        builder.process(doctype);

        DocumentType doctypeNode = (DocumentType) builder.getDocument().child(0);
        assertEquals("html", doctypeNode.nodeName());
    }

    @Test
    public void testProcessEndOfFile() {
        tokenizer = new Tokenizer();
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", null, null);
        Token eof = new Token.EOF();

        builder.process(eof);

        assertEquals(0, builder.getDocument().childCount());
    }

    @Test
    public void testInsertNode() {
        tokenizer = new Tokenizer();
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", null, null);
        Element root = new Element("root");
        Token startTag = new Token.StartTag("root");

        builder.process(startTag);
        builder.insertNode(root);

        Element parsedRoot = builder.getDocument().child(0);
        assertEquals("root", parsedRoot.nodeName());
        assertEquals(root, parsedRoot);
    }

    @Test
    public void testPopStackToClose() {
        tokenizer = new Tokenizer();
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", null, null);
        Element root = new Element("root");
        Element child = new Element("child");
        Element grandChild = new Element("grandChild");
        Stack stack = builder.stack;

        builder.process(new Token.StartTag("root"));
        builder.process(new Token.StartTag("child"));
        builder.process(new Token.StartTag("grandChild"));
        builder.process(new Token.EndTag("grandChild"));
        builder.process(new Token.EndTag("child"));

        builder.popStackToClose(new Token.EndTag("child"));

        assertEquals(2, stack.size());
        assertEquals(root, stack.peek());
        assertEquals(child, stack.get(stack.size() - 2));
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        tokenizer = new Tokenizer();
        builder = new XmlTreeBuilder();
        builder.initialiseParse("", null, null);
        Token unexpectedToken = new Token(-1, "unknown");

        builder.process(unexpectedToken);
    }
}