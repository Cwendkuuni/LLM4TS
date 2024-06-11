package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token startTag = new StartTag("root");
        Element el = parser.insert(startTag);
        assertEquals("root", el.nodeName());
        assertNotNull(el.baseUri());
        Attributes attrs = el.attributes();
        assertTrue(attrs.isEmpty());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token startTag = new StartTag("root");
        Element root = parser.insert(startTag);
        Token endTag = new EndTag("root");
        parser.popStackToClose(endTag);
        assertTrue(root.parentNode() == null);
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token commentToken = new CommentToken("-- comment");
        Comment comment = parser.insert(commentToken);
        assertEquals("-- comment", comment.getData());
        assertNull(comment.parentNode());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token characterToken = new Token(TokenType.Character, " ");
        TextNode character = parser.insert(characterToken);
        assertEquals(" ", character.text());
        assertNotNull(character.parentNode());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Doctype doctype = new Doctype("html", null, null);
        parser.insert(doctype);
        Document doc = parser.doc;
        DocumentType doctypeNode = doc.doctype();
        assertEquals("html", doctypeNode.nodeName());
        assertNull(doctypeNode.pubSysKey());
    }

    @Test(expected = AssertionError.class)
    public void testValidateUnexpectedTokenType() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token invalidToken = new Token(TokenType.Invalid, "invalid token");
        parser.process(invalidToken);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String inputFragment = "<root><element attr=\"value\"/><comment>-- comment</comment></root>";
        List<Node> nodes = parser.parseFragment(inputFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document doc = nodes.get(0).document();
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        Element element = (Element) root.childNodes().get(0);
        assertEquals("element", element.nodeName());
        Attributes attrs = element.attributes();
        assertEquals("value", attrs.get("attr"));
        assertEquals(0, attrs.size());
        assertEquals(1, nodes.size());
    }

    @Test
    public void testParseFragmentWithXmlDeclaration() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String inputFragment = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<root><element attr=\"value\"/><comment>-- comment</comment></root>";
        List<Node> nodes = parser.parseFragment(inputFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document doc = nodes.get(0).document();
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        Element element = (Element) root.childNodes().get(0);
        assertEquals("element", element.nodeName());
        Attributes attrs = element.attributes();
        assertEquals("value", attrs.get("attr"));
        assertEquals(0, attrs.size());
        XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.childNodes().get(0);
        assertEquals("html", xmlDeclaration.nodeName());
        assertFalse(xmlDeclaration.isStandalone());
        assertEquals(1, nodes.size());
    }
}