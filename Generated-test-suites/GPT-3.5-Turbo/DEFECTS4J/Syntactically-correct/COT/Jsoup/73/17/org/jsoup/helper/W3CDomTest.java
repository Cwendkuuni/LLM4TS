
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;

import java.io.StringWriter;

import static org.junit.Assert.*;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
    }
    
    @Test
    public void testConvert() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDoc = w3cBuilder.convert(jsoupDoc);
        
        assertNotNull(w3cDoc);
        assertEquals("Test", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
    }
    
    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        String w3cString = w3cDom.asString(w3cDoc);
        
        assertNotNull(w3cString);
        assertTrue(w3cString.contains("<title>Test</title>"));
    }
}
