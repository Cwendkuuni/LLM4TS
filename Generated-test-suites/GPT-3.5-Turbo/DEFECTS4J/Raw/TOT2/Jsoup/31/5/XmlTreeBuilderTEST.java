package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("test input", "test base uri", null);
        Document doc = builder.getDocument();
        Element root = doc.child(0);
        assertEquals("Document", root.nodeName());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTagToken = new Token(Token.TokenType.StartTag);
        startTagToken.asStartTag().name("div");
        assertTrue(builder.process(startTagToken));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.stack.add(root);
        Node node = new TextNode("text node");
        builder.insertNode(node);
        assertEquals("text node", root.text());
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test comment");
        builder.insert(commentToken);
        Node insertedNode = builder.currentElement().childNode(0);
        assertTrue(insertedNode instanceof Comment);
        assertEquals("test comment", insertedNode.outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(new Token.EndTag().name("div"));
        builder.insert(new Token.EndTag().name("span"));
        builder.insert(new Token.EndTag().name("div"));

        Element root = builder.stack.get(0);
        assertEquals("div", root.nodeName());

        builder.popStackToClose(new Token.EndTag().name("div"));
        assertEquals(1, builder.stack.size());
        assertEquals("span", builder.stack.get(0).nodeName());
    }
}