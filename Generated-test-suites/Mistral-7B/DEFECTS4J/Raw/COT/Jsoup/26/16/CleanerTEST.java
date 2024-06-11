import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class CleanerTest {

 private final Whitelist whitelist = Whitelist.relaxed();
 private final Cleaner cleaner = new Cleaner(whitelist);

 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(null);
 assertNull(cleaner.whitelist);
 }

 @Test
 public void testCleanMethodWithValidInput() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanMethodWithInvalidInput() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('Hello World');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidMethodWithValidInput() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithInvalidInput() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('Hello World');</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsSafeTagMethod() {
 assertTrue(cleaner.whitelist.isSafeTag("h1"));
 assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttributeMethod() {
 Element element = new Element("div", null, new Attributes());
 element.attr("id", "test");
 assertTrue(cleaner.whitelist.isSafeAttribute("div", element, new Attribute("id", "test")));
 }

 @Test(expected = NullPointerException.class)
 public void testIsSafeAttributeMethodWithNullArguments() {
 assertTrue(cleaner.whitelist.isSafeAttribute(null, null, null)); // tag
 assertTrue(cleaner.whitelist.isSafeAttribute("div", null, null)); // element
 assertTrue(cleaner.whitelist.isSafeAttribute("div", new Element("div"), null)); // attribute
 }

 @Test
 public void testCopySafeNodesMethodWithEmptyInput() {
 Element source = new Element("div", null);
 Element dest = new Element("div", null);
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethodWithValidInput() {
 Element source = new Element("html", null, new Attributes().put("id", "test"));
 Element dest = new Element("html", null);
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethodWithInvalidInput() {
 Element source = new Element("html", null, new Attributes().put("onclick", "alert('Hello World')"));
 Element dest = new Element("html", null);
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCreateSafeElementMethodWithEmptyInput() {
 Element source = new Element("div", null);
 Element dest = new Element("div", null);
 ElementMeta meta = cleaner.createSafeElement(source);
 assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementMethodWithValidInput() {
 Element source = new Element("div", null, new Attributes().put("class", "test").put("id", "test"));
 Element dest = new Element("div", null);
 ElementMeta meta = cleaner.createSafeElement(source);
 assertEquals(1, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementMethodWithInvalidInput() { // "onclick" is not a safe attribute for "div" tag
 Element source = new Element("div", null, new Attributes().put("onclick", "alert('Hello World')"));
 Element dest = new Element("div", null);
 ElementMeta meta = cleaner.createSafeElement(source);
 assertEquals(1, meta.numAttribsDiscarded);
 }

}