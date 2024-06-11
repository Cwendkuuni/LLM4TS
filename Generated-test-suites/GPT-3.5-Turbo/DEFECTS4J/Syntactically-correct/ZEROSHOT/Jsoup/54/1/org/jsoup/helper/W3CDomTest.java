import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;

public class W3CDomTest {

    private W3CDom w3CDom;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoupNotNull() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupNull() {
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        Document out = new Document();
        w3CDom.convert(jsoupDoc, out);
        assertEquals(w3cDoc, out);
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        String w3cString = w3CDom.asString(w3cDoc);
        assertNotNull(w3cString);
        assertTrue(w3cString.length() > 0);
    }
}