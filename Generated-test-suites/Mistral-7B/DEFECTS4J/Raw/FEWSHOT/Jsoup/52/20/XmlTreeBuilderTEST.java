package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.StartTag, "<tag name=\"test\" attr1=\"value1\" attr2=\"value2\"/>");
        xmlTreeBuilder.process(token);

        Element element = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("tag", element.nodeName());
        assertEquals("value1", element.attribute(0).getValue());
        assertEquals("value2", element.attribute(1).getValue());
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.EndTag, "</tag>");
        xmlTreeBuilder.process(token);

        Element parentElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 2);
        Element childElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);

        assertEquals("tag", childElement.nodeName());
        assertEquals("tag", parentElement.nodeName());
        assertTrue(parentElement.hasChild(childElement));

        xmlTreeBuilder.stack.remove(xmlTreeBuilder.stack.size() - 1);
        assertNull(parentElement.getChild(0));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.Comment, "<!-- this is a test comment -->");
        xmlTreeBuilder.process(token);

        Comment comment = (Comment) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("this is a test comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.Character, "test");
        xmlTreeBuilder.process(token);

        TextNode textNode = (TextNode) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("test", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.Doctype, "<!DOCTYPE html SYSTEM \"about:legacy-compat\" [><!ATTLIST html foo CDATA #IMPLIED>]>");
        xmlTreeBuilder.process(token);

        DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("html", documentType.nodeName());
        assertEquals("about:legacy-compat", documentType.getPublicIdentifier());
        assertNull(documentType.getSystemIdentifier());
        assertEquals("", documentType.getInternalSubset());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.stack.add(childElement);

        xmlTreeBuilder.popStackToClose(new Token(Token.TokenType.EndTag, "</child>"));

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("parent", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<parent><child attr1=\"value1\" attr2=\"value2\"/><grandchild/></parent>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());

        assertEquals(3, nodes.size());
        Element parentElement = (Element) nodes.get(0);
        Element childElement = (Element) nodes.get(1);
        Element grandchildElement = (Element) nodes.get(2);

        assertEquals("parent", parentElement.nodeName());
        assertEquals("child", childElement.nodeName());
        assertEquals("grandchild", grandchildElement.nodeName());
        assertTrue(parentElement.hasChild(childElement));
        assertTrue(childElement.hasChild(grandchildElement));
    }
}