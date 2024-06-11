package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.CDataSection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.DocumentType;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr=\"value\">Hello World</element></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "http://example.com");

        assertEquals("root", doc.getDocumentElement().getNodeName());
        assertEquals(1, doc.getDocumentElement().getChildNodes().getLength());

        Element root = doc.getDocumentElement();
        Element element = (Element) root.getChildNodes().item(0);
        assertEquals("element", element.getNodeName());
        assertEquals("value", element.getAttribute("attr"));
        assertEquals("Hello World", element.getTextContent());
    }

    @Test
    public void testParseWithComment() {
        String xml = "<root><!-- This is a comment --><element attr=\"value\">Hello World</element></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "http://example.com");

        assertEquals(2, doc.getDocumentElement().getChildNodes().getLength());

        Element root = doc.getDocumentElement();
        Element element = (Element) root.getChildNodes().item(1);
        assertEquals("element", element.getNodeName());
        assertEquals("value", element.getAttribute("attr"));
        assertEquals("Hello World", element.getTextContent());

        Comment comment = (Comment) root.getChildNodes().item(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testParseWithCDataSection() {
        String xml = "<root><![CDATA[ <element attr=\"value\">Hello World</element> ]]></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "http://example.com");

        assertEquals(1, doc.getDocumentElement().getChildNodes().getLength());

        Element root = doc.getDocumentElement();
        Element element = (Element) root.getChildNodes().item(0);
        assertEquals("element", element.getNodeName());
        assertEquals("value", element.getAttribute("attr"));
        assertEquals("<element attr=\"value\">Hello World</element>", element.getTextContent());

        Node cdataNode = element.getFirstChild();
        assertTrue(cdataNode instanceof CDataSection);
    }

    @Test
    public void testParseWithNoRootElement() {
        String xml = "<element attr=\"value\">Hello World</element>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "http://example.com");

        assertEquals("element", doc.getDocumentElement().getNodeName());
        assertEquals("value", doc.getDocumentElement().getAttribute("attr"));
        assertEquals("Hello World", doc.getDocumentElement().getTextContent());
    }

    @Test
    public void testParseWithEmptyDocument() {
        String xml = "<root/>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "http://example.com");

        assertEquals("root", doc.getDocumentElement().getNodeName());
        assertFalse(doc.getDocumentElement().hasChildNodes());
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String xml = "<root><element/></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "http://example.com");

        assertEquals("root", doc.getDocumentElement().getNodeName());
        assertEquals(1, doc.getDocumentElement().getChildNodes().getLength());

        Element root = doc.getDocumentElement();
        Element element = (Element) root.getChildNodes().item(0);
        assertEquals("element", element.getNodeName());
        assertTrue(element.isSelfClosing());
        assertFalse(element.hasChildNodes());
    }

    @Test
    public void testParseWithDoctype() {
        String xml = "<!DOCTYPE root SYSTEM \"http://example.com/root.dtd\">\n<root><element attr=\"value\">Hello World</element></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "http://example.com");

        DocumentType doctype = doc.getDoctype();
        assertEquals("root", doctype.getName());
        assertEquals("http://example.com/root.dtd", doctype.getPublicSystemIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertNull(doctype.getInternalSubset());

        Element root = doc.getDocumentElement();
        assertEquals(1, root.getChildNodes().getLength());

        Element element = (Element) root.getChildNodes().item(0);
        assertEquals("element", element.getNodeName());
        assertEquals("value", element.getAttribute("attr"));
        assertEquals("Hello World", element.getTextContent());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"value\">Hello World</element></root>";
        NodeList nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).getChildNodes();

        assertEquals(1, nodes.getLength());
        assertEquals("root", nodes.item(0).getNodeName());
        assertEquals(1, nodes.item(0).getChildNodes().getLength());

        Element root = (Element) nodes.item(0);
        Element element = (Element) root.getChildNodes().item(0);
        assertEquals("element", element.getNodeName());
        assertEquals("value", element.getAttribute("attr"));
        assertEquals("Hello World", element.getTextContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullInput() {
        new XmlTreeBuilder().parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        new XmlTreeBuilder().parse(new StringReader(""), null);
    }

    @Test
    public void testProcessStartTag() {
        String xml = "<root><element attr=\"value\">Hello World</element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = builder.tokenize(xml);

        Element root = builder.insert(token.nextToken());
        assertEquals("root", root.getNodeName());

        token = token.nextToken();
        Element element = builder.insert(token.asStartTag());
        assertEquals("element", element.getNodeName());
        assertEquals("value", element.getAttribute("attr"));
        assertSame(root, element.getParentNode());
    }

    @Test
    public void testProcessEndTag() {
        String xml = "<root><element/></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = builder.tokenize(xml);

        Element root = builder.insert(token.nextToken());
        Element element = builder.insert(token.nextToken().asStartTag());
        assertEquals("element", element.getNodeName());
        assertTrue(element.isSelfClosing());
        assertSame(root, element.getParentNode());

        token = token.nextToken();
        builder.popStackToClose(token.asEndTag());
        assertTrue(root.getChildNodes().item(0).getNextSibling() instanceof Text);
    }

    @Test
    public void testProcessComment() {
        String xml = "<root><!-- This is a comment --><element attr=\"value\">Hello World</element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = builder.tokenize(xml);

        element(builder, token.nextToken(), "root");
        comment(builder, token.nextToken());
        element(builder, token.nextToken().asStartTag(), "element");
        text(builder, token.nextToken(), "Hello World");
        element(builder, token.nextToken().asEndTag(), "element");

        Node root = builder.doc.getDocumentElement();
        assertEquals(1, root.getChildNodes().getLength());
        assertEquals("element", root.getChildNodes().item(0).getNodeName());
        assertEquals("value", root.getChildNodes().item(0).getAttributes().getNamedItem("attr").getNodeValue());
        assertEquals("This is a comment", root.getChildNodes().item(1).getData());
    }

    @Test
    public void testProcessCharacterData() {
        String xml = "<root><![CDATA[ <element attr=\"value\">Hello World</element> ]]></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = builder.tokenize(xml);

        token = token.nextToken();
        cdata(builder, token);

        Node root = builder.doc.getDocumentElement();
        assertEquals(1, root.getChildNodes().getLength());
        assertEquals("element", root.getChildNodes().item(0).getNodeName());
        assertEquals("value", root.getChildNodes().item(0).getAttributes().getNamedItem("attr").getNodeValue());
        assertEquals("<element attr=\"value\">Hello World</element>", root.getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testProcessDoctype() {
        String xml = "<!DOCTYPE root SYSTEM \"http://example.com/root.dtd\">\n<root><element attr=\"value\">Hello World</element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = builder.tokenize(xml);

        docType(builder, token.nextToken());
        element(builder, token.nextToken().asStartTag(), "root");
        element(builder, token.nextToken().asEndTag(), "root");
        element(builder, token.nextToken().asStartTag(), "element");
        text(builder, token.nextToken(), "Hello World");
        element(builder, token.nextToken().asEndTag(), "element");

        Document doc = builder.doc;
        DocumentType doctype = doc.getDoctype();
        assertEquals("root", doctype.getName());
        assertEquals("http://example.com/root.dtd", doctype.getPublicSystemIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertNull(doctype.getInternalSubset());
        assertEquals(1, doc.getDocumentElement().getChildNodes().getLength());
        assertEquals("element", doc.getDocumentElement().getChildNodes().item(0).getNodeName());
        assertEquals("value", doc.getDocumentElement().getChildNodes().item(0).getAttributes().getNamedItem("attr").getNodeValue());
        assertEquals("Hello World", doc.getDocumentElement().getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testPopStackToClose() {
        String xml = "<root><element/><element/></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = builder.tokenize(xml);

        Element root = builder.insert(token.nextToken());
        Element element1 = builder.insert(token.nextToken().asStartTag());
        assertEquals("element", element1.getNodeName());
        assertTrue(element1.isSelfClosing());
        assertSame(root, element1.getParentNode());

        Element element2 = builder.insert(token.nextToken().asStartTag());
        assertEquals("element", element2.getNodeName());
        assertFalse(element2.isSelfClosing());
        assertSame(root, element2.getParentNode());

        builder.popStackToClose(token.nextToken().asEndTag());
        assertTrue(root.getChildNodes().item(0).getNextSibling() instanceof Text);
        assertTrue(root.getChildNodes().item(1).getNextSibling() instanceof Text);
    }

    private void text(XmlTreeBuilder builder, Token token, String expectedText) {
        Text textNode = (Text) builder.insert(token.asCharacter());
        assertEquals(expectedText, textNode.getData());
    }

    private void comment(XmlTreeBuilder builder, Token token) {
        Comment commentNode = (Comment) builder.insert(token.asComment());
        assertEquals(token.getData(), commentNode.getData());
    }

    private void cdata(XmlTreeBuilder builder, Token token) {
        CDataSection cdataNode = (CDataSection) builder.insert(token);
        assertEquals(token.getData(), cdataNode.getData());
    }

    private void docType(XmlTreeBuilder builder, Token token) {
        DocumentType doctypeNode = (DocumentType) builder.insert(token.asDoctype());
        assertEquals(token.getData(), doctypeNode.getName());
        assertNull(doctypeNode.getPublicIdentifier());
        assertNull(doctypeNode.getSystemIdentifier());
        assertNull(doctypeNode.getInternalSubset());
    }

    private void element(XmlTreeBuilder builder, Token token, String expectedTagName) {
        Tag tag = Tag.valueOf(token.name().toUpperCase(), builder.settings);
        Element element = builder.insert(token.asStartTag());
        assertEquals(expectedTagName, element.getNodeName());
        assertSame(tag, element.getTagName());
    }
}