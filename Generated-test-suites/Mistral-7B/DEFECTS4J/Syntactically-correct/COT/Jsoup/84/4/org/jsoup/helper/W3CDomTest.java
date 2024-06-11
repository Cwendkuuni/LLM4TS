package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Basic functionality test
        assertEquals(w3cDocument.getDocumentElement().getTagName(), "html");

        // Edge case: empty Document
        org.jsoup.nodes.Document emptyJsoupDocument = org.jsoup.Jsoup.parse("");
        Document emptyW3cDocument = new W3CDom().fromJsoup(emptyJsoupDocument);
        new W3CDom().convert(emptyJsoupDocument, emptyW3cDocument);
        assertEquals(emptyW3cDocument.getDocumentElement().getNodeName(), "#document-fragment");

        // Edge case: null Document
        Document nullW3cDocument = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> new W3CDom().convert(jsoupDocument, nullW3cDocument));

        // Exception handling: ParserConfigurationException
        Assertions.assertThrows(IllegalStateException.class, () -> new W3CDom().fromJsoup(null));

        // Test serialize method
        String w3cString = new W3CDom().asString(w3cDocument);
        assertEquals(w3cString, "<html><body><h1>Hello World</h1></body></html>");
    }
}