package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String input = "<root><element attr=\"value\"/><element tag=\"unknown\"/><!DOCTYPE html SYSTEM \"about:blank\"><!-- comment --></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(input));

        assertNotNull(doc);
        assertEquals("root", doc.tagName());

        List<Node> children = doc.childNodes();
        assertEquals(3, children.size());

        Element root = (Element) children.get(0);
        assertEquals("element", root.childNodes().get(0).tagName());
        assertEquals("value", root.childNodes().get(0).attribute(0).value());
        assertEquals("unknown", root.childNodes().get(1).tagName());

        DocumentType doctype = (DocumentType) children.get(2);
        assertEquals("html", doctype.tagName());
        assertEquals("about:blank", doctype.systemIdentifier());
        assertNull(doctype.publicIdentifier());

        Comment comment = (Comment) children.get(3);
        assertEquals("comment", comment.tagName());
    }

    @Test
    public void testParseEmpty() {
        Document doc = new XmlTreeBuilder().parse(new StringReader(""));
        assertNull(doc);
    }

    @Test
    public void testParseWithErrors() {
        String input = "<root><element attr=\"value\"<tag/><!DOCTYPE html SYSTEM \"about:blank\"-->\n";
        try {
            new XmlTreeBuilder().parse(new StringReader(input));
            assertFalse("parse should have failed", true);
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testParseFragment() {
        String input = "<root><element attr=\"value\"/></root>";
        List<Node> fragment = new XmlTreeBuilder().parseFragment(input, null, null, null);

        assertNotNull(fragment);
        assertEquals(1, fragment.size());

        Element root = (Element) fragment.get(0);
        assertEquals("root", root.tagName());
        assertEquals("element", root.childNodes().get(0).tagName());
        assertEquals("value", root.childNodes().get(0).attribute(0).value());
    }

    @Test
    public void testParseFragmentWithContext() {
        String input = "<root><element attr=\"value\"/></root>";
        Element context = new Element("context");
        List<Node> fragment = new XmlTreeBuilder().parseFragment(input, context, null, null);

        assertNotNull(fragment);
        assertEquals(1, fragment.size());

        Element root = (Element) fragment.get(0);
        assertSame(context, root.parent());
        assertEquals("element", root.tagName());
        assertEquals("value", root.childNodes().get(0).attribute(0).value());
    }

    @Test
    public void testProcessStartTag() {
        Token token = new Token(StartTag.class, "<root>");
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.process(token);

        Element root = builder.currentElement();
        assertEquals("root", root.tagName());
        assertNull(root.parent());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testProcessEndTag() {
        Element root = new Element("root");
        stack.add(root);
        Token token = new Token(EndTag.class, "</root>");
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.process(token);

        assertNull(stack.get(stack.size() - 1));
    }

    @Test
    public void testProcessComment() {
        Token token = new Token(Comment.class, "-- comment --");
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.process(token);

        Comment comment = (Comment) builder.currentElement();
        assertEquals("comment", comment.tagName());
        assertEquals(" comment --", comment.data());
    }

    @Test
    public void testProcessCharacter() {
        Token token = new Token(Character.class, "hello");
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.process(token);

        TextNode textNode = (TextNode) builder.currentElement();
        assertEquals("hello", textNode.data());
    }

    @Test
    public void testProcessDoctype() {
        Token token = new Token(Doctype.class, "<!DOCTYPE html SYSTEM \"about:blank\">");
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.process(token);

        DocumentType doctype = (DocumentType) builder.currentElement();
        assertEquals("html", doctype.tagName());
        assertEquals("about:blank", doctype.systemIdentifier());
        assertNull(doctype.publicIdentifier());
    }

    @Test
    public void testProcessEOF() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.process(new Token(Token.EOF.class));

        assertEquals(0, doc.childNodes().size());
    }

    @Test(expected = RuntimeException.class)
    public void testProcessUnexpectedTokenType() {
        Token token = new Token(Token.Unknown.class, "unknown");
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.process(token);
    }

    @Test
    public void testInsertNodeSelfClosingTag() {
        Element element = new Element("element");
        Token token = new Token.StartTag(null, "tag", new Attribute[0], true);

        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(token);

        Element tag = builder.currentElement();
        assertEquals("tag", tag.tagName());
        tag.setSelfClosing(true);
        assertEquals(0, tag.childNodes().size());
    }

    @Test
    public void testInsertNodeUnknownTag() {
        Element parent = new Element("parent");
        Token token = new Token.StartTag(null, "unknown", new Attribute[0], false);

        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(token);

        Element child = builder.currentElement();
        assertEquals("unknown", child.tagName());
        assertEquals(0, child.childNodes().size());
    }

    @Test
    public void testInsertNodeText() {
        Element parent = new Element("parent");
        Token token = new Token.Character("text");

        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(token);

        TextNode textNode = (TextNode) builder.currentElement();
        assertEquals("text", textNode.data());
    }

    @Test
    public void testInsertNodeCData() {
        Element parent = new Element("parent");
        Token token = new Token.Character("<!--CDATA[text]-->", true);

        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(token);

        CDataNode cdataNode = (CDataNode) builder.currentElement();
        assertEquals("text", cdataNode.data());
    }

    @Test
    public void testInsertNodeComment() {
        Element parent = new Element("parent");
        Token token = new Token.Comment("comment");

        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(token);

        Comment comment = (Comment) builder.currentElement();
        assertEquals("comment", comment.data());
    }

    @Test
    public void testInsertNodeDocumentType() {
        Document doc = new Document();
        Token token = new Token.Doctype("html", null, null, null, null, null);

        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(token);

        DocumentType documentType = (DocumentType) builder.currentElement();
        assertEquals("html", documentType.tagName());
        assertEquals("about:blank", documentType.systemIdentifier());
        assertNull(documentType.publicIdentifier());
    }
}