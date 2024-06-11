import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.NodeList;

public class W3CDomTest {
    
    @Test
    public void testFromJsoupNotNull() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvertSetsDocumentURI() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.factory.newDocument();
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html>").location("http://example.com");
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals(w3cDoc.getDocumentURI(), "http://example.com");
    }
    
    @Test
    public void testAsStringNotNull() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.factory.newDocument();
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
    }

}