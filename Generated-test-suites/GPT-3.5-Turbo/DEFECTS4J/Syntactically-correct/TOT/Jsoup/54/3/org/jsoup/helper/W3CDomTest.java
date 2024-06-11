import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup_NullInput_ThrowsException() {
        org.jsoup.nodes.Document jsoupDoc = null;
        try {
            Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        } catch (IllegalArgumentException e) {
            assertEquals("Input jsoup doc cannot be null", e.getMessage());
        }
    }

    @Test
    public void testFromJsoup_ValidInput_ReturnsValidW3CDocument() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        Element rootEl = w3cDoc.getDocumentElement();
        assertEquals("html", rootEl.getTagName());
        Element bodyEl = (Element) rootEl.getElementsByTagName("body").item(0);
        assertNotNull(bodyEl);
        Element h1El = (Element) bodyEl.getElementsByTagName("h1").item(0);
        assertNotNull(h1El);
    }

    @Test
    public void testConvert_NullInput_ThrowsException() {
        org.jsoup.nodes.Document jsoupDoc = null;
        Document w3cDoc = null;
        try {
            w3cDom.convert(jsoupDoc, w3cDoc);
        } catch (IllegalArgumentException e) {
            assertEquals("Input jsoup doc cannot be null", e.getMessage());
        }
    }

    @Test
    public void testConvert_ValidInput_UpdatesW3CDocument() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document w3cDoc = docBuilder.newDocument();
        
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        assertNotNull(w3cDoc);
        Element rootEl = w3cDoc.getDocumentElement();
        assertEquals("html", rootEl.getTagName());
        Element bodyEl = (Element) rootEl.getElementsByTagName("body").item(0);
        assertNotNull(bodyEl);
        Element h1El = (Element) bodyEl.getElementsByTagName("h1").item(0);
        assertNotNull(h1El);
    }

    @Test
    public void testConvert_WithLocation_UpdatesW3CDocumentURI() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document w3cDoc = docBuilder.newDocument();
        
        jsoupDoc.setBaseUri("https://www.example.com");
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        assertEquals("https://www.example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testCopyAttributes_ValidInput_CopiesAttributesToW3CElement() {
        org.jsoup.nodes.Element source = Jsoup.parse("<div id=\"test\" class=\"example\"></div>").select("div").first();
        Element destination = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("div");
        
        w3cDom.copyAttributes(source, destination);
        
        assertTrue(destination.hasAttribute("id"));
        assertEquals("test", destination.getAttribute("id"));
        assertTrue(destination.hasAttribute("class"));
        assertEquals("example", destination.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces_NoNamespaces_ReturnsEmptyString() {
        org.jsoup.nodes.Element element = Jsoup.parse("<div></div>").select("div").first();

        String prefix = w3cDom.updateNamespaces(element);

        assertEquals("", prefix);
        assertTrue(w3cDom.namespaces.isEmpty());
    }

    @Test
    public void testUpdateNamespaces_WithNamespace_ReturnsPrefix() {
        org.jsoup.nodes.Element element = Jsoup.parse("<div xmlns=\"http://www.example.com\"></div>").select("div").first();

        String prefix = w3cDom.updateNamespaces(element);

        assertEquals("", prefix);
        assertTrue(w3cDom.namespaces.containsKey(""));
        assertEquals("http://www.example.com", w3cDom.namespaces.get(""));
    }

    @Test
    public void testUpdateNamespaces_WithNamespaceAndPrefix_ReturnsPrefix() {
        org.jsoup.nodes.Element element = Jsoup.parse("<div xmlns:test=\"http://www.example.com\"></div>").select("div").first();

        String prefix = w3cDom.updateNamespaces(element);

        assertEquals("test", prefix);
        assertTrue(w3cDom.namespaces.containsKey("test"));
        assertEquals("http://www.example.com", w3cDom.namespaces.get("test"));
    }

    @Test
    public void testAsString_ValidInput_ReturnsStringRepresentation() {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document w3cDoc = docBuilder.newDocument();
        Element rootEl = w3cDoc.createElement("root");
        w3cDoc.appendChild(rootEl);
        Element childEl = w3cDoc.createElement("child");
        rootEl.appendChild(childEl);
        Text textNode = w3cDoc.createTextNode("Hello World!");
        childEl.appendChild(textNode);
        
        String result = w3cDom.asString(w3cDoc);
        
        assertNotNull(result);
        assertTrue(result.contains("<root>"));
        assertTrue(result.contains("<child>"));
        assertTrue(result.contains("Hello World!"));
    }

}