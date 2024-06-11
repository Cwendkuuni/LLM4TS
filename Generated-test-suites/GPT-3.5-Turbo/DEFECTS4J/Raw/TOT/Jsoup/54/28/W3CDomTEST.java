
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class W3CDomTest {
    
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDocument;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDocument = Parser.parseBodyFragment("<body><div id='div1' class='class1'></div><div id='div2' class='class2'></div></body>", "");
    }

    @Test
    public void testFromJsoup_typicalUseCase() {
        Document xmlDocument = w3cDom.fromJsoup(jsoupDocument);
        assertNotNull(xmlDocument);
        assertNotNull(xmlDocument.getDocumentElement());
    }

    @SuppressWarnings({"ConstantConditions", "SuspiciousMethodCalls"})
    @Test
    public void testFromJsoup_errorScenario() {
        try {
            Document nullDocument = null;
            w3cDom.fromJsoup(nullDocument);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Input jsoup Document cannot be null", e.getMessage());
        }
    }

    @Test
    public void testConvert_typicalUseCase() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDocument = builder.newDocument();
            w3cDom.convert(jsoupDocument, w3cDocument);

            NodeList nodeList = w3cDocument.getElementsByTagName("div");
            assertEquals(2, nodeList.getLength());

            Element firstDiv = (Element) nodeList.item(0);
            assertEquals("div", firstDiv.getNodeName());
            assertEquals("div1", firstDiv.getAttribute("id"));
            assertEquals("class1", firstDiv.getAttribute("class"));

            Element secondDiv = (Element) nodeList.item(1);
            assertEquals("div", secondDiv.getNodeName());
            assertEquals("div2", secondDiv.getAttribute("id"));
            assertEquals("class2", secondDiv.getAttribute("class"));
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testConvert_edgeCase() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDocument = builder.newDocument();
            org.jsoup.nodes.Document emptyJsoupDocument = Parser.parseBodyFragment("", "");

            w3cDom.convert(emptyJsoupDocument, w3cDocument);

            NodeList nodeList = w3cDocument.getElementsByTagName("body");
            assertEquals(1, nodeList.getLength());

            Element bodyElement = (Element) nodeList.item(0);
            assertTrue(Element.class.isInstance(bodyElement));
            assertEquals("body", bodyElement.getNodeName());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testConvert_errorScenario() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDocument = builder.newDocument();

            org.jsoup.nodes.Document nullJsoupDocument = null;
            w3cDom.convert(nullJsoupDocument, w3cDocument);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Input jsoup Document cannot be null", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAsString_typicalUseCase() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDocument = builder.newDocument();
            w3cDom.convert(jsoupDocument, w3cDocument);

            String xmlString = w3cDom.asString(w3cDocument);
            assertNotNull(xmlString);

            org.jsoup.nodes.Document parsedJsoupDoc = Parser.parseBodyFragment(xmlString, "");
            Elements divElements = parsedJsoupDoc.getElementsByTag("div");
            assertEquals(2, divElements.size());

            Element firstDiv = divElements.get(0);
            assertEquals("div", firstDiv.tagName());
            assertEquals("div1", firstDiv.id());
            assertEquals("class1", firstDiv.className());

            Element secondDiv = divElements.get(1);
            assertEquals("div", secondDiv.tagName());
            assertEquals("div2", secondDiv.id());
            assertEquals("class2", secondDiv.className());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAsString_edgeCase() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDocument = builder.newDocument();
            org.jsoup.nodes.Document emptyJsoupDocument = Parser.parseBodyFragment("", "");

            w3cDom.convert(emptyJsoupDocument, w3cDocument);

            String xmlString = w3cDom.asString(w3cDocument);
            assertNotNull(xmlString);

            org.jsoup.nodes.Document parsedJsoupDoc = Parser.parseBodyFragment(xmlString, "");
            Elements bodyElements = parsedJsoupDoc.getElementsByTag("body");
            assertTrue(bodyElements.size() > 0);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAsString_errorScenario() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDocument = builder.newDocument();

            org.w3c.dom.Document nullW3cDocument = null;
            w3cDom.asString(nullW3cDocument);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Input Document cannot be null", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
}
