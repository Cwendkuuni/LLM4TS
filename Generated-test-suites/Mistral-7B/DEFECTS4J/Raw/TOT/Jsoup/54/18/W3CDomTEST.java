import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

 @Test
 public void testFromJsoup() {
 // Test with a valid jsoup Document
 org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
 // Check if the conversion was successful
 assertEquals("HTML", w3cDoc.getDocumentElement().getTagName());
 // Test with an empty jsoup Document
 jsoupDoc = org.jsoup.Jsoup.parse("");
 w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
 // Check if the conversion was successful
 assertNotNull(w3cDoc);
 // Test with a jsoup Document with invalid HTML
 jsoupDoc = org.jsoup.Jsoup.parse("<html><body><script>alert('Hello World')</script></body></html>");
 try {
 new W3CDom().fromJsoup(jsoupDoc);
 fail("Expected exception, but no exception was thrown.");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 }

 @Test
 public void testConvert() {
 // Test with a valid jsoup Document and an empty W3C Document
 org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document w3cDoc = new Document();
 new W3CDom().convert(jsoupDoc, w3cDoc);
 // Check if the conversion was successful
 assertEquals("HTML", w3cDoc.getDocumentElement().getTagName());
 // Test with an empty jsoup Document and a W3C Document
 jsoupDoc = org.jsoup.Jsoup.parse("");
 Document emptyW3CDoc = new Document();
 new W3CDom().convert(jsoupDoc, emptyW3CDoc);
 // Check if no new elements were added
 assertEquals(0, emptyW3CDoc.getDocumentElement().getChildNodes().getLength());
 // Test with a jsoup Document with invalid HTML and a W3C Document
 jsoupDoc = org.jsoup.Jsoup.parse("<html><body><script>alert('Hello World')</script></body></html>");
 Document w3cDocWithError = new Document();
 try {
 new W3CDom().convert(jsoupDoc, w3cDocWithError);
 fail("Expected exception, but no exception was thrown.");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 // Check if no new elements were added to the W3C Document with an error
 assertEquals(0, w3cDocWithError.getDocumentElement().getChildNodes().getLength());
 }

 @Test
 public void testAsString() {
 // Test with a valid W3C Document
 Document w3cDoc = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>")).cloneNode(true);
 String w3cString = new W3CDom().asString(w3cDoc);
 // Check if the conversion was successful
 assertEquals("<html><body><h1>Hello World</h1></body></html>", w3cString);
 // Test with an empty W3C Document
 Document emptyW3CDoc = new Document();
 String emptyW3CString = new W3CDom().asString(emptyW3CDoc);
 // Check if an empty string was returned
 assertEquals("", emptyW3CString);
 }
}