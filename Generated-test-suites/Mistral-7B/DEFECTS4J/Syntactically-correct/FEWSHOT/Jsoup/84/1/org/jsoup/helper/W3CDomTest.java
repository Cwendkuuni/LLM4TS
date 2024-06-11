package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        Assert.assertEquals(jsoupDocument.child(0).nodeName(), w3cDocument.getDocumentElement().getTagName());
        Assert.assertEquals(jsoupDocument.child(0).nodeName(), w3cDocument.getDocumentElement().getElementsByTagName("html").item(0).getNodeName());

        Element html = (Element) w3cDocument.getDocumentElement();
        Assert.assertEquals(html.getChildNodes().getLength(), 2);
        Assert.assertEquals(html.getElementsByTagName("head").item(0).getNodeName(), "head");
        Assert.assertEquals(html.getElementsByTagName("body").item(0).getNodeName(), "body");

        Assert.assertEquals(html.getElementsByTagName("body").item(0).getChildNodes().getLength(), 1);
        Assert.assertEquals(html.getElementsByTagName("body").item(0).getChildNodes().item(0).getNodeName(), "p");
        Assert.assertEquals(html.getElementsByTagName("body").item(0).getChildNodes().item(0).getTextContent(), "Hello World");
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head/><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String w3cString = new W3CDom().asString(w3cDocument);

        org.jsoup.nodes.Document parsedJsoupDocument = org.jsoup.Jsoup.parse(w3cString);
        Assert.assertEquals(jsoupDocument.html(), parsedJsoupDocument.html());
    }
}