package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CharacterData;
import org.jsoup.parser.Token.Error;

import java.io.StringReader;
import java.util.List;
import java.util.Stack;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(""), null, new Parser(new XmlTreeBuilder()));
        assertNotNull(xmlDoc);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlDoc.outputSettings().syntax());
        assertEquals(0, xmlDoc.childNodes().size());
    }

    @Test
    public void testParseWithRootElement() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader("<root></root>"), null, new Parser(new XmlTreeBuilder()));
        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        assertNotNull(root.childNode(0));
        assertEquals(0, root.childNode(0).childNodes().size());
    }

    @Test
    public void testParseWithSelfClosingRootElement() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader("<root/>"), null, new Parser(new XmlTreeBuilder()));
        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseWithRootElementAndChildren() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader("<root><child1/><child2><grandchild/></child2></root>"), null, new Parser(new XmlTreeBuilder()));
        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(2, root.childNodes().size());

        Element child1 = (Element) root.childNode(0);
        assertEquals("child1", child1.tagName());
        assertEquals(0, child1.childNodes().size());

        Element child2 = (Element) root.childNode(1);
        assertEquals("child2", child2.tagName());
        assertEquals(1, child2.childNodes().size());

        Element grandchild = (Element) child2.childNode(0);
        assertEquals("grandchild", grandchild.tagName());
        assertEquals(0, grandchild.childNodes().size());
    }

    @Test
    public void testParseWithRootElementAndAttributes() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader("<root attr1=\"value1\" attr2='value2'/>"), null, new Parser(new XmlTreeBuilder()));
        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(2, root.attributes().size());

        Attribute attr1 = root.attr("attr1");
        assertEquals("value1", attr1.value());

        Attribute attr2 = root.attr("attr2");
        assertEquals("value2", attr2.value());
    }

    @Test
    public void testParseWithProcessingInstruction() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>"), null, new Parser(new XmlTreeBuilder()));
        XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlDoc.childNodes().get(0);
        assertEquals("xml", xmlDeclaration.nodeName());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.data());

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        assertNotNull(root.childNode(0));
        assertEquals(0, root.childNode(0).childNodes().size());
    }

    @Test
    public void testParseWithComment() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader("<!-- This is a comment -->\n<root></root>"), null, new Parser(new XmlTreeBuilder()));
        Comment comment = (Comment) xmlDoc.childNodes().get(0);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.text());

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        assertNotNull(root.childNode(0));
        assertEquals(0, root.childNode(0).childNodes().size());
    }

    @Test
    public void testParseWithCDataSection() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader("<root><![CDATA[This is a CData section]]></root>"), null, new Parser(new XmlTreeBuilder()));
        CDataNode cdata = (CDataNode) xmlDoc.rootElement().childNode(0);
        assertEquals("CDATA[", cdata.nodeName());
        assertEquals("This is a CData section]", cdata.text());

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        assertNotNull(root.childNode(0));
        assertEquals(0, root.childNode(0).childNodes().size());
    }

    @Test
    public void testParseWithError() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader("<root><!--<![CDATA[This is an error]]-->\n<child/>"), null, new Parser(new XmlTreeBuilder()));
        Error error = (Error) xmlDoc.childNodes().get(0);
        assertEquals("parser-error", error.nodeName());
        assertEquals("Error parsing XML: StartTag: invalid element name (antlr:2:13) [Error Id:100]", error.text());

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<child1><child2><grandchild/></child2></child1>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, null, new Parser(new XmlTreeBuilder()));

        assertEquals(1, nodes.size());
        Element child1 = (Element) nodes.get(0);
        assertEquals("child1", child1.tagName());
        assertEquals(1, child1.childNodes().size());

        Element child2 = (Element) child1.childNode(0);
        assertEquals("child2", child2.tagName());
        assertEquals(1, child2.childNodes().size());

        Element grandchild = (Element) child2.childNode(0);
        assertEquals("grandchild", grandchild.tagName());
        assertEquals(0, grandchild.childNodes().size());
    }

    @Test
    public void testParseFragmentWithContext() {
        String xmlFragment = "<child1><child2><grandchild/></child2></child1>";
        Element context = new Element("root");
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, context, null, new Parser(new XmlTreeBuilder()));

        assertEquals(1, nodes.size());
        Element child1 = (Element) nodes.get(0);
        assertEquals("child1", child1.tagName());
        assertEquals(1, child1.childNodes().size());

        Element child2 = (Element) child1.childNode(0);
        assertEquals("child2", child2.tagName());
        assertEquals(1, child2.childNodes().size());

        Element grandchild = (Element) child2.childNode(0);
        assertEquals("grandchild", grandchild.tagName());
        assertEquals(0, grandchild.childNodes().size());

        assertEquals(context, child1.parent());
    }
}