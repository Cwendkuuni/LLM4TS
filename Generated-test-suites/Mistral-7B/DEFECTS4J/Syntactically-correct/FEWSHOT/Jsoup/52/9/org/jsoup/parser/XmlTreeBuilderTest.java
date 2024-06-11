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
import org.jsoup.parser.Token;

public class XmlTreeBuilderTest {

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"value\"><subElement/><text>Hello World</text></element></root>";
        Document doc = XmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList());
        assertNotNull(doc);
        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        List<Attribute> attributes = element.attributes();
        assertEquals(1, attributes.size());
        Attribute attr = attributes.get(0);
        assertEquals("attr", attr.getKey());
        assertEquals("value", attr.getValue());
        List<Node> subElements = element.children();
        assertEquals(2, subElements.size());
        Element subElement = (Element) subElements.get(0);
        assertTrue(subElement.tagName().isEmpty());
        TextNode text = (TextNode) subElements.get(1);
        assertEquals("Hello World", text.text());
    }

    @Test
    public void testParseFragmentWithComment() {
        String xmlFragment = "<root><!-- This is a comment --><element attr=\"value\"><subElement/><text>Hello World</text></element></root>";
        Document doc = XmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList());
        assertNotNull(doc);
        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        List<Node> subElements = element.children();
        assertEquals(2, subElements.size());
        Element subElement = (Element) subElements.get(0);
        assertTrue(subElement.tagName().isEmpty());
        TextNode text = (TextNode) subElements.get(1);
        assertEquals("Hello World", text.text());
        int indexOfComment = root.childNodes().indexOf(root.childNodes().get(0));
        assertNotNull(root.childNodes().get(indexOfComment));
        Comment comment = (Comment) root.childNodes().get(indexOfComment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseFragmentWithEmptyDocument() {
        Document doc = XmlTreeBuilder.parseFragment("", null, new ParseErrorList());
        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentWithNullInput() {
        XmlTreeBuilder.parseFragment(null, null, new ParseErrorList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentWithNullBaseUri() {
        XmlTreeBuilder.parseFragment("<root><element/></root>", null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseFragmentWithNullParseErrorList() {
        XmlTreeBuilder.parseFragment("<root><element/></root>", null, null);
    }

    @Test
    public void testProcessStartTag() {
        Token startTagToken = new Token.StartTag("element", null, new Attribute("attr", "value"));
        Element element = new XmlTreeBuilder().insert(startTagToken);
        assertEquals("element", element.tagName());
        List<Attribute> attributes = element.attributes();
        assertEquals(1, attributes.size());
        Attribute attr = attributes.get(0);
        assertEquals("attr", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testProcessEndTag() {
        Token endTagToken = new Token.EndTag("element");
        new XmlTreeBuilder().popStackToClose(endTagToken);
    }

    @Test
    public void testProcessComment() {
        Token commentToken = new Token.Comment("This is a comment");
        new XmlTreeBuilder().insert(commentToken);
    }

    @Test
    public void testProcessCharacter() {
        Token characterToken = new Token.Character('H');
        new XmlTreeBuilder().insert(characterToken);
        TextNode textNode = (TextNode) new XmlTreeBuilder().doc.childNodes().get(0);
        assertEquals("H", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        Token doctypeToken = new Token.Doctype("doctype", "public", "public.dtd", "");
        DocumentType documentTypeNode = new XmlTreeBuilder().insert(doctypeToken);
        assertEquals("doctype", documentTypeNode.name());
        assertEquals("public", documentTypeNode.publicId());
        assertEquals("public.dtd", documentTypeNode.systemId());
        assertEquals("", documentTypeNode.internalSubset());
    }
}