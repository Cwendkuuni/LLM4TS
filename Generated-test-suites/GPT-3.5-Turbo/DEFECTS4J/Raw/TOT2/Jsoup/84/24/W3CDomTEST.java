import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        Element root = w3cDoc.getDocumentElement();
        assertEquals("html", root.getTagName());
        assertEquals("Head", root.getElementsByTagName("head").item(0).getTagName());
        assertEquals("body", root.getElementsByTagName("body").item(0).getTagName());
    }

    @Test
    public void testConvert() {
        Document jsoupDoc = Jsoup.parse("<html><head></head><body><p>Hello World!</p></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getNamespaceURI());
        assertEquals("Hello World!", w3cDoc.getElementsByTagName("p").item(0).getTextContent());
    }

    @Test
    public void testAsString() {
        Document jsoupDoc = Jsoup.parse("<html><head></head><body><p>Hello World!</p></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertEquals("<html><head/><body><p>Hello World!</p></body></html>", w3cDom.asString(w3cDoc));
    }
}