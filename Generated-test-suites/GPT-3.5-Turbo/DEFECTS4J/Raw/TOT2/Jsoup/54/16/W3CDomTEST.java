package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.W3CDom;

import org.junit.Test;
import org.w3c.dom.Comment;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Edge case: Ensure the converted document is not null
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Typical case: Test if the document URI is set correctly
        assertEquals(jsoupDoc.location(), w3cDoc.getDocumentURI());
        
        // Edge case: Test if the conversion handles empty documents
        jsoupDoc = new Document("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("", w3cDoc.getDocumentURI());
    }
    
    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(new Document(""));
        
        // Typical case: Test if the document is correctly converted to a string
        String result = w3cDom.asString(w3cDoc);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                          "<!--xmlns=\"http://www.w3.org/1999/xhtml\"-->";
        assertEquals(expected, result);
        
        // Error scenario: Test if TransformerException is correctly handled
        org.w3c.dom.Document errorDoc = w3cDom.fromJsoup(new Document(""));
        errorDoc.removeChild(errorDoc.getDocumentElement());
        assertThrows(IllegalStateException.class, () -> w3cDom.asString(errorDoc));
    }

}