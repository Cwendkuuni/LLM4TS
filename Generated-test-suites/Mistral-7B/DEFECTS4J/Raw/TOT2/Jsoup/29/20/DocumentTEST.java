import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.OutputSettings;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Element.*;
import static org.jsoup.nodes.Attributes.id;

public class DocumentTest {

 // Test for constructor
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 Assert.assertEquals("http://example.com", doc.baseUri());
 Assert.assertNotNull(doc.head());
 Assert.assertNotNull(doc.body());
 }

 // Test for createShell method
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head(); // check that head exists
 Element head = html.getElementsByTag("head").first(); // check that head has a head child
 Element body = html.getElementsByTag("body").first(); // check that body exists
 Assert.assertNotNull(html);
 Assert.assertNotNull(head);
 Assert.assertNotNull(body);
 }

 // Test for head and body accessor methods
 @Test
 public void testHeadAndBodyAccessors() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 Element body = doc.body();

 // Check that head and body exist
 Assert.assertNotNull(head);
 Assert.assertNotNull(body);

 // Test setting and getting title
 doc.title("Test Title");
 Assert.assertEquals("Test Title", doc.title());
 }

 // Test for createElement method
 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 Assert.assertEquals("p", p.tagName());
 Assert.assertEquals("http://example.com", p.baseUri());
 }

 // Test for normalise method
 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.appendText("Test Text");
 Element p = doc.createElement("p");
 doc.appendChild(p);
 doc.appendText("Test Text 2");

 doc.normalise();

 Element body = doc.body();
 TextNode textNode1 = (TextNode) body.child(0);
 TextNode textNode2 = (TextNode) body.child(1);

 Assert.assertEquals("Test Text 2", textNode1.text());
 Assert.assertEquals("Test Text", textNode2.text());
 }

 // Test for findFirstElementByTagName method
 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("http://example.com");
 Element html = doc.appendElement("html");
 Element head = html.appendElement("head");
 Element title = head.appendElement("title");
 title.text("Test Title");

 Element titleElement = doc.findFirstElementByTagName("title", doc);
 Assert.assertEquals("Test Title", titleElement.text());
 }

 // Test for outerHtml method
 @Test
 public void testOuterHtml() {
 Document doc = new Document("http://example.com");
 doc.appendChild(new TextNode("Test Text"));

 Assert.assertEquals("<html><head><body><text node=\"\">Test Text</text></body></html>", doc.outerHtml());
 }

 // Test for text method
 @Test
 public void testTextMethod() {
 Document doc = new Document("http://example.com");
 doc.body().text("Test Text");

 Assert.assertEquals("Test Text", doc.body().text());
 }

 // Test for nodeName method
 @Test
 public void testNodeNameMethod() {
 Document doc = new Document("http://example.com");

 Assert.assertEquals("#document", doc.nodeName());
 }

 // Test for clone method
 @Test
 public void testCloneMethod() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = new OutputSettings();
 doc.outputSettings(outputSettings);

 Document clone = doc.clone();
 OutputSettings cloneOutputSettings = clone.outputSettings();

 Assert.assertNotSame(outputSettings, cloneOutputSettings);
 }

 // Test for QuirksMode enum
 @Test
 public void testQuirksMode() {
 Document doc = new Document("http://example.com");
 doc.quirksMode(Document.QuirksMode.quirks);

 Assert.assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
 }
}