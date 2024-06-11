package org.jsoup.helper;

import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("<html xmlns:xmlns=\"http://www.w3.org/2000/xmlns/\"><head><title>Test</title></head><body><p>Hello World</p></body></html>", W3CDom.asString(w3cDoc));
    }

    @Test
    public void testFromJsoupNullInput() {
        assertNull(W3CDom.fromJsoup(null));
    }

    @Test
    public void testConvert() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        W3CDom.convert(jsoupDoc, w3cDoc);
        assertEquals("<html xmlns:xmlns=\"http://www.w3.org/2000/xmlns/\"><head><title>Test</title></head><body><p>Hello World</p></body></html>", W3CDom.asString(w3cDoc));
    }

    @Test
    public void testConvertNullInput() {
        Document w3cDoc = null;
        Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        W3CDom.convert(jsoupDoc, w3cDoc);
        assertNull(w3cDoc);
    }

    @Test
    public void testConvertEmptyDocument() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document jsoupDoc = org.jsoup.Jsoup.parse("");
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        W3CDom.convert(jsoupDoc, w3cDoc);
        assertEquals("", W3CDom.asString(w3cDoc));
    }

    @Test
    public void testConvertNullOutput() {
        Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = null;
        W3CDom.convert(jsoupDoc, w3cDoc);
        assertNull(w3cDoc);
    }

    @Test
    public void testAsStringNullInput() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document doc = null;
        String result = W3CDom.asString(doc);
        assertNull(result);
    }

    @Test
    public void testAsStringEmptyDocument() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document doc = W3CDom.fromJsoup(org.jsoup.Jsoup.parse(""));
        String result = W3CDom.asString(doc);
        assertNotNull(result);
        assertEquals("", result);
    }

}