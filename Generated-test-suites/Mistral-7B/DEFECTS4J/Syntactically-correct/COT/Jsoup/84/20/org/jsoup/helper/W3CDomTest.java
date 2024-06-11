package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws Exception {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String expectedXml = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\"><head/><body><h1>Test</h1></body></html>";
        String actualXml = this.asString(w3cDocument);
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testConvert() throws Exception {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        String expectedXml = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\"><head/><body><h1>Test</h1></body></html>";
        String actualXml = this.asString(w3cDocument);
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testW3CBuilder() throws Exception {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 xmlns:test=\"http://test\">Test</h1></body></html>");
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element rootElement = w3cDocument.getDocumentElement();
        String expectedPrefix = "test";
        String expectedNamespace = "http://test";
        Assert.assertEquals(expectedPrefix, rootElement.getNamespaceURI().substring(1));
        Assert.assertEquals(expectedNamespace, rootElement.getNamespaceURI());
    }

    @Test
    public void testW3CBuilderEmptyDocument() throws Exception {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.nodes.Document.newDocument();
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        String expectedXml = "<document/>";
        String actualXml = this.asString(w3cDocument);
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testW3CBuilderTextNode() throws Exception {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.nodes.Document.newDocument();
        Element rootElement = jsoupDocument.createElement("root");
        Text textNode = jsoupDocument.createTextNode("Test Text");
        rootElement.appendChild(textNode);
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element rootElementW3c = w3cDocument.getDocumentElement();
        Text textNodeW3c = (Text) rootElementW3c.getFirstChild();
        String expectedText = "Test Text";
        String actualText = textNodeW3c.getNodeValue();
        Assert.assertEquals(expectedText, actualText);
    }

    @Test
    public void testW3CBuilderComment() throws Exception {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.nodes.Document.newDocument();
        Element rootElement = jsoupDocument.createElement("root");
        Comment comment = jsoupDocument.createComment("Test Comment");
        rootElement.appendChild(comment);
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element rootElementW3c = w3cDocument.getDocumentElement();
        Comment commentW3c = (Comment) rootElementW3c.getFirstChild();
        String expectedComment = "Test Comment";
        String actualComment = commentW3c.getNodeValue();
        Assert.assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testW3CBuilderDataNode() throws Exception {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.nodes.Document.newDocument();
        Element rootElement = jsoupDocument.createElement("root");
        DataNode dataNode = jsoupDocument.createDataNode("Test Data");
        rootElement.appendChild(dataNode);
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Element rootElementW3c = w3cDocument.getDocumentElement();
        Text dataNodeW3c = (Text) rootElementW3c.getFirstChild();
        String expectedData = "Test Data";
        String actualData = dataNodeW3c.getNodeValue();
        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testAsStringNullInput() throws Exception {
        Document document = null;
        Assert.assertNull(new W3CDom().asString(document));
    }

    private String asString(Document document) throws Exception {
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();
    }

}