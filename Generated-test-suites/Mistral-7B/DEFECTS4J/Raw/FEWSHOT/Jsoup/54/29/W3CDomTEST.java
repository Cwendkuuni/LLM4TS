package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.rs.XMLNames;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head/><body><p>Test</p></body></html>");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertEquals(jsoupDoc.tagName(), w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html xmlns:foo=\"http://foo.com\"><head/><body><p xmlns:bar=\"http://bar.com\">Test</p></body></html>");
        Document w3cDoc = new Document();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        Element html = w3cDoc.getDocumentElement();
        assertNotNull(html);
        assertEquals("html", html.getTagName());

        Element head = (Element) html.getElementsByTagName("head").item(0);
        assertNull(head);

        Element body = (Element) html.getElementsByTagName("body").item(0);
        assertNotNull(body);
        assertEquals("body", body.getTagName());

        Element p = (Element) body.getElementsByTagName("p").item(0);
        assertNotNull(p);
        assertEquals("p", p.getTagName());

        assertEquals("Test", p.getTextContent());

        String namespaceFoo = html.getNamespaceURI();
        assertSame(namespaceFoo, "http://foo.com");

        String namespaceBar = p.getNamespaceURI();
        assertSame(namespaceBar, "http://bar.com");

        assertEquals("foo:html", html.getTagName(XMLNames.XML_NS_PREFIX_NS));
        assertEquals("bar:p", p.getTagName(XMLNames.XML_NS_PREFIX_NS));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element html = w3cDoc.createElement("html");
        html.setAttribute("xmlns:xhtml", "http://www.w3.org/1999/xhtml");
        w3cDoc.appendChild(html);
        Element head = w3cDoc.createElement("head");
        html.appendChild(head);
        Element body = w3cDoc.createElement("body");
        html.appendChild(body);
        Text text = w3cDoc.createTextNode("Test");
        body.appendChild(text);

        Element comment = w3cDoc.createComment("Test Comment");
        body.appendChild(comment);

        new W3CDom().convert(w3cDoc, w3cDoc);

        String w3cString = new W3CDom().asString(w3cDoc);
        assertEquals("<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><head/><body><p>Test</p><!--Test Comment--></body></html>", w3cString);
    }
}