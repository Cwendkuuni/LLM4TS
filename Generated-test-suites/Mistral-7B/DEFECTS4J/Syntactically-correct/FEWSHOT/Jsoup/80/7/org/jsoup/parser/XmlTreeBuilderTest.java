package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Xml.newComment;
import static org.jsoup.nodes.Xml.newComment;
import static org.jsoup.nodes.Xml.newComment;
import static org.jsoup.nodes.Xml.newComment;
import static org.jsoup.nodes.Xml.newCDataNode;
import static org.jsoup.nodes.Xml.newComment;
import static org.jsoup.nodes.Xml.newComment;
import static org.jsoup.nodes.Xml.newDocument;
import static org.jsoup.nodes.Xml.newElement;
import static org.jsoup.nodes.Xml.newTextNode;
import static org.jsoup.nodes.Xml.tag;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;

public class XmlTreeBuilderTest {

    @Test
    public void testParseWithDefaultSettings() {
        String xmlString = "<root><element attr=\"value\"/></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertEquals("root", xmlDoc.tagName());
        assertEquals(1, xmlDoc.childNodeSize());

        Element root = xmlDoc.child(0);
        assertEquals("element", root.tagName());
        assertEquals(1, root.childNodeSize());
        assertEquals("value", root.attribute(0).value());
    }

    @Test
    public void testParseWithCustomSettings() {
        String xmlString = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><element attr=\"value\" xsi:type=\"xsd:string\"/></root>";
        ParseSettings settings = new ParseSettings();
        settings.setNamespaceAware(true);

        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "", new ParseErrorList(), settings);

        assertEquals("root", xmlDoc.tagName());
        assertEquals(1, xmlDoc.childNodeSize());

        Element root = xmlDoc.child(0);
        assertEquals("element", root.tagName());
        assertEquals(1, root.childNodeSize());

        Element element = root.child(0);
        assertEquals("value", element.attribute(0).value());
        assertEquals("xsd:string", element.attribute(1).value());
    }

    @Test
    public void testProcessStartTag() {
        String xmlString = "<root><element attr=\"value\"/></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xmlString), "").doc;

        Token tagToken = doc.tokenize().get(0);
        assertEquals(XmlTreeBuilder.TokenType.StartTag, tagToken.type);

        Element element = builder.insert(tagToken.asStartTag());

        assertEquals("element", element.tagName());
        assertEquals("", element.baseUri());
        assertEquals(1, element.childNodeSize());
        assertEquals("value", element.attribute(0).value());
    }

    @Test
    public void testProcessEndTag() {
        String xmlString = "<root><element><subElement/></element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xmlString), "").doc;

        Token tagToken = doc.tokenize().get(doc.tokenize().size() - 1);
        assertEquals(XmlTreeBuilder.TokenType.EndTag, tagToken.type);

        Element parentElement = builder.stack.get(builder.stack.size() - 2);
        Element element = parentElement.child(0);

        builder.popStackToClose(tagToken.asEndTag());

        assertEquals(0, parentElement.childNodeSize());
    }

    @Test
    public void testProcessComment() {
        String xmlString = "<!-- This is a comment -->";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xmlString), "").doc;

        Token commentToken = doc.tokenize().get(0);
        assertEquals(XmlTreeBuilder.TokenType.Comment, commentToken.type);

        Comment comment = builder.insert(commentToken);

        assertEquals("", comment.baseUri());
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testProcessCharacter() {
        String xmlString = "<root>Text</root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xmlString), "").doc;

        Token characterToken = doc.tokenize().get(2);
        assertEquals(XmlTreeBuilder.TokenType.Character, characterToken.type);

        TextNode textNode = (TextNode) builder.insert(characterToken);

        assertEquals("", textNode.baseUri());
        assertEquals("Text", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xmlString), "").doc;

        Token doctypeToken = doc.tokenize().get(0);
        assertEquals(XmlTreeBuilder.TokenType.Doctype, doctypeToken.type);

        Doctype doctype = (Doctype) builder.insert(doctypeToken);

        assertEquals("root", doctype.name());
        assertEquals("", doctype.publicId());
        assertEquals("", doctype.systemId());
        assertEquals(1, doctype.entities().size());

        Attribute entityAttribute = doctype.entities().get(0);
        assertEquals("element", entityAttribute.name());
        assertEquals("#REQUIRED", entityAttribute.value());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"value\"/><subElement/></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();

        List<Node> nodes = builder.parseFragment(xmlFragment, "", new ParseErrorList(), ParseSettings.preserveCase);

        Document document = newDocument();
        document.appendChild(nodes.get(0));

        assertEquals(1, document.childNodeSize());
        assertEquals("root", document.tagName());

        Element root = document.child(0);
        assertEquals(2, root.childNodeSize());

        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute(0).value());

        Element subElement = root.child(1);
        assertEquals("subElement", subElement.tagName());
        assertEquals(0, subElement.childNodeSize());
    }
}