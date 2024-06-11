import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Parser.parseBodyFragment("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Assert that the conversion was successful
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Parser.parseBodyFragment("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Assert that the converted W3C document is not null
        assertNotNull(w3cDoc);
        
        // Assert that the conversion was done correctly by checking the root element
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Parser.parseBodyFragment("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Serialize the W3C document to a string
        String serializedDoc = w3cDom.asString(w3cDoc);
        
        // Assert that the serialized string is not null or empty
        assertNotNull(serializedDoc);
        assertFalse(serializedDoc.isEmpty());
    }
}