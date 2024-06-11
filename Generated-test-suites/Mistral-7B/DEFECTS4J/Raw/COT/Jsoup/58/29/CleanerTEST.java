import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 private Document createTestDocument() {
 return Jsoup.parse("<html><head></head><body><h1 id='test' class='test' data-test='test'>Test <script>alert('XSS');</script></h1></body></html>");
 }

 @Test
 public void testCleaner_ValidDocument() {
 // Given a valid document
 Document dirtyDocument = createTestDocument();

 // When creating a cleaner with a valid whitelist
 Whitelist validWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(validWhitelist);

 // Then the document is considered valid and cleaned correctly
 Assert.assertTrue(cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertNull(cleanDocument.body().child(0).child(0).attr("data-test"));
 Assert.assertNull(cleanDocument.body().child(0).child(0).child(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleaner_NullWhitelist() {
 // Given a null whitelist
 Whitelist nullWhitelist = null;

 // When creating a cleaner with a null whitelist
 Cleaner cleaner = new Cleaner(nullWhitelist);
 }

 @Test
 public void testCleaner_InvalidDocument() {
 // Given an invalid document
 Document dirtyDocument = createTestDocument();

 // When creating a cleaner with an invalid whitelist (one that does not allow the 'script' tag)
 Whitelist invalidWhitelist = Whitelist.simpleText();
 invalidWhitelist.addTag("script", null);
 Cleaner cleaner = new Cleaner(invalidWhitelist);

 // Then the document is considered invalid and cleaned correctly
 Assert.assertFalse(cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertNull(cleanDocument.body().child(0));
 }

 @Test
 public void testCleaner_IllegalTagInBody() {
 // Given a document with an illegal tag in the body
 Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS');</script></body></html>");

 // When creating a cleaner with a valid whitelist
 Whitelist validWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(validWhitelist);

 // Then the document is considered invalid and cleaned correctly
 Assert.assertFalse(cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertNull(cleanDocument.body().child(0));
 }

 @Test
 public void testCleaner_IllegalTagInHead() {
 // Given a document with an illegal tag in the head
 Document dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS');</script></head><body></body></html>");

 // When creating a cleaner with a valid whitelist
 Whitelist validWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(validWhitelist);

 // Then the document is considered valid but the illegal tag is removed from the head
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertEquals(0, cleanDocument.head().childNodeSize());
 }

 @Test
 public void testCleaner_EmptyDocument() {
 // Given an empty document
 Document dirtyDocument = Jsoup.parse("<html></html>");

 // When creating a cleaner with a valid whitelist
 Whitelist validWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(validWhitelist);

 // Then the document is considered valid and cleaned correctly
 Assert.assertTrue(cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertEquals(0, cleanDocument.childNodeSize());
 }

 @Test
 public void testCleaner_CleanDocumentWithWhitespace() {
 // Given a document with whitespace
 Document dirtyDocument = Jsoup.parse("<html><body> <p>Hello World</p> </body></html>");

 // When creating a cleaner with a valid whitelist
 Whitelist validWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(validWhitelist);

 // Then the document is considered valid and cleaned correctly
 // with the whitespace removed
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertEquals("<html><body><p>Hello World</p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleaner_CleanDocumentWithComments() {
 // Given a document with comments
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World<!-- This is a comment --></p></body></html>");

 // When creating a cleaner with a valid whitelist
 Whitelist validWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(validWhitelist);

 // Then the document is considered valid and cleaned correctly
 // with the comments removed
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertEquals("<html><body><p>Hello World</p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleaner_CleanDocumentWithScriptInTextNode() {
 // Given a document with a script in a text node
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World<script>alert('XSS');</script></p></body></html>");

 // When creating a cleaner with a valid whitelist
 Whitelist validWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(validWhitelist);

 // Then the document is considered invalid and cleaned correctly
 // with the script in the text node removed
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertEquals("<html><body><p>Hello World</p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleaner_CleanDocumentWithScriptInElement() {
 // Given a document with a script in an element
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><p>Hello World</p></body></html>");

 // When creating a cleaner with a valid whitelist
 Whitelist validWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(validWhitelist);

 // Then the document is considered invalid and cleaned correctly
 // with the script in the element removed
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertEquals("<html><body><p>Hello World</p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleaner_CleanDocumentWithIframe() {
 // Given a document with an iframe
 Document dirtyDocument = Jsoup.parse("<html><body><iframe src='http://evil.com'></iframe></body></html>");

 // When creating a cleaner with a valid whitelist
 Whitelist validWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(validWhitelist);

 // Then the document is considered invalid and cleaned correctly
 // with the iframe removed
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleaner_CleanDocumentWithStyle() {
 // Given a document with a style tag
 Document dirtyDocument = Jsoup.parse("<html><head><style>body { background-color: red; }</style></head><body></body></html>");

 // When creating a cleaner with a valid whitelist
 Whitelist validWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(validWhitelist);

 // Then the document is considered valid but the style tag is removed
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
 }

}