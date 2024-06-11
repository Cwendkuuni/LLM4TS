package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><elem attr=\"attrValue\">Hello</elem><elem2/><!--comment--></root>";
        Document doc = XmlTreeBuilder.parse(xml, null);

        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        List<Node> children = root.children();
        assertEquals(2, children.size());

        Element elem = (Element) children.get(0);
        assertEquals("elem", elem.tagName());
        assertEquals("attrValue", elem.attributes().get("attr"));
        assertEquals("Hello", elem.text());

        Element elem2 = (Element) children.get(1);
        assertEquals("elem2", elem2.tagName());
        assertNull(elem2.attributes().get("attr"));
        assertEquals(0, elem2.children().size());

        Comment comment = (Comment) root.child(2);
        assertEquals("comment", comment.tagName());
        assertEquals("--comment--", comment.text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<elem attr=\"attrValue\">Hello</elem>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, null, null, null);

        Element elem = (Element) nodes.get(0);
        assertEquals("elem", elem.tagName());
        assertEquals("attrValue", elem.attributes().get("attr"));
        assertEquals("Hello", elem.text());
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidTokenType() {
        String xml = "<root>"; // missing closing tag
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.setInput(xml);
        builder.initialiseParse(xml, null, null, builder.defaultSettings());

        tokenizer.nextToken(); // start tag
        tokenizer.nextToken(); // unexpected EOF

        builder.process(null); // should throw an exception
    }

    @Test
    public void testProcessUnknownTagSelfClosing() {
        String xml = "<unknown/>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.setInput(xml);
        builder.initialiseParse(xml, null, null, builder.defaultSettings());

        Token startTag = tokenizer.nextToken();
        Element elem = builder.insert(startTag);

        assertEquals("unknown", elem.tagName());
        assertTrue(elem.isSelfClosing());
    }

    @Test
    public void testProcessUnknownTagNotSelfClosing() {
        String xml = "<unknown>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.setInput(xml);
        builder.initialiseParse(xml, null, null, builder.defaultSettings());

        Token startTag = tokenizer.nextToken();
        Element elem = builder.insert(startTag);

        assertEquals("unknown", elem.tagName());
        assertFalse(elem.isSelfClosing());
    }

    @Test
    public void testProcessComment() {
        String xml = "<!--comment-->";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.setInput(xml);
        builder.initialiseParse(xml, null, null, builder.defaultSettings());

        Token comment = tokenizer.nextToken();
        Comment xmlDeclaration = (Comment) builder.insert(comment);

        assertEquals("comment", xmlDeclaration.tagName());
        assertEquals("--comment--", xmlDeclaration.text());
    }

    @Test
    public void testProcessXmlDeclaration() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.setInput(xml);
        builder.initialiseParse(xml, null, null, builder.defaultSettings());

        Token xmlDeclarationToken = tokenizer.nextToken();
        XmlDeclaration xmlDeclaration = (XmlDeclaration) builder.insert(xmlDeclarationToken);

        assertEquals("xml", xmlDeclaration.tagName());
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertFalse(xmlDeclaration.isSelfClosing());
    }
}