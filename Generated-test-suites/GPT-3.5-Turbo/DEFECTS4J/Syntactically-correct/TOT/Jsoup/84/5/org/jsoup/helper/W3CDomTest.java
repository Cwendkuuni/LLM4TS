
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private static DocumentBuilder documentBuilder;
    private static Transformer transformer;

    @BeforeClass
    public static void setUp() throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
    }

    // Test case for fromJsoup() method

    @Test(expected = IllegalArgumentException.class)
    public void fromJsoup_nullInput_throwsIllegalArgumentException() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.fromJsoup(null);
    }

    @Test
    public void fromJsoup_validInput_returnsW3CDocument() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    // Test case for convert() method

    @Test
    public void convert_jsoupDocumentWithLocation_setsDocumentURI() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        jsoupDocument.setBaseUri("http://example.com");
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        w3cDom.convert(jsoupDocument, w3cDocument);
        assertEquals("http://example.com", w3cDocument.getDocumentURI());
    }

    @Test
    public void convert_jsoupDocumentWithElement_createsValidW3CDocument() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        w3cDom.convert(jsoupDocument, w3cDocument);

        Element documentElement = w3cDocument.getDocumentElement();
        assertEquals("http://www.w3.org/1999/xhtml", documentElement.getNamespaceURI());
        assertEquals("html", documentElement.getLocalName());

        Element bodyElement = (Element) documentElement.getFirstChild();
        assertEquals("http://www.w3.org/1999/xhtml", bodyElement.getNamespaceURI());
        assertEquals("body", bodyElement.getLocalName());

        Element h1Element = (Element) bodyElement.getFirstChild();
        assertEquals("http://www.w3.org/1999/xhtml", h1Element.getNamespaceURI());
        assertEquals("h1", h1Element.getLocalName());

        assertEquals("Hello World", h1Element.getTextContent());
    }

    // Test case for asString() method

    @Test
    public void asString_validInput_returnsString() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDocument = documentBuilder.newDocument();
        Element rootElement = w3cDocument.createElement("root");
        w3cDocument.appendChild(rootElement);
        String xmlString = w3cDom.asString(w3cDocument);

        assertNotNull(xmlString);
        assertTrue(xmlString.contains("<root></root>"));
    }
}

