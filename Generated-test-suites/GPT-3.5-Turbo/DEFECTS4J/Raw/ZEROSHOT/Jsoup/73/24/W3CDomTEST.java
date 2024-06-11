import static org.junit.Assert.*;
import org.junit.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello World!", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);

        org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());

        NodeList headElements = rootElement.getElementsByTagName("head");
        assertEquals(1, headElements.getLength());
        Node head = headElements.item(0);

        NodeList titleElements = head.getChildNodes();
        assertEquals(1, titleElements.getLength());
        Node title = titleElements.item(0);
        assertEquals("Test", title.getTextContent());

        NodeList bodyElements = rootElement.getElementsByTagName("body");
        assertEquals(1, bodyElements.getLength());
        Node body = bodyElements.item(0);

        NodeList h1Elements = body.getElementsByTagName("h1");
        assertEquals(1, h1Elements.getLength());
        Node h1 = h1Elements.item(0);
        assertEquals("Hello World!", h1.getTextContent());
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);

        String w3cDocString = w3cDom.asString(w3cDoc);
        String expectedString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";

        assertEquals(expectedString, w3cDocString);
    }
}