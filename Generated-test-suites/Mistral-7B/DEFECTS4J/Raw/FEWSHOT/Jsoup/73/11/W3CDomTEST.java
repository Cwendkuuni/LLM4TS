package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals(jsoupDoc.tagName(), w3cDoc.getDocumentElement().getTagName());

        // Test for complex element with attributes, children and namespaces
        String xmlns = "xmlns:test";
        jsoupDoc = org.jsoup.Jsoup.parse("<html xmlns:test=\"http://test.com\" xmlns:html=\"http://w3.org/1999/xhtml\">" +
                "<html:html xml:lang=\"en\">" +
                "<head>" +
                "<title>Test Page</title>" +
                "</head>" +
                "<body>" +
                "<test:div id=\"testDiv\" class=\"testClass\">" +
                "<test:p>Hello World</test:p>" +
                "</test:div>" +
                "</body>" +
                "</html>");
        w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Element html = w3cDoc.getDocumentElement();
        assertEquals("html", html.getTagName());
        assertEquals("http://w3.org/1999/xhtml", html.getNamespaceURI());
        Element head = (Element) html.getElementsByTagName("head").item(0);
        assertEquals("head", head.getTagName());
        assertEquals("http://w3.org/1999/xhtml", head.getNamespaceURI());
        Element title = (Element) head.getElementsByTagName("title").item(0);
        assertEquals("title", title.getTagName());
        assertEquals("Test Page", title.getTextContent());
        Element div = (Element) html.getElementsByTagNameNS("test", "div").item(0);
        assertEquals("div", div.getTagName());
        assertEquals("test:div", div.getNodeName());
        assertEquals("http://test.com", div.getNamespaceURI());
        assertEquals("testDiv", div.getAttribute("id"));
        assertEquals("testClass", div.getAttribute("class"));
        Element p = (Element) div.getElementsByTagName("p").item(0);
        assertEquals("p", p.getTagName());
        assertEquals("Hello World", p.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        Document outDoc = new Document();
        new W3CDom().convert(jsoupDoc, outDoc);
        assertEquals(w3cDoc.getDocumentElement().getTagName(), outDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String w3cDocString = new W3CDom().asString(w3cDoc);
        org.jsoup.nodes.Document jsoupDocFromString = org.jsoup.Jsoup.parse(w3cDocString);
        assertEquals(jsoupDoc.html(), jsoupDocFromString.html());
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1 id=\"h1\">Hello World</h1></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        NodeVisitor nodeVisitor = new W3CDom.W3CBuilder(w3cDoc) {
            private int count = 0;

            @Override
            public void head(Node source, int depth) {
                assertEquals("h1", source.getNodeName());
                assertEquals(1, ++count);
                super.head(source, depth);
            }
        };
        new W3CDom().convert(jsoupDoc, w3cDoc, nodeVisitor);
        assertEquals(1, count);
    }

    @Test
    public void testW3CBuilderWithComment() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><!-- Test Comment --><body></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        NodeVisitor nodeVisitor = new NodeVisitor() {
            private int commentCount = 0;

            @Override
            public void head(Node source, int depth) {
                if (source instanceof Comment) {
                    assertEquals("Test Comment", ((Comment) source).getData());
                    assertEquals(1, ++commentCount);
                }
                super.head(source, depth);
            }
        };
        new W3CDom().convert(jsoupDoc, w3cDoc, nodeVisitor);
        assertEquals(1, commentCount);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertWithNullDocument() {
        new W3CDom().convert(null, new Document());
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupWithNullDocument() {
        new W3CDom().fromJsoup(null);
    }
}