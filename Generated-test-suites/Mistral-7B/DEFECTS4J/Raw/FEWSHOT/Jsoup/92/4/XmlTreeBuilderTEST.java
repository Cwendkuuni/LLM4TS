package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(""), "baseUri");
        assertTrue(xmlDoc.body() == null);
    }

    @Test
    public void testParseXmlWithRootElement() {
        String xmlString = "<root></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "baseUri");
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertTrue(root.nodeName().equals("root"));
        assertTrue(root.childNodes().size() == 0);
    }

    @Test
    public void testParseXmlWithElementAndAttributes() {
        String xmlString = "<element attr1='value1' attr2='value2'>content</element>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "baseUri");
        Element element = xmlDoc.selectElement("element");
        assertNotNull(element);
        assertTrue(element.nodeName().equals("element"));
        assertTrue(element.attr("attr1").equals("value1"));
        assertTrue(element.attr("attr2").equals("value2"));
        assertTrue(element.childNodes().size() == 1);
        assertTrue(element.text().equals("content"));
    }

    @Test
    public void testParseXmlWithTextNode() {
        String xmlString = "<root>content</root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "baseUri");
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertTrue(root.nodeName().equals("root"));
        assertTrue(root.childNodes().size() == 1);
        assertTrue(root.childNodes().get(0) instanceof TextNode);
        assertTrue(root.childNodes().get(0).text().equals("content"));
    }

    @Test
    public void testParseXmlWithComment() {
        String xmlString = "<root><!-- comment --></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "baseUri");
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertTrue(root.nodeName().equals("root"));
        List<Node> commentNodes = root.childNodes().stream().filter(node -> node instanceof Comment).collect(Collectors.toList());
        assertTrue(commentNodes.size() == 1);
        assertTrue(commentNodes.get(0).text().equals(" comment "));
    }

    @Test
    public void testParseXmlWithCDataSection() {
        String xmlString = "<root><![CDATA[content]]></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "baseUri");
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertTrue(root.nodeName().equals("root"));
        List<Node> cdataNodes = root.childNodes().stream().filter(node -> node instanceof CDataNode).collect(Collectors.toList());
        assertTrue(cdataNodes.size() == 1);
        assertTrue(cdataNodes.get(0).text().equals("content"));
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xmlString = "<root><tag /></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "baseUri");
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertTrue(root.nodeName().equals("root"));
        List<Element> tagElements = root.getElementsByTag("tag");
        assertTrue(tagElements.size() == 1);
        assertTrue(tagElements.get(0).nodeName().equals("tag"));
        assertTrue(tagElements.get(0).childNodes().size() == 0);
    }

    @Test
    public void testParseXmlWithNestedElements() {
        String xmlString = "<root><element><subelement>content</subelement></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "baseUri");
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertTrue(root.nodeName().equals("root"));
        List<Element> elementNodes = root.getElementsByTag("element");
        assertTrue(elementNodes.size() == 1);
        Element element = elementNodes.get(0);
        assertTrue(element.nodeName().equals("element"));
        List<Element> subelementNodes = element.getElementsByTag("subelement");
        assertTrue(subelementNodes.size() == 1);
        Element subelement = subelementNodes.get(0);
        assertTrue(subelement.nodeName().equals("subelement"));
        assertTrue(subelement.childNodes().size() == 1);
        assertTrue(subelement.childNodes().get(0) instanceof TextNode);
        assertTrue(subelement.childNodes().get(0).text().equals("content"));
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"about:blank\">\n<root></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "baseUri");
        DocumentType doctype = xmlDoc.doctype();
        assertNotNull(doctype);
        assertTrue(doctype.nodeName().equals("root"));
        assertTrue(doctype.system() != null && doctype.system().equals("about:blank"));
    }

    @Test
    public void testParseXmlWithXmlDeclaration() {
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "baseUri");
        XmlDeclaration xmlDeclaration = xmlDoc.xmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertTrue(xmlDeclaration != null && xmlDeclaration.data().equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
    }

    @Test
    public void testParseXmlWithFragment() {
        String xmlFragment = "<element><subelement>content</subelement></element>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, null, null, null);
        Element element = (Element) nodes.get(0);
        assertNotNull(element);
        assertTrue(element.nodeName().equals("element"));
        List<Element> subelementNodes = element.getElementsByTag("subelement");
        assertTrue(subelementNodes.size() == 1);
        Element subelement = subelementNodes.get(0);
        assertTrue(subelement.nodeName().equals("subelement"));
        assertTrue(subelement.childNodes().size() == 1);
        assertTrue(subelement.childNodes().get(0) instanceof TextNode);
        assertTrue(subelement.childNodes().get(0).text().equals("content"));
    }
}