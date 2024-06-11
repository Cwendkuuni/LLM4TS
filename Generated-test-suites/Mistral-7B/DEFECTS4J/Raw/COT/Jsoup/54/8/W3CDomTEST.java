package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.parser.ParserConfigurationException;

import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    private W3CDom w3CDom = new W3CDom();

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullDocument() {
        W3CDom w3CDom = new W3CDom();
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        w3CDom.convert(jsoupDocument, w3cDocument);

        Assert.assertEquals(3, w3cDocument.getDocumentElement().getChildNodes().getLength());
        Assert.assertEquals("h1", w3cDocument.getDocumentElement().getChildNodes().item(0).getTagName());
        Assert.assertEquals("Hello World", w3cDocument.getDocumentElement().getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testEmptyW3CDocument() {
        Document w3cDocument = w3CDom.fromJsoup(org.jsoup.nodes.Document.newDocument());
        Assert.assertEquals(0, w3cDocument.getDocumentElement().getChildNodes().getLength());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        w3CDom.convert(jsoupDocument, w3cDocument);
        String w3cString = w3CDom.asString(w3cDocument);

        String expected = "<html><body><h1>Hello World</h1></body></html>";
        Assert.assertEquals(expected, w3cString);
    }

    @Test
    public void testAsStringNullDocument() {
        Document w3cDocument = null;
        String w3cString = w3CDom.asString(w3cDocument);

        Assert.assertNull(w3cString);
    }

    @Test
    public void testW3CDomException() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document w3cDocument = builder.newDocument();
            w3CDom.convert(null, w3cDocument);
        } catch (ParserConfigurationException e) {
            // Expected
        }
    }

    @Test
    public void testW3CDomTransformerException() {
        Document w3cDocument = w3CDom.fromJsoup(org.jsoup.nodes.Document.newDocument());
        try {
            w3CDom.asString(w3cDocument);
            Assert.fail("Expected TransformerException");
        } catch (TransformerException e) {
            // Expected
        }
    }

    @Test
    public void testW3CDomStringWriterException() {
        Document w3cDocument = w3CDom.fromJsoup(org.jsoup.nodes.Document.newDocument());
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource domSource = new DOMSource(w3cDocument);

        try {
            transformer.transform(domSource, result);
            Assert.fail("Expected IOException");
        } catch (TransformerException e) {
            // Expected
        } catch (IOException e) {
            String w3cString = writer.toString();
            String expected = "<document></document>";
            Assert.assertEquals(expected, w3cString);
        }
    }

    @Test
    public void testW3CDomEmptyComment() {
        Document w3cDocument = w3CDom.fromJsoup(org.jsoup.nodes.Document.newDocument());
        Comment comment = w3cDocument.createComment("");
        w3cDocument.appendChild(comment);

        String w3cString = w3CDom.asString(w3cDocument);
        String expected = "<!-- -- >";
        Assert.assertEquals(expected, w3cString);
    }

    @Test
    public void testW3CDomTextNode() {
        Document w3cDocument = w3CDom.fromJsoup(org.jsoup.nodes.Document.newDocument());
        Text text = w3cDocument.createTextNode("Hello World");
        w3cDocument.appendChild(text);

        String w3cString = w3CDom.asString(w3cDocument);
        String expected = "Hello World";
        Assert.assertEquals(expected, w3cString);
    }

    @Test
    public void testW3CDomTextNodeWithWhitespace() {
        Document w3cDocument = w3CDom.fromJsoup(org.jsoup.nodes.Document.newDocument());
        Text text = w3cDocument.createTextNode(" Hello World ");
        w3cDocument.appendChild(text);

        String w3cString = w3CDom.asString(w3cDocument);
        String expected = " Hello World ";
        Assert.assertEquals(expected, w3cString);
    }
}