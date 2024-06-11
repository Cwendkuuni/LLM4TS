package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.Iterator;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("", "", null);
        assertNotNull(builder.doc);
        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = builder.tokeniser.nextToken();
        Token.StartTag startTag = (Token.StartTag) node;

        Element element = builder.insert(startTag);
        assertEquals("element", element.getNodeName());
        assertEquals("", element.getBaseURI());
        assertEquals(startTag.attributes.size(), element.getAttributes().getLength());

        for (Object key : startTag.attributes.keySet()) {
            assertEquals(key.toString(), element.getAttribute(key.toString()));
        }

        assertEquals(element, builder.currentElement().getLastChild());
        assertFalse(builder.stack.isEmpty());
    }

    @Test
    public void testProcessSelfClosingStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = builder.tokeniser.nextToken();
        Token.StartTag startTag = (Token.StartTag) node;
        startTag.setSelfClosing(true);

        Element element = builder.insert(startTag);
        assertEquals("element", element.getNodeName());
        assertEquals("", element.getBaseURI());
        assertEquals(startTag.attributes.size(), element.getAttributes().getLength());

        for (Object key : startTag.attributes.keySet()) {
            assertEquals(key.toString(), element.getAttribute(key.toString()));
        }

        assertFalse(element.hasChildNodes());
        assertFalse(builder.stack.isEmpty());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node startTagNode = builder.tokeniser.nextToken();
        Node endTagNode = builder.tokeniser.nextToken();

        Token.StartTag startTag = (Token.StartTag) startTagNode;
        Token.EndTag endTag = (Token.EndTag) endTagNode;

        Element startElement = builder.insert(startTag);
        builder.insert(endTag);

        assertTrue(builder.stack.isEmpty());
        assertNull(builder.currentElement());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = builder.tokeniser.nextToken();
        Token.Comment commentToken = (Token.Comment) node;

        builder.insert(commentToken);

        Comment comment = (Comment) builder.currentElement().getLastChild();
        assertEquals(commentToken.getData(), comment.getData());

        assertFalse(builder.stack.isEmpty());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = builder.tokeniser.nextToken();
        Token.Character characterToken = (Token.Character) node;

        builder.insert(characterToken);

        Text textNode = (Text) builder.currentElement().getLastChild();
        assertEquals(characterToken.getData(), textNode.getData());

        assertFalse(builder.stack.isEmpty());
    }

    @Test
    public void testProcessDocumentType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = builder.tokeniser.nextToken();
        Token.Doctype doctypeToken = (Token.Doctype) node;

        builder.insert(doctypeToken);

        Document document = builder.doc;
        DocumentType doctype = (DocumentType) document.getDoctype();

        assertEquals(doctypeToken.getName(), doctype.getNodeName());
        assertEquals(doctypeToken.getPublicIdentifier(), doctype.getPublicId());
        assertEquals(doctypeToken.getSystemIdentifier(), doctype.getSystemId());
        assertEquals("", doctype.getBaseURI());

        assertTrue(builder.stack.isEmpty());
        assertNull(builder.currentElement());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Element element1 = new Element("element1", "", null);
        Element element2 = new Element("element2", "", null);
        Element element3 = new Element("element3", "", null);

        builder.stack.add(element3);
        builder.stack.add(element2);
        builder.stack.add(element1);

        builder.popStackToClose(new Token.EndTag("element1"));
        assertEquals(2, builder.stack.size());
        assertEquals(element2, builder.stack.peek());

        builder.popStackToClose(new Token.EndTag("element2"));
        assertTrue(builder.stack.isEmpty());
    }
}