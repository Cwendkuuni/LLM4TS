
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        
        // Create a basic jsoup document for testing
        Document jsoupDoc = Document.createShell("");
        jsoupDoc.head().appendElement("title").appendText("Test Document");
        jsoupDoc.body().appendElement("h1").appendText("Hello, World!");
        
        // Convert jsoup document to W3C document
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        // Ensure that the converted document has the correct title and heading
        assertEquals("Test Document", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello, World!", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }
    
    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        
        // Create a basic jsoup document for testing
        Document jsoupDoc = Document.createShell("");
        jsoupDoc.head().appendElement("title").appendText("Test Document");
        jsoupDoc.body().appendElement("h1").appendText("Hello, World!");
        
        // Create a new W3C document
        org.w3c.dom.Document w3cDoc = w3cDom.factory.newDocumentBuilder().newDocument();
        
        // Convert the jsoup document to the W3C document
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Ensure that the converted document has the correct title and heading
        assertEquals("Test Document", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
        assertEquals("Hello, World!", w3cDoc.getElementsByTagName("h1").item(0).getTextContent());
    }
    
    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        
        // Create a basic W3C document for testing
        org.w3c.dom.Document w3cDoc = w3cDom.factory.newDocumentBuilder().newDocument();
        Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);
        Element child = w3cDoc.createElement("child");
        child.setTextContent("Test Element");
        root.appendChild(child);
        
        // Serialize the W3C document to a string
        String docString = w3cDom.asString(w3cDoc);
        
        // Ensure that the serialized string contains the root element and child element
        assertTrue(docString.contains("<root>"));
        assertTrue(docString.contains("<child>Test Element</child>"));
    }
}
