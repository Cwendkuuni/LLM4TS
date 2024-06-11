
import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

public class W3CDomTest {
    private W3CDom w3cDom;
    private Document jsoupDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document out = w3cDoc.implementation.createDocument("", "", null);
        w3cDom.convert(jsoupDoc, out);
        assertNotNull(out);
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String str = w3cDom.asString(w3cDoc);
        assertNotNull(str);
        assertTrue(str.contains("<html>"));
    }
}
