package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("", "", new ParseErrorList());
        assertNotNull(builder.doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("root");
        Element el = builder.insert(startTag);
        assertEquals("root", el.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.stack.add(root);
        Token endTag = new Token.EndTag("root");
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("<!-- comment -->");
        builder.insert(commentToken);
        assertEquals(1, builder.doc.childNodes().size());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('a');
        builder.insert(characterToken);
        assertEquals("a", builder.doc.childNodes().get(0).nodeValue());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("doctype");
        builder.insert(doctypeToken);
        assertEquals(1, builder.doc.childNodes().size());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token unexpectedToken = new Token(Token.Type.UNKNOWN, "");
        builder.process(unexpectedToken);
    }

    @Test
    public void testParseFragmentEmpty() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("", "", new ParseErrorList());
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragmentSimple() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><child/></root>", "", new ParseErrorList());
        assertEquals(3, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        Text nodeText = (Text) nodes.get(2);
        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());
        assertEquals("", child.text().getWholeText());
        assertEquals("", nodeText.getWholeText());
    }

    @Test
    public void testParseFragmentSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><selfClosingTag/></root>", "", new ParseErrorList());
        assertEquals(4, nodes.size());
        Element root = (Element) nodes.get(0);
        Element selfClosingTag = (Element) nodes.get(1);
        Text nodeText = (Text) nodes.get(2);
        assertEquals("root", root.nodeName());
        assertEquals("selfClosingTag", selfClosingTag.nodeName());
        assertEquals("", selfClosingTag.text().getWholeText());
        assertEquals("", nodeText.getWholeText());
    }

    @Test
    public void testParseFragmentEmptyComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><!-- --></root>", "", new ParseErrorList());
        assertEquals(3, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
    }

    @Test
    public void testParseFragmentXmlDeclaration() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">",
                "", new ParseErrorList());
        assertEquals(5, nodes.size());
        Element root = (Element) nodes.get(0);
        DocumentType xmlDeclaration = (DocumentType) nodes.get(1);
        assertEquals("xml", xmlDeclaration.nodeName());
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlDeclaration.publicId());
        assertEquals("", xmlDeclaration.systemId());
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlDeclaration.internalSubset());
        assertEquals("root", root.nodeName());
    }

    @Test(expected = AssertionError.class)
    public void testParseFragmentInvalidXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><child><", "", new ParseErrorList());
    }
}