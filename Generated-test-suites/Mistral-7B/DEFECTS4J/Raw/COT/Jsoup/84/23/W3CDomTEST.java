package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.jaxb.JAXBElement;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Basic functionality test
        Assert.assertNotNull(w3cDoc);
        Assert.assertEquals("html", w3cDoc.getDocumentElement().getTagName());

        // Edge cases and exception handling tests
        testFromJsoupNullInput(null);
        testFromJsoupEmptyInput(new org.jsoup.nodes.Document());
        testFromJsoupWithNamespaces(jsoupDoc);
    }

    private void testFromJsoupNullInput(Document in) {
        try {
            new W3CDom().fromJsoup(in);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private void testFromJsoupEmptyInput(Document in) {
        Document w3cDoc = new W3CDom().fromJsoup(in);
        Assert.assertNotNull(w3cDoc);
        Assert.assertEquals(0, w3cDoc.getChildNodes().getLength());
    }

    private void testFromJsoupWithNamespaces(org.jsoup.nodes.Document in) {
        Document w3cDoc = new W3CDom().fromJsoup(in);

        // Basic functionality test
        Assert.assertNotNull(w3cDoc);
        Assert.assertEquals("html", w3cDoc.getDocumentElement().getTagName());

        // Test if namespaces are correctly handled
        Element htmlElement = w3cDoc.getDocumentElement();
        Element headElement = htmlElement.getElementsByTagNameNS("http://www.w3.org/1999/xhtml", "head").item(0);
        Assert.assertNotNull(headElement);
        Assert.assertEquals("head", headElement.getTagName());
        Assert.assertEquals("http://www.w3.org/1999/xhtml", headElement.getNamespaceURI());
        Assert.assertEquals("", headElement.getPrefix());

        // Test if attributes with prefix are correctly handled
        Element bodyElement = htmlElement.getElementsByTagName("body").item(0);
        Assert.assertNotNull(bodyElement);
        Assert.assertEquals("body", bodyElement.getTagName());
        Assert.assertEquals("", bodyElement.getNamespaceURI());
        Assert.assertNull(bodyElement.getPrefix());

        Assert.assertEquals("http://www.w3.org/1999/xhtml", htmlElement.getNamespaceURI());
        Assert.assertEquals("xmlns", htmlElement.getAttributeNode("xmlns").getNodeName());
        Assert.assertEquals("http://www.w3.org/1999/xhtml", htmlElement.getAttributeNode("xmlns").getNodeValue());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Document outputDoc = new Document();
        new W3CDom().convert(w3cDoc, outputDoc);

        // Basic functionality test
        Assert.assertEquals(jsoupDoc.childNodes().size(), outputDoc.getChildNodes().getLength());
        Assert.assertEquals(jsoupDoc.childNodes().item(0).getNodeName(), outputDoc.getChildNodes().item(0).getNodeName());

        // Edge cases and exception handling tests
        testConvertNullInput(null, null);
        testConvertEmptyInput(new Document(), new org.jsoup.nodes.Document());
    }

    private void testConvertNullInput(Document inDoc, Document outDoc) {
        try {
            new W3CDom().convert(inDoc, outDoc);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private void testConvertEmptyInput(Document inDoc, org.jsoup.nodes.Document jsoupDoc) {
        Document outDoc = new Document();
        new W3CDom().convert(inDoc, outDoc);
        Assert.assertEquals(0, outDoc.getChildNodes().getLength());
    }

    @Test
    public void testAsString() {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>"));
        String w3cDocAsString = new W3CDom().asString(w3cDoc);

        // Basic functionality test
        Assert.assertNotNull(w3cDocAsString);
        Assert.assertTrue(w3cDocAsString.contains("<html>"));
        Assert.assertTrue(w3cDocAsString.contains("<body>"));
        Assert.assertTrue(w3cDocAsString.contains("<h1>Hello World</h1>"));

        // Edge cases
        Document emptyDoc = new Document();
        String emptyDocAsString = new W3CDom().asString(emptyDoc);
        Assert.assertEquals("", emptyDocAsString);

        Document commentDoc = new Document();
        Element commentEl = commentDoc.createComment("This is a comment");
        commentDoc.appendChild(commentEl);
        String commentDocAsString = new W3CDom().asString(commentDoc);
        Assert.assertTrue(commentDocAsString.contains("<!--This is a comment-->"));

        Document textDoc = new Document();
        Element textEl = textDoc.createTextNode("This is a text node.");
        textDoc.appendChild(textEl);
        String textDocAsString = new W3CDom().asString(textDoc);
        Assert.assertEquals("This is a text node.", textDocAsString);
    }
}