package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.*;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        // Given: a jsoup Document
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");

        // When: converting jsoup Document to W3C Document
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Then: the W3C Document should not be null
        assertNotNull(w3cDoc);

        // And: the W3C Document should contain the expected elements
        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
        assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());
        assertEquals("body", w3cDoc.getDocumentElement().getChildNodes().item(0).getNodeName());
        assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().item(0).getChildNodes().getLength());
        assertEquals("p", w3cDoc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeName());
        assertEquals("Hello World", w3cDoc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getTextContent());
    }

    // Add test cases for other methods (convert, asString, etc.) following the same pattern
}