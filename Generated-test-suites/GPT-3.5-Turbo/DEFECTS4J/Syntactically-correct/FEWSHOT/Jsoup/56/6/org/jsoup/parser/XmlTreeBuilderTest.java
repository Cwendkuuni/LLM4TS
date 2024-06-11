package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder treeBuilder;
    private ParseSettings parseSettings;
    private String baseUri;

    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
        parseSettings = ParseSettings.preserveCase;
        baseUri = "http://example.com";
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = treeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParse() {
        String input = "<root><element>text</element></root>";
        Document doc = treeBuilder.parse(input, baseUri);
        assertEquals("root", doc.nodeName());
        assertEquals("element", doc.child(0).nodeName());
        assertEquals("text", doc.child(0).child(0).nodeName());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><element>text</element></root>";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors, parseSettings);
        assertEquals("root", treeBuilder.doc.nodeName());
        assertTrue(treeBuilder.stack.contains(treeBuilder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, treeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        Element el = treeBuilder.insert(startTag);
        assertEquals("element", el.nodeName());
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("element");
        treeBuilder.insert(endTag);
        assertFalse(treeBuilder.stack.contains("element"));
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = new Token.Comment("comment");
        treeBuilder.insert(commentToken);
        assertEquals("comment", treeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testProcess_Character() {
        Token.Character characterToken = new Token.Character("text");
        treeBuilder.insert(characterToken);
        assertEquals("text", treeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", baseUri);
        treeBuilder.insert(doctype);
        assertEquals("html", treeBuilder.currentElement().nodeName());
    }

    @Test
    public void testInsertNode() {
        Node node = new Element("element");
        treeBuilder.insertNode(node);
        assertEquals("element", treeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsert() {
        Token.StartTag startTag = new Token.StartTag("element");
        Element el = treeBuilder.insert(startTag);
        assertEquals("element", el.nodeName());
    }

    @Test
    public void testInsert_SelfClosingTag() {
        Token.StartTag startTag = new Token.StartTag("element", ParseSettings.preserveCase, true);
        treeBuilder.insert(startTag);
        assertEquals("element", treeBuilder.doc.childNode(0).nodeName());
    }

    @Test
    public void testInsert_CommentToken_Bogus() {
        Token.Comment commentToken = new Token.Comment("<!-- comment -->");
        treeBuilder.insert(commentToken);
        assertEquals("comment", treeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsert_CommentToken_NonBogus() {
        Token.Comment commentToken = new Token.Comment("comment");
        treeBuilder.insert(commentToken);
        assertEquals("comment", treeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsert_CharacterToken() {
        Token.Character characterToken = new Token.Character("text");
        treeBuilder.insert(characterToken);
        assertEquals("text", treeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsert_DoctypeToken() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", baseUri);
        treeBuilder.insert(doctype);
        assertEquals("html", treeBuilder.currentElement().nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("element");
        treeBuilder.stack.add(new Element("element"));
        treeBuilder.popStackToClose(endTag);
        assertTrue(treeBuilder.stack.isEmpty());
    }

    @Test
    public void testPopStackToClose_NotFound() {
        Token.EndTag endTag = new Token.EndTag("element");
        treeBuilder.stack.add(new Element("other"));
        treeBuilder.popStackToClose(endTag);
        assertFalse(treeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element>text</element>";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors, parseSettings);
        assertEquals("element", nodes.get(0).nodeName());
        assertEquals("text", nodes.get(0).childNode(0).nodeName());
    }
}