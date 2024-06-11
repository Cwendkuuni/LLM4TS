package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.CharacterToken;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;

import org.junit.Test;

/**
 * Test class for XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseEmptyXml() {
        String xml = "";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        String xml = "<root><name>John Doe</name></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element name = (Element) children.get(0);
        assertEquals("name", name.nodeName());
        assertEquals("John Doe", name.text());
    }

    @Test
    public void testParseXmlWithComment() {
        String xml = "<root><!-- This is a comment --><name>John Doe</name></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element name = (Element) children.get(0);
        assertEquals("name", name.nodeName());
        assertEquals("John Doe", name.text());
        Node comment = doc.childNodes().get(1);
        assertEquals(Node.NodeType.COMMENT_NODE, comment.nodeType());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlWithMixedTagsAndAttributes() {
        String xml = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://www.example.com/\"><name attr=\"value\">John Doe</name><x:tag attr1=\"value1\" attr2=\"value2\"></x:tag></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(2, children.size());
        Element name = (Element) children.get(0);
        assertEquals("name", name.nodeName());
        assertEquals("John Doe", name.text());
        assertEquals("value", name.attributes().get("attr").getValue());
        Element tag = (Element) children.get(1);
        assertEquals("tag", tag.nodeName());
        assertEquals("http://www.example.com/", tag.namespaceURI());
        List<Node> tagChildren = tag.childNodes();
        assertEquals(0, tagChildren.size());
        assertEquals("value1", tag.attributes().get("attr1").getValue());
        assertEquals("value2", tag.attributes().get("attr2").getValue());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xml = "<root><name>John Doe</name></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        assertNotNull(doc);
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element name = (Element) children.get(0);
        assertEquals("name", name.nodeName());
        assertEquals("John Doe", name.text());
        assertNull(name.getNextSibling());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidInput() {
        String xml = "<root></root>"; // valid XML
        Document doc = XmlTreeBuilder.parse(new StringReader(""), ""); // invalid input
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token(TokenType.StartTag, "<tag>", null);
        Token endTag = new Token(TokenType.EndTag, "</tag>", null);
        builder.stack = new Stack<Element>();
        builder.stack.add(new Element("root"));
        builder.insert(startTag);
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testPopStackToCloseWithNoMatchingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token(TokenType.StartTag, "<tag>", null);
        Token endTag = new Token(TokenType.EndTag, "</nonMatchingTag>", null);
        builder.stack = new Stack<Element>();
        builder.stack.add(new Element("root"));
        builder.insert(startTag);
        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testProcessCommentToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token commentToken = new Token(TokenType.Comment, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", false);
        builder.insert(commentToken);
        Comment comment = (Comment) builder.doc.childNodes().get(0);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", comment.getData());
    }

    @Test
    public void testProcessCharacterToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token characterToken = new CharacterToken('&', "amp;");
        builder.insert(characterToken);
        TextNode textNode = (TextNode) builder.doc.childNodes().get(0);
        assertEquals("amp", textNode.data());
    }

    @Test
    public void testProcessDoctypeToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Doctype doctype = new Doctype("xml", null, null, null, null, null, null);
        builder.insert(doctype);
        DocumentType documentType = (DocumentType) builder.doc.childNodes().get(0);
        assertEquals("xml", documentType.nodeName());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<tag attr1=\"value1\" attr2=\"value2\"><subTag/><subTag><subTag/></subTag></tag>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xmlFragment), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element tag = (Element) nodes.get(0);
        assertEquals("tag", tag.nodeName());
        List<Node> children = tag.childNodes();
        assertEquals(2, children.size());
        Element subTag1 = (Element) children.get(0);
        assertEquals(0, subTag1.childNodes().size());
        Element subTag2 = (Element) children.get(1);
        assertEquals(1, subTag2.childNodes().size());
        Element subSubTag = (Element) subTag2.childNodes().get(0);
        assertEquals(0, subSubTag.childNodes().size());
    }
}