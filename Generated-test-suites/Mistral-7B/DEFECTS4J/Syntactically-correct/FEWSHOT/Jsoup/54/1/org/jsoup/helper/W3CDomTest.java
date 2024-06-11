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
import org.w3c.dom.processing.NamespacePrefixMapper;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Title</title></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("Title", w3cDoc.getDocumentElement().getTagName());
        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Title</title></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = new Document();
        new W3CDom().convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
        assertEquals("Title", w3cDoc.getDocumentElement().getTagName());
        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
    }

    @Test
    public void testAsString() {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><head><title>Title</title></head><body><p>Hello World</p></body></html>"));
        String result = new W3CDom().asString(w3cDoc);
        assertTrue(result.contains("<html>"));
        assertTrue(result.contains("<head>"));
        assertTrue(result.contains("<title>Title</title>"));
        assertTrue(result.contains("<body>"));
        assertTrue(result.contains("<p>Hello World</p>"));
        assertTrue(result.contains("</html>"));
    }

    @Test
    public void testNamespaces() {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html xmlns:foo=\"http://example.com/foo\" xmlns:bar=\"http://example.com/bar\"><head><title>Title</title></head><body><p>Hello World</p></body></html>"));
        Element html = w3cDoc.getDocumentElement();
        NamespacePrefixMapper mapper = html.getNamespaceURI() != null ? html.getNamespaceURI().createMapping("prefix", html.getNamespaceURI()) : null;
        assertEquals("http://example.com/foo", mapper.getNamespaceURI("foo"));
        assertEquals("http://example.com/bar", mapper.getNamespaceURI("bar"));
    }

    @Test
    public void testTextNodes() {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><head><title>Title</title></head><body><p>Hello World<script>alert('Hi')</script></p></body></html>"));
        Element p = (Element) w3cDoc.getElementsByTagName("p").item(0);
        Text text = (Text) p.getFirstChild();
        assertEquals("Hello World", text.getData());
    }

    @Test
    public void testCommentNodes() {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><head><!-- This is a comment --></head><body></body></html>"));
        Comment comment = (Comment) w3cDoc.getElementsByTagName("comment").item(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testDataNodes() {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><head></head><body><script type=\"text/javascript\">var s=\"Hello World\";</script></body></html>"));
        Node node = w3cDoc.getElementsByTagName("script").item(0);
        Text dataNode = (Text) node.getFirstChild();
        assertEquals("var s=\"Hello World\";", dataNode.getData());
    }

    @Test
    public void testEmptyDocument() {
        Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html></html>"));
        assertEquals("", new W3CDom().asString(w3cDoc));
    }
}