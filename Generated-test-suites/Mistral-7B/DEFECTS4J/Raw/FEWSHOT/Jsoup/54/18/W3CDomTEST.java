package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><head/><body><h1>Testing</h1></body></html>");
        Document out = new W3CDom().fromJsoup(in);
        Assert.assertNotNull(out);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><head/><body><h1>Testing</h1></body></html>");
        Document doc = new Document();
        new W3CDom().convert(in, doc);
        Assert.assertNotNull(doc.getDocumentElement());
        Assert.assertEquals("h1", doc.getDocumentElement().getTagName());
        Assert.assertEquals("Testing", doc.getDocumentElement().getTextContent());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><head/><body><h1>Testing</h1></body></html>");
        Document doc = new W3CDom().fromJsoup(in);
        String result = new W3CDom().asString(doc);
        Assert.assertNotNull(result);
        Assert.assertEquals("<html><head/><body><h1>Testing</h1></body></html>", result);
    }

    @Test
    public void testNamespaces() {
        String xmlnsPrefix = "xmlns";
        String xmlnsKey = "xmlns:x";
        String prefix = "x";
        String namespace = "http://example.com/namespace";

        HashMap<String, String> namespaces = new HashMap<>();
        namespaces.put(prefix, namespace);

        org.w3c.dom.Element element = doc.createElementNS(namespace, "element");
        element.setAttribute(xmlnsPrefix, namespace);
        element.setAttribute(xmlnsKey, prefix);

        W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        builder.head(element, 0);

        Assert.assertEquals(namespace, element.getNamespaceURI());
        Assert.assertEquals(prefix, element.getTagName());
    }
}