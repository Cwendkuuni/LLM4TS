import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.attributes.Attribute;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class CleanerTest {

 private static final String HTML_SAMPLE = "<html><head><title>Test Page</title></head><body><div id='test' data-test='data'><h1>Hello World</h1><script>alert('XSS');</script></div></body></html>";

 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 }

 @Test
 public void testCleanMethod() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertFalse("Expected clean document to not contain script tag", cleanDocument.body().hasMetaElements());
 Assert.assertFalse("Expected clean document to not contain script tag", cleanDocument.body().hasText());
 Assert.assertTrue("Expected clean document to contain h1 tag", cleanDocument.body().hasTag("h1"));
 Assert.assertEquals("Expected clean document to have same number of children as original", dirtyDocument.body().childNodes().size(), cleanDocument.body().childNodes().size());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanerWithNullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test
 public void testIsValidMethod() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);

 Assert.assertFalse("Expected isValid method to return false with sample HTML", cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithValidHtml() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 String validHtml = "<html><body><div><h1>Test</h1></div></body></html>";
 Document validDocument = Jsoup.parse(validHtml);

 Assert.assertTrue("Expected isValid method to return true with valid HTML", cleaner.isValid(validDocument));
 }

 @Test
 public void testCleaningVisitor() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse("<html><head><title>Test Page</title></head><body><div id='test' data-test='data'><h1>Hello World</h1><script>alert('XSS');</script></div></body></html>");
 Element root = dirtyDocument.body();
 Element destination = new Element("body");

 CleaningVisitor cleaningVisitor = new CleaningVisitor(root, destination);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(root);

 // Check that only allowed elements and attributes are present in the destination
 Element cleanedElement = destination;
 Element child = cleanedElement.child(0);
 while (child != null) {
 if (child instanceof Element) {
 Element element = (Element) child;
 Assert.assertTrue("Expected allowed tag: " + element.tagName(), cleaner.whitelist.isSafeTag(element.tagName()));

 // Check that all attributes are allowed
 for (Attribute attribute : element.attributes()) {
 String tagName = element.tagName();
 String attrName = attribute.getKey();
 Assert.assertTrue("Expected allowed attribute: " + tagName + "." + attrName, cleaner.whitelist.isSafeAttribute(tagName, element, attribute));
 }
 }
 child = child.nextSibling();
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsValidMethodWithEmptyDocument() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document emptyDocument = new Document();

 cleaner.isValid(emptyDocument);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsValidMethodWithNullDocument() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());

 cleaner.isValid(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanMethodWithNullDocument() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());

 cleaner.clean(null);
 }

 @Test
 public void testCopySafeNodesMethod() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document dirtyDocument = Jsoup.parse("<html><head><title>Test Page</title></head><body><div id='test' data-test='data'><h1>Hello World</h1><script>alert('XSS');</script></div></body></html>");
 Element source = dirtyDocument.body();
 Element destination = new Element("body");

 int numDiscarded = cleaner.copySafeNodes(source, destination);

 // Check that only allowed elements and attributes are present in the destination
 Element cleanedElement = destination;
 Element child = cleanedElement.child(0);
 while (child != null) {
 if (child instanceof Element) {
 Element element = (Element) child;
 Assert.assertTrue("Expected allowed tag: " + element.tagName(), cleaner.whitelist.isSafeTag(element.tagName()));

 // Check that all attributes are allowed
 for (Attribute attribute : element.attributes()) {
 String tagName = element.tagName();
 String attrName = attribute.getKey();
 Assert.assertTrue("Expected allowed attribute: " + tagName + "." + attrName, cleaner.whitelist.isSafeAttribute(tagName, element, attribute));
 }
 }
 child = child.nextSibling();
 }

 // Check that the number of discarded nodes is as expected
 Elements sourceElements = source.children();
 int numExpectedDiscarded = 0;
 for (Element sourceElement : sourceElements) {
 if (!cleaner.whitelist.isSafeTag(sourceElement.tagName())) {
 numExpectedDiscarded++;
 }
 }
 Assert.assertEquals("Expected number of discarded nodes to match", numExpectedDiscarded, numDiscarded);
 }

 @Test
 public void testCreateSafeElementMethod() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 String htmlSample = "<html><body><div id='test' data-test='data'><h1>Hello World</h1><script>alert('XSS');</script></div></body></html>";
 Document dirtyDocument = Jsoup.parse(htmlSample);
 Element sourceElement = dirtyDocument.body().getElementsByTag("div").first();

 ElementMeta meta = cleaner.createSafeElement(sourceElement);

 // Check that the created element only contains allowed attributes
 Assert.assertTrue("Expected allowed tag: " + sourceElement.tagName(), cleaner.whitelist.isSafeTag(sourceElement.tagName()));
 Element safeElement = meta.el;
 for (Attribute attribute : safeElement.attributes()) {
 String tagName = sourceElement.tagName();
 String attrName = attribute.getKey();
 Assert.assertTrue("Expected allowed attribute: " + tagName + "." + attrName, cleaner.whitelist.isSafeAttribute(tagName, sourceElement, attribute));
 }

 // Check that the number of discarded attributes is as expected
 int numExpectedDiscarded = 0;
 for (Attribute sourceAttribute : sourceElement.attributes()) {
 if (!cleaner.whitelist.isSafeAttribute(sourceElement.tagName(), sourceElement, sourceAttribute)) {
 numExpectedDiscarded++;
 }
 }
 Assert.assertEquals("Expected number of discarded attributes to match", numExpectedDiscarded, meta.numAttribsDiscarded);
 }

 @Test
 public void testCopySafeNodesMethodWithEmptyDocument() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document emptyDocument = new Document();
 Element source = emptyDocument.body();
 Element destination = new Element("body");

 int numDiscarded = cleaner.copySafeNodes(source, destination);

 Assert.assertEquals("Expected number of discarded nodes to be 0", 0, numDiscarded);
 }

 @Test
 public void testCreateSafeElementMethodWithEmptyElement() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document emptyDocument = new Document();
 Element sourceElement = new Element("div");

 ElementMeta meta = cleaner.createSafeElement(sourceElement);

 Assert.assertEquals("Expected created element to be empty", null, meta.el);
 Assert.assertEquals("Expected number of discarded attributes to be 0", 0, meta.numAttribsDiscarded);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateSafeElementMethodWithNullElement() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());

 cleaner.createSafeElement(null);
 }

 @Test(expected = NullPointerException.class)
 public void testCleaningVisitorMethodWithNullSource() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 NodeVisitor cleaningVisitor = new CleaningVisitor(null, new Element("body"));
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(new Element("html"));
 }

 @Test(expected = NullPointerException.class)
 public void testCleaningVisitorMethodWithNullDestination() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Element root = new Element("html");
 NodeVisitor cleaningVisitor = new CleaningVisitor(root, null);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(root);
 }
}