import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document inputJsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");
        Document outputW3CDoc = W3CDom.fromJsoup(inputJsoupDoc);
        assertNotNull(outputW3CDoc);
        assertEquals("<html><body><p>Hello World</p></body></html>", W3CDom.asString(outputW3CDoc));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document inputJsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");
        Document outputW3CDoc = W3CDom.fromJsoup(inputJsoupDoc);
        Document expectedW3CDoc = DocumentBuilderFactory.newInstance().newDocument();
        W3CDom.convert(inputJsoupDoc, expectedW3CDoc);
        assertEquals(expectedW3CDoc.getDocumentElement().getTagName(), outputW3CDoc.getDocumentElement().getTagName());
        assertEquals(expectedW3CDoc.getDocumentElement().getChildNodes().getLength(), outputW3CDoc.getDocumentElement().getChildNodes().getLength());
    }

    // Add test cases for W3CBuilder and asString methods here

    // 

    // Add all test cases here

    // 
}