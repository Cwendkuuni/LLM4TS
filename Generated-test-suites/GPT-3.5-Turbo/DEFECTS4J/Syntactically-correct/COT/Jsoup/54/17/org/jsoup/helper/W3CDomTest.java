import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document convertedDoc = w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals(w3cDoc, convertedDoc);
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cString);
    }

}