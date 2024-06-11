import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.append("<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>");

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("Test Document", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello World!", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.append("<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>");

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Document outW3cDoc = w3cDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(outW3cDoc);
        assertEquals("Test Document", outW3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello World!", outW3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.append("<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>");

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String strW3cDoc = w3cDom.asString(w3cDoc);

        String expectedStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>";

        assertEquals(expectedStr, strW3cDoc);
    }

}