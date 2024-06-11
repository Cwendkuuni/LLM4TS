import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        
        assertTrue(xmlString.length() > 0);
    }
}