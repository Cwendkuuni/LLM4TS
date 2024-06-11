package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        Element titleElement = w3cDocument.getElementsByTagName("title").item(0);
        Assert.assertEquals("Title", titleElement.getTextContent());

        Element bodyElement = w3cDocument.getDocumentElement().getElementsByTagName("body").item(0);
        Text pText = (Text) bodyElement.getElementsByTagName("p").item(0).getFirstChild();
        Assert.assertEquals("Hello World", pText.getNodeValue());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        Document emptyW3CDocument = new W3CDom().newDocument();
        new W3CDom().convert(jsoupDocument, emptyW3CDocument);

        Assert.assertEquals(w3cDocument.getDocumentElement(), emptyW3CDocument.getDocumentElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testAsString() {
        Document w3cDocument = new W3CDom().newDocument();
        w3cDocument.appendChild(w3cDocument.createTextNode("Hello World"));

        String stringRepresentation = new W3CDom().asString(w3cDocument);
        Assert.assertEquals("Hello World", stringRepresentation);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringTransformerException() {
        Document w3cDocument = new W3CDom().newDocument();
        try {
            new W3CDom().asString(w3cDocument);
        } catch (TransformerException e) {
            throw new IllegalStateException(e);
        }
    }
}