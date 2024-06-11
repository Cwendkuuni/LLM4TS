package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private static final String XML_SAMPLE = "<root><a attr=\"value\"><b attr=\"value2\"/><c/></a></root>";

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("root");
        builder.process(token);
        Element root = (Element) builder.stack.peek();
        assertEquals("root", root.nodeName());
        assertNotNull(root.baseUri());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.EndTag("root");
        builder.process(token);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Comment("This is a comment");
        builder.process(token);
        Comment comment = (Comment) builder.current.childNodes().get(0);
        assertEquals("This is a comment", comment.data());
        assertEquals(builder.baseUri, comment.baseUri());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Character('A');
        builder.process(token);
        TextNode textNode = (TextNode) builder.current.childNodes().get(0);
        assertEquals("A", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Doctype("html", null, null);
        builder.process(token);
        DocumentType documentType = (DocumentType) builder.current.childNodes().get(0);
        assertEquals("html", documentType.nodeName());
        assertNull(documentType.publicIdentifier());
        assertNull(documentType.systemIdentifier());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.insertNode(root);
        assertEquals(root, builder.current.childNodes().get(0));
    }

    @Test
    public void testProcessStartTagAndInsertElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("a");
        builder.process(token);
        Element a = builder.insert(token);
        assertEquals("a", a.nodeName());
        assertNotNull(a.baseUri());
        assertEquals(0, a.childNodes().size());
    }

    @Test
    public void testProcessStartTagWithSelfClosing() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("img", true);
        builder.process(token);
        Element img = builder.insert(token);
        assertEquals("img", img.nodeName());
        assertTrue(img.isSelfClosing());
    }

    @Test
    public void testProcessEndTagAndPopStack() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("a");
        builder.process(startTag);
        Element a = builder.insert(startTag);

        Token endTag = new Token.EndTag("a");
        builder.process(endTag);

        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessEndTagNotFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("a");
        builder.process(startTag);
        Element a = builder.insert(startTag);

        Token endTag = new Token.EndTag("b"); // different tag name
        builder.process(endTag);

        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testParseXmlSample() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(XML_SAMPLE, null, null);

        Element root = (Element) builder.doc;
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());

        Element a = (Element) root.childNodes().get(0);
        assertEquals("a", a.nodeName());
        assertEquals(2, a.childNodes().size());

        Element b = (Element) a.childNodes().get(0);
        assertEquals("b", b.nodeName());
        assertEquals(0, b.childNodes().size());

        Element c = (Element) a.childNodes().get(1);
        assertEquals("c", c.nodeName());
        assertEquals(0, c.childNodes().size());
    }
}