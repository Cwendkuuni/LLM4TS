import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.nodes.Document;
import org.jsoup.helper.W3CDom;

public class W3CDomTest {
    
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        // Typical use case test
        Document jsoupDoc = new Document();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        
        // Edge case test
        jsoupDoc = null;
        assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.fromJsoup(jsoupDoc);
        });
        
        // Exception handling test
        jsoupDoc = new Document();
        jsoupDoc.location("http://example.com");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert() {
        // Typical use case test
        Document jsoupDoc = new Document();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Edge case test
        jsoupDoc = new Document();
        jsoupDoc.location("http://example.com");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Exception handling test
        jsoupDoc = null;
        Document w3cDoc = null;
        assertThrows(IllegalStateException.class, () -> {
            w3cDom.convert(jsoupDoc, w3cDoc);
        });
    }

    @Test
    public void testAsString() {
        // Typical use case test
        Document w3cDoc = new W3CDom().fromJsoup(new Document());
        String xmlString = w3cDom.asString(w3cDoc);
        assertNotNull(xmlString);
        
        // Edge case test
        w3cDoc = null;
        assertThrows(IllegalArgumentException.class, () -> {
            w3cDom.asString(w3cDoc);
        });
    }
}