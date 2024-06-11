import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Text;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

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

import static org.junit.Assert.*;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private Document w3cDoc;

    @Before
    public void setUp() throws Exception {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    }

    @After
    public void tearDown() throws Exception {
        w3cDom = null;
        jsoupDoc = null;
        w3cDoc = null;
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNull() {
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document in = new org.jsoup.nodes.Document("");
        Document out = w3cDom.fromJsoup(in);
        assertNotNull(out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNull() {
        w3cDom.convert(null, w3cDoc);
    }

    @Test
    public void testConvertDocumentURI() throws Exception {
        jsoupDoc.setBaseUri("http://example.com");
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertParserConfigurationException() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        W3CDom newW3CDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
        w3cDoc = newW3CDom.fromJsoup(jsoupDoc);
        newW3CDom.convert(jsoupDoc, w3cDoc);
    }

    @Test
    public void testW3CBuilderHeadElement() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        w3cBuilder.head(sourceElement, 0);
        assertEquals("div", w3cDoc.getFirstChild().getNodeName());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text", "");
        w3cBuilder.head(sourceText, 0);
        assertEquals("text", w3cDoc.getFirstChild().getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment", "");
        w3cBuilder.head(sourceComment, 0);
        assertEquals("comment", w3cDoc.getFirstChild().getNodeValue());
        assertEquals(org.w3c.dom.Comment.class, w3cDoc.getFirstChild().getClass());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data", "");
        w3cBuilder.head(sourceData, 0);
        assertEquals("data", w3cDoc.getFirstChild().getNodeValue());
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.w3c.dom.Element w3cElement = w3cDoc.createElement("div");
        w3cDoc.appendChild(w3cElement);
        w3cBuilder.dest = w3cElement;
        w3cBuilder.tail(new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), ""), 0);
        assertNull(w3cBuilder.dest);
    }

    @Test
    public void testW3CBuilderCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        org.w3c.dom.Element destElement = w3cDoc.createElement("div");
        w3cBuilder.copyAttributes(sourceElement, destElement);
        assertEquals(sourceElement.attributes().size(), destElement.getAttributes().getLength());
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        sourceElement.attr("xmlns", "http://www.w3.org/2000/xmlns/");
        String prefix = w3cBuilder.updateNamespaces(sourceElement);
        assertEquals("", prefix);
        assertEquals("http://www.w3.org/2000/xmlns/", w3cBuilder.namespaces.get(""));
    }

    @Test
    public void testAsString() {
        String xmlString = w3cDom.asString(w3cDoc);
        assertNotNull(xmlString);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringTransformerException() throws TransformerException {
        Document doc = new Document() {
            @Override
            public String getNodeName() {
                return null;
            }
        };
        w3cDom.asString(doc);
    }
}