import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private W3CDom w3cDom;
    private Document jsoupDoc;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head></head><body><div id='content'>Hello World</div></body></html>");
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDocument);
        assertEquals("document", w3cDocument.getNodeName());
    }

    @Test
    public void testConvert() {
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);
        Document newW3cDoc = w3cDom.convert(jsoupDoc, w3cDocument);
        assertNotNull(newW3cDoc);
        assertEquals("Hello World", newW3cDoc.getElementById("content").getTextContent());
    }

    @Test
    public void testW3CBuilderHead() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(jsoupDoc.body(), 0);
        Element element = w3cDoc.getDocumentElement();
        assertNotNull(element);
        assertEquals("body", element.getTagName());
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDoc);
        String strDoc = w3cDom.asString(w3cDocument);
        assertNotNull(strDoc);
        assertTrue(strDoc.contains("<html>"));
    }
}