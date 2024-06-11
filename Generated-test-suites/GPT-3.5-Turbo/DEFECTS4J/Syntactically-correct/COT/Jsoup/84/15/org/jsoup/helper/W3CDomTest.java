
import org.jsoup.nodes.Document;
import org.jsoup.helper.W3CDom;
import org.jsoup.Jsoup;

import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void fromJsoupTest() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello, world!", w3cDoc.getElementsByTagName("p").item(0).getTextContent());
    }

    @Test
    public void convertTest() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Document convertedDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        assertSame(w3cDoc, convertedDoc);
    }

    @Test
    public void asStringTest() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String w3cDocStr = w3cDom.asString(w3cDoc);

        assertNotNull(w3cDocStr);
        assertTrue(w3cDocStr.contains("<title>Test</title>"));
        assertTrue(w3cDocStr.contains("<p>Hello, world!</p>"));
    }

}
