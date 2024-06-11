package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<foo>bar</foo>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(baseUri, xmlTreeBuilder.baseUri());
        assertTrue(errors.isEmpty());
        assertNotNull(xmlTreeBuilder.stack());
        assertNotNull(xmlTreeBuilder.doc());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("foo", new Attributes());
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        assertTrue(xmlTreeBuilder.process(new Token(startTag)));

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(1, children.size());
        Element child = children.get(0);
        assertEquals("foo", child.tagName());
        assertEquals("parent", child.parent().tagName());
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("foo");
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        xmlTreeBuilder.stack().push(new Element("foo", ""));
        assertTrue(xmlTreeBuilder.process(new Token(endTag)));

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(0, children.size());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment("This is a comment", "");
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        assertTrue(xmlTreeBuilder.process(new Token(comment)));

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(1, children.size());
        Comment commentNode = (Comment) children.get(0);
        assertEquals("This is a comment", commentNode.getData());
        assertEquals("parent", commentNode.parent().tagName());
    }

    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character("This is a character");
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        assertTrue(xmlTreeBuilder.process(new Token(character)));

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(1, children.size());
        TextNode textNode = (TextNode) children.get(0);
        assertEquals("This is a character", textNode.text());
        assertEquals("parent", textNode.parent().tagName());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        assertTrue(xmlTreeBuilder.process(new Token(doctype)));

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(1, children.size());
        DocumentType doctypeNode = (DocumentType) children.get(0);
        assertEquals("html", doctypeNode.nodeName());
        assertEquals("publicId", doctypeNode.publicId());
        assertEquals("systemId", doctypeNode.systemId());
    }

    @Test
    public void testInsertNode() {
        Element parent = new Element("parent", "");
        
        Element child = new Element("child", "");
        xmlTreeBuilder.stack().push(parent);
        xmlTreeBuilder.insertNode(child);

        Elements children = parent.children();
        assertEquals(1, children.size());
        assertEquals(child, children.get(0));
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag startTag = new Token.StartTag("foo", new Attributes(), true);
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(1, children.size());
        assertEquals(element, children.get(0));
    }

    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("foo", new Attributes(), false);
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertTrue(element.tag().isKnownTag());
        assertFalse(element.tag().isSelfClosing());

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(1, children.size());
        assertEquals(element, children.get(0));
    }

    @Test
    public void testInsert_Comment_NoBogus() {
        Token.Comment commentToken = new Token.Comment("This is a comment", "");
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        xmlTreeBuilder.insert(commentToken);

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(1, children.size());
        Comment commentNode = (Comment) children.get(0);
        assertEquals("This is a comment", commentNode.getData());
        assertEquals("parent", commentNode.parent().tagName());
    }

    @Test
    public void testInsert_Comment_Bogus() {
        Token.Comment commentToken = new Token.Comment("!This is a comment", "");
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        xmlTreeBuilder.insert(commentToken);

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(1, children.size());
        XmlDeclaration xmlDeclaration = (XmlDeclaration) children.get(0);
        assertNotNull(xmlDeclaration);
        assertEquals("This is a comment", xmlDeclaration.attr("data"));
        assertEquals("parent", xmlDeclaration.parent().tagName());
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character("This is a character");
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        xmlTreeBuilder.insert(characterToken);

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(1, children.size());
        TextNode textNode = (TextNode) children.get(0);
        assertEquals("This is a character", textNode.text());
        assertEquals("parent", textNode.parent().tagName());
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        xmlTreeBuilder.stack().push(new Element("parent", ""));
        xmlTreeBuilder.insert(doctype);

        Elements children = xmlTreeBuilder.stack().get(0).children();
        assertEquals(1, children.size());
        DocumentType doctypeNode = (DocumentType) children.get(0);
        assertEquals("html", doctypeNode.nodeName());
        assertEquals("publicId", doctypeNode.publicId());
        assertEquals("systemId", doctypeNode.systemId());
    }

    @Test
    public void testPopStackToClose_Found() {
        Token.EndTag endTag = new Token.EndTag("foo");
        Element parent = new Element("parent", "");
        Element foo = new Element("foo", "");
        xmlTreeBuilder.stack().push(parent);
        xmlTreeBuilder.stack().push(foo);
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack().size());
        assertEquals(parent, xmlTreeBuilder.stack().get(0));
    }

    @Test
    public void testPopStackToClose_NotFound() {
        Token.EndTag endTag = new Token.EndTag("bar");
        Element parent = new Element("parent", "");
        Element foo = new Element("foo", "");
        xmlTreeBuilder.stack().push(parent);
        xmlTreeBuilder.stack().push(foo);
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, xmlTreeBuilder.stack().size());
        assertEquals(parent, xmlTreeBuilder.stack().get(0));
        assertEquals(foo, xmlTreeBuilder.stack().get(1));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>hello world</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertEquals("hello world", element.text());
    }
}