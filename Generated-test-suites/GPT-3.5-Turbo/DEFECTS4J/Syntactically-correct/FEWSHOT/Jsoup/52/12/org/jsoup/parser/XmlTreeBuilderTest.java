package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    // Test initialiseParse method

    @Test
    public void testInitialiseParse() {
        String input = "<tag></tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(input, xmlTreeBuilder.getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());

        // Test that the doc element is added to the stack
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(Document.class, xmlTreeBuilder.getStack().get(0).getClass());

        // Test that the output settings are set to XML syntax
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDocument().outputSettings().syntax());
    }

    // Test process method

    @Test
    public void testProcess_startTag() {
        Token token = new Token.StartTag("tag");

        assertTrue(xmlTreeBuilder.process(token));

        // Test that the start tag is correctly added to the stack and inserted into the current element
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals(Element.class, xmlTreeBuilder.getStack().get(1).getClass());
        assertEquals(Element.class, xmlTreeBuilder.getCurrentElement().getClass());
        assertEquals("tag", xmlTreeBuilder.getStack().get(1).nodeName());
    }

    @Test
    public void testProcess_endTag() {
        Token token = new Token.EndTag("tag");

        assertTrue(xmlTreeBuilder.process(token));

        // Test that the end tag correctly removes elements from the stack
        assertEquals(1, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testProcess_comment() {
        Token token = new Token.Comment("comment");

        assertTrue(xmlTreeBuilder.process(token));

        // Test that the comment is correctly inserted into the current element
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(Comment.class, childNodes.get(0).getClass());
        assertEquals("comment", ((Comment) childNodes.get(0)).getData());
    }

    @Test
    public void testProcess_character() {
        Token token = new Token.Character("character");

        assertTrue(xmlTreeBuilder.process(token));

        // Test that the character is correctly inserted into the current element
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(TextNode.class, childNodes.get(0).getClass());
        assertEquals("character", ((TextNode) childNodes.get(0)).getWholeText());
    }

    @Test
    public void testProcess_doctype() {
        Token token = new Token.Doctype("doctype", "public", "system");

        assertTrue(xmlTreeBuilder.process(token));

        // Test that the doctype is correctly inserted into the current element
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(DocumentType.class, childNodes.get(0).getClass());
        assertEquals("doctype", ((DocumentType) childNodes.get(0)).getName());
        assertEquals("public", ((DocumentType) childNodes.get(0)).getPublicIdentifier());
        assertEquals("system", ((DocumentType) childNodes.get(0)).getSystemIdentifier());
    }

    // Test insertNode method

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");

        xmlTreeBuilder.insertNode(element);

        // Test that the element is correctly appended to the current element
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodes().size());
        assertEquals(Element.class, xmlTreeBuilder.getCurrentElement().childNodes().get(0).getClass());
        assertEquals(element, xmlTreeBuilder.getCurrentElement().childNodes().get(0));
    }

    // Test insert method

    @Test
    public void testInsert_startTag() {
        Token.StartTag startTag = new Token.StartTag("tag");

        Element element = xmlTreeBuilder.insert(startTag);

        // Test that the start tag is correctly inserted and added to the stack
        assertEquals(element, xmlTreeBuilder.getCurrentElement().childNodes().get(0));
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals(element, xmlTreeBuilder.getStack().get(1));
    }

    @Test
    public void testInsert_comment() {
        Token.Comment commentToken = new Token.Comment("comment");

        xmlTreeBuilder.insert(commentToken);

        // Test that the comment is correctly inserted into the current element
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(Comment.class, childNodes.get(0).getClass());
        assertEquals("comment", ((Comment) childNodes.get(0)).getData());
    }

    @Test
    public void testInsert_character() {
        Token.Character characterToken = new Token.Character("character");

        xmlTreeBuilder.insert(characterToken);

        // Test that the character is correctly inserted into the current element
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(TextNode.class, childNodes.get(0).getClass());
        assertEquals("character", ((TextNode) childNodes.get(0)).getWholeText());
    }

    @Test
    public void testInsert_doctype() {
        Token.Doctype doctype = new Token.Doctype("doctype", "public", "system");

        xmlTreeBuilder.insert(doctype);

        // Test that the doctype is correctly inserted into the current element
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(DocumentType.class, childNodes.get(0).getClass());
        assertEquals("doctype", ((DocumentType) childNodes.get(0)).getName());
        assertEquals("public", ((DocumentType) childNodes.get(0)).getPublicIdentifier());
        assertEquals("system", ((DocumentType) childNodes.get(0)).getSystemIdentifier());
    }

    // Test popStackToClose method

    @Test
    public void testPopStackToClose_notFound() {
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.popStackToClose(endTag);

        // Test that the stack is not modified if the end tag is not found
        assertEquals(1, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testPopStackToClose_found() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        xmlTreeBuilder.getStack().add(element);
        xmlTreeBuilder.getStack().add(new Element(Tag.valueOf("other"), "http://example.com"));

        xmlTreeBuilder.popStackToClose(endTag);

        // Test that the first occurrence of the end tag is correctly removed from the stack
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(element, xmlTreeBuilder.getStack().get(0));
    }

    // Test parseFragment method

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        // Test that the input fragment is correctly parsed and returned as a list of child nodes of the document
        assertEquals(1, childNodes.size());
        assertEquals(Element.class, childNodes.get(0).getClass());
        assertEquals("tag", ((Element) childNodes.get(0)).nodeName());
    }

    // Additional tests

    @Test
    public void testGettersAndSetters() {
        // Test getters and setters for private fields
        xmlTreeBuilder.setErrors(null);
        assertNull(xmlTreeBuilder.getErrors());
        xmlTreeBuilder.setInput("");
        assertEquals("", xmlTreeBuilder.getInput());
        xmlTreeBuilder.setBaseUri("http://example.com");
        assertEquals("http://example.com", xmlTreeBuilder.getBaseUri());
        xmlTreeBuilder.setStack(null);
        assertNull(xmlTreeBuilder.getStack());
        xmlTreeBuilder.setDocument(null);
        assertNull(xmlTreeBuilder.getDocument());
    }
}