package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;
    private LinkedList<Element> stack;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
        stack = new LinkedList<>();
        stack.add(doc);
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse("", "", errors);

        assertEquals(doc, xmlTreeBuilder.currentElement());
        assertEquals(stack, xmlTreeBuilder.getStack());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());

        Element el = xmlTreeBuilder.insert(startTag);

        assertEquals("tag", el.nodeName());
        assertEquals(doc, el.ownerDocument());
        assertEquals(stack.getLast(), el);
    }

    @Test
    public void testProcessSelfClosingStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes(), true);

        Element el = xmlTreeBuilder.insert(startTag);

        assertEquals("tag", el.nodeName());
        assertEquals(doc, el.ownerDocument());
        assertEquals(stack.getLast(), el);
        assertTrue(el.tag().isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(stack.getFirst(), doc);
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");

        xmlTreeBuilder.insert(commentToken);

        Node node = doc.childNode(0);
        assertTrue(node instanceof Comment);
        assertEquals("comment", ((Comment) node).getData());
        assertEquals(baseUri, ((Comment) node).baseUri());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("text");

        xmlTreeBuilder.insert(characterToken);

        Node node = doc.childNode(0);
        assertTrue(node instanceof TextNode);
        assertEquals("text", ((TextNode) node).getWholeText());
        assertEquals(baseUri, ((TextNode) node).baseUri());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("name", "publicId", "systemId");

        xmlTreeBuilder.insert(doctype);

        Node node = doc.childNode(0);
        assertTrue(node instanceof DocumentType);
        assertEquals("name", ((DocumentType) node).getName());
        assertEquals("publicId", ((DocumentType) node).getPublicIdentifier());
        assertEquals("systemId", ((DocumentType) node).getSystemIdentifier());
        assertEquals(baseUri, ((DocumentType) node).baseUri());
    }
}