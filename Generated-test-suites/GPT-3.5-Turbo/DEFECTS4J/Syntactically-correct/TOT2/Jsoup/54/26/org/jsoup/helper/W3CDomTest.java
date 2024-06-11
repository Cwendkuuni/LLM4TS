import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        Document jsoupDoc = Jsoup.parse("<html><body><div>Hello World</div></body></html>");
        W3CDom w3CDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvert() {
        Document jsoupDoc = Jsoup.parse("<html><body><div>Hello World</div></body></html>");
        W3CDom w3CDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        Element bodyElement = (Element) rootElement.getElementsByTagName("body").item(0);
        assertEquals("body", bodyElement.getTagName());
    }

    @Test
    public void testAsString() {
        Document jsoupDoc = Jsoup.parse("<html><body><div>Hello World</div></body></html>");
        W3CDom w3CDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        String w3cString = w3CDom.asString(w3cDoc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-16\" standalone=\"no\"?><html><body><div>Hello World</div></body></html>", w3cString);
    }
}