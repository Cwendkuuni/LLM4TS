
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("html", "<html></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("html", "<html></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Document out = null;
        w3cDom.convert(jsoupDoc, out);

        assertNotNull(out);
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("html", "<html></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String xmlString = w3cDom.asString(w3cDoc);

        assertNotNull(xmlString);
    }
}
