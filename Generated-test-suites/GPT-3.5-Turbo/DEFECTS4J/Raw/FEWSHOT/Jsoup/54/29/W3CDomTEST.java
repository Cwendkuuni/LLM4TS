
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {
    private W3CDom w3CDom;
    private static final String EXAMPLE_HTML = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
    private static final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        Document doc = Jsoup.parse(EXAMPLE_HTML);
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(doc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        Document doc = Jsoup.parse(EXAMPLE_HTML);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDoc = builder.newDocument();
            w3CDom.convert(doc, w3cDoc);
            assertEquals(EXPECTED_XML, w3CDom.asString(w3cDoc));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testW3CBuilderHead() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("h1");
        element.text("Hello World!");
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        w3CBuilder.head(element, 0);
        org.w3c.dom.Element w3cElement = w3CBuilder.getDestination();
        assertNotNull(w3cElement);
        assertEquals("h1", w3cElement.getTagName());
        Text text = (Text) w3cElement.getFirstChild();
        assertNotNull(text);
        assertEquals("Hello World!", text.getWholeText());
    }

    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Element element1 = new org.jsoup.nodes.Element("h1");
        org.jsoup.nodes.Element element2 = new org.jsoup.nodes.Element("div");
        element2.appendChild(element1);
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        w3CBuilder.setDestination(null);
        w3CBuilder.setDestination(element2);
        w3CBuilder.tail(element1, 0);
        org.w3c.dom.Element w3cElement = w3CBuilder.getDestination();
        assertNotNull(w3cElement);
        assertEquals("div", w3cElement.getTagName());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        element.attr("id", "test");
        element.attr("class", "test-class");
        element.attr("data-test", "test data");
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        org.w3c.dom.Element w3cElement = w3CBuilder.getDestination();
        w3CBuilder.copyAttributes(element, w3cElement);
        assertEquals("test", w3cElement.getAttribute("id"));
        assertEquals("test-class", w3cElement.getAttribute("class"));
        assertEquals("test data", w3cElement.getAttribute("data-test"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        element.attr("xmlns", "http://www.example.com");
        element.attr("xmlns:test", "http://www.example.com/test");
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        String prefix = w3CBuilder.updateNamespaces(element);
        assertEquals("test", prefix);
        assertEquals("http://www.example.com/test", w3CBuilder.getNamespaces().get("test"));
    }

    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDoc = builder.newDocument();
            Element root = w3cDoc.createElement("root");
            w3cDoc.appendChild(root);
            String xml = w3CDom.asString(w3cDoc);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root/>", xml);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
