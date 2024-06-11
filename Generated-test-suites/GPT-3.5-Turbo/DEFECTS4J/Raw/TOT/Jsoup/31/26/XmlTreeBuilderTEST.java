package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse("input", "baseUri", errors);

        assertEquals("baseUri", xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attr1", "value1");

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("tag", element.tagName());
        assertEquals("baseUri", element.baseUri());
        assertEquals("value1", element.attr("attr1"));
        assertEquals(doc, element.ownerDocument());
        assertEquals(1, element.siblingIndex());
        assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testProcessStartTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attr1", "value1");
        startTag.selfClosing = true;

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("tag", element.tagName());
        assertEquals("baseUri", element.baseUri());
        assertEquals("value1", element.attr("attr1"));
        assertEquals(doc, element.ownerDocument());
        assertEquals(1, element.siblingIndex());
        assertFalse(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
        assertFalse(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testProcessStartTagUnknownTag() {
        Token.StartTag startTag = new Token.StartTag("unknownTag");
        startTag.attributes.put("attr1", "value1");

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("unknownTag", element.tagName());
        assertEquals("baseUri", element.baseUri());
        assertEquals("value1", element.attr("attr1"));
        assertEquals(doc, element.ownerDocument());
        assertEquals(1, element.siblingIndex());
        assertFalse(element.tag().isKnownTag());
        assertFalse(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testProcessEndTag() {
        Element element1 = new Element("tag1");
        Element element2 = new Element("tag2");
        Element element3 = new Element("tag1");

        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag("tag1");

        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.contains(element2));
        assertTrue(xmlTreeBuilder.stack.contains(element3));
        assertFalse(xmlTreeBuilder.stack.contains(element1));
    }

    @Test
    public void testProcessUnknownToken() {
        Token unknownToken = new Token(Token.TokenType.Unknown, "unknown");

        try {
            xmlTreeBuilder.process(unknownToken);
        } catch (Exception e) {
            assertTrue(e instanceof Validate.AssertionError);
            assertEquals("Unexpected token type: Unknown", e.getMessage());
        }
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof Comment);
        Comment comment = (Comment) doc.childNode(0);
        assertEquals("comment", comment.getData());
        assertEquals("baseUri", comment.baseUri());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("character");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) doc.childNode(0);
        assertEquals("character", textNode.text());
        assertEquals("baseUri", textNode.baseUri());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("name", "publicId", "systemId");

        xmlTreeBuilder.insert(doctype);

        assertEquals(1, doc.childNodeSize());
        assertTrue(doc.childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) doc.childNode(0);
        assertEquals("name", documentType.nodeName());
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals("baseUri", documentType.baseUri());
    }

}