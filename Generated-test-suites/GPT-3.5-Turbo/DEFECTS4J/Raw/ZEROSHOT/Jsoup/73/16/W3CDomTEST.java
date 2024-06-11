import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private static W3CDom w3CDom;

    @BeforeClass
    public static void setUp() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoupShouldConvertJsoupDocumentToW3CDocument() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.html("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");

        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello World!", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testConvertShouldConvertJsoupDocumentToW3CDocument() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.html("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");

        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        org.w3c.dom.Document outDoc = w3CDom.convert(jsoupDoc, w3cDoc);

        assertEquals("Test", outDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello World!", outDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testAsStringShouldSerializeW3CDocumentToString() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.html("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");

        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        String result = w3CDom.asString(w3cDoc);

        assertTrue(result.contains("<title>Test</title>"));
        assertTrue(result.contains("<h1>Hello World!</h1>"));
    }

}