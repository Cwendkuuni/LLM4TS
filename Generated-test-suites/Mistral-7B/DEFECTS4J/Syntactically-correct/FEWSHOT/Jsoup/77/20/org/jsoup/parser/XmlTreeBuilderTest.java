package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("element", null, null);
        Element element = parser.insert(startTag);
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token endTag = new Token.EndTag("element");
        parser.popStackToClose(endTag);
        assertTrue(parser.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token comment = new Token.Comment("This is a comment");
        parser.insert(comment);
        assertTrue(parser.doc.body().child(0).nodeName().equals("#comment"));
        assertTrue(parser.doc.body().child(0).getText().equals("This is a comment"));
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token character = new Token.Character('a');
        parser.insert(character);
        TextNode textNode = (TextNode) parser.doc.body().child(0);
        assertTrue(textNode.getData().equals("a"));
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token doctype = new Token.Doctype("html", null, null, null);
        parser.insert(doctype);
        DocumentType documentTypeNode = (DocumentType) parser.doc.childNodes().get(0);
        assertTrue(documentTypeNode.nodeName().equals("#document-type-decl"));
        assertTrue(documentTypeNode.tagName().equals("html"));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xmlFragment = "<root><element>Hello</element></root>";
        List<Node> nodes = parser.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertTrue(nodes.size() == 3);
        Element root = (Element) nodes.get(0);
        Element element = (Element) nodes.get(1);
        TextNode textNode = (TextNode) nodes.get(2);
        assertTrue(root.tagName().equals("root"));
        assertTrue(element.tagName().equals("element"));
        assertTrue(textNode.getData().equals("Hello"));
    }
}