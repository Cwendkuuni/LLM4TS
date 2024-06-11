import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        w3cDom.convert(jsoupDoc, w3cDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);

        String result = w3cDom.asString(w3cDoc);

        assertNotNull(result);
        assert(result.length() > 0);
    }
}