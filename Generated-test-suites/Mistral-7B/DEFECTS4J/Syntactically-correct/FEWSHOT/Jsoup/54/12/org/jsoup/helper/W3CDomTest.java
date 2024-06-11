package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws Exception {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"container\">\n" +
                "<p id=\"p1\">Hello World!</p>\n" +
                "<script id=\"script1\">console.log('Script loaded.');</script>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        Document jsoupDocument = Jsoup.parse(html);
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        assertEquals("HTMLDocument", w3cDocument.getNodeName());
        assertNotNull(w3cDocument.getDocumentElement());
        assertEquals("html", w3cDocument.getDocumentElement().getNodeName());

        Element container = w3cDocument.getElementsByTagName("div").item(0);
        assertNotNull(container);
        assertEquals("div", container.getNodeName());
        assertEquals("container", container.getAttribute("id"));

        Element p1 = w3cDocument.getElementsByTagName("p").item(0);
        assertNotNull(p1);
        assertEquals("p", p1.getNodeName());
        assertEquals("p1", p1.getAttribute("id"));

        Text textNode = (Text) w3cDocument.getElementsByTagName("script").item(0).getFirstChild();
        assertNotNull(textNode);
        assertEquals("Script loaded.", textNode.getData());

        Comment comment = (Comment) w3cDocument.getElementsByTagName("script").item(0).getPreviousSibling();
        assertNotNull(comment);
        assertEquals("<!-- -->", comment.getNodeValue());

        assertEquals(html, new W3CDom().asString(w3cDocument));
    }

    @Test
    public void testEmptyDocument() throws Exception {
        Document emptyJsoupDocument = Jsoup.parse("");
        Document w3cEmptyDocument = new W3CDom().fromJsoup(emptyJsoupDocument);

        assertEquals("Document", w3cEmptyDocument.getNodeName());
        assertNull(w3cEmptyDocument.getDocumentElement());
        assertEquals(0, w3cEmptyDocument.getChildNodes().getLength());
    }

}