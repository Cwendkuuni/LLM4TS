import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.appendElement("div");
        
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.appendElement("div").text("hello");
        
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        Element root = w3cDoc.getDocumentElement();
        assertEquals("div", root.getNodeName());
        Text textNode = (Text) root.getFirstChild();
        assertEquals("hello", textNode.getNodeValue());
    }
   
    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element div = jsoupDoc.appendElement("div");

        Element w3cElement = w3cDom.fromJsoup(jsoupDoc).createElement("div");
        w3cDom.asString(w3cElement);

        assertNotNull(w3cElement);
    }
}