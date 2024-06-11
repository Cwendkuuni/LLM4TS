import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test constructor
 */
 @Test
 public void testCleanerConstruction() {
 cleaner = new Cleaner(whitelist);
 Assert.assertNotNull(cleaner);
 }

 /**
 * Test clean method with valid document
 */
 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Document.newDocument();
 dirtyDocument.body().appendElement("p").text("Hello World!");
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 Assert.assertNotNull(cleanedDocument);
 Assert.assertEquals("p", cleanedDocument.body().tagName());
 Assert.assertEquals("Hello World!", cleanedDocument.body().text());
 }

 /**
 * Test clean method with invalid document
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidDocument() {
 Document dirtyDocument = null;
 cleaner.clean(dirtyDocument);
 }

 /**
 * Test isValid method with valid document
 */
 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Document.newDocument();
 dirtyDocument.body().appendElement("p").text("Hello World!");
 Assert.assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid document
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = null;
 cleaner.isValid(dirtyDocument);
 }

 /**
 * Test copySafeNodes method with empty source element
 */
 @Test
 public void testCopySafeNodesEmptySourceElement() {
 Element source = new Element(Tag.valueOf("p"));
 Element destination = new Element(Tag.valueOf("div"));
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 Assert.assertEquals(0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with safe tag and attributes
 */
 @Test
 public void testCopySafeNodesSafeTagAndAttributes() {
 Element source = new Element(Tag.valueOf("p"), null, new Attributes(){{ put("class", "test"); }});
 Element destination = new Element(Tag.valueOf("div"));
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 Assert.assertEquals(0, numDiscarded);
 Assert.assertEquals("class", destination.tagName());
 Assert.assertEquals("test", destination.classes());
 }

 /**
 * Test copySafeNodes method with unsafe tag
 */
 @Test(expected = IllegalStateException.class)
 public void testCopySafeNodesUnsafeTag() {
 Element source = new Element(Tag.valueOf("script"), null, new Attributes());
 Element destination = new Element(Tag.valueOf("div"));
 cleaner.copySafeNodes(source, destination);
 }

 /**
 * Test copySafeNodes method with unsafe tag and attributes
 */
 @Test(expected = IllegalStateException.class)
 public void testCopySafeNodesUnsafeTagAndAttributes() {
 Element source = new Element(Tag.valueOf("script"), null, new Attributes(){{ put("src", "javascrip:alert('Test')"); }});
 Element destination = new Element(Tag.valueOf("div"));
 cleaner.copySafeNodes(source, destination);
 }

 /**
 * Test createSafeElement method with safe tag and attributes
 */
 @Test
 public void testCreateSafeElementSafeTagAndAttributes() {
 Element source = new Element(Tag.valueOf("p"), null, new Attributes(){{ put("class", "test"); }});
 Element dest = cleaner.createSafeElement(source);
 Assert.assertEquals(Tag.valueOf("p"), dest.tagName());
 Assert.assertEquals("class", dest.tagName());
 Assert.assertEquals("test", dest.classes());
 }

 /**
 * Test createSafeElement method with unsafe tag
 */
 @Test(expected = IllegalStateException.class)
 public void testCreateSafeElementUnsafeTag() {
 Element source = new Element(Tag.valueOf("script"), null, new Attributes());
 Element dest = cleaner.createSafeElement(source);
 }

 /**
 * Test createSafeElement method with unsafe tag and attributes
 */
 @Test(expected = IllegalStateException.class)
 public void testCreateSafeElementUnsafeTagAndAttributes() {
 Element source = new Element(Tag.valueOf("script"), null, new Attributes(){{ put("src", "javascrip:alert('Test')"); }});
 Element dest = cleaner.createSafeElement(source);
 }

}