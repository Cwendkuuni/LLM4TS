package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Traversal;
import org.w3c.dom.traversal.DocumentTraversal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);

        DocumentTraversal traversal = (DocumentTraversal) w3cDocument;
        Element root = (Element) traversal.getDocumentElement();
        Assert.assertEquals("html", root.getTagName());

        Element title = (Element) traversal.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", title.getTextContent());

        Element body = (Element) traversal.getElementsByTagName("body").item(0);
        Element p = (Element) traversal.getElementsByTagName("p").item(0);
        Assert.assertEquals("Hello World", p.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");

        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        DocumentTraversal traversal = (DocumentTraversal) w3cDocument;
        Element root = (Element) traversal.getDocumentElement();
        Assert.assertEquals("html", root.getTagName());

        Element title = (Element) traversal.getElementsByTagName("title").item(0);
        Assert.assertEquals("Test", title.getTextContent());

        Element body = (Element) traversal.getElementsByTagName("body").item(0);
        Element p = (Element) traversal.getElementsByTagName("p").item(0);
        Assert.assertEquals("Hello World", p.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
        w3cDocument.createElement("html");
        w3cDocument.createElement("head").appendChild(w3cDocument.createTextNode("Test"));
        w3cDocument.createElement("body").appendChild(w3cDocument.createElement("p")).appendChild(w3cDocument.createTextNode("Hello World"));

        String xmlString = new W3CDom().asString(w3cDocument);
        Assert.assertEquals("<html><head><textarea class=\"CodeMirror-live-code\">Test</textarea></head><body><p>Hello World</p></body></html>", xmlString);
    }
}