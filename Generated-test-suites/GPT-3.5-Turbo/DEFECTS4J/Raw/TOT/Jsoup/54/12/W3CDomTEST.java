import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup_NullInput_ThrowsException() {
        W3CDom converter = new W3CDom();

        try {
            converter.fromJsoup(null);
            fail("NullPointerException should be thrown for null input");
        } catch (NullPointerException e) {
            // Exception is expected
        }
    }

    @Test
    public void testFromJsoup_ValidInput_ReturnsW3CDocument() {
        W3CDom converter = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Hello World</h1></body></html>");

        Document w3cDoc = converter.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert_BlankLocation_UriNotSetInW3CDocument() {
        W3CDom converter = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("");

        Document w3cDoc = null;
        try {
            w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException should not be thrown");
        }

        converter.convert(jsoupDoc, w3cDoc);

        assertNull(w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_ValidLocation_UriSetInW3CDocument() {
        W3CDom converter = new W3CDom();
        String location = "http://example.com";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(location);

        Document w3cDoc = null;
        try {
            w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException should not be thrown");
        }

        converter.convert(jsoupDoc, w3cDoc);

        assertEquals(location, w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_ValidJsoupDocument_CreatesValidW3CDocument() {
        W3CDom converter = new W3CDom();
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        Document w3cDoc = null;
        try {
            w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException should not be thrown");
        }

        converter.convert(jsoupDoc, w3cDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());

        NodeList childNodes = rootElement.getChildNodes();
        assertEquals(2, childNodes.getLength());

        Element headElement = (Element) childNodes.item(0);
        assertNotNull(headElement);
        assertEquals("head", headElement.getTagName());

        Element bodyElement = (Element) childNodes.item(1);
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.getTagName());

        NodeList bodyChildren = bodyElement.getChildNodes();
        assertEquals(1, bodyChildren.getLength());

        Element h1Element = (Element) bodyChildren.item(0);
        assertNotNull(h1Element);
        assertEquals("h1", h1Element.getTagName());

        Text h1Text = (Text) h1Element.getChildNodes().item(0);
        assertNotNull(h1Text);
        assertEquals("Hello World", h1Text.getData());
    }

    @Test
    public void testAsString_ValidW3CDocument_ReturnsStringRepresentation() {
        W3CDom converter = new W3CDom();
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        Document w3cDoc = null;
        try {
            w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException should not be thrown");
        }

        converter.convert(jsoupDoc, w3cDoc);

        String w3cDocAsString = converter.asString(w3cDoc);

        assertNotNull(w3cDocAsString);
        assertTrue(w3cDocAsString.contains(html));
    }
}