import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup_NullInput() {
        W3CDom w3cDom = new W3CDom();
        try {
            Document doc = w3cDom.fromJsoup(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testConvert_BlankLocation() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_WithTextNode() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.appendChild(new org.w3c.dom.TextNode("Test Text"));
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("Test Text", w3cDom.asString(w3cDoc));
    }

    // Additional test cases for each method can be added here

}