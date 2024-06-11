
import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

public class W3CDomTest {

    private W3CDom w3cDom;
    private Document jsoupDocument;
    private org.w3c.dom.Document w3cDocument;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDocument = Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        w3cDocument = w3cDom.fromJsoup(jsoupDocument);
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.w3c.dom.Element rootElement = w3cDocument.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testAsString() {
        String w3cString = w3cDom.asString(w3cDocument);
        assertNotNull(w3cString);
        assertTrue(w3cString.contains("<html>"));
        assertTrue(w3cString.contains("<body>"));
        assertTrue(w3cString.contains("<h1>"));
    }

}
