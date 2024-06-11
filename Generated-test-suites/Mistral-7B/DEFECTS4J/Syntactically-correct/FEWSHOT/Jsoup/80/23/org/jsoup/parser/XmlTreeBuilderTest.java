package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><element attr='value'/><element2 attr='value2'/></root>";
        String baseUri = "https://example.com/";

        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertNotNull(xmlTreeBuilder.doc);
        assertEquals("root", xmlTreeBuilder.doc.tagName());
        assertEquals(2, xmlTreeBuilder.doc.childNodeSize());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><element attr='value'/><element2 attr='value2'/></root>";

        Token token = new Token(Token.StartTag.class, "<root>", null);
        xmlTreeBuilder.process(token);

        Element root = (Element) xmlTreeBuilder.stack.get(0);
        assertEquals("root", root.tagName());
        assertEquals("https://example.com/", root.baseUri());

        token = new Token(Token.StartTag.class, "<element attr='value'>", null);
        xmlTreeBuilder.process(token);

        Element element = (Element) xmlTreeBuilder.stack.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals(0, element.childNodeSize());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><element><element2/></element></root>";

        Token token = new Token(Token.StartTag.class, "<root>", null);
        xmlTreeBuilder.process(token);

        Element root = (Element) xmlTreeBuilder.stack.get(0);
        assertEquals("root", root.tagName());
        assertEquals("https://example.com/", root.baseUri());

        token = new Token(Token.StartTag.class, "<element>", null);
        xmlTreeBuilder.process(token);

        Element element = (Element) xmlTreeBuilder.stack.get(0);
        assertEquals("element", element.tagName());
        assertEquals("", element.baseUri());

        token = new Token(Token.EndTag.class, "</element>", null);
        xmlTreeBuilder.process(token);

        assertNull(xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><!-- comment --></root>";

        Token token = new Token(Token.Comment.class, "<!-- comment -->", null);
        xmlTreeBuilder.process(token);

        Comment comment = (Comment) xmlTreeBuilder.stack.get(0);
        assertEquals("comment", comment.data());
        assertEquals(0, comment.childNodeSize());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root>&#x20ac;</root>";

        Token token = new Token(Token.Character.class, "&#x20ac;", null);
        xmlTreeBuilder.process(token);

        TextNode textNode = (TextNode) xmlTreeBuilder.stack.get(0);
        assertEquals("\u00A3", textNode.data());
        assertEquals(0, textNode.childNodeSize());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!DOCTYPE root SYSTEM \"root.dtd\">";

        Token token = new Token(Token.Doctype.class, "<!DOCTYPE root SYSTEM \"root.dtd\">", null);
        xmlTreeBuilder.process(token);

        DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.get(0);
        assertEquals("root", documentType.nodeName());
        assertEquals("root.dtd", documentType.systemId());
        assertEquals(null, documentType.publicId());
        assertEquals(null, documentType.internalSubset());
        assertNull(documentType.isStandalone());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Element root = new Element("root", "", ParseSettings.preserveCase);
        Element element1 = new Element("element1", "", ParseSettings.preserveCase);
        Element element2 = new Element("element2", "", ParseSettings.preserveCase);
        root.appendChild(element1);
        root.appendChild(element2);
        xmlTreeBuilder.stack.add(root);

        xmlTreeBuilder.popStackToClose(new Token.EndTag("element1"));

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(element2, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><element attr='value'/><element2 attr='value2'/></root>";

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(2, nodes.size());
        Node root = nodes.get(0);
        assertEquals("root", root.tagName());
        assertEquals(2, root.childNodeSize());

        Node element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals(0, element.childNodeSize());

        Node element2 = root.child(1);
        assertEquals("element2", element2.tagName());
        assertEquals("value2", element2.attribute("attr"));
        assertEquals(0, element2.childNodeSize());
    }

    @Test
    public void testParseFragmentWithEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        List<Node> nodes = xmlTreeBuilder.parseFragment("", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(0, nodes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInvalidTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Token token = new Token(1000, "", null);
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testParseFragmentWithEmptyStack() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        List<Node> nodes = xmlTreeBuilder.parseFragment("", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(0, nodes.size());
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragmentWithError() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<root><!", "", errors, ParseSettings.preserveCase);

        assertEquals(0, nodes.size());
        assertEquals(1, errors.size());
        assertEquals("<!", errors.get(0).toString());
    }

}