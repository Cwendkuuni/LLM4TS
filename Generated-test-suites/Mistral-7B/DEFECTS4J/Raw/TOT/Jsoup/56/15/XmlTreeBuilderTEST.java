import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseSimpleXml() throws IOException {
        String xmlInput = "<root><element1>Hello World</element1><element2>123</element2></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://test.com/");

        Element root = xmlDoc.rootElement();
        Element element1 = root.selectFirst("element1");
        Element element2 = root.selectFirst("element2");

        assertEquals("Hello World", element1.text());
        assertEquals("123", element2.text());
    }

    @Test
    public void testParseEmptyXml() throws IOException {
        String xmlInput = "<root></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://test.com/");

        Element root = xmlDoc.rootElement();
        assertEquals("", root.text());
    }

    @Test
    public void testParseXmlWithInvalidToken() throws IOException {
        String xmlInput = "<root><invalid>"; // missing > in tag
        try {
            Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://test.com/");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseXmlWithSelfClosingTag() throws IOException {
        String xmlInput = "<root><img src=\"image.jpg\" /> </root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://test.com/");

        Element root = xmlDoc.rootElement();
        List<Node> images = root.select("img");
        assertEquals(1, images.size());
        Element img = (Element) images.get(0);
        assertTrue(img.hasAttr("src"));
    }

    @Test
    public void testParseXmlWithComment() throws IOException {
        String xmlInput = "<root><!-- This is a comment --><element>Hello World</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://test.com/");

        Element root = xmlDoc.rootElement();
        Element element = root.selectFirst("element");
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseXmlWithDoctype() throws IOException {
        String xmlInput = "<!DOCTYPE root SYSTEM \"http://test.com/root.dtd\">\n<root></root>";
        Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://test.com/");

        DocumentType doctype = xmlDoc.doctype();
        assertEquals("root", doctype.name());
        assertEquals("http://test.com/root.dtd", doctype.systemIdentifier());
    }

    @Test
    public void testParseXmlWithInvalidTokenInComment() throws IOException {
        String xmlInput = "<root><!-- This is a <comment> --><element>Hello World</element></root>";
        try {
            Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://test.com/");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseXmlWithInvalidAttribute() throws IOException {
        String xmlInput = "<root><element attribute=\"invalid=\"></root>";
        try {
            Document xmlDoc = XmlTreeBuilder.parse(xmlInput, "http://test.com/");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseXmlFragment() throws IOException {
        String xmlFragment = "<root><element>Hello</element></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://test.com/", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();

        Element root = (Element) nodes.get(0);
        Element element = (Element) nodes.get(1);
        assertEquals("Hello", element.text());
    }

    @Test
    public void testParseXmlFragmentWithInvalidToken() throws IOException {
        String xmlFragment = "<root><!-- This is a comment --><element>Hello</element></root>";
        try {
            List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://test.com/", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
}