package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilder() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = new Document();
        xmlTreeBuilder.initialiseParse("", "", null);
        xmlTreeBuilder.process(new Token("", "", 0, 0, -1)); // EOF
        assertEquals(document, xmlTreeBuilder.getDocument());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root");
        xmlTreeBuilder.initialiseParse("", "", null);
        xmlTreeBuilder.process(new Token.StartTag("root"));
        assertEquals(root, xmlTreeBuilder.getDocument().getRootElement());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root");
        xmlTreeBuilder.initialiseParse("", "", null);
        xmlTreeBuilder.process(new Token.StartTag("root", new Attribute("attr1", "value1"), new Attribute("attr2", "value2")));
        Element element = root.element("attr1");
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", root.attr("attr2"));
    }

    @Test
    public void testProcessSelfClosingStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);
        xmlTreeBuilder.process(new Token.StartTag("img", true, new Attribute("src", "image.jpg")));
        Element img = xmlTreeBuilder.getDocument().select("img").first();
        assertEquals("img", img.tagName());
        assertTrue(img.isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);
        Element root = new Element("root");
        xmlTreeBuilder.process(new Token.StartTag("root"));
        xmlTreeBuilder.process(new Token.StartTag("child1"));
        xmlTreeBuilder.process(new Token.EndTag("child1"));
        xmlTreeBuilder.process(new Token.EndTag("root"));
        Element rootElement = xmlTreeBuilder.getDocument().getRootElement();
        assertEquals("root", rootElement.tagName());
        assertEquals(1, rootElement.childSize());
        assertTrue(rootElement.child(0).isDetached());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);
        xmlTreeBuilder.process(new Token.Comment("This is a comment"));
        Comment comment = xmlTreeBuilder.getDocument().comment();
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);
        xmlTreeBuilder.process(new Token.Character(' '));
        Element root = xmlTreeBuilder.getDocument().getRootElement();
        TextNode textNode = (TextNode) root.child(0);
        assertEquals(" ", textNode.data());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);
        xmlTreeBuilder.process(new Token.Doctype("html", "", ""));
        DocumentType doctypeNode = xmlTreeBuilder.getDocument().doctype();
        assertEquals("html", doctypeNode.name());
        assertNull(doctypeNode.publicIdentifier());
        assertNull(doctypeNode.systemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", null);
        Element root = new Element("root");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        xmlTreeBuilder.process(new Token.StartTag("root"));
        xmlTreeBuilder.process(new Token.StartTag("child1"));
        xmlTreeBuilder.process(new Token.EndTag("child1"));
        xmlTreeBuilder.process(new Token.StartTag("child2"));
        xmlTreeBuilder.process(new Token.EndTag("child2"));
        xmlTreeBuilder.process(new Token.EndTag("root"));
        Iterator<Element> it = xmlTreeBuilder.getDocument().iterator();
        assertEquals("root", it.next().tagName());
        assertEquals("child2", it.next().tagName());
        xmlTreeBuilder.popStackToClose(new Token.EndTag("child1"));
        assertEquals(2, xmlTreeBuilder.getDocument().childSize());
        assertTrue(xmlTreeBuilder.getDocument().select("child1").isEmpty());
    }
}