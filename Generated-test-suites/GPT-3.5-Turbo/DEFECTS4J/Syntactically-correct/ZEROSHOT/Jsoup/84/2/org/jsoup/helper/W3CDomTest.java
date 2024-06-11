import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import javax.xml.parsers.DocumentBuilderFactory;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
    }
    
    @Test
    public void testFromJsoupNotNull() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document w3cDoc = factory.newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals(jsoupDoc.location(), w3cDoc.getDocumentURI());
    }
    
    @Test
    public void testAsString() {
        Document w3cDoc = new Document();
        String stringDoc = w3cDom.asString(w3cDoc);
        assertNotNull(stringDoc);
    }
}