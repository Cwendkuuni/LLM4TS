
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<!-- comment -->");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("org.w3c.dom.Comment", w3cDoc.getFirstChild().getNodeName());
        assertEquals(" comment ", ((Comment) w3cDoc.getFirstChild()).getData());
    }
    
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<div>text<p>paragraph</p></div>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("org.w3c.dom.Element", w3cDoc.getFirstChild().getNodeName());
        Element root = (Element) w3cDoc.getFirstChild();
        assertEquals("div", root.getTagName());
        assertEquals(2, root.getChildNodes().getLength());
        
        Element p = (Element) root.getElementsByTagName("p").item(0);
        assertEquals("p", p.getTagName());
        
        Text textNode = (Text) p.getFirstChild();
        assertEquals("paragraph", textNode.getTextContent());
    }
    
    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<div>text<p>paragraph</p></div>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><div>text<p>paragraph</p></div>", xmlString);
    }
}
