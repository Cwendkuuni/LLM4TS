import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

public class W3CDomTest {
    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    @Test
    public void testFromJsoup() throws Exception {
        String html = "<html><body><div id=\"testId\">Hello, World!</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("HTML", w3cDoc.getDocumentElement().getNodeName());
        assertEquals("BODY", w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getNodeName());
        assertEquals("DIV", w3cDoc.getElementById("testId").getNodeName());
        assertEquals("Hello, World!", w3cDoc.getElementById("testId").getTextContent());
    }

    @Test
    public void testConvert() throws Exception {
        String html = "<html><body><div id=\"testId\">Hello, World!</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = createW3CDocument();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("HTML", w3cDoc.getDocumentElement().getNodeName());
        assertEquals("BODY", w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getNodeName());
        assertEquals("DIV", w3cDoc.getElementById("testId").getNodeName());
        assertEquals("Hello, World!", w3cDoc.getElementById("testId").getTextContent());
    }

    @Test
    public void testW3CBuilder() throws Exception {
        String html = "<html><body><div id=\"testId\">Hello, World!</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        NodeTraversor traversor = new NodeTraversor(builder);
        traversor.traverse(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("HTML", w3cDoc.getDocumentElement().getNodeName());
        assertEquals("BODY", w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getNodeName());
        assertEquals("DIV", w3cDoc.getElementById("testId").getNodeName());
        assertEquals("Hello, World!", w3cDoc.getElementById("testId").getTextContent());
    }

    @Test
    public void testHead() throws Exception {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(Parser.htmlParser().parseBodyFragment("<div>")).child(0);
        org.w3c.dom.Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(sourceEl, 0);

        assertEquals("DIV", w3cDoc.getElementsByTagName("div").item(0).getNodeName());
    }

    @Test
    public void testTail() throws Exception {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(Parser.htmlParser().parseBodyFragment("<div>")).child(0);
        org.w3c.dom.Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(sourceEl, 0);
        builder.tail(sourceEl, 0);

        Node parent = w3cDoc.getElementsByTagName("div").item(0).getParentNode();
        assertTrue(parent instanceof org.w3c.dom.Element);
        assertEquals("BODY", parent.getNodeName());
    }

    @Test
    public void testCopyAttributes() throws Exception {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(Parser.htmlParser().parseBodyFragment("<div attr1=\"value1\" attr2=\"value2\">")).child(0);
        Element destEl = createW3CDocument().createElement("div");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(createW3CDocument());
        builder.copyAttributes(sourceEl, destEl);

        NamedNodeMap attributes = destEl.getAttributes();
        assertEquals(2, attributes.getLength());
        Attr attribute1 = (Attr) attributes.getNamedItem("attr1");
        Attr attribute2 = (Attr) attributes.getNamedItem("attr2");
        assertNotNull(attribute1);
        assertNotNull(attribute2);
        assertEquals("value1", attribute1.getValue());
        assertEquals("value2", attribute2.getValue());
    }

    @Test
    public void testUpdateNamespaces() throws Exception {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element(Parser.htmlParser().parseBodyFragment("<div xmlns=\"urn:test\" xmlns:prefix=\"urn:prefix\">")).child(0);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(createW3CDocument());
        String prefix = builder.updateNamespaces(el);

        assertEquals("prefix", prefix);
        assertEquals(2, builder.namespaces.size());
        assertEquals("urn:test", builder.namespaces.get(""));
        assertEquals("urn:prefix", builder.namespaces.get("prefix"));
    }

    @Test
    public void testAsString() throws Exception {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div id=\"testId\">Hello, World!</div></body></html>");
        org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cDocString = new W3CDom().asString(w3cDoc);

        assertNotNull(w3cDocString);
        assertEquals("<HTML><BODY><DIV id=\"testId\">Hello, World!</DIV></BODY></HTML>", w3cDocString);
    }

    private org.w3c.dom.Document createW3CDocument() throws ParserConfigurationException {
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }
}