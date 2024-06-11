package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("tag", null, null);
        Element element = builder.insert(startTag);
        assertNotNull(element);
        assertTrue(element.nodeName().equals("tag"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token endTag = new Token.EndTag("tag");
        builder.popStackToClose(endTag);
        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token character = new Token.Character('a');
        TextNode textNode = (TextNode) builder.insert(character);
        assertNotNull(textNode);
        assertTrue(textNode.text().equals("a"));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token comment = new Token.Comment("This is a comment");
        Comment commentNode = (Comment) builder.insert(comment);
        assertNotNull(commentNode);
        assertTrue(commentNode.data().equals("This is a comment"));
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token doctype = new Token.Doctype("html", null, null, null);
        DocumentType doctypeNode = (DocumentType) builder.insert(doctype);
        assertNotNull(doctypeNode);
        assertTrue(doctypeNode.name().equals("html"));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = builder.doc;
        Element child = new Element("child");
        builder.insertNode(child);
        root.appendChild(child);
        assertTrue(root.childNodes().size() == 1);
        assertTrue(root.childNodes().get(0).nodeName().equals("child"));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element parent = new Element("parent");
        Element child = new Element("child");
        builder.stack.add(parent);
        builder.stack.add(child);

        Token endTag = new Token.EndTag("child");
        builder.popStackToClose(endTag);

        assertTrue(builder.stack.size() == 1);
        assertTrue(builder.stack.get(0).nodeName().equals("parent"));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<root><child></child></root>";
        Document doc = builder.parseFragment(inputFragment, null, null);

        Element root = doc.rootElement();
        List<Node> childNodes = root.childNodes();
        assertTrue(childNodes.size() == 1);
        assertTrue(childNodes.get(0).nodeName().equals("child"));
        assertTrue(childNodes.get(0).childNodes().isEmpty());
    }
}