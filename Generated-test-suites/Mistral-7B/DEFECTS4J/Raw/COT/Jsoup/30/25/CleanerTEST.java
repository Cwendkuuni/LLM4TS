import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.Validate;

public class CleanerTest {

 private Document createTestDocument() {
 return Jsoup.parse("<html><body><div id='test' ><h1>Test <script>alert('XSS')</script></h1></div></body></html>");
 }

 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 assert (cleaner != null);
 }

 @Test
 public void testCleanMethod() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Document dirtyDocument = createTestDocument();
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertFalse(cleanDocument.body().hasText()); // checks if body is empty
 Assert.assertFalse(cleanDocument.body().hasMetaCharset()); // checks if meta charset is removed
 Assert.assertFalse(cleanDocument.body().hasScripts()); // checks if scripts are removed

 Element testDiv = cleanDocument.body().getElementById("test");
 Assert.assertTrue(testDiv != null); // checks if element with id "test" is present
 Assert.assertFalse(testDiv.hasText()); // checks if text inside the element is removed
 Assert.assertFalse(testDiv.hasAttributes()); // checks if attributes of the element are removed
 }

 @Test
 public void testIsValidMethod_ValidDocument() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Document validDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
 Assert.assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testIsValidMethod_InvalidDocument() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Document invalidDocument = createTestDocument();
 Assert.assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidMethod_NullDocument() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Assert.assertFalse(cleaner.isValid(null));
 }

 @Test
 public void testCopySafeNodesMethod_EmptyRoot() {
 Element root = new Element("html");
 int numDiscarded = Cleaner.copySafeNodes(root, null);
 Assert.assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethod_SafeElement() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Element root = new Element("div");
 root.classes("test");
 Element destination = new Element("body");
 int numDiscarded = Cleaner.copySafeNodes(root, destination);

 Assert.assertEquals(0, numDiscarded);
 Assert.assertEquals(1, destination.childrenSize());
 Assert.assertEquals("div", destination.child(0).tagName());
 Assert.assertEquals("test", destination.child(0).classes().get(0));
 }

 @Test
 public void testCopySafeNodesMethod_UnsafeElement() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Element root = new Element("script");
 Element destination = new Element("body");
 int numDiscarded = Cleaner.copySafeNodes(root, destination);

 Assert.assertEquals(1, numDiscarded);
 Assert.assertEquals(0, destination.childrenSize());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesMethod_NullDestination() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Element root = new Element("div");
 int numDiscarded = Cleaner.copySafeNodes(root, null);
 }

 @Test
 public void testCreateSafeElementMethod_SafeAttribute() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Element sourceEl = new Element("div").addClass("test").attr("data-test", "value");
 ElementMeta meta = cleaner.createSafeElement(sourceEl);

 Assert.assertEquals("div", meta.el.tagName());
 Assert.assertEquals(2, meta.el.childrenSize());
 Assert.assertEquals("test", meta.el.classes().get(0));
 Assert.assertEquals("value", meta.el.attr("data-test"));
 }

 @Test
 public void testCreateSafeElementMethod_UnsafeAttribute() {
 Cleaner cleaner = new Cleaner(Whitelist.DEFAULT);
 Element sourceEl = new Element("div").addClass("test").attr("onclick", "alert('XSS')");
 ElementMeta meta = cleaner.createSafeElement(sourceEl);

 Assert.assertEquals("div", meta.el.tagName());
 Assert.assertEquals(1, meta.el.childrenSize());
 Assert.assertEquals(0, meta.numAttribsDiscarded);
 }

}