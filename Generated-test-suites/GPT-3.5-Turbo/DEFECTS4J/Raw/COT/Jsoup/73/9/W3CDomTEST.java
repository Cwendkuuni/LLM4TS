
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDocument;
    private Document w3cDocument;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDocument = Jsoup.parse("<html><head><title>Test</title></head><body><div class=\"test\"><p>Hello World</p></div></body></html>");
        w3cDocument = w3cDom.fromJsoup(jsoupDocument);
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        Document newW3cDocument = w3cDom.fromJsoup(jsoupDocument);

        assertEquals(w3cDocument.getDocumentURI(), newW3cDocument.getDocumentURI());
    }

    @Test
    public void testAsString() {
        String w3cString = w3cDom.asString(w3cDocument);

        assertNotNull(w3cString);
        assertEquals("", w3cString);
    }
}
