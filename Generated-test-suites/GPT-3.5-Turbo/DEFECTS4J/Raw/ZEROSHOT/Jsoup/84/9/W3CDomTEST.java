import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    private W3CDom w3CDom;

    @Before
    public void setup() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.Element jsoupEl = jsoupDoc.createElement("div");
        w3CDom.convert(jsoupDoc, w3cDoc);

        Element w3cEl = w3cDoc.createElement("div");

        assertNotNull(w3cEl);
    }

    @Test
    public void testAsString() {
        Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        String w3cDocString = w3CDom.asString(w3cDoc);

        assertNotNull(w3cDocString);
    }
}