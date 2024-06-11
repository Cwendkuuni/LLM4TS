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
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document out = new W3CDom().fromJsoup(in);

        Assert.assertNotNull(out);
        Assert.assertEquals("<html xmlns:xmlns=\"http://www.w3.org/2000/xmlns/\"><head><title>Test</title></head><body><p>Hello World</p></body></html>", asString(out));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p id=\"paragraph\">Hello World</p></body></html>");
        Document doc = new Document();
        new W3CDom().convert(in, doc);

        Assert.assertNotNull(doc.getDocumentElement());
        Assert.assertEquals("html", doc.getDocumentElement().getTagName());

        Element root = doc.getDocumentElement();
        Assert.assertEquals(1, root.getChildNodes().getLength());

        Element head = root.getElementsByTagName("head").item(0);
        Assert.assertNotNull(head);
        Assert.assertEquals(1, head.getChildNodes().getLength());

        Element title = head.getElementsByTagName("title").item(0);
        Assert.assertNotNull(title);
        Assert.assertEquals("Test", title.getTextContent());

        Element body = root.getElementsByTagName("body").item(0);
        Assert.assertNotNull(body);
        Assert.assertEquals(1, body.getChildNodes().getLength());

        Element p = body.getElementsByTagName("p").item(0);
        Assert.assertNotNull(p);
        Assert.assertEquals("Hello World", p.getTextContent());
        Assert.assertEquals("paragraph", p.getAttribute("id"));
    }

    @Test
    public void testConvertWithNamespaces() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:head><title>Test</title></xhtml:head><body><xhtml:p xmlns:my=\"http://example.com\">Hello World</xhtml:p></body></html>");
        Document doc = new Document();
        new W3CDom().convert(in, doc);

        Assert.assertEquals("http://www.w3.org/1999/xhtml", doc.getDocumentElement().getNamespaceURI());
        Assert.assertEquals("xhtml", doc.getDocumentElement().getTagName());

        Element root = doc.getDocumentElement();
        Assert.assertEquals(1, root.getChildNodes().getLength());

        Element head = root.getElementsByTagNameNS("*", "head").item(0);
        Assert.assertNotNull(head);
        Assert.assertEquals(1, head.getChildNodes().getLength());

        Element title = head.getElementsByTagName("title").item(0);
        Assert.assertNotNull(title);
        Assert.assertEquals("Test", title.getTextContent());

        Element body = root.getElementsByTagNameNS("*", "body").item(0);
        Assert.assertNotNull(body);
        Assert.assertEquals(1, body.getChildNodes().getLength());

        Element p = body.getElementsByTagNameNS("*", "p").item(0);
        Assert.assertNotNull(p);
        Assert.assertEquals("Hello World", p.getTextContent());

        Assert.assertEquals("http://example.com", p.getNamespaceURI());
        Assert.assertEquals("my", p.getPrefix());
    }

    @Test
    public void testAsString() {
        Document doc = new Document();
        Element root = doc.createElement("html");
        root.appendChild(doc.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\""));
        root.appendChild(doc.createElement("head"));
        root.appendChild(doc.createComment("This is a comment"));
        root.appendChild(doc.createTextNode("Hello World"));
        doc.appendChild(root);

        String expected = "<!--This is a comment-->\n<html xmlns=\"http://www.w3.org/1998/xml\">\n<head></head>\n<![CDATA[Hello World]]>\n";
        String actual = new W3CDom().asString(doc);

        Assert.assertEquals(expected, actual);
    }

    private String asString(Document doc) {
        return new String(doc.getDocumentElement().getDocumentURI().getBytes(0, doc.getDocumentElement().getDocumentURI().length()));
    }
}