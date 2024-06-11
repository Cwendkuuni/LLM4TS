import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;

public class W3CDomTest {

    private W3CDom w3CDom;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.convert(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testAsString() {
        Document w3cDoc = new Document();
        String result = w3CDom.asString(w3cDoc);
        assertNotNull(result);
    }

}