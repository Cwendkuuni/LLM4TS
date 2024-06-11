import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

public class W3CDomTest {
    private W3CDom w3CDom;
    private Document document;
    private Element rootElement;

    @Before
    public void setUp() throws IOException {
        w3CDom = new W3CDom();
        document = Parser.parseBodyFragment("<html><head></head><body></body></html>");
        rootElement = document.getElementsByTag("html").first();
    }

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("http://www.w3.org/1999/xhtml", w3cDocument.getDocumentElement().getNamespaceURI());
    }

    @Test
    public void testConvert() {
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3CDom.convert(document, w3cDocument);
        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("http://www.w3.org/1999/xhtml", w3cDocument.getDocumentElement().getNamespaceURI());
        Assert.assertEquals(rootElement.tagName(), w3cDocument.getDocumentElement().getTagName());
        Assert.assertEquals(rootElement.attr("class"), w3cDocument.getDocumentElement().getAttribute("class"));
    }

    @Test
    public void testConvertNullDocument() {
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3CDom.convert(null, w3cDocument);
        Assert.assertNull(w3cDocument.getDocumentElement());
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(document);
        builder.head(rootElement, 0);
        org.w3c.dom.Element w3cElement = builder.getDest();
        Assert.assertNotNull(w3cElement);
        Assert.assertEquals("http://www.w3.org/1999/xhtml", w3cElement.getNamespaceURI());
        Assert.assertEquals(rootElement.tagName(), w3cElement.getTagName());
    }

    @Test
    public void testW3CBuilderTail() {
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        builder.head(rootElement, 0);
        builder.tail(rootElement, 0);
        org.w3c.dom.Element w3cElement = builder.getDest();
        Assert.assertNull(w3cElement.getParentNode());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("Hello World", "");
        builder.head(textNode, 0);
        org.w3c.dom.Element w3cElement = builder.getDest();
        Assert.assertEquals("Hello World", w3cElement.getFirstChild().getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadCommentNode() {
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("Comment");
        builder.head(comment, 0);
        org.w3c.dom.Element w3cElement = builder.getDest();
        Assert.assertEquals("Comment", w3cElement.getFirstChild().getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("Data", "");
        builder.head(dataNode, 0);
        org.w3c.dom.Element w3cElement = builder.getDest();
        Assert.assertEquals("Data", w3cElement.getFirstChild().getNodeValue());
    }

    @Test
    public void testCopyAttributes() {
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("div");
        sourceElement.attr("class", "test-class");
        sourceElement.attr("data-attr", "test-data");
        org.w3c.dom.Element w3cElement = w3cDocument.createElement("div");
        builder.copyAttributes(sourceElement, w3cElement);
        Assert.assertEquals("test-class", w3cElement.getAttribute("class"));
        Assert.assertEquals("test-data", w3cElement.getAttribute("data-attr"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        element.attr("xmlns", "http://www.w3.org/1999/xhtml");
        element.attr("xmlns:prefix", "urn:test");
        String prefix = builder.updateNamespaces(element);
        Assert.assertEquals("", prefix);
        Assert.assertEquals("http://www.w3.org/1999/xhtml", builder.getNamespaces().get(""));
        Assert.assertEquals("urn:test", builder.getNamespaces().get("prefix"));
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        org.w3c.dom.Element root = w3cDocument.createElement("root");
        w3cDocument.appendChild(root);
        String xmlString = w3CDom.asString(w3cDocument);
        Assert.assertNotNull(xmlString);
        Assert.assertTrue(xmlString.contains("<root"));
        Assert.assertTrue(xmlString.contains("</root>"));
    }
}