package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {
    
    private static W3CDom w3cDom;
    
    @BeforeClass
    public static void setup() {
        w3cDom = new W3CDom();
    }
    
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test(expected = NullPointerException.class)
    public void testFromJsoupNullInput() {
        w3cDom.fromJsoup(null);
    }
    
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = null;
        try {
            w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
    }
    
}