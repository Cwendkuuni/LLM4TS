package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse("", "", errors);

        assertEquals(0, errors.size());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTagToken() {
        Token token = new Token.StartTag("tag");

        boolean result = xmlTreeBuilder.process(token);

        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("tag", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testProcess_EndTagToken() {
        Token token = new Token.EndTag("tag");
        xmlTreeBuilder.stack.push(new Element("tag"));

        boolean result = xmlTreeBuilder.process(token);

        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_CommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");

        xmlTreeBuilder.process(commentToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
        assertEquals("comment", ((Comment) xmlTreeBuilder.currentElement().childNodes().get(0)).getData());
    }

    @Test
    public void testProcess_CharacterToken() {
        Token.Character characterToken = new Token.Character("data");

        xmlTreeBuilder.process(characterToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("data", ((TextNode) xmlTreeBuilder.currentElement().childNodes().get(0)).getWholeText());
    }

    @Test
    public void testProcess_DoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("name");

        xmlTreeBuilder.process(doctypeToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        assertEquals("name", ((DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0)).getName());
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");

        Element result = xmlTreeBuilder.insert(startTag);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Element);
        assertEquals("tag", ((Element) xmlTreeBuilder.currentElement().childNodes().get(0)).nodeName());
        assertEquals("tag", result.nodeName());
        assertFalse(result.isKnownTag());
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag startTag = new Token.StartTag("tag", true);

        Element result = xmlTreeBuilder.insert(startTag);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Element);
        assertEquals("tag", ((Element) xmlTreeBuilder.currentElement().childNodes().get(0)).nodeName());
        assertEquals("tag", result.nodeName());
        assertTrue(result.isKnownTag());
        assertTrue(result.tag().isSelfClosing());
    }

    @Test
    public void testInsert_CommentToken_Bogus() {
        Token.Comment commentToken = new Token.Comment("<!comment>");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof XmlDeclaration);
        assertEquals("<!comment>", ((XmlDeclaration) xmlTreeBuilder.currentElement().childNodes().get(0)).wholeDeclaration());
    }

    @Test
    public void testInsert_CharacterToken() {
        Token.Character characterToken = new Token.Character("data");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("data", ((TextNode) xmlTreeBuilder.currentElement().childNodes().get(0)).getWholeText());
    }

    @Test
    public void testInsert_DoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("name");

        xmlTreeBuilder.insert(doctypeToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        assertEquals("name", ((DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0)).getName());
    }

    @Test
    public void testPopStackToClose_NotFound() {
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToClose_Found() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element("tag");
        xmlTreeBuilder.stack.push(new Element("otherTag"));
        xmlTreeBuilder.stack.push(element);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("otherTag", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        ParseErrorList errors = new ParseErrorList();

        List<Node> result = xmlTreeBuilder.parseFragment("<tag></tag>", "", errors);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Element);
        assertEquals("tag", ((Element) result.get(0)).nodeName());
    }
}