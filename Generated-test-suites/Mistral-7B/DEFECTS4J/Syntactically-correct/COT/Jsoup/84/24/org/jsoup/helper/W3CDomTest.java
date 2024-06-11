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
import java.util.Stack;

public class W3CDomTest {

    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        // Basic functionality test for fromJsoup method
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        // Basic functionality test for convert method (no exception handling)
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body></body></html>");
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        Assert.assertNotNull(w3cDocument.getDocumentElement());
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertWithParserConfigurationException() throws ParserConfigurationException {
        // Test for ParserConfigurationException in convert method
        factory.setNamespaceAware(false); // set to false to trigger exception
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body></body></html>");
        Document w3cDocument = Document.newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);
    }

    @Test
    public void testAsString() {
        // Basic functionality test for asString method (no exception handling)
        Document w3cDocument = factory.newDocument();
        Element root = w3cDocument.createElement("html");
        root.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        root.appendChild(w3cDocument.createTextNode("Hello World"));
        w3cDocument.appendChild(root);
        String result = new W3CDom().asString(w3cDocument);
        Assert.assertEquals("<html xmlns=\"http://www.w3.org/1999/xhtml\">Hello World</html>", result);
    }

    @Test(expected = TransformerException.class)
    public void testAsStringWithTransformerException() {
        // Test for TransformerException in asString method
        Document w3cDocument = factory.newDocument();
        Element root = w3cDocument.createElement("html");
        root.appendChild(w3cDocument.createTextNode("Hello World"));
        w3cDocument.appendChild(root);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(w3cDocument);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        w3cDocument.setDocumentURI("data:text/html;charset=UTF-8");
        new W3CDom().asString(w3cDocument); // This line will trigger TransformerException
    }
}