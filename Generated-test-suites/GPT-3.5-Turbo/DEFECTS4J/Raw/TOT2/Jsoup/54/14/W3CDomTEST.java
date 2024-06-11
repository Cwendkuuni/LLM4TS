import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {
    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
    }

    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, w3cDoc);
        Elements elements = w3cDoc.select("body");
        assertEquals(0, elements.size());
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        String asString = w3CDom.asString(w3cDoc);
        assertNotNull(asString);
    }
}