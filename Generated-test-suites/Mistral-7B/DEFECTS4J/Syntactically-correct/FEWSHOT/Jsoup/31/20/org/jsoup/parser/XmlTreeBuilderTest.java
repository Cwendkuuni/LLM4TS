package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("test", "baseUri");
        xmlTreeBuilder.insertNode(element);
        assertNotNull(xmlTreeBuilder.currentElement().getChildNodes().get(0));
    }

    @Test
    public void testInsertSTARTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("test", null, null, null, null, false);
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("test", element.nodeName());
        assertNotNull(xmlTreeBuilder.currentElement().getChildNodes().get(0));
    }

    @Test
    public void testInsertEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element rootElement = new Element("root", "baseUri");
        xmlTreeBuilder.stack.add(rootElement);
        Token.EndTag endTag = new Token.EndTag("test", null);
        xmlTreeBuilder.popStackToClose(endTag);
        assertNull(xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        assertNotNull(xmlTreeBuilder.currentElement().getChildNodes().get(0));
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character('A');
        xmlTreeBuilder.insert(characterToken);
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().getChildNodes().get(0);
        assertEquals("A", textNode.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", null, null, "baseUri");
        xmlTreeBuilder.insert(doctypeToken);
        DocumentType documentTypeNode = (DocumentType) xmlTreeTreeBuilder.currentElement().getChildNodes().get(0);
        assertEquals("html", documentTypeNode.nodeName());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token("unknown", null);
        xmlTreeBuilder.process(token);
    }
}