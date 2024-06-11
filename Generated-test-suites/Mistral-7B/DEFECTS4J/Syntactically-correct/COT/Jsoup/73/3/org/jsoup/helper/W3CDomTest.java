package org.jsoup.helper;

import org.junit.jupiter.api.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.junit.jupiter.api.Assertions.*;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        // Given
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p id='test'>Hello</p></body></html>");
        W3CDom w3cDom = new W3CDom();

        // When
        Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);

        // Then
        assertNotNull(w3cDocument);

        // Test the converted document has the same structure and content as the original jsoup document
        assertEquals(jsoupDoc.html(), w3cDom.asString(w3cDocument));
    }

    @Test
    public void testFromJsoupWithEmptyDocument() throws ParserConfigurationException {
        // Given
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("");
        W3CDom w3cDom = new W3CDom();

        // When
        Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);

        // Then
        assertNotNull(w3cDocument);
        assertEquals("", w3cDom.asString(w3cDocument));
    }

    @Test
    public void testFromJsoupWithNullDocument() {
        // Given
        W3CDom w3cDom = new W3CDom();

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> w3cDom.fromJsoup(null));
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        // Given
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p id='test'>Hello</p></body></html>");
        Document w3cDocument = W3CDom.getInstance().fromJsoup(jsoupDoc);
        W3CDom w3cDom = new W3CDom();

        // When
        w3cDom.convert(jsoupDoc, w3cDocument);

        // Then
        assertEquals(jsoupDoc.html(), w3cDom.asString(w3cDocument));
    }

    @Test
    public void testConvertWithNullInputs() {
        // Given
        W3CDom w3cDom = new W3CDom();

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> w3cDom.convert(null, null));
    }

    @Test
    public void testAsString() throws ParserConfigurationException {
        // Given
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p id='test'>Hello</p></body></html>");
        Document w3cDocument = W3CDom.getInstance().fromJsoup(jsoupDoc);
        W3CDom w3cDom = new W3CDom();

        // When
        String jsoupString = w3cDom.asString(w3cDocument);

        // Then
        assertEquals(jsoupDoc.html(), jsoupString);
    }

    @Test
    public void testAsStringWithNullDocument() {
        // Given
        W3CDom w3cDom = new W3CDom();

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> w3cDom.asString(null));
    }
}