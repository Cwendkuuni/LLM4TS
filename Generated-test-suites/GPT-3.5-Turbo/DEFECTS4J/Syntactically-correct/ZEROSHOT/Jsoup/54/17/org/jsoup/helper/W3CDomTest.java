import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class W3CDomTest {
    
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);        
    }
    
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        org.w3c.dom.Document outputDoc = w3cDom.convert(jsoupDoc, w3cDoc);
        
        assertNotNull(outputDoc);        
    }
    
    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body><h1>Test</h1></body></html>");
        
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        String outputString = w3cDom.asString(w3cDoc);
        
        assertNotNull(outputString);
    }
}