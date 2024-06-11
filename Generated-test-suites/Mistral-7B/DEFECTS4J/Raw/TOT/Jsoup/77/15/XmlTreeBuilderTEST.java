import org.jsoup.Jsoup;
import org.jsoup.Node;
import org.jsoup.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");

        assertEquals("http://example.com", document.baseUri());
        assertEquals(XmlTreeBuilder.class.getSimpleName(), document.outputSettings().syntax().name());
        assertTrue(document.hasText());
        assertFalse(document.outputSettings().prettyPrint());
        assertFalse(document.outputSettings().trimOutputWhitespace());

        List<Node> children = document.childNodes();
        assertEquals(1, children.size());
        Node node = children.get(0);
        assertEquals(Node.DOCTYPE_NODE, node.nodeType());
        DocumentType doctype = (DocumentType) node;
        assertEquals("xml", doctype.nodeName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertNull(doctype.getPubSysKey());
    }

    @Test
    public void testParseSimpleXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element attr=\"value\">Content</element></root>";
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");

        Element root = document.getElementsByTagNames("root").get(0);
        Element element = root.getElementsByTagNames("element").get(0);
        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attributes().get("attr").get());
        assertEquals("Content", element.childNodes().get(0).text());

        List<Node> children = document.childNodes();
        assertEquals(1, children.size());
        assertEquals("root", children.get(0).nodeName());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element /></root>";
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");

        Element root = document.getElementsByTagNames("root").get(0);
        Element element = root.getElementsByTagNames("element").get(0);
        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertTrue(element.getNodeType() == Node.ELEMENT_NODE);
        assertFalse(element.hasTextContent());
        assertEquals(1, element.childNodes().size());
        assertEquals("", element.childNodes().get(0).nodeName());
        assertEquals("1.0", element.childNodes().get(0).attributes().get("xml:version").get());
    }

    @Test
    public void testParseXmlWithComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><!-- This is a comment --><element attr=\"value\">Content</element></root>";
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");

        Element root = document.getElementsByTagNames("root").get(0);
        Element element = root.getElementsByTagNames("element").get(0);
        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attributes().get("attr").get());
        assertEquals("Content", element.childNodes().get(0).text());

        List<Node> children = document.childNodes();
        assertEquals(2, children.size());
        Node comment = children.get(0);
        assertEquals(Node.COMMENT_NODE, comment.nodeType());
        assertEquals("This is a comment", comment.text());
    }

    @Test(expected = RuntimeException.class)
    public void testParseXmlWithUnexpectedToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><!-- This is a comment --><script type=\"text/javascript\">alert('Hello');</script>";
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");

        // This test case should fail with a RuntimeException since script tags are not supported in XML.
    }

    @Test
    public void testParseXmlFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root element=\"value\">Content</root>";

        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        assertEquals("value", root.attributes().get("element").get());
        assertEquals("Content", root.childNodes().get(0).text());
    }
}