import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        Document document = xmlTreeBuilder.parse("<>", "", parser);
        assertEquals(0, document.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        Document document = xmlTreeBuilder.parse("<root><child>Hello World</child></root>", "", parser);
        Element root = (Element) document.child(0);
        Element child = (Element) root.child(0);
        assertEquals("Hello World", child.text().toString());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        Document document = xmlTreeBuilder.parse("<root><img src='image.jpg'/></root>", "", parser);
        Element root = (Element) document.child(0);
        assertTrue(root.childNodes().size() > 0);
        assertTrue(root.child(0) instanceof Element);
        Element img = (Element) root.child(0);
        assertTrue(img.tagName().equals("img"));
        assertTrue(img.attributes().hasKey("src"));
        assertEquals("image.jpg", img.attr("src"));
    }

    @Test
    public void testParseXmlWithComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        Document document = xmlTreeBuilder.parse("<root><!-- This is a comment --><child>Hello World</child></root>", "", parser);
        assertTrue(document.childNodes().size() > 1);
        Element root = (Element) document.child(0);
        Element child = (Element) root.child(0);
        Element comment = (Comment) document.child(1);
        assertEquals("This is a comment", comment.data());
        assertEquals("Hello World", child.text().toString());
    }

    @Test
    public void testParseXmlWithCDataSection() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        Document document = xmlTreeBuilder.parse("<root><![CDATA[This is a CDATA section]]><child>Hello World</child></root>", "", parser);
        assertTrue(document.childNodes().size() > 1);
        Element root = (Element) document.child(0);
        Element child = (Element) root.child(1);
        CDataNode cDataNode = (CDataNode) document.child(0);
        assertEquals("This is a CDATA section", cDataNode.text().toString());
        assertEquals("Hello World", child.text().toString());
    }

    @Test
    public void testParseXmlWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        Document document = xmlTreeBuilder.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                "<html><body><p>Hello World</p></body></html>", "", parser);
        DocumentType doctypeNode = (DocumentType) document.child(0);
        assertEquals("html", doctypeNode.nodeName());
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctypeNode.publicId());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctypeNode.systemId());
        Element body = (Element) document.child(1);
        Element p = (Element) body.child(0);
        assertEquals("Hello World", p.text().toString());
    }

    @Test
    public void testParseXmlWithInvalidTagName() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        try {
            xmlTreeBuilder.parse("<root><invalidTag></root>", "", parser);
            fail("Expected ParseException to be thrown");
        } catch (org.jsoup.ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseXmlWithInvalidAttributeName() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        try {
            xmlTreeBuilder.parse("<root <invalidAttribute='foo' /></root>", "", parser);
            fail("Expected ParseException to be thrown");
        } catch (org.jsoup.ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseXmlWithInvalidAttributeValue() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        try {
            xmlTreeBuilder.parse("<root <invalidAttribute='foo bar' /></root>", "", parser);
            fail("Expected ParseException to be thrown");
        } catch (org.jsoup.ParseException e) {
            // Expected
        }
    }
}