import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleTagNames("p", "div");
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test method for {@link Cleaner#Cleaner(Whitelist)}
 */
 @Test
 public void testCleaner() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test method for {@link Cleaner#clean(Document)}
 */
 @Test
 public void testClean() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='p1'>Hello World!</p><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p id='p1'>Hello World!</p></body></html>", cleanDocument.body().html());
 }

 /**
 * Test method for {@link Cleaner#isValid(Document)}
 */
 @Test
 public void testIsValid() {
 Document invalidDocument = Jsoup.parse("<html><head><title>Invalid Document</title></head><body><p id='p1'>Hello World!</p></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 /**
 * Test method for {@link Cleaner#clean(Document)} with empty document
 */
 @Test
 public void testCleanEmptyDocument() {
 Document emptyDocument = Jsoup.parse("<html></html>");
 Document cleanEmptyDocument = cleaner.clean(emptyDocument);
 assertEquals("<html></html>", cleanEmptyDocument.body().html());
 }

 /**
 * Test method for {@link Cleaner#clean(Document)} with null document
 */
 @Test (expected = NullPointerException.class)
 public void testCleanNullDocument() {
 cleaner.clean(null);
 }

 /**
 * Test method for {@link Cleaner#isValid(Document)} with null document
 */
 @Test (expected = NullPointerException.class)
 public void testIsValidNullDocument() {
 cleaner.isValid(null);
 }

 /**
 * Test method for {@link Cleaner#clean(Document)} with body containing only text
 */
 @Test
 public void testCleanText() {
 Document textDocument = Jsoup.parse("<html><body>Hello World!</body></html>");
 Document cleanTextDocument = cleaner.clean(textDocument);
 assertEquals("<html><body>Hello World!</body></html>", cleanTextDocument.body().html());
 }

 /**
 * Test method for {@link Cleaner#clean(Document)} with body containing only tags with no attributes
 */
 @Test
 public void testCleanTagsWithoutAttributes() {
 Document tagsDocument = Jsoup.parse("<html><body><p></p><div></div></body></html>");
 Document cleanTagsDocument = cleaner.clean(tagsDocument);
 assertEquals("<html><body><p></p><div></div></body></html>", cleanTagsDocument.body().html());
 }

 /**
 * Test method for {@link Cleaner#clean(Document)} with body containing tags with attributes
 */
 @Test
 public void testCleanTagsWithAttributes() {
 Document tagsWithAttributesDocument = Jsoup.parse("<html><body><p id='p1' class='class1'>Hello World! <span title='tooltip'>Tooltip</span></p></body></html>");
 Document cleanTagsWithAttributesDocument = cleaner.clean(tagsWithAttributesDocument);
 assertEquals("<html><body><p id='p1' class='class1'></p></body></html>", cleanTagsWithAttributesDocument.body().html());
 }

 /**
 * Test method for {@link Cleaner#clean(Document)} with body containing tags with disallowed attributes
 */
 @Test
 public void testCleanTagsWithDisallowedAttributes() {
 Document tagsWithDisallowedAttributesDocument = Jsoup.parse("<html><body><p id='p1' onclick='alert(\"XSS\")'>Hello World!</p></body></html>");
 Document cleanTagsWithDisallowedAttributesDocument = cleaner.clean(tagsWithDisallowedAttributesDocument);
 assertEquals("<html><body><p id='p1'></p></body></html>", cleanTagsWithDisallowedAttributesDocument.body().html());
 }

 /**
 * Test method for {@link Cleaner#clean(Document)} with body containing tags with enforced attributes
 */
 @Test
 public void testCleanTagsWithEnforcedAttributes() {
 Document tagsWithEnforcedAttributesDocument = Jsoup.parse("<html><body><p id='p1' class='class1' style='color: red'>Hello World!</p></body></html>");
 Document cleanTagsWithEnforcedAttributesDocument = cleaner.clean(tagsWithEnforcedAttributesDocument);
 assertEquals("<html><body><p id='p1' class='class1' style='color: red'></p></body></html>", cleanTagsWithEnforcedAttributesDocument.body().html());
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)} with safe element
 */
 @Test
 public void testCopySafeNodesSafeElement() {
 Element source = new Element("p", "p1");
 source. classes(). add("class1");
 Element destination = new Element("p");
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, destination);
 int numDiscarded = cleaningVisitor.numDiscarded;
 cleaner.copySafeNodes(source, destination);
 assertEquals(source.html(), destination.html());
 assertEquals(numDiscarded, 0);
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)} with unsafe element
 */
 @Test
 public void testCopySafeNodesUnsafeElement() {
 Element source = new Element("script");
 Element destination = new Element("p");
 CleaningVisitor cleaningVisitor = new CleaningVisitor(source, destination);
 int numDiscarded = cleaningVisitor.numDiscarded;
 cleaner.copySafeNodes(source, destination);
 assertEquals(source.html(), "");
 assertEquals(numDiscarded, 1);
 }
}