import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");

        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        NodeList nodeList = w3cDoc.getElementsByTagName("title");
        assertEquals(1, nodeList.getLength());
        assertEquals("Test", nodeList.item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");

        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);

        NodeList nodeList = w3cDoc.getElementsByTagName("p");
        assertEquals(1, nodeList.getLength());
        assertEquals("Hello World", ((Text) nodeList.item(0).getFirstChild()).getData());
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");

        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);

        String w3cString = w3cDom.asString(w3cDoc);

        assertNotNull(w3cString);
        assertTrue(w3cString.contains("Hello World"));
    }
}