package org.jsoup.helper;

import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.transform.TransformerException;
import java.io.StringWriter;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Title</h1></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Element root = w3cDoc.getDocumentElement();
        assertEquals("html", root.getTagName());
        Element body = (Element) root.getElementsByTagName("body").item(0);
        Element h1 = (Element) body.getElementsByTagName("h1").item(0);
        Text text = (Text) h1.getChildNodes().item(0);
        assertEquals("Title", text.getWholeText());
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Title</h1></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Document outputDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        Element root = outputDoc.getDocumentElement();
        assertEquals("html", root.getTagName());
        Element body = (Element) root.getElementsByTagName("body").item(0);
        Element h1 = (Element) body.getElementsByTagName("h1").item(0);
        Text text = (Text) h1.getChildNodes().item(0);
        assertEquals("Title", text.getWholeText());
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attr("id", "test");
        sourceEl.attr("class", "test-div");
        sourceEl.attr("data-attr", "value");

        Element dest = new mockElement();
        w3cBuilder.dest = dest;

        w3cBuilder.head(sourceEl, 0);

        assertEquals("div", dest.getTagName());
        assertEquals("test", dest.getAttribute("id"));
        assertEquals("test-div", dest.getAttribute("class"));
        assertEquals("value", dest.getAttribute("data-attr"));
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attr("id", "test");

        Element dest = new mockElement();
        w3cBuilder.dest = dest;

        w3cBuilder.tail(sourceEl, 0);

        assertEquals(null, w3cBuilder.dest);
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attr("id", "test");
        sourceEl.attr("class", "test-div");
        sourceEl.attr("data-attr", "value");

        Element dest = new mockElement();
        w3cBuilder.copyAttributes(sourceEl, dest);

        assertEquals("test", dest.getAttribute("id"));
        assertEquals("test-div", dest.getAttribute("class"));
        assertEquals("value", dest.getAttribute("data-attr"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attr("xmlns", "http://www.example.com");

        String prefix = w3cBuilder.updateNamespaces(sourceEl);

        assertEquals("", prefix);
        assertEquals("http://www.example.com", w3cBuilder.namespaces.get(""));
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Title</title></head><body><h1>Heading</h1><p>Content</p></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String result = w3cDom.asString(w3cDoc);

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Title</title></head><body><h1>Heading</h1><p>Content</p></body></html>", result);
    }

}

class mockElement implements Element {

    @Override
    public String getTagName() {
        return null;
    }

    @Override
    public String getAttribute(String name) {
        return null;
    }

    @Override
    public void setAttribute(String name, String value) {

    }

    @Override
    public boolean hasAttribute(String name) {
        return false;
    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public void setIdAttribute(String name, boolean isId) {

    }

    // Implement other abstract methods

}