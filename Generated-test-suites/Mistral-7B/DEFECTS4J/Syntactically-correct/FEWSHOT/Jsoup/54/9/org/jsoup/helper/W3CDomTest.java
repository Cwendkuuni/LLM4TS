package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Comment;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", W3CDom.asString(w3cDoc));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Hello</title></head><body><p id='p1'>Hello World</p></body></html>");
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        W3CDom.convert(jsoupDoc, w3cDoc);

        Element html = (Element) w3cDoc.getDocumentElement();
        assertEquals("html", html.getTagName());
        Element head = (Element) html.getElementsByTagName("head").item(0);
        Element title = (Element) head.getElementsByTagName("title").item(0);
        assertEquals("title", title.getTagName());
        assertEquals("Hello", title.getTextContent());
        Element body = (Element) html.getElementsByTagName("body").item(0);
        Element p = (Element) body.getElementsByTagName("p").item(0);
        assertEquals("p", p.getTagName());
        assertEquals("Hello World", p.getTextContent());
        assertEquals("p1", p.getAttribute("id"));

        Text textNode = (Text) body.getElementsByTagName("p").item(0).getFirstChild();
        assertEquals("Hello World", textNode.getNodeValue());

        Comment comment = (Comment) body.getElementsByTagName("!--comment--").item(0);
        assertEquals("!--comment--", comment.getNodeValue());

        DocumentType docType = (DocumentType) w3cDoc.getDoctype();
        assertNotNull(docType);
        assertEquals("html", docType.getName());
        assertEquals("-//W3C//DTD HTML 4.0 Transitional//EN", docType.getPublicId());
        assertEquals("http://www.w3.org/TR/REC-html40/loose.dtd", docType.getSystemId());
    }

    @Test
    public void testNullJsoupDoc() {
        assertNull(W3CDom.fromJsoup(null));
    }

    @Test
    public void testEmptyJsoupDoc() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("");
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("", W3CDom.asString(w3cDoc));
    }

}