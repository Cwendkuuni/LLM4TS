package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.namespace.QName;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.*;

public class W3CDomTest {

    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document jsoupDocument;
    private Document w3cDocument;

    @Before
    public void setup() throws Exception {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        jsoupDocument = builder.newDocument();
        jsoupDocument.appendChild(jsoupDocument.createElement("root"));

        w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        new W3CDom().convert(jsoupDocument, w3cDocument);
        assertEquals(jsoupDocument.getDocumentElement().getTagName(), w3cDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testAsString() {
        assertEquals(new W3CDom().asString(jsoupDocument), new W3CDom().asString(w3cDocument));
    }

    // Test for methods with no parameters

    @Test
    public void testValidateNotNull() {
        try {
            new W3CDom().validateNotNull(null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        assertTrue(W3CDom.isBlank(null));
        assertTrue(W3CDom.isBlank(""));
        assertFalse(W3CDom.isBlank(""));
    }

    // Test for methods with an org.jsoup.nodes.Document parameter

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullDocument() {
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testFromJsoupEmptyDocument() {
        Document emptyDocument = builder.newDocument();
        Document w3cEmptyDocument = new W3CDom().fromJsoup(emptyDocument);
        assertEquals(emptyDocument.getDocumentElement().getTagName(), w3cEmptyDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testConvertWithLocation() {
        String location = "http://example.com";
        jsoupDocument.getDocumentElement().setAttribute("location", location);
        new W3CDom().convert(jsoupDocument, w3cDocument);
        assertEquals(location, w3cDocument.getDocumentURI().toString());
    }

    @Test
    public void testConvertEmptyDocument() {
        Document emptyDocument = builder.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        new W3CDom().convert(emptyDocument, w3cDocument);
        assertEquals(w3cDocument.getDocumentElement().getTagName(), jsoupDocument.getDocumentElement().getTagName());
        assertEquals(w3cDocument.getDocumentElement().getTagName(), emptyDocument.getDocumentElement().getTagName());
    }

    @Test
    public void testConvertWithChildren() {
        Element rootEl = jsoupDocument.createElement("root");
        rootEl.appendChild(jsoupDocument.createElement("child1"));
        rootEl.appendChild(jsoupDocument.createElement("child2"));
        jsoupDocument.appendChild(rootEl);

        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element rootW3c = w3cDocument.getDocumentElement();
        assertEquals(rootEl.getTagName(), rootW3c.getTagName());
        assertEquals(2, rootW3c.getChildNodes().getLength());

        Element child1W3c = (Element) rootW3c.getFirstChild();
        Element child2W3c = (Element)rootW3c.getChildNodes().item(1);
        assertEquals(child1W3c.getTagName(), "child1");
        assertEquals(child2W3c.getTagName(), "child2");
    }

    // Test for methods with a Document parameter

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullDocument() {
        new W3CDom().convert(null, w3cDocument);
    }

    @Test
    public void testConvertEmptyDocumentToEmptyDocument() {
        new W3CDom().convert(builder.newDocument(), w3cDocument);
        assertEquals(w3cDocument.getDocumentElement().getTagName(), "");
        assertEquals(0, w3cDocument.getDocumentElement().getChildNodes().getLength());
    }

    @Test
    public void testConvertDocumentToDocument() {
        Document w3cEmptyDocument = new W3CDom().fromJsoup(jsoupDocument);
        // Test with empty document
        new W3CDom().convert(w3cEmptyDocument, w3cDocument);
        assertEquals(w3cDocument.getDocumentElement().getTagName(), w3cEmptyDocument.getDocumentElement().getTagName());
        assertEquals(0, w3cDocument.getDocumentElement().getChildNodes().getLength());

        // Test with document containing elements
        Element rootEl = jsoupDocument.createElement("root");
        rootEl.appendChild(jsoupDocument.createElement("child1"));
        rootEl.appendChild(jsoupDocument.createElement("child2"));
        jsoupDocument.appendChild(rootEl);

        Document w3cDocumentWithElements = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(w3cDocumentWithElements, w3cDocument);

        Element rootW3c = w3cDocument.getDocumentElement();
        assertEquals(rootEl.getTagName(), rootW3c.getTagName());
        assertEquals(2, rootW3c.getChildNodes().getLength());

        Element child1W3c = (Element) rootW3c.getFirstChild();
        Element child2W3c = (Element)rootW3c.getChildNodes().item(1);
        assertEquals(child1W3c.getTagName(), "child1");
        assertEquals(child2W3c.getTagName(), "child2");
    }

    // Test for static methods

    @Test
    public void testUpdateNamespaces() {
        String xmlnsKey = "xmlns";
        String xmlnsPrefix = "xmlns:";
        String prefix = "prefix";
        String namespace = "namespace";

        org.jsoup.nodes.Element sourceEl = builder.newDocument().createElement("root");
        sourceEl.setAttribute(xmlnsKey, namespace);
        sourceEl.setAttribute(xmlnsPrefix + prefix, namespace);

        Map<String, String> namespaces = new HashMap<>();
        namespaces.put(prefix, namespace);

        String result = new W3CDom().W3CBuilder.updateNamespaces(sourceEl, namespaces);
        assertEquals(prefix, result);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element sourceEl = builder.newDocument().createElement("root");
        sourceEl.setAttribute("key1", "value1");
        sourceEl.setAttribute("key2", "value2");

        Element el = builder.newDocument().createElement("root");
        new W3CDom().copyAttributes(sourceEl, el);

        assertEquals(2, el.getAttributes().getLength());
        assertEquals("value1", el.getAttribute("key1"));
        assertEquals("value2", el.getAttribute("key2"));
    }

    @Test
    public void testAsString() {
        Document document = builder.newDocument();
        document.appendChild(document.createElement("root"));
        document.getDocumentElement().appendChild(document.createTextNode("test"));

        String expected = "<root>\n test\n</root>";
        assertEquals(expected, new W3CDom().asString(document));
    }
}