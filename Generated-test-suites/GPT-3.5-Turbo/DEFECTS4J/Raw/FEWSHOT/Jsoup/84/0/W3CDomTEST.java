package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoupNotNull() {
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNull() {
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvertRootElement() {
        String html = "<html><body></body></html>";
        Document jsoupDoc = Parser.parse(html, "");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvertTextNode() {
        String html = "<html><body>Text node</body></html>";
        Document jsoupDoc = Parser.parse(html, "");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Text textNode = (Text) w3cDoc.getDocumentElement().getFirstChild().getFirstChild();
        assertEquals("Text node", textNode.getData());
    }

    @Test
    public void testConvertCommentNode() {
        String html = "<html><body><!-- Comment --></body></html>";
        Document jsoupDoc = Parser.parse(html, "");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Comment commentNode = (Comment) w3cDoc.getDocumentElement().getFirstChild().getFirstChild();
        assertEquals(" Comment ", commentNode.getData());
    }

    @Test
    public void testConvertEmptyElement() {
        String html = "<html><body><hr></body></html>";
        Document jsoupDoc = Parser.parse(html, "");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Element hrElement = (Element) w3cDoc.getDocumentElement().getFirstChild().getFirstChild();
        assertEquals("hr", hrElement.getTagName());
        assertTrue(hrElement.hasChildNodes());
    }

    @Test
    public void testConvertAttributes() {
        String html = "<html><body><div id=\"divId\" class=\"divClass\"></div></body></html>";
        Document jsoupDoc = Parser.parse(html, "");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Element divElement = (Element) w3cDoc.getDocumentElement().getFirstChild().getFirstChild();
        assertEquals("divId", divElement.getAttribute("id"));
        assertEquals("divClass", divElement.getAttribute("class"));
    }

    @Test
    public void testAsString() {
        String html = "<html><body><div></div></body></html>";
        Document jsoupDoc = Parser.parse(html, "");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String w3cDocAsString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cDocAsString);
        assertTrue(w3cDocAsString.contains("<div/>"));
    }
}