import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvert() {
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<div><span>Hello</span></div>");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("div", rootElement.getTagName());
        assertNotNull(rootElement.getElementsByTagName("span").item(0));
    }

    @Test
    public void testAsString() {
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<h1>Title</h1>");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        String w3cString = w3CDom.asString(w3cDoc);
        assertNotNull(w3cString);
        assertTrue(w3cString.contains("Title"));
    }
}