import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document originalDocument;

 @Before
 public void setUp() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 originalDocument = Jsoup.parse("<html><body><h1 id='title'>Hello World</h1><script>alert('XSS')</script></body></html>");
 }

 @Test
 public void testCleanerConstructor() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 Document cleanedDocument = cleaner.clean(originalDocument);
 assertNotNull(cleanedDocument);
 assertFalse(cleanedDocument.select("script").text().contains("alert('XSS')"));
 }

 @Test
 public void testIsValidMethod() {
 assertFalse(cleaner.isValid(originalDocument));
 }

 @Test
 public void testCopySafeNodesWithSafeTag() {
 Element safeElement = new Element(Tag.H1, originalDocument.baseUri(), new Attributes().put("id", "title"));
 Element safeDestination = new Element(Tag.H1, originalDocument.baseUri(), new Attributes());
 int numDiscarded = cleaner.copySafeNodes(safeElement, safeDestination);
 assertEquals(0, numDiscarded);
 assertNotNull(safeDestination.text());
 assertNotNull(safeDestination.child(0).text());
 }

 @Test
 public void testCopySafeNodesWithUnsafeTag() {
 Element unsafeElement = new Element(Tag.SCRIPT, originalDocument.baseUri(), new Attributes());
 int numDiscarded = cleaner.copySafeNodes(unsafeElement, new Element(Tag.DIV, originalDocument.baseUri(), new Attributes()));
 assertEquals(1, numDiscarded);
 assertNull(unsafeElement.text());
 assertNull(unsafeElement.child(0));
 }

 @Test
 public void testCreateSafeElementWithSafeTag() {
 Element sourceElement = new Element(Tag.H1, originalDocument.baseUri(), new Attributes().put("id", "title"));
 Element destinationElement = cleaner.createSafeElement(sourceElement);
 assertEquals(sourceElement.tagName(), destinationElement.tagName());
 assertNotNull(destinationElement.text());
 assertNotNull(destinationElement.child(0).text());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateSafeElementWithNullElement() {
 cleaner.createSafeElement(null);
 }

 @Test
 public void testIsSafeTagAndIsSafeAttributeMethods() {
 assertFalse(cleaner.whitelist.isSafeTag("script", originalDocument, null));
 assertFalse(cleaner.whitelist.isSafeAttribute("title", "script", originalDocument, null));
 assertTrue(cleaner.whitelist.isSafeTag("h1", originalDocument, null));
 assertTrue(cleaner.whitelist.isSafeAttribute("id", "h1", originalDocument, null));
 }

 @Test
 public void testGetEnforcedAttributesMethod() {
 Element tagH1 = new Element(Tag.H1, originalDocument.baseUri(), new Attributes());
 List<Attribute> enforcedAttributes = cleaner.whitelist.getEnforcedAttributes("h1");
 assertNotNull(enforcedAttributes);
 assertTrue(enforcedAttributes.isEmpty());
 }
}