import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = new DocumentImpl();
        
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testAsString() {
        Document w3cDoc = new DocumentImpl();
        
        W3CDom w3cDom = new W3CDom();
        String result = w3cDom.asString(w3cDoc);
        
        assertNotNull(result);
    }
}