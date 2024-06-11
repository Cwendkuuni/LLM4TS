package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testInsertElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.insertNode(root);
        Element child1 = new Element("child1");
        builder.insertNode(child1);
        Element child2 = new Element("child2");
        builder.insertNode(child2);
        root.appendChild(child1);
        root.appendChild(child2);
        assertEquals(2, root.childNodes().size());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Comment comment = new Comment("This is a comment");
        builder.insert(comment);
        Element root = new Element("root");
        builder.insertNode(root);
        root.appendChild(comment);
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testInsertCDataNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String cdata = "This is a CDATA section";
        CDataNode cdataNode = new CDataNode(cdata);
        builder.insert(new Token.Character(Token.Character.TYPE_CDATA, cdata));
        Element root = new Element("root");
        builder.insertNode(root);
        root.appendChild(cdataNode);
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testInsertTextNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String text = "This is a text node";
        TextNode textNode = new TextNode(text);
        builder.insert(new Token.Character(Token.Character.TYPE_TEXT, text));
        Element root = new Element("root");
        builder.insertNode(root);
        root.appendChild(textNode);
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testInsertDocumentType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String doctypeName = "html";
        DocumentType documentType = new DocumentType(doctypeName, null, null);
        builder.insert(new Token.Doctype(doctypeName, null, null));
        Element root = new Element("root");
        builder.insertNode(root);
        root.appendChild(documentType);
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testInsertDoctypeWithPublicIdAndSystemId() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String doctypeName = "html";
        String publicId = "public id";
        String systemId = "system id";
        DocumentType documentType = new DocumentType(doctypeName, publicId, systemId);
        builder.insert(new Token.Doctype(doctypeName, publicId, systemId));
        Element root = new Element("root");
        builder.insertNode(root);
        root.appendChild(documentType);
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("tag", null, null, null, null, null, null, null, null);
        Element root = new Element("root");
        builder.insertNode(root);
        builder.process(startTag);
        Element child = builder.insert(startTag);
        root.appendChild(child);
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.insertNode(root);
        Element child1 = new Element("child1");
        builder.insertNode(child1);
        root.appendChild(child1);
        Token endTag = new Token.EndTag("child1");
        builder.process(endTag);
        assertEquals(0, child1.childNodes().size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.insertNode(root);
        Token comment = new Token.Comment("This is a comment");
        builder.process(comment);
        Comment commentNode = (Comment) builder.insert(comment);
        root.appendChild(commentNode);
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.insertNode(root);
        Token character = new Token.Character("text");
        builder.process(character);
        TextNode textNode = (TextNode) builder.insert(character);
        root.appendChild(textNode);
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testProcessEOF() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.insertNode(root);
        builder.process(new Token.EOF());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseFragmentSingleElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<element></element>";
        List<Node> nodes = builder.parseFragment(inputFragment, null, null, new Parser(builder));
        assertEquals(1, nodes.size());
        assertEquals("element", nodes.get(0).nodeName());
    }

    @Test
    public void testParseFragmentMultipleElements() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<element1><element2></element2></element1>";
        List<Node> nodes = builder.parseFragment(inputFragment, null, null, new Parser(builder));
        assertEquals(2, nodes.size());
        assertEquals("element1", nodes.get(0).nodeName());
        assertEquals("element2", nodes.get(1).nodeName());
    }

    @Test
    public void testParseFragmentWithEmptyElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<element/>";
        List<Node> nodes = builder.parseFragment(inputFragment, null, null, new Parser(builder));
        assertEquals(1, nodes.size());
        assertEquals("element", nodes.get(0).nodeName());
        assertNull(nodes.get(0).childNodes());
    }

    @Test
    public void testParseFragmentWithSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<image src=\"image.jpg\" />";
        List<Node> nodes = builder.parseFragment(inputFragment, null, null, new Parser(builder));
        assertEquals(1, nodes.size());
        Element imageElement = (Element) nodes.get(0);
        assertEquals("image", imageElement.nodeName());
        assertNull(imageElement.childNodes());
    }

    @Test
    public void testParseFragmentWithAttribute() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag attribute=\"value\"></tag>";
        List<Node> nodes = builder.parseFragment(inputFragment, null, null, new Parser(builder));
        assertEquals(1, nodes.size());
        Element tagElement = (Element) nodes.get(0);
        assertEquals("tag", tagElement.nodeName());
        List<Attribute> attributes = tagElement.attributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get(0);
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testParseFragmentWithCDataSection() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag><![CDATA[This is a CDATA section]]></tag>";
        List<Node> nodes = builder.parseFragment(inputFragment, null, null, new Parser(builder));
        assertEquals(1, nodes.size());
        Element tagElement = (Element) nodes.get(0);
        assertEquals("tag", tagElement.nodeName());
        List<Node> cdataNodes = tagElement.childNodes();
        assertEquals(1, cdataNodes.size());
        CDataNode cdataNode = (CDataNode) cdataNodes.get(0);
        assertEquals("This is a CDATA section", cdataNode.text());
    }
}