package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(StartTag.class, "root", null, null);
        Element root = builder.insert(token);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.stack.add(root);
        Token token = new Token(EndTag.class, "root");
        builder.popStackToClose(token);
        assertNull(builder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(Comment.class, "This is a comment");
        builder.insert(token);
        Comment comment = (Comment) builder.doc.childNodes().get(0);
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(Character.class, "A");
        builder.insert(token);
        TextNode textNode = (TextNode) builder.doc.childNodes().get(0);
        assertEquals("A", textNode.data());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(Doctype.class, "html", "public", "http://www.w3.org/TR/html4/strict.dtd");
        builder.insert(token);
        DocumentType documentType = (DocumentType) builder.doc.childNodes().get(0);
        assertEquals("html", documentType.nodeName());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.publicId());
    }

    @Test
    public void testParseFragmentEmpty() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNull(nodes);
    }

    @Test
    public void testParseFragmentSimple() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root></root>", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseFragmentWithChildren() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><child></child></root>", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertEquals("child", child.nodeName());
        assertEquals(0, child.childNodes().size());
    }

    @Test
    public void testParseFragmentWithComments() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><!-- Comment 1 --></root><!-- Comment 2 -->", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(0, children.size());
        List<Node> comments = builder.doc.childNodes();
        assertEquals(2, comments.size());
        Comment comment1 = (Comment) comments.get(0);
        assertEquals("Comment 1", comment1.data());
        Comment comment2 = (Comment) comments.get(1);
        assertEquals("Comment 2", comment2.data());
    }

    @Test
    public void testParseFragmentWithSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><tag/></root>", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element tag = (Element) children.get(0);
        assertEquals("tag", tag.nodeName());
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testParseFragmentWithSelfClosingTagProcessedAsElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><![CDATA[ <tag/> ]]></root>", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element tag = (Element) children.get(0);
        assertEquals("tag", tag.nodeName());
        assertTrue(tag.isSelfClosing());
    }
}