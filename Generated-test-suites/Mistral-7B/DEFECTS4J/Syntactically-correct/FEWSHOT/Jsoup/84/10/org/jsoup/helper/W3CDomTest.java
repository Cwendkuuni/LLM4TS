package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document input = org.jsoup.Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        Document output = new W3CDom().fromJsoup(input);
        Assert.assertNotNull(output);

        // Check if the document has the correct root element
        Assert.assertEquals("html", output.getDocumentElement().getTagName());

        // Check if the document has the correct number of child elements
        Assert.assertEquals(2, output.getDocumentElement().getChildNodes().getLength());

        // Check if the head element is present
        Element head = (Element) output.getDocumentElement().getChildNodes().item(0);
        Assert.assertEquals("head", head.getTagName());
        Assert.assertNull(head.getTextContent());

        // Check if the body element is present
        Element body = (Element) output.getDocumentElement().getChildNodes().item(1);
        Assert.assertEquals("body", body.getTagName());
        Assert.assertNotNull(body.getTextContent());
        Assert.assertEquals("Test", body.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document input = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
        Document document = new Document();
        new W3CDom().convert(input, document);

        // Check if the document has the correct root element
        Assert.assertEquals("html", document.getDocumentElement().getTagName());

        // Check if the head element is present
        Element head = (Element) document.getDocumentElement().getElementsByTagName("head").item(0);
        Assert.assertEquals("head", head.getTagName());
        Element title = (Element) head.getElementsByTagName("title").item(0);
        Assert.assertEquals("title", title.getTagName());
        Assert.assertEquals("Test", title.getTextContent());

        // Check if the body element is present
        Element body = (Element) document.getDocumentElement().getElementsByTagName("body").item(0);
        Assert.assertEquals("body", body.getTagName());
        Element h1 = (Element) body.getElementsByTagName("h1").item(0);
        Assert.assertEquals("h1", h1.getTagName());
        Assert.assertEquals("Test", h1.getTextContent());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document input = org.jsoup.Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        Document document = new W3CDom().fromJsoup(input);
        String output = new W3CDom().asString(document);

        // Check if the output string starts with "<html>" and ends with "</html>"
        Assert.assertTrue(output.startsWith("<html>"));
        Assert.assertTrue(output.endsWith("</html>"));

        // Check if the head element is present in the output string
        int headIndex = output.indexOf("<head>");
        Assert.assertTrue(headIndex > 0);

        // Check if the body element is present in the output string
        int bodyIndex = output.indexOf("<body>");
        Assert.assertTrue(bodyIndex > 0);

        // Check if the h1 element is present in the output string
        int h1Index = output.indexOf("<h1>Test</h1>");
        Assert.assertTrue(h1Index > 0);
    }

    @Test
    public void testEmptyDocument() {
        Document document = new Document();
        new W3CDom().convert(org.jsoup.Jsoup.parse(""), document);
        String output = new W3CDom().asString(document);

        // Check if the output string is empty
        Assert.assertEquals("", output);
    }

    @Test
    public void testNamespaces() {
        org.jsoup.nodes.Document input = org.jsoup.Jsoup.parse("<html xmlns:foo=\"http://example.com/foo\">" +
                "<head></head>" +
                "<body xmlns:bar=\"http://example.com/bar\">" +
                "<foo:element>Hello world!</foo:element>" +
                "</body></html>");
        Document document = new Document();
        new W3CDom().convert(input, document);

        // Check if the document has the correct root element
        Element root = document.getDocumentElement();
        Assert.assertEquals("html", root.getTagName());

        // Check if the foo namespace is correctly set
        Map<String, String> fooNamespace = (Map<String, String>) root.getNamespaceURI();
        Assert.assertEquals("http://example.com/foo", fooNamespace.get("foo"));

        // Check if the foo:element is present
        Element fooElement = (Element) root.getElementsByTagNameNS("http://example.com/foo", "element").item(0);
        Assert.assertEquals("element", fooElement.getTagName());
        Assert.assertEquals("Hello world!", fooElement.getTextContent());

        // Check if the bar namespace is correctly set
        Map<String, String> barNamespace = (Map<String, String>) fooElement.getParentNode().getNamespaceURI();
        Assert.assertEquals("http://example.com/bar", barNamespace.get("bar"));
    }
}