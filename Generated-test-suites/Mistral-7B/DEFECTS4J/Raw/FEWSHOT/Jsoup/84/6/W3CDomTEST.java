package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");

        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testAsString() {
        Document doc = W3CDom.fromJsoup(org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>"));
        String result = new W3CDom().asString(doc);

        assertTrue(result.contains("<html>"));
        assertTrue(result.contains("<head>"));
        assertTrue(result.contains("<title>Test</title>"));
        assertTrue(result.contains("<body>"));
        assertTrue(result.contains("<p>Hello World!</p>"));
        assertTrue(result.contains("</body>"));
        assertTrue(result.contains("</html>"));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        W3CDom w3CDom = new W3CDom();
        w3CDom.convert(jsoupDoc, w3cDoc);

        DocumentTraversal traversal = w3cDoc.createDocumentTraversal();
        NodeFilter filter = new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                return node instanceof Text;
            }
        };
        NodeIterator textNodes = traversal.getIteratorNextSibling(w3cDoc.getDocumentElement(), null, filter);

        int count = 0;
        while (textNodes.hasNextNode()) {
            Node node = textNodes.nextNode();
            assertEquals(node.getTextContent(), jsoupDoc.text());
            count++;
        }

        assertEquals(1, count);
    }

}