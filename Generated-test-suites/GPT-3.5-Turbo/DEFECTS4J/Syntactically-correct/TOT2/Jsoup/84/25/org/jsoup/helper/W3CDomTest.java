import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.junit.Test;
import org.w3c.dom.*;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("<html><body></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Check if the conversion is successful
        assertEquals("Document should not be null", true, w3cDoc != null);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("<html><body></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Check if the conversion is successful
        assertEquals("Document should not be null", true, w3cDoc != null);

        w3cDom.convert(jsoupDoc, w3cDoc);

        // Check if the conversion process is completed
        assertEquals("Conversion should be completed", true, w3cDoc.getChildNodes().getLength() > 0);
    }

    @Test
    public void testW3CBuilder() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document w3cDoc = db.newDocument();
        
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        Element testElement = w3cDoc.createElement("test");
        TextNode textNode = new TextNode("test text", "");

        // Test head method
        w3cBuilder.head(testElement, 1);
        w3cBuilder.head(textNode, 1);

        // Test tail method
        w3cBuilder.tail(testElement, 1);

        // Check if the element is successfully created and modified
        assertEquals("Element should be created", true, testElement.getChildNodes().getLength() > 0);
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("<html><body></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Check if the conversion is successful
        assertEquals("Document should not be null", true, w3cDoc != null);

        w3cDom.convert(jsoupDoc, w3cDoc);

        String docString = w3cDom.asString(w3cDoc);

        // Check if the document is successfully serialized to a string
        assertEquals("Document should be serialized to a string", true, docString.length() > 0);
    }

}