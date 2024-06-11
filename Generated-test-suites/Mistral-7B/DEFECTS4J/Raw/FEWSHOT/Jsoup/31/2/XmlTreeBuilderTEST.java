package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jetbrains.annotations.NotNull;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child1/><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        assertEquals(DocumentType.class, builder.doc.getClass());
        assertEquals("root", builder.doc.nodeName());
        assertEquals(2, builder.stack.size());
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        Token token = new Token(); // create an invalid token
        builder.process(token);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child1/><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        Token startTag1 = new Token.StartTag("child1", null, null);
        builder.process(startTag1);

        assertEquals(2, builder.stack.size());
        Element child1 = (Element) builder.stack.peek();
        assertEquals("child1", child1.nodeName());

        Token startTag2 = new Token.StartTag("child2", null, null);
        builder.process(startTag2);

        Element child2 = (Element) builder.stack.peek();
        assertEquals("child2", child2.nodeName());
        assertEquals("child2", child1.child(0).nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child1/><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        Token startTag1 = new Token.StartTag("child1", null, null);
        builder.process(startTag1);

        Token endTag1 = new Token.EndTag("child1");
        builder.process(endTag1);

        assertEquals(1, builder.stack.size());

        Token startTag2 = new Token.StartTag("child2", null, null);
        builder.process(startTag2);

        Token endTag2 = new Token.EndTag("child2");
        builder.process(endTag2);

        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><!-- A comment --></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        Token commentToken = new Token.Comment("<!-- A comment -->");
        builder.process(commentToken);

        Element comment = (Element) builder.stack.peek();
        assertEquals(1, builder.stack.size());
        assertEquals("-->", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root>&#x26;<child/></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        Token characterToken = new Token.Character("&x26;");
        builder.process(characterToken);

        Element textNode = (Element) builder.stack.peek();
        assertEquals(1, builder.stack.size());
        assertEquals("&amp;", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<!DOCTYPE root [ <!ENTITY e " + "\u0065" + " broad > ]>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        Token doctypeToken = new Token.Doctype(null, null, "root", null, null);
        builder.process(doctypeToken);

        DocumentType doctypeNode = (DocumentType) builder.doc;
        assertEquals(1, builder.stack.size());
        assertEquals("root", doctypeNode.nodeName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child1><child2></child2></child1></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        Token startTag1 = new Token.StartTag("child1", null, null);
        builder.process(startTag1);

        Token startTag2 = new Token.StartTag("child2", null, null);
        builder.process(startTag2);

        Token endTag2 = new Token.EndTag("child2");
        builder.process(endTag2);

        assertEquals(1, builder.stack.size());
        Element child1 = (Element) builder.stack.peek();
        assertEquals("child1", child1.nodeName());

        Token endTag1 = new Token.EndTag("child1");
        builder.process(endTag1);

        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessUnknownEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child1/><unknown/></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        Token startTag1 = new Token.StartTag("child1", null, null);
        builder.process(startTag1);

        Token endTagUnknown = new Token.EndTag("unknown");
        builder.process(endTagUnknown);

        assertEquals(1, builder.stack.size());
        Element child1 = (Element) builder.stack.peek();
        assertEquals("child1", child1.nodeName());
    }

    @Test
    public void testProcessSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><img src=\"image.jpg\" /> <child1 /></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        Token startTagImg = new Token.StartTag("img", null, null, true);
        builder.process(startTagImg);

        assertEquals(2, builder.stack.size());
        Document doc = builder.doc;
        Element img = (Element) doc.getElementsByTag("img").get(0);
        assertEquals("img", img.nodeName());
        assertEquals("image.jpg", img.attr("src"));

        Token endTagChild1 = new Token.EndTag("child1");
        builder.process(endTagChild1);

        assertEquals(1, builder.stack.size());
        assertEquals("root", ((Element) builder.stack.peek()).nodeName());
    }
}