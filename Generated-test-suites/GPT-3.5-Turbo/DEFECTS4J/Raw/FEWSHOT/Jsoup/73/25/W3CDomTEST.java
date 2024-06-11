
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        DocumentBuilder builder = null;
        try {
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            assertNotNull(e);
        }

        Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");

        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        DocumentBuilder builder = null;
        try {
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            assertNotNull(e);
        }

        Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");

        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        w3cDom.convert(jsoupDoc, w3cDoc);

        Element root = w3cDoc.getDocumentElement();
        assertNotNull(root);

        Element body = (Element) root.getElementsByTagName("body").item(0);
        assertNotNull(body);

        Element h1 = (Element) body.getElementsByTagName("h1").item(0);
        assertNotNull(h1);

        Text text = (Text) h1.getFirstChild();
        assertNotNull(text);
        assertEquals("Hello World", text.getWholeText());
    }

    @Test
    public void testAsString() {
        DocumentBuilder builder = null;
        try {
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            assertNotNull(e);
        }

        Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");

        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String result = w3cDom.asString(w3cDoc);

        assertNotNull(result);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-16\" standalone=\"no\"?><html><body><h1>Hello World</h1></body></html>", result);
    }
}
