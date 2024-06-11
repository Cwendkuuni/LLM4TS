package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr1=\"value1\">Content</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
        assertNotNull(doc);
        Element rootElement = doc.getRootElement();
        assertNotNull(rootElement);
        assertEquals("root", rootElement.tagName());
        List<Node> children = rootElement.children();
        assertNotNull(children);
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals("value1", element.attribute("attr1"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr1=\"value1\">Content</element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0).childNodes();
        Element element = (Element) nodes.get(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals("value1", element.attribute("attr1"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testProcessStartTag() {
        String xml = "<root><element attr1=\"value1\"></element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(new StringReader(xml), "http://example.com");
        Element rootElement = builder.doc.getRootElement();
        Element element = rootElement.children().get(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertNull(element.attributes().getNamed("attr2"));
        assertEquals("value1", element.attributes().get("attr1"));
        assertEquals(0, element.children().size());
    }

    @Test
    public void testProcessEndTag() {
        String xml = "<root><element><subelement></subelement></element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(new StringReader(xml), "http://example.com");
        Element rootElement = builder.doc.getRootElement();
        Element element = rootElement.children().get(0);
        assertEquals(1, element.children().size());
        builder.popStackToClose(new Token.EndTag(Tag.valueOf("subelement", ParseSettings.preserveCase).toString()));
        assertEquals(0, element.children().size());
    }

    @Test
    public void testProcessComment() {
        String xml = "<!-- This is a comment --><root><element><!-- Another comment --></element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(new StringReader(xml), "http://example.com");
        List<Node> rootChildren = builder.doc.childNodes();
        assertEquals(1, rootChildren.size());
        Element rootElement = (Element) rootChildren.get(0);
        List<Node> elementChildren = rootElement.children();
        assertEquals(1, elementChildren.size());
        Element element = (Element) elementChildren.get(0);
        List<Node> commentNodes = element.childNodes();
        assertEquals(2, commentNodes.size());
        assertEquals(Node.Element.COMMENT_TYPE, commentNodes.get(0).nodeType());
        assertEquals("This is a comment", commentNodes.get(0).text());
        assertEquals(Node.Element.ELEMENT_TYPE, commentNodes.get(1).nodeType());
        assertEquals("element", commentNodes.get(1).nodeName());
    }

    @Test
    public void testProcessCharacter() {
        String xml = "<root>&lt;Hello&gt; World!</root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(new StringReader(xml), "http://example.com");
        List<Node> rootChildren = builder.doc.childNodes();
        assertEquals(1, rootChildren.size());
        Element rootElement = (Element) rootChildren.get(0);
        List<Node> elementChildren = rootElement.children();
        assertEquals(1, elementChildren.size());
        TextNode textNode = (TextNode) elementChildren.get(0);
        assertEquals("Hello World!", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        String xml = "<!DOCTYPE root SYSTEM \"root.dtd\"><root></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(new StringReader(xml), "http://example.com");
        DocumentType documentType
                = (DocumentType) builder.doc.childNodes().get(0);
        assertEquals("root", documentType.name());
        assertEquals("root.dtd", documentType.publicId());
        assertEquals(null, documentType.systemId());
        assertEquals(null, documentType.getPubSysKey());
    }
}