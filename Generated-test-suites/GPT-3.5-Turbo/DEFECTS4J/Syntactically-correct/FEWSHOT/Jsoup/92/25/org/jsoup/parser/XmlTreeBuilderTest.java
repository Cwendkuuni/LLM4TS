package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, treeBuilder.defaultSettings());
    }

    @Test
    public void testInitialiseParse() {
        treeBuilder.initialiseParse(new StringReader(""), "", new Parser(treeBuilder));
        assertEquals(1, treeBuilder.stack.size());
        assertEquals(treeBuilder.doc, treeBuilder.stack.get(0));

        Document.OutputSettings.Syntax syntax = treeBuilder.doc.outputSettings().syntax();
        assertEquals(Document.OutputSettings.Syntax.xml, syntax);
    }

    @Test
    public void testParse() {
        Document document = treeBuilder.parse(new StringReader(""), "");
        assertTrue(document instanceof Document);
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = treeBuilder.parseFragment("", "", new Parser(treeBuilder));
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testParseFragmentWithContext() {
        Element element = new Element(Tag.valueOf("div", treeBuilder.defaultSettings()), "");
        List<Node> nodes = treeBuilder.parseFragment("", element, "", new Parser(treeBuilder));
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("test");
        startTag.selfClosing(false);

        Element element = treeBuilder.insert(startTag);
        assertTrue(treeBuilder.stack.contains(element));
        assertEquals("test", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("test");

        Element element = new Element(Tag.valueOf("test", treeBuilder.defaultSettings()), "");
        treeBuilder.stack.add(element);

        treeBuilder.popStackToClose(endTag);
        assertFalse(treeBuilder.stack.contains(element));
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment();
        comment.data("test");

        treeBuilder.insert(comment);
        Comment insertedComment = (Comment) treeBuilder.currentElement().childNode(0);

        assertEquals("test", insertedComment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character();
        character.data("test");
        character.isCData(false);

        treeBuilder.insert(character);
        TextNode textNode = (TextNode) treeBuilder.currentElement().childNode(0);

        assertEquals("test", textNode.getWholeText());
    }

    @Test
    public void testProcessCharacterAsCData() {
        Token.Character character = new Token.Character();
        character.data("test");
        character.isCData(true);

        treeBuilder.insert(character);
        CDataNode cDataNode = (CDataNode) treeBuilder.currentElement().childNode(0);

        assertEquals("test", cDataNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("public");
        doctype.systemIdentifier("system");

        treeBuilder.insert(doctype);
        DocumentType documentType = (DocumentType) treeBuilder.currentElement().childNode(0);

        assertEquals("html", documentType.nodeName());
        assertEquals("public", documentType.attr("publicid"));
        assertEquals("system", documentType.attr("systemid"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProcessUnexpectedToken() {
        Token token = new Token();
        token.type(Token.TokenType.Attribute);

        treeBuilder.process(token);
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("test", treeBuilder.defaultSettings()), "");

        treeBuilder.insertNode(element);
        assertEquals(element, treeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertXmlDeclarationCommentTokenBogus() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.bogus(true);
        commentToken.data("test");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("test", "", "");
        Comment comment = new Comment("test");

        treeBuilder.insert(commentToken);
        assertEquals(xmlDeclaration, treeBuilder.currentElement().childNode(0));
        treeBuilder.insert(commentToken);
        assertEquals(comment, treeBuilder.currentElement().childNode(1));
    }

    @Test
    public void testInsertXmlDeclarationCommentTokenNotBogus() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.bogus(false);
        commentToken.data("test");

        treeBuilder.insert(commentToken);
        Comment comment = (Comment) treeBuilder.currentElement().childNode(0);

        assertEquals("test", comment.getData());
    }

    @Test
    public void testInsertXmlDeclarationCommentTokenNull() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.bogus(false);
        commentToken.setData(null);

        treeBuilder.insert(commentToken);
        Comment comment = (Comment) treeBuilder.currentElement().childNode(0);

        assertNull(comment.getData());
    }

    @Test
    public void testInsertXmlAttributelessStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("test");
        startTag.selfClosing(true);

        treeBuilder.insert(startTag);
        Element element = treeBuilder.currentElement().childElement(0);

        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertUnknownAttributelessStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("unknown");
        startTag.selfClosing(true);

        treeBuilder.insert(startTag);
        Element element = treeBuilder.currentElement().childElement(0);

        assertFalse(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertStartElement() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("test");
        startTag.selfClosing(false);
        startTag.attributes.put("key", "value");

        treeBuilder.insert(startTag);
        Element element = treeBuilder.currentElement().childElement(0);

        assertTrue(element.tag().isKnownTag());
        assertTrue(element.hasAttr("key"));
        assertEquals("value", element.attr("key"));
    }
}