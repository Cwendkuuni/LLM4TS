import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.jsoup.helper.W3CDom;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull("W3C document should not be null", w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull("W3C document should not be null", w3cDoc);
    }

    @Test
    public void testW3CBuilder() {
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        Element element = w3cDoc.createElement("testElement");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
        w3cBuilder.head(jsoupElement, 0);
        NodeList childNodes = element.getChildNodes();
        assertEquals("Child nodes should be added", 1, childNodes.getLength());
    }

    @Test
    public void testAsString() {
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element element = w3cDoc.createElement("testElement");
        Text textNode = w3cDoc.createTextNode("testText");
        element.appendChild(textNode);
        W3CDom w3cDom = new W3CDom();
        String result = w3cDom.asString(w3cDoc);
        assertNotNull("Result should not be null", result);
    }

}