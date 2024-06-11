package org.jsoup.helper;

import org.jUnit.framework.Assert;
import org.jUnit.framework.Test;
import org.jUnit.framework.TestCase;
import org.jUnit.framework.TestSuite;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest extends TestCase {

    public W3CDomTest(String name) {
        super(name);
    }

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cString = new W3CDom().asString(w3cDoc);
        Document parsedW3cDoc = org.jsoup.Jsoup.parse(w3cString);
        assertEquals(jsoupDoc, parsedW3cDoc);
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        new W3CDom().convert(jsoupDoc, w3cDoc);
        String w3cString = new W3CDom().asString(w3cDoc);
        Document parsedW3cDoc = org.jsoup.Jsoup.parse(w3cString);
        assertEquals(jsoupDoc, parsedW3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullJsoupDoc() {
        new W3CDom().fromJsoup(null);
    }

    @Test
    public void testEmptyJsoupDoc() {
        Document jsoupDoc = org.jsoup.Jsoup.parse("");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cString = new W3CDom().asString(w3cDoc);
        Document parsedW3cDoc = org.jsoup.Jsoup.parse(w3cString);
        assertEquals(jsoupDoc, parsedW3cDoc);
    }

    @Test
    public void testConvertWithDocumentURI() {
        Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        String location = "http://example.com";
        jsoupDoc.setBaseUri(location);
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        new W3CDom().convert(jsoupDoc, w3cDoc);
        String w3cString = new W3CDom().asString(w3cDoc);
        Document parsedW3cDoc = org.jsoup.Jsoup.parse(w3cString);
        assertEquals(location, parsedW3cDoc.getDocumentURI().toString());
        assertEquals(jsoupDoc, parsedW3cDoc);
    }

    @Test
    public void testConvertWithNullW3CDoc() {
        new W3CDom().convert(null, null);
    }

    @Test
    public void testConvertWithEmptyW3CDoc() {
        Document w3cDoc = new Document();
        new W3CDom().convert(null, w3cDoc);
        String w3cString = new W3CDom().asString(w3cDoc);
        Document parsedW3cDoc = org.jsoup.Jsoup.parse(w3cString);
        assertEquals(parsedW3cDoc, parsedW3cDoc);
    }

    @Test
    public void testAsStringNullDoc() {
        String result = new W3CDom().asString(null);
        assertEquals("", result);
    }

    @Test
    public void testAsStringEmptyDoc() {
        Document doc = new Document();
        String result = new W3CDom().asString(doc);
        assertEquals("", result);
    }

    @Test
    public void testAsString() {
        Document doc = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        String result = new W3CDom().asString(doc);
        String expected = "<html><head></head><body><h1>Test</h1></body></html>";
        assertEquals(expected, result);
    }

    public static Test suite() {
        return new TestSuite(W3CDomTest.class);
    }

    public static void main(String[] args) {
        jUnit.textui.TestRunner.run(suite());
    }
}