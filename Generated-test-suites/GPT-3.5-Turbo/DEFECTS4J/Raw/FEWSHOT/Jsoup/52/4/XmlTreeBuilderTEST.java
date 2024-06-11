
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @After
    public void tearDown() {
        xmlTreeBuilder = null;
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assert stack contains document node
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.stack.get(0), xmlTreeBuilder.doc);

        // Assert document output settings syntax is xml
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder spyXmlTreeBuilder = mock(XmlTreeBuilder.class);
        Token.StartTag startTag = new Token.StartTag("tag", "name", null, false);

        assertTrue(xmlTreeBuilder.process(startTag));
        verify(spyXmlTreeBuilder).insert(startTag.asStartTag());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder spyXmlTreeBuilder = mock(XmlTreeBuilder.class);
        Token.EndTag endTag = new Token.EndTag("tag", "name");

        assertTrue(xmlTreeBuilder.process(endTag));
        verify(spyXmlTreeBuilder).popStackToClose(endTag.asEndTag());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder spyXmlTreeBuilder = mock(XmlTreeBuilder.class);
        Token.Comment commentToken = new Token.Comment("This is a comment");

        assertTrue(xmlTreeBuilder.process(commentToken));
        verify(spyXmlTreeBuilder).insert(commentToken.asComment());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder spyXmlTreeBuilder = mock(XmlTreeBuilder.class);
        Token.Character characterToken = new Token.Character("This is a character");

        assertTrue(xmlTreeBuilder.process(characterToken));
        verify(spyXmlTreeBuilder).insert(characterToken.asCharacter());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder spyXmlTreeBuilder = mock(XmlTreeBuilder.class);
        Token.Doctype doctypeToken = new Token.Doctype("doctype", "public", "system");

        assertTrue(xmlTreeBuilder.process(doctypeToken));
        verify(spyXmlTreeBuilder).insert(doctypeToken.asDoctype());
    }

    @Test
    public void testProcessUnexpectedToken() {
        Token token = new Token(null);
        try {
            xmlTreeBuilder.process(token);
            fail("Expected Validate.fail() to be called");
        } catch (AssertionError e) {
            assertEquals("Unexpected token type: null", e.getMessage());
        }
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"), "baseUri");
        xmlTreeBuilder.stack.add(element);

        Node node = new TextNode("text", "baseUri");
        xmlTreeBuilder.insertNode(node);

        List<Node> childNodes = xmlTreeBuilder.stack.get(0).childNodes();
        assertEquals(1, childNodes.size());
        assertSame(node, childNodes.get(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "name", null, true);
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals(Tag.valueOf("tag"), element.tag());
        assertEquals("name", element.baseUri());
        assertNull(element.attributes().asList());
        assertSame(element, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testInsertSelfClosingStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "name", null, true);
        Tag tag = xmlTreeBuilder.insert(startTag).tag();
        assertTrue(tag.isKnownTag());
        assertTrue(tag.isSelfClosing());
        assertFalse(tag.hasAttributes());
    }

    @Test
    public void testInsertNonSelfClosingStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "name", null, false);
        Element element = xmlTreeBuilder.insert(startTag);
        assertSame(element, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Comment comment = xmlTreeBuilder.insert(commentToken);

        assertEquals("This is a comment", comment.getData());
        assertEquals("baseUri", comment.baseUri());

        commentToken = new Token.Comment("<!doctype html>");
        comment = xmlTreeBuilder.insert(commentToken);

        assertEquals("<!doctype html>", comment.getData());
        assertEquals("baseUri", comment.baseUri());
    }

    @Test
    public void testInsertBogusComment() {
        Token.Comment commentToken = new Token.Comment("<![" + "CDATA[This is a CDATA section" + "]]>");
        Comment comment = xmlTreeBuilder.insert(commentToken);

        assertEquals("<![" + "CDATA[This is a CDATA section" + "]]>", comment.getData());
        assertEquals("baseUri", comment.baseUri());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        TextNode textNode = xmlTreeBuilder.insert(characterToken);

        assertEquals("This is a character", textNode.getWholeText());
        assertEquals("baseUri", textNode.baseUri());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype", "public", "system");
        DocumentType documentType = xmlTreeBuilder.insert(doctypeToken);

        assertEquals("doctype", documentType.getName());
        assertEquals("public", documentType.getPublicIdentifier());
        assertEquals("system", documentType.getSystemIdentifier());
        assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag", "name");
        Element firstFound = new Element(Tag.valueOf("tag"), "baseUri", null);
        Element next = new Element(Tag.valueOf("tag"), "baseUri", null);
        
        xmlTreeBuilder.stack.add(next);
        xmlTreeBuilder.stack.add(firstFound);
        
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag("tag", "name");
        Element next = new Element(Tag.valueOf("tag"), "baseUri", null);
        
        xmlTreeBuilder.stack.add(next);
        
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(next, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>This is a fragment</tag>";
        String baseUri = "http://example.com";

        ParseErrorList errors = new ParseErrorList();
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        assertEquals(Tag.valueOf("tag"), childNodes.get(0).nodeName());
    }

}
