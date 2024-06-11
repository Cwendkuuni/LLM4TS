import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.CleaningVisitor;

public class CleanerTest {

 private final Whitelist whitelist = Whitelist.simpleText();
 private final Cleaner cleaner = new Cleaner(whitelist);

 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(whitelist);
 Assert.assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod_ValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanMethod_InvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidMethod_ValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Assert.assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethod_InvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><h1>Hello World</h1></body></html>");
 Assert.assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanMethod_NullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testCleaningVisitor_SafeTag() {
 Element source = Jsoup.parse("<html><body><h1 id='test' class='test'>Hello World</h1></body></html>").body().getChild(0);
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, null);
 cleaningVisitor.head(source, 0);
 Assert.assertNotNull(cleaningVisitor.destination);
 Assert.assertEquals("h1", cleaningVisitor.destination.tagName());
 }

 @Test
 public void testCleaningVisitor_UnsafeTag() {
 Element source = Jsoup.parse("<html><body><script id='test' src='javascript:alert(\"XSS\")'>Hello World</script></body></html>").body().getChild(0);
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, null);
 cleaningVisitor.head(source, 0);
 Assert.assertEquals(1, cleaningVisitor.numDiscarded);
 }

 @Test
 public void testCleaningVisitor_TextNode() {
 Element source = Jsoup.parse("<html><body><p>Hello World</p></body></html>").body().getChild(0).getChild(0);
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, null);
 cleaningVisitor.head(source, 0);
 Assert.assertNotNull(cleaningVisitor.destination);
 Assert.assertEquals("p", cleaningVisitor.destination.tagName());
 }

 @Test
 public void testCleaningVisitor_DataNode() {
 Element source = Jsoup.parse("<html><body><img data-src='image.jpg'></body></html>").body().getChild(0).getChild(0);
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, null);
 cleaningVisitor.head(source, 0);
 Assert.assertNotNull(cleaningVisitor.destination);
 Assert.assertEquals("img", cleaningVisitor.destination.tagName());
 }

 @Test
 public void testCleaningVisitor_CommentNode() {
 Element source = Jsoup.parse("<html><!-- This is a comment --><body></body></html>").body();
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, null);
 cleaningVisitor.head(source, 0);
 // No assertions here, as comments are discarded by default
 }

 @Test
 public void testCreateSafeElement_SafeTagAndAttributes() {
 Element source = Jsoup.parse("<html><body><p id='test' class='test'>Hello World</p></body></html>").body().getChild(0).getChild(0);
 Element dest = cleaner.createSafeElement(source);
 Assert.assertEquals("p", dest.tagName());
 Assert.assertTrue(dest.hasAttr("id"));
 Assert.assertTrue(dest.hasAttr("class"));
 }

 @Test
 public void testCreateSafeElement_SafeTagNoAttributes() {
 Element source = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>").body().getChild(0).getChild(0);
 Element dest = cleaner.createSafeElement(source);
 Assert.assertEquals("h1", dest.tagName());
 Assert.assertFalse(dest.hasAttr("id"));
 Assert.assertFalse(dest.hasAttr("class"));
 }

 @Test
 public void testCreateSafeElement_UnsafeTagAndAttributes() {
 Element source = Jsoup.parse("<html><body><script id='test' src='javascript:alert(\"XSS\")'>Hello World</script></body></html>").body().getChild(0).getChild(0);
 Element dest = cleaner.createSafeElement(source);
 Assert.assertNull(dest);
 }

 @Test
 public void testCreateSafeElement_UnsafeTagNoAttributes() {
 Element source = Jsoup.parse("<html><body><script>Hello World</script></body></html>").body().getChild(0).getChild(0);
 Element dest = cleaner.createSafeElement(source);
 Assert.assertNull(dest);
 }
}