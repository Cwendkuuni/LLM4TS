import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CleanerTest {

 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Document.create("<html><body><div id='test'><script>alert('XSS')</script></div></body></html>");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCleanerConstruction() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 Document cleaned = cleaner.clean(dirtyDocument);
 assertNotNull(cleaned);
 assertNull(cleaned.body().child(0));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testIsValidMethod() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidValidDocument() {
 Document validDocument = Document.create("<html><body><div id='test'>Hello World</div></body></html>");
 whitelist.addTags("div", "*");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testCopySafeNodesMethod() {
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertFalse(dirtyDocument.body().child(0).toString().contains("script"));
 assertSame(cleanDocument.body().child(0).tagName(), Tag.DIV);
 }

 @Test
 public void testCopySafeNodesEmptyDocument() {
 Document emptyDocument = Document.create("");
 int numDiscarded = cleaner.copySafeNodes(emptyDocument.body(), cleanDocument.body());
 assertNull(cleanDocument.body());
 }

 @Test
 public void testCreateSafeElementMethod() {
 Element sourceElement = new Element(Tag.DIV, null, new Attributes());
 sourceElement.attributes().put("id", "test");
 sourceElement.attributes().put("style", "color:red;");
 Elementdestination = cleaner.createSafeElement(sourceElement);
 assertNotNull(destination);
 assertSame(destination.tagName(), Tag.DIV);
 assertNull(destination.attributes().get("style"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementNullElement() {
 cleaner.createSafeElement(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementInvalidTag() {
 Element sourceElement = new Element(Tag.SCRIPT, null, new Attributes());
 cleaner.createSafeElement(sourceElement);
 }

 @Test
 public void testCreateSafeElementSafeTag() {
 Element sourceElement = new Element(Tag.DIV, null, new Attributes());
 sourceElement.attributes().put("id", "test");
 sourceElement.attributes().put("style", "color:red;");
 Element destination = cleaner.createSafeElement(sourceElement);
 assertNotNull(destination);
 assertSame(destination.tagName(), Tag.DIV);
 assertNull(destination.attributes().get("style"));
 }

 @Test
 public void testCreateSafeElementWithAllowedAttributes() {
 Element sourceElement = new Element(Tag.DIV, null, new Attributes().put("id", "test").put("class", "test-class"));
 Element destination = cleaner.createSafeElement(sourceElement);
 assertNotNull(destination);
 assertSame(destination.tagName(), Tag.DIV);
 assertNotNull(destination.attributes().get("id"));
 assertNotNull(destination.attributes().get("class"));
 }

 @Test
 public void testCreateSafeElementWithDisallowedAttributes() {
 Element sourceElement = new Element(Tag.DIV, null, new Attributes().put("id", "test").put("onclick", "alert('XSS')"));
 Element destination = cleaner.createSafeElement(sourceElement);
 assertNotNull(destination);
 assertSame(destination.tagName(), Tag.DIV);
 assertNull(destination.attributes().get("onclick"));
 }

 @Test
 public void testCreateSafeElementWithEnforcedAttributes() {
 Element sourceElement = new Element(Tag.DIV, null, new Attributes().put("id", "test"));
 Element destination = cleaner.createSafeElement(sourceElement);
 whitelist.addAttributes("div", "id", "*");
 assertNotNull(destination);
 assertNotNull(destination.attributes().get("id"));
 }

}