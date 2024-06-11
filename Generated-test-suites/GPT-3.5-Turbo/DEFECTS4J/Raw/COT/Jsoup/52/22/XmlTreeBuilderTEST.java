
package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void initialiseParseTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        builder.initialiseParse(input, baseUri, errors);
        // Verify that the document is placed onto the stack
        assertEquals(builder.stack.peek(), builder.doc);
        // Verify that syntax is set to XML in output settings
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void process_StartTagTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        builder.process(startTag);
        // Verify that the start tag is inserted into the stack
        assertEquals(startTag.asStartTag(), builder.stack.peek().tag());
    }

    @Test
    public void process_EndTagTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        builder.stack.push(element);
        builder.process(endTag);
        // Verify that the stack is correctly popped to close the element
        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void process_CommentTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test comment");
        builder.process(commentToken);
        // Verify that the comment is inserted into the document
        assertFalse(builder.doc.childNodes().isEmpty());
        assertTrue(builder.doc.childNode(0) instanceof Comment);
    }

    @Test
    public void process_CharacterTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("text");
        builder.process(characterToken);
        // Verify that the character is inserted into the document
        assertFalse(builder.doc.childNodes().isEmpty());
        assertTrue(builder.doc.childNode(0) instanceof TextNode);
        assertEquals("text", ((TextNode) builder.doc.childNode(0)).getWholeText());
    }

    @Test
    public void process_DoctypeTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        builder.process(doctype);
        // Verify that the doctype is inserted into the document
        assertFalse(builder.doc.childNodes().isEmpty());
        assertTrue(builder.doc.childNode(0) instanceof DocumentType);
    }

    @Test
    public void insertNodeTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        builder.stack.push(element);
        Node node = new Element(Tag.valueOf("p"), "http://example.com");
        builder.insertNode(node);
        // Verify that the node is appended to the current element
        assertFalse(builder.doc.childNodes().isEmpty());
        assertTrue(builder.doc.childNode(0) instanceof Element);
        assertEquals("p", ((Element) builder.doc.childNode(0)).tagName());
    }

    @Test
    public void insert_StartTagTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        builder.insert(startTag);
        // Verify that the start tag element is inserted into the document and added to the stack
        assertFalse(builder.doc.childNodes().isEmpty());
        assertTrue(builder.doc.childNode(0) instanceof Element);
        assertEquals("div", ((Element) builder.doc.childNode(0)).tagName());
        assertEquals(startTag.attributes, ((Element) builder.doc.childNode(0)).attributes());
    }

    @Test
    public void insert_CommentTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test comment");
        builder.insert(commentToken);
        // Verify that the comment is inserted into the document
        assertFalse(builder.doc.childNodes().isEmpty());
        assertTrue(builder.doc.childNode(0) instanceof Comment);
    }

    @Test
    public void insert_CharacterTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("text");
        builder.insert(characterToken);
        // Verify that the character is inserted into the document
        assertFalse(builder.doc.childNodes().isEmpty());
        assertTrue(builder.doc.childNode(0) instanceof TextNode);
        assertEquals("text", ((TextNode) builder.doc.childNode(0)).getWholeText());
    }

    @Test
    public void insert_DoctypeTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        builder.insert(doctype);
        // Verify that the doctype is inserted into the document
        assertFalse(builder.doc.childNodes().isEmpty());
        assertTrue(builder.doc.childNode(0) instanceof DocumentType);
    }

    @Test
    public void popStackToCloseTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("div"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("p"), "http://example.com");
        Element element3 = new Element(Tag.valueOf("div"), "http://example.com");
        builder.stack.push(element1);
        builder.stack.push(element2);
        builder.stack.push(element3);
        Token.EndTag endTag = new Token.EndTag("div");
        builder.popStackToClose(endTag);
        // Verify that the first occurrence of "div" in the stack is removed
        assertFalse(builder.stack.contains(element3));
        assertTrue(builder.stack.contains(element2));
        assertTrue(builder.stack.contains(element1));
    }

    @Test
    public void parseFragmentTest() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<div></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors);
        // Verify that the fragment is correctly parsed and returned as a list of nodes
        assertFalse(nodes.isEmpty());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", ((Element) nodes.get(0)).tagName());
    }
}

