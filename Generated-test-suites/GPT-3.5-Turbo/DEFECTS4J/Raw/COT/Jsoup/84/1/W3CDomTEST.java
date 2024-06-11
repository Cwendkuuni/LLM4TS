import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {
    private W3CDom w3cDom;
    private Document jsoupDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head></head><body><div id='content'>Hello World</div></body></html>");
    }

    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("HTML", w3cDoc.getNodeName());
    }

    @Test
    public void testConvert() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupElement = jsoupDoc.select("#content").first();
        org.w3c.dom.Element w3cElement = w3cDoc.createElement("div");
        w3cDom.convert(jsoupElement, w3cElement);

        assertNotNull(w3cElement);
        assertEquals("div", w3cElement.getNodeName());
        assertEquals("content", w3cElement.getAttribute("id"));
    }

    @Test
    public void testConvertTextNode() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.TextNode jsoupTextNode = (org.jsoup.nodes.TextNode) jsoupDoc.select("#content").first().childNode(0);
        org.w3c.dom.Text w3cText = w3cDoc.createTextNode(jsoupTextNode.getWholeText());

        assertNotNull(w3cText);
        assertEquals("Hello World", w3cText.getWholeText());
    }

    @Test
    public void testConvertCommentNode() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Comment jsoupComment = (org.jsoup.nodes.Comment) jsoupDoc.select("#content").first().childNode(0);
        org.w3c.dom.Comment w3cComment = w3cDoc.createComment(jsoupComment.getData());

        assertNotNull(w3cComment);
        assertEquals("Hello", w3cComment.getData());
    }

    @Test
    public void testConvertDataNode() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.DataNode jsoupDataNode = (org.jsoup.nodes.DataNode) jsoupDoc.select("#content").first().childNode(0);
        org.w3c.dom.Text w3cDataNode = w3cDoc.createTextNode(jsoupDataNode.getWholeData());

        assertNotNull(w3cDataNode);
        assertEquals("Hello World", w3cDataNode.getWholeData());
    }

    @Test
    public void testCopyAttributes() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupElement = jsoupDoc.select("#content").first();
        org.w3c.dom.Element w3cElement = w3cDoc.createElement("div");
        w3cDom.convert(jsoupElement, w3cElement);

        assertNotNull(w3cElement);
        assertEquals("content", w3cElement.getAttribute("id"));
    }

    @Test
    public void testUpdateNamespaces() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element jsoupElement = jsoupDoc.select("#content").first();
        org.w3c.dom.Element w3cElement = w3cDoc.createElement("div");
        String prefix = w3cDom.updateNamespaces(jsoupElement);

        assertNotNull(w3cElement);
        assertEquals("", prefix);
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Text w3cText = w3cDoc.createTextNode("Hello World");

        String w3cDocStr = w3cDom.asString(w3cDoc);
        String w3cTextStr = w3cDom.asString(w3cText);

        assertNotNull(w3cDocStr);
        assertNotNull(w3cTextStr);
        assertEquals("<html><head></head><body><div id=\"content\">Hello World</div></body></html>", w3cDocStr);
        assertEquals("Hello World", w3cTextStr);
    }
}