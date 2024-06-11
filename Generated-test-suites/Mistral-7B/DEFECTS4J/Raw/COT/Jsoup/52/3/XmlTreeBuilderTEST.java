package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTreeBuilder;

import org.junit.Test;

/**
 * XmlTreeBuilderTest
 */
public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<root><child/></root>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
        assertNotNull(xmlTreeBuilder.doc);
    }

    @Test
    public void testProcess_StartTag() {
        String input = "<root><child/></root>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token token = new Token(Token.TokenType.StartTag, "<child>", null, null);
        xmlTreeBuilder.process(token);

        Element root = xmlTreeBuilder.doc.getRootElement();
        Element child = root.getChild(0);
        assertEquals("child", child.nodeName());
    }

    @Test
    public void testProcess_EndTag() {
        String input = "<root><child/></root>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token token = new Token(Token.TokenType.EndTag, "</child>", null, null);
        xmlTreeBuilder.process(token);

        Element root = xmlTreeBuilder.doc.getRootElement();
        Element child = root.getChild(0);
        assertNull(child.getChild(0));
    }

    @Test
    public void testProcess_Comment() {
        String input = "<root><!-- comment --></root>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token token = new Token(Token.TokenType.Comment, "-->comment<--", null, null);
        xmlTreeBuilder.process(token);

        Element root = xmlTreeBuilder.doc.getRootElement();
        Comment comment = (Comment) root.child(0);
        assertEquals("comment", comment.text());
    }

    @Test
    public void testProcess_Character() {
        String input = "<root>&#123;</root>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token token = new Token(Token.TokenType.Character, "&#123;", null, null);
        xmlTreeBuilder.process(token);

        Element root = xmlTreeBuilder.doc.getRootElement();
        TextNode textNode = (TextNode) root.child(0);
        assertEquals("&#123;", textNode.text());
    }

    @Test
    public void testProcess_Doctype() {
        String input = "<!DOCTYPE root [ <!ELEMENT child EMPTY> ]>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token token = new Token(Token.TokenType.Doctype, null, "root", null);
        xmlTreeBuilder.process(token);

        DocumentType doctypeNode = (DocumentType) xmlTreeTreeBuilder.doc.getDoctype();
        assertEquals("root", doctypeNode.nodeName());
    }

    @Test(expected = AssertionError.class)
    public void testProcess_UnexpectedTokenType() {
        String input = "<root>"; // No corresponding end tag
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Token token = new Token(123, null, null, null); // Invalid token type
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertNode() {
        String baseUri = "http://example.com/";
        Element root = new Element("root", baseUri);
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insertNode(root);
        xmlTreeBuilder.doc.appendChild(root);
        assertEquals(root, xmlTreeBuilder.doc.getRootElement());
    }

    @Test
    public void testInsert_Comment() {
        String baseUri = "http://example.com/";
        String commentText = "some comment";
        Comment comment = new Comment(commentText, baseUri);
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(comment);

        Element root = xmlTreeBuilder.doc.getRootElement();
        Node node = root.child(0);
        assertEquals(comment, node);
    }

    @Test
    public void testInsert_TextNode() {
        String baseUri = "http://example.com/";
        String text = "some text";
        TextNode textNode = new TextNode(text, baseUri);
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(textNode);

        Element root = xmlTreeBuilder.doc.getRootElement();
        Node node = root.child(0);
        assertEquals(textNode, node);
    }

    @Test
    public void testInsert_DocumentType() {
        String baseUri = "http://example.com/";
        String doctypeName = "doctype";
        DocumentType documentType = new DocumentType(doctypeName, null, null, baseUri);
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(documentType);

        Element root = xmlTreeBuilder.doc.getRootElement();
        Node node = root.child(0);
        assertEquals(documentType, node);
    }

    @Test
    public void testPopStackToClose_EmptyStack() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertThrows(RuntimeException.class, () -> xmlTreeBuilder.popStackToClose(new Token.EndTag("root")));
    }

    @Test
    public void testPopStackToClose_NoMatchingElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root", null);
        xmlTreeBuilder.stack.add(root);

        assertThrows(RuntimeException.class, () -> xmlTreeBuilder.popStackToClose(new Token.EndTag("child")));
    }

    @Test
    public void testPopStackToClose_MatchingElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root", null);
        Element child = new Element("child", null);
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(child);

        xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
        assertEquals(root, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
    }

    @Test
    public void testParseFragment() {
        String input = "<root><child/></root>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors);

        assertEquals(Arrays.asList(new Element("root", baseUri), new Element("child", baseUri), new TextNode("", baseUri)), nodes);
    }

    @Test(expected = IOException.class)
    public void testParseFragment_IOException() {
        String input = "Invalid input"; // Not a valid XML input
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(input, baseUri, errors);
    }

    @Test
    public void testParseFragment_MalformedXml() {
        String input = "<root><child><child/><child/></child></root>"; // Malformed XML
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors);

        assertEquals(4, errors.size());
        assertEquals(Arrays.asList(new Element("root", baseUri), new Element("child", baseUri), new Element("child", baseUri), new TextNode("", baseUri)), nodes);
    }
}