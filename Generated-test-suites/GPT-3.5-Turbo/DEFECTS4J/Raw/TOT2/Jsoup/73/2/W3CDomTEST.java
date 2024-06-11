package org.jsoup.helper;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testAsString() {
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, w3cDoc);
        String result = w3CDom.asString(w3cDoc);
        assertNotNull(result);
    }

    @Test(expected = TransformerException.class)
    public void testAsStringException() {
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, w3cDoc);
        // Trying to throw a TransformerException
        StringWriter writer = new StringWriter();
        writer.close();
        String result = w3CDom.asString(w3cDoc);
    }
}