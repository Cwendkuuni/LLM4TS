package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Text;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        // Create a jsoup document
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = org.jsoup.Jsoup.parse(html);

        // Convert jsoup document to w3c document
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Check if w3c document is not null
        assertNotNull(w3cDoc);

        // Check if w3c document has expected elements
        org.w3c.dom.Element w3cRoot = w3cDoc.getDocumentElement();
        assertEquals("html", w3cRoot.getTagName());
        assertEquals("head", w3cRoot.getElementsByTagName("head").item(0).getNodeName());
        assertEquals("title", w3cRoot.getElementsByTagName("title").item(0).getNodeName());
        assertEquals("body", w3cRoot.getElementsByTagName("body").item(0).getNodeName());
        assertEquals("h1", w3cRoot.getElementsByTagName("h1").item(0).getNodeName());
    }

    @Test
    public void testConvert() {
        // Create a jsoup document
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = org.jsoup.Jsoup.parse(html);

        // Create a w3c document
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        // Convert jsoup document to w3c document
        Document convertedDoc = new Document("");
        w3cDom.convert(jsoupDoc, convertedDoc);

        // Check if converted document has same elements as original jsoup document
        Element originalBody = jsoupDoc.body();
        Element convertedBody = convertedDoc.body();
        assertNotNull(convertedBody);
        assertEquals(originalBody.childNodeSize(), convertedBody.childNodeSize());

        for (int i = 0; i < originalBody.childNodeSize(); i++) {
            org.jsoup.nodes.Node originalChild = originalBody.childNode(i);
            org.jsoup.nodes.Node convertedChild = convertedBody.childNode(i);

            // Check if the node types are the same
            assertEquals(originalChild.getClass(), convertedChild.getClass());

            // Test specific node types
            if (originalChild instanceof org.jsoup.nodes.TextNode) {
                org.jsoup.nodes.TextNode originalText = (org.jsoup.nodes.TextNode) originalChild;
                Text convertedText = (Text) convertedChild;
                assertEquals(originalText.getWholeText(), convertedText.getWholeText());
            }
        }
    }

    @Test
    public void testAsString() {
        // Create a w3c document
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Convert w3c document to string
        String result = w3cDom.asString(w3cDoc);

        // Check if result string is not null
        assertNotNull(result);

        // Check if result string contains expected HTML elements
        assertTrue(result.contains("<html>"));
        assertTrue(result.contains("<head>"));
        assertTrue(result.contains("<title>"));
        assertTrue(result.contains("<body>"));
        assertTrue(result.contains("<h1>"));
    }

}