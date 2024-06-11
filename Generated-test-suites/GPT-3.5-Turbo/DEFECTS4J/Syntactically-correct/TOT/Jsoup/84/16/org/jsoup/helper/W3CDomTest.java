import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setup() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        jsoupDoc = Jsoup.parse(html);
    }

    @Test
    public void testFromJsoupNotNull() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDocument);
    }

    @Test
    public void testFromJsoupTitleTag() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);

        NodeList titleTags = w3cDocument.getElementsByTagName("title");

        assertEquals(1, titleTags.getLength());
        assertEquals("Test", titleTags.item(0).getTextContent());
    }

    @Test
    public void testConvertNotNull() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document out = builder.newDocument();
        w3cDom.convert(jsoupDoc, out);

        assertNotNull(out);
    }

    @Test
    public void testConvertElementCount() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document out = builder.newDocument();
        w3cDom.convert(jsoupDoc, out);

        NodeList jsoupElements = jsoupDoc.getAllElements().get(0).childNodes();
        NodeList w3cElements = out.getChildNodes();

        assertEquals(jsoupElements.getLength(), w3cElements.getLength());
    }

    @Test
    public void testAsStringNotNull() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document out = builder.newDocument();
        w3cDom.convert(jsoupDoc, out);

        String w3cString = w3cDom.asString(out);

        assertNotNull(w3cString);
    }

    @Test
    public void testAsStringResult() {
        W3CDom w3cDom = new W3CDom();
        Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document out = builder.newDocument();
        w3cDom.convert(jsoupDoc, out);

        String w3cString = w3cDom.asString(out);

        assertEquals("<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>", w3cString);
    }
}