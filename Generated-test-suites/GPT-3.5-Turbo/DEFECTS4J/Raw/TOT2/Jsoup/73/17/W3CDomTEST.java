import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Typical use case
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("Hello World", ((Element)w3cDoc.getElementsByTagName("h1").item(0)).getTextContent());

        // Edge case
        jsoupDoc = new org.jsoup.nodes.Document("<empty></empty>");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals(0, w3cDoc.getElementsByTagName("empty").getLength());

        // Error scenario - null input
        assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.fromJsoup(null);
        });
    }

    @Test
    public void testConvert() {
        // Typical use case
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("Hello World", ((Element)w3cDoc.getElementsByTagName("h1").item(0)).getTextContent());

        // Edge case
        jsoupDoc = new org.jsoup.nodes.Document("<html></html>");
        w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getNodeName());

        // Error scenario - null input
        assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.convert(null, w3cDoc);
        });
    }

    @Test
    public void testAsString() {
        // Typical use case
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        String result = w3cDom.asString(w3cDoc);
        
        assertNotNull(result);
        assertTrue(result.contains("<h1>Hello World</h1>"));

        // Edge case
        w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        
        String emptyResult = w3cDom.asString(w3cDoc);
        
        assertNotNull(emptyResult);
        assertEquals("", emptyResult);

        // Error scenario - null input
        assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.asString(null);
        });
    }
}