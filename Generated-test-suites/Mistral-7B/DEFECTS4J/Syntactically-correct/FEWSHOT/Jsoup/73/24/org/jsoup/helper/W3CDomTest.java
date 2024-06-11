package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello world</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(jsoupDoc.tagName(), w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello world</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        new W3CDom().convert(jsoupDoc, w3cDoc);
        assertSame(jsoupDoc, w3cDoc);
    }

    @Test
    public void testAsString() {
        Document doc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1 id='test'>Hello world</h1></body></html>"));
        String result = new W3CDom().asString(doc);
        assertNotNull(result);
        assertEquals("<html>\n<body>\n<h1 id='test'>Hello world</h1>\n</body>\n</html>", result);
    }

    @Test
    public void testW3CBuilder() {
        Document doc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1 id='test' xmlns:test='http://test.com'><p>Hello world</p></h1></body></html>"));
        new W3CDom().convert(org.jsoup.Jsoup.parse("<html><body><h1 id='test' xmlns:test='http://test.com'><p>Hello world</p></h1></body></html>"), doc);
        Element root = doc.getDocumentElement();
        assertEquals("html", root.getTagName());
        assertEquals("http://test.com/", root.getNamespaceURI());
        Element h1 = (Element) root.getElementsByTagName("h1").item(0);
        assertEquals("h1", h1.getTagName());
        assertEquals("test", h1.getNamespaceURI());
        Element p = (Element) h1.getElementsByTagName("p").item(0);
        assertEquals("p", p.getTagName());
        assertEquals("", p.getNamespaceURI());
        Text text = (Text) p.getChildNodes().item(0);
        assertEquals("Hello world", text.getData());
        Comment comment = (Comment) h1.getElementsByTagName("comment").item(0);
        assertNull(comment);
    }
}