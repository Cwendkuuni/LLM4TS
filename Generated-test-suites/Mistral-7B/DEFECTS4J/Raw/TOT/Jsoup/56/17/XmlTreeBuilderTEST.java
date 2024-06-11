import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.parser.BaseParser;
import org.jsoup.parser.tokenizer.Token;
import org.jsoup.parser.tokenizer.Tokens;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlDocument() {
        String xmlInput = "<root><element attr='value'><subElement/></element></root>";
        XmlTreeBuilder xmlParser = new XmlTreeBuilder();

        Document xmlDoc = xmlParser.parse(xmlInput, "baseUrl");

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.nodeName());
        List<Node> rootChildren = root.childNodes();
        assertEquals(1, rootChildren.size());
        Element element = (Element) rootChildren.get(0);
        assertEquals("element", element.nodeName());
        List<Node> elementChildren = element.childNodes();
        assertEquals(1, elementChildren.size());
        assertTrue(elementChildren.get(0) instanceof Element);
        Element subElement = (Element) elementChildren.get(0);
        assertTrue(subElement.nodeName().isEmpty());
    }

    @Test
    public void testParseInvalidXmlDocument() {
        String xmlInput = "<root><element attr='value'><subElement>"; // missing closing tag
        XmlTreeBuilder xmlParser = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();

        Document xmlDoc = xmlParser.parse(xmlInput, "baseUrl");

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.line());
        assertEquals(1, error.column());
        assertEquals("root.line:1: char 39: Element 'subElement' is not closed", error.getMessage());
    }

    @Test
    public void testParseXmlDocumentWithComments() {
        String xmlInput = "<root><!--comment-->\n<element attr='value'><subElement/><!--comment--></element>\n</root>";
        XmlTreeBuilder xmlParser = new XmlTreeBuilder();

        Document xmlDoc = xmlParser.parse(xmlInput, "baseUrl");

        Element root = xmlDoc.rootElement();
        List<Node> rootChildren = root.childNodes();
        assertEquals(1, rootChildren.size());
        Element element = (Element) rootChildren.get(0);
        List<Node> elementChildren = element.childNodes();
        assertEquals(1, elementChildren.size());
        assertTrue(elementChildren.get(0) instanceof Element);
        Element subElement = (Element) elementChildren.get(0);
        assertTrue(subElement.nodeName().isEmpty());

        List<Node> comments = root.childNodes();
        assertEquals(2, comments.size());
        Node comment1 = comments.get(0);
        assertTrue(comment1 instanceof Comment);
        assertEquals("comment", comment1.nodeName());
        Node comment2 = comments.get(1);
        assertTrue(comment2 instanceof Comment);
        assertEquals("comment", comment2.nodeName());
    }

    @Test
    public void testParseXmlDocumentWithDoctype() {
        String xmlInput = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]>\n<root><element attr='value'><subElement/></element></root>";
        XmlTreeBuilder xmlParser = new XmlTreeBuilder();

        Document xmlDoc = xmlParser.parse(xmlInput, "baseUrl");

        Element root = xmlDoc.rootElement();
        assertEquals("root", root.nodeName());
        List<Node> rootChildren = root.childNodes();
        assertEquals(1, rootChildren.size());
        Element element = (Element) rootChildren.get(0);
        assertEquals("element", element.nodeName());
        List<Node> elementChildren = element.childNodes();
        assertEquals(1, elementChildren.size());
        assertTrue(elementChildren.get(0) instanceof Element);
        Element subElement = (Element) elementChildren.get(0);
        assertTrue(subElement.nodeName().isEmpty());

        DocumentType doctype = root.childNodes().get(0);
        assertEquals("root", doctype.nodeName());
        assertEquals("root", doctype.localName());
        assertEquals("[ <!ATTLIST element attr CDATA #REQUIRED> ]", doctype.text());
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<element attr='value'><subElement/></element>";
        XmlTreeBuilder xmlParser = new XmlTreeBuilder();

        List<Node> fragment = xmlParser.parseFragment(xmlFragment, "baseUrl", new ParseErrorList(), XmlTreeBuilder.ParseSettings.preserveCase);

        Element element = (Element) fragment.get(0);
        assertEquals("element", element.nodeName());
        List<Node> elementChildren = element.childNodes();
        assertEquals(1, elementChildren.size());
        assertTrue(elementChildren.get(0) instanceof Element);
        Element subElement = (Element) elementChildren.get(0);
        assertTrue(subElement.nodeName().isEmpty());
    }
}