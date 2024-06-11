import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = " ";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element>Hello World</element></root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        assertNotNull(doc);
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.nodeName());
        Text text = (Text) element.childNodes().get(0);
        assertEquals("Hello World", text.text());
    }

    @Test
    public void testParseXmlWithComments() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><!--comment1--><element>Hello World</element><!--comment2--></root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        assertNotNull(doc);
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.nodeName());
        Text text = (Text) element.childNodes().get(0);
        assertEquals("Hello World", text.text());
        List<Node> comments = root.childNodes();
        assertEquals(2, comments.size());
        assertTrue(comments.get(0) instanceof Comment);
        assertTrue(comments.get(1) instanceof Comment);
    }

    @Test
    public void testParseXmlWithCDataSection() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><![CDATA[<script>alert('Hello World');</script>]]><element>Hello World</element></root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        assertNotNull(doc);
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(2, children.size());
        Element element = (Element) children.get(1);
        assertEquals("element", element.nodeName());
        Text text = (Text) element.childNodes().get(0);
        assertEquals("Hello World", text.text());
        Node cdata = children.get(0);
        assertTrue(cdata instanceof CDataNode);
    }

    @Test
    public void testParseXmlWithInvalidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><!--comment-->Hello World</root>"; // comment should not be allowed in the element content
        List<ParseError> errors = new ArrayList<>();
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri", errors, XmlTreeBuilder.ParseSettings.preserveCase);
        assertNotNull(doc);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.Type.MISMATCHED_TAG, error.getType());
        assertEquals("Error parsing start tag : : comment. Expected :{START_TAG,START_TAG,ENTITY_REF,SCRIPT_PI,COMMENT,DOCTYPE,CDATA,PI,EOF}, but found :COMMENT\n" +
                "at line 1 column 15", error.getMessage());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><img src=\"image.jpg\" />Hello World</root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        assertNotNull(doc);
        List<Node> nodes = doc.childNodes();
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        Element img = (Element) children.get(0);
        assertEquals("img", img.nodeName());
        assertNull(img.childNodes().size());
        assertEquals("Hello World", children.get(1).text());
    }

    @Test
    public void testParseXmlWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head><title>Test</title></head>" +
                "<body><p>Hello World</p></body></html>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        assertNotNull(doc);
        List<Node> nodes = doc.childNodes();
        assertEquals(3, nodes.size());
        DocumentType doctype = (DocumentType) nodes.get(0);
        assertEquals("html", doctype.getName());
        Document html = (Document) nodes.get(1);
        assertEquals(2, html.childNodes().size());
        Element head = (Element) html.childNodes().get(0);
        assertEquals("head", head.nodeName());
        List<Node> headChildren = head.childNodes();
        assertEquals(1, headChildren.size());
        Element title = (Element) headChildren.get(0);
        assertEquals("title", title.nodeName());
        Text titleText = (Text) title.childNodes().get(0);
        assertEquals("Test", titleText.text());
        Element body = (Element) html.childNodes().get(1);
        assertEquals("body", body.nodeName());
        List<Node> bodyChildren = body.childNodes();
        assertEquals(1, bodyChildren.size());
        Element p = (Element) bodyChildren.get(0);
        assertEquals("p", p.nodeName());
        Text pText = (Text) p.childNodes().get(0);
        assertEquals("Hello World", pText.text());
    }

    @Test
    public void testParseXmlWithSelfClosingTagAndDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head><title>Test</title></head>" +
                "<body><img src=\"image.jpg\" />Hello World</body></html>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        assertNotNull(doc);
        List<Node> nodes = doc.childNodes();
        assertEquals(3, nodes.size());
        DocumentType doctype = (DocumentType) nodes.get(0);
        assertEquals("html", doctype.getName());
        Document html = (Document) nodes.get(1);
        assertEquals(2, html.childNodes().size());
        Element head = (Element) html.childNodes().get(0);
        assertEquals("head", head.nodeName());
        List<Node> headChildren = head.childNodes();
        assertEquals(1, headChildren.size());
        Element title = (Element) headChildren.get(0);
        assertEquals("title", title.nodeName());
        Text titleText = (Text) title.childNodes().get(0);
        assertEquals("Test", titleText.text());
        Element body = (Element) html.childNodes().get(1);
        assertEquals("body", body.nodeName());
        List<Node> bodyChildren = body.childNodes();
        assertEquals(2, bodyChildren.size());
        Element img = (Element) bodyChildren.get(0);
        assertEquals("img", img.nodeName());
        assertNull(img.childNodes().size());
        Text imgText = (Text) bodyChildren.get(1).text();
        assertEquals("Hello World", imgText.text());
    }
}