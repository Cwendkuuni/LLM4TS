import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Test converting jsoup Document to W3C Document
        Document jsoupDoc = new Document("");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        // Test converting jsoup Document to W3C Document
        Document jsoupDoc = new Document("");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testW3CBuilder() {
        // Test W3CBuilder class methods
        Document jsoupDoc = new Document("<html><body><div>Hello</div></body></html>");
        org.w3c.dom.Document w3cDoc = new org.jsoup.helper.W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testAsString() {
        // Test serializing W3C Document to String
        Document jsoupDoc = new Document("<html><body><div>Hello</div></body></html>");
        org.w3c.dom.Document w3cDoc = new org.jsoup.helper.W3CDom().fromJsoup(jsoupDoc);
        String w3cString = new org.jsoup.helper.W3CDom().asString(w3cDoc);

        assertNotNull(w3cString);
    }

}