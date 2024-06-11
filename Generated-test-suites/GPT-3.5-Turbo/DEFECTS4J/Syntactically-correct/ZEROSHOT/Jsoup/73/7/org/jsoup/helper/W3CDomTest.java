import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {
    private Document jsoupDoc;
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Hello World</h1></body></html>");
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoupNotNull() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvertDocumentURI() {
        String url = "http://www.example.com";
        jsoupDoc.setBaseUri(url);
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals(url, w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvertRootElement() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head></head><body><h1>Hello World</h1></body></html>";
        assertEquals(expected, w3cDom.asString(w3cDoc));
    }
}