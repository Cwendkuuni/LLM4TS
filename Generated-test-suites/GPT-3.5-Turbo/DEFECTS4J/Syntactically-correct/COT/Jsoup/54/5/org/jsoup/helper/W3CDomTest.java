import org.jsoup.helper.W3CDom;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        W3CDom w3CDom = new W3CDom();
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        W3CDom w3CDom = new W3CDom();
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        Document out = w3CDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(out);
        assertEquals("html", out.getDocumentElement().getNodeName());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        W3CDom w3CDom = new W3CDom();
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        String docString = w3CDom.asString(w3cDoc);
        assertNotNull(docString);
        assertTrue(docString.contains("<html>"));
        assertTrue(docString.contains("</html>"));
    }
}