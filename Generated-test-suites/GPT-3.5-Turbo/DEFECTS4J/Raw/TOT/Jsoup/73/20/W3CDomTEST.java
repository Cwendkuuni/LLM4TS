import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Typical use case
        String html = "<html><head><title>Test</title></head><body></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        // Typical use case
        String html = "<html><head><title>Test</title></head><body></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        org.w3c.dom.Document result = w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(result);
    }

    @Test
    public void testAsString() {
        // Typical use case
        String html = "<html><head><title>Test</title></head><body></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);

        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
        assertEquals("<html><head><title>Test</title></head><body></body></html>", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoup_NullInput() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.fromJsoup(null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConvert_NullInput() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(null, null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAsString_NullInput() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.asString(null);
    }

}