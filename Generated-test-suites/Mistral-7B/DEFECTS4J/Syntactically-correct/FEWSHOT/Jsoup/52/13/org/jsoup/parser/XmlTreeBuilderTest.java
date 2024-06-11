package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.xml.XmlDeclaration;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.CharacterToken;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;

    @Before
    public void setup() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testProcessStartTag() {
        String input = "<root><element attr='value'/></root>";
        Token token = builder.tokenise(input);
        StartTag startTag = token.asStartTag();
        Element element = builder.insert(startTag);
        assertEquals("root", element.parentNode().nodeName());
        assertEquals("root", element.nodeName());
        assertEquals("value", element.attribute(0).getValue());
        assertEquals("value", element.attribute(0).getName());
    }

    @Test
    public void testProcessEndTag() {
        String input = "<root><element/></root>";
        Token token = builder.tokenise(input)[1];
        EndTag endTag = token.asEndTag();
        builder.popStackToClose(endTag);
        Element element = (Element) builder.stack.get(builder.stack.size() - 1);
        assertSame(element, endTag.getParent());
    }

    @Test
    public void testProcessComment() {
        String input = "<!-- This is a comment -->";
        Token token = builder.tokenise(input)[0];
        Comment comment = builder.insert(token.asComment());
        assertEquals(comment.getData(), comment.text());
    }

    @Test
    public void testProcessCharacter() {
        String input = "<root>&#x03B3;</root>";
        Token token = builder.tokenise(input)[0];
        CharacterToken characterToken = token.asCharacter();
        TextNode textNode = builder.insert(token.asCharacter());
        assertEquals("α", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        String input = "<!DOCTYPE root SYSTEM \"about:blank\">";
        Token token = builder.tokenise(input)[0];
        Doctype doctype = token.asDoctype();
        DocumentType documentType = (DocumentType) builder.insert(token);
        assertEquals("root", documentType.nodeName());
        assertEquals("about:blank", documentType.systemId());
    }

    @Test
    public void testProcessSelfClosingTag() {
        String input = "<root><img src='image.png'/></root>";
        Token token = builder.tokenise(input)[1];
        StartTag startTag = token.asStartTag();
        startTag.setSelfClosing(true);
        Element img = builder.insert(startTag);
        assertEquals("img", img.nodeName());
        assertEquals(1, img.childNodes().size());
    }

    @Test(expected = AssertionError.class)
    public void testValidateUnexpectedTokenType() {
        String input = "<root>123</root>";
        Token token = builder.tokenise(input)[0];
        builder.process(token);
    }

    @Test
    public void testParseFragment() {
        String input = "<root><element attr='value'><subelement/></element></root>";
        List<Node> parsedNodes = builder.parseFragment(input, null, null);
        Node root = parsedNodes.get(0);
        assertEquals("root", root.nodeName());
        assertEquals(3, root.childNodes().size());
        Element element = (Element) root.childNodes().get(0);
        assertEquals("element", element.nodeName());
        assertEquals(1, element.childNodes().size());
        Element subelement = (Element) element.childNodes().get(0);
        assertEquals("subelement", subelement.nodeName());
        assertNull(subelement.childNodes());
    }

    @Test
    public void testParseFragmentWithXmlDeclaration() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element attr='value'/></root>";
        List<Node> parsedNodes = builder.parseFragment(input, null, null);
        Node root = parsedNodes.get(0);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        Element element = (Element) root.childNodes().get(0);
        assertEquals("element", element.nodeName());
        assertEquals(1, element.childNodes().size());
        Attribute xmlVersion = element.attribute(0);
        assertEquals("version", xmlVersion.getName());
        assertEquals("1.0", xmlVersion.getValue());
    }
}