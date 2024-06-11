import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Comment;

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
import java.util.Stack;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        Document document = new Document("");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(document);
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        Document document = new Document("");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(document);
        DocumentBuilder builder;
        try {
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document out = builder.newDocument();
            w3cDom.convert(document, out);
            assertNotNull(out);
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testW3CBuilder() {
        Document document = new Document("");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(document);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        assertNotNull(w3cBuilder);
    }

    @Test
    public void testW3CBuilderHead() {
        Document document = new Document("");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(document);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("div");
        w3cBuilder.head(source, 0);
        Element dest = w3cBuilder.dest;
        assertNotNull(dest);
    }

    @Test
    public void testW3CBuilderTail() {
        Document document = new Document("");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(document);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("div");
        w3cBuilder.head(source, 0);
        w3cBuilder.tail(source, 0);
        Element dest = w3cBuilder.dest;
        assertNull(dest);
    }

    @Test
    public void testW3CBuilderCopyAttributes() {
        Document document = new Document("");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(document);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("div");
        Element el = (Element) w3cDocument.createElementNS("", "div");
        w3cBuilder.copyAttributes(source, el);
        assertNotNull(el);
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        Document document = new Document("");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(document);
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDocument);
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
        String namespace = w3cBuilder.updateNamespaces(el);
        assertNotNull(namespace);
    }

    @Test
    public void testAsString() {
        Document document = new Document("");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(document);
        String output = w3cDom.asString(w3cDocument);
        assertNotNull(output);
    }

}