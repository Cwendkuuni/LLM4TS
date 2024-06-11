
package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder.Token;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
        assertEquals(1, treeBuilder.stack.size());
        assertEquals(treeBuilder.doc, treeBuilder.stack.get(0));
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("test"), "baseUri");
        Token.StartTag startTag = new Token.StartTag(element.tagName(), element.attributes);
        assertTrue(treeBuilder.process(startTag));
        assertEquals(element, treeBuilder.currentElement().children().get(0));
        assertTrue(treeBuilder.stack.contains(element));
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("test"), "baseUri");
        treeBuilder.stack.add(element);
        Token.EndTag endTag = new Token.EndTag(element.tagName());
        assertTrue(treeBuilder.process(endTag));
        assertFalse(treeBuilder.stack.contains(element));
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Comment comment = new Comment("comment", "baseUri");
        Token.Comment commentToken = new Token.Comment(comment.getData());
        assertTrue(treeBuilder.process(commentToken));
        assertEquals(comment, treeBuilder.currentElement().children().get(0));
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        TextNode textNode = new TextNode("data", "baseUri");
        Token.Character characterToken = new Token.Character(textNode.getWholeText());
        assertTrue(treeBuilder.process(characterToken));
        assertEquals(textNode, treeBuilder.currentElement().children().get(0));
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        DocumentType doctype = new DocumentType("name", "public", "system", "baseUri");
        Token.Doctype doctypeToken = new Token.Doctype(doctype.getName(), doctype.getPublicIdentifier(), doctype.getSystemIdentifier());
        assertTrue(treeBuilder.process(doctypeToken));
        assertEquals(doctype, treeBuilder.currentElement().children().get(0));
    }

    @Test(expected = AssertionError.class)
    public void testProcess_UnexpectedToken() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.Character);
        treeBuilder.process(token);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("test"), "baseUri");
        treeBuilder.stack.add(element);
        TextNode textNode = new TextNode("data", "baseUri");
        treeBuilder.insertNode(textNode);
        assertEquals(textNode, element.children().get(0));
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("test"), "baseUri");
        Token.StartTag startTag = new Token.StartTag(element.tagName(), element.attributes);
        Element insertedElement = treeBuilder.insert(startTag);
        assertEquals(element, insertedElement);
        assertTrue(element.isBlock());
        assertEquals(1, treeBuilder.currentElement().children().size());
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("test"), "baseUri");
        element.setSelfClosing();
        Token.StartTag startTag = new Token.StartTag(element.tagName(), element.attributes);
        Element insertedElement = treeBuilder.insert(startTag);
        assertEquals(element, insertedElement);
        assertTrue(element.isBlock());
        assertEquals(1, treeBuilder.currentElement().children().size());
        assertFalse(treeBuilder.stack.contains(element));
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Comment comment = new Comment("comment", "baseUri");
        Token.Comment commentToken = new Token.Comment(comment.getData());
        treeBuilder.insert(commentToken);
        assertEquals(comment, treeBuilder.currentElement().children().get(0));
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        TextNode textNode = new TextNode("data", "baseUri");
        Token.Character characterToken = new Token.Character(textNode.getWholeText());
        treeBuilder.insert(characterToken);
        assertEquals(textNode, treeBuilder.currentElement().children().get(0));
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        DocumentType doctype = new DocumentType("name", "public", "system", "baseUri");
        Token.Doctype doctypeToken = new Token.Doctype(doctype.getName(), doctype.getPublicIdentifier(), doctype.getSystemIdentifier());
        treeBuilder.insert(doctypeToken);
        assertEquals(doctype, treeBuilder.currentElement().children().get(0));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("test"), "baseUri");
        Element element2 = new Element(Tag.valueOf("test"), "baseUri");
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);
        Token.EndTag endTag = new Token.EndTag(element1.tagName());
        treeBuilder.popStackToClose(endTag);
        assertFalse(treeBuilder.stack.contains(element1));
        assertTrue(treeBuilder.stack.contains(element2));
    }
}
