import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DocumentTest {

 // Test for constructor
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 }

 // Test for createShell method
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertEquals("head", html.tagName());
 }

 // Test for head and body accessor methods
 @Test
 public void testHeadAndBodyMethods() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 assertEquals("head", head.tagName());
 Element body = doc.body();
 assertEquals("body", body.tagName());
 }

 // Test for title method
 @Test
 public void testTitleMethod() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 // Test for title method with empty title
 @Test
 public void testTitleMethodWithEmptyTitle() {
 Document doc = new Document("http://example.com");
 assertEquals("", doc.title());
 }

 // Test for createElement method
 @Test
 public void testCreateElementMethod() {
 Document doc = new Document("http://example.com");
 Element newElement = doc.createElement("div");
 assertEquals("div", newElement.tagName());
 }

 // Test for normalise method
 @Test
 public void testNormaliseMethod() {
 Document doc = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body><head>Test</head></body></html>");
 doc.normalise();
 Element body = doc.body();
 Element head = doc.getElementsByTag("head").first();
 assertFalse(head.childNodes().hasText());
 assertTrue(body.childNodes().get(0) instanceof TextNode);
 }

 // Test for findFirstElementByTagName method
 @Test
 public void testFindFirstElementByTagNameMethod() {
 Document doc = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><body><head></head></body></html>");
 Element head = doc.findFirstElementByTagName("head", doc.body());
 assertSame(doc.head(), head);
 }

 // Test for outerHtml method
 @Test
 public void testOuterHtmlMethod() {
 Document doc = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>");
 assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
 }

 // Test for text method
 @Test
 public void testTextMethod() {
 Document doc = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>");
 doc.text("Test Text");
 assertEquals("Test Text", doc.outerHtml());
 }

 // Test for nodeName method
 @Test
 public void testNodeNameMethod() {
 Document doc = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>");
 assertEquals("#document", doc.nodeName());
 }

 // Test for OutputSettings class
 @Test
 public void testOutputSettingsClass() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = doc.outputSettings();
 assertNull(settings.escapeMode());
 assertSame(Charset.forName("UTF-8"), settings.charset());
 assertTrue(settings.prettyPrint());
 assertTrue(settings.indentAmount() > 0);
 }

 // Test for setters in OutputSettings class
 @Test
 public void testOutputSettingsSetters() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = doc.outputSettings();
 settings.escapeMode(Entities.EscapeMode.extended);
 settings.charset(Charset.forName("ISO-8859-1"));
 settings.prettyPrint(false);
 settings.indentAmount(0);
 assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
 assertSame(Charset.forName("ISO-8859-1"), settings.charset());
 assertFalse(settings.prettyPrint());
 assertEqual(0, settings.indentAmount());
 }

 // Test for exception handling in title method when title is null
 @Test(expected = NullPointerException.class)
 public void testTitleMethodNullTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }
}