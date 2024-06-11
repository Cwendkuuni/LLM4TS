import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Whitelist.AttributeSpec;
import org.jsoup.safety.Whitelist.TagNameSpec;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

 private static final String HTML_WITH_VALID_TAGS = "<html><body><p>Hello world!</p></body></html>";
 private static final String HTML_WITH_INVALID_TAGS = "<html><body><script>alert('XSS');</script></body></html>";
 private static final String HTML_WITH_VALID_TAGS_AND_INVALID_ATTRIBUTES = "<html><body><img src='image.jpg' onclick='alert('XSS')'></body></html>";
 private static final String HTML_WITH_INVALID_TAGS_AND_VALID_ATTRIBUTES = "<html><body><iframe src='attacker.com' title='Title'></iframe></body></html>";

 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 }

 @Test
 public void testCleanMethod_ValidInput() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_VALID_TAGS);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndHtml());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertEquals("<html><body><p>Hello world!</p></body></html>", cleanDocument.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanMethod_NullInput() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndHtml());
 Document dirtyDocument = null;

 try {
 cleaner.clean(dirtyDocument);
 } catch (IllegalArgumentException e) {
 // pass
 }

 Assert.assertEquals("Argument cannot be null", e.getMessage());
 }

 @Test
 public void testIsValidMethod_ValidInput() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_VALID_TAGS);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndHtml());

 Assert.assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethod_InvalidInput() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_INVALID_TAGS);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndHtml());

 Assert.assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodesMethod_ValidInput() {
 Element root = Jsoup.parse(HTML_WITH_VALID_TAGS).body();
 Element destination = new Element(Tag.valueOf("div"));
 int numDiscarded = Cleaner.copySafeNodes(root, destination);

 Assert.assertEquals(1, numDiscarded); // One p tag is discarded due to it not being a whitelisted tag
 Assert.assertEquals(1, destination.childNodes().size());
 Assert.assertEquals("Hello world!", destination.childNodes().get(0).text());
 }

 @Test
 public void testCopySafeNodesMethod_InvalidInput() {
 Element root = Jsoup.parse(HTML_WITH_INVALID_TAGS).body();
 Element destination = new Element(Tag.valueOf("div"));
 int numDiscarded = Cleaner.copySafeNodes(root, destination);

 Assert.assertEquals(2, numDiscarded); // One script tag and one body tag are discarded
 Assert.assertEquals(0, destination.childNodes().size());
 }

 @Test
 public void testCreateSafeElementMethod_ValidInput() {
 Element sourceElement = Jsoup.parse(HTML_WITH_VALID_TAGS).body().getElementsByTag("p").get(0);
 ElementMeta meta = Cleaner.createSafeElement(sourceElement);

 Assert.assertEquals("<p></p>", meta.el.html());
 Assert.assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementMethod_InvalidInput() {
 Element sourceElement = Jsoup.parse(HTML_WITH_INVALID_TAGS).body().getElementsByTag("script").get(0);
 ElementMeta meta = Cleaner.createSafeElement(sourceElement);

 Assert.assertNull(meta.el);
 Assert.assertEquals(1, sourceElement.attributes().size()); // The onclick attribute is discarded
 }

 @Test
 public void testIsSafeTagMethod() {
 Whitelist whitelist = new Whitelist.SimpleTagBasedWhitelist(TagNameSpec.TAG_P);
 Cleaner cleaner = new Cleaner(whitelist);

 Assert.assertTrue(cleaner.whitelist.isSafeTag("p"));
 Assert.assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttributeMethod() {
 Whitelist whitelist = new Whitelist.SimpleAttributeBasedWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);

 // Test valid attributes
 Tag pTag = Tag.valueOf("p");
 AttributeSpec pAttributeSpec = new AttributeSpec("id", "id");
 Assert.assertTrue(cleaner.whitelist.isSafeAttribute(pTag.name(), null, pAttributeSpec));

 // Test invalid attributes
 Tag imgTag = Tag.valueOf("img");
 AttributeSpec imgAttributeSpec1 = new AttributeSpec("src", "src");
 AttributeSpec imgAttributeSpec2 = new AttributeSpec("onclick", "onclick");
 Assert.assertFalse(cleaner.whitelist.isSafeAttribute(imgTag.name(), null, imgAttributeSpec1));
 Assert.assertFalse(cleaner.whitelist.isSafeAttribute(imgTag.name(), null, imgAttributeSpec2));

 // Test valid attributes on specific tags
 AttributeSpec pAttributeSpec = new AttributeSpec("class", "class");
 Assert.assertTrue(cleaner.whitelist.isSafeAttribute(pTag.name(), null, pAttributeSpec));
 }

 @Test
 public void testGetEnforcedAttributesMethod() {
 Whitelist whitelist = new Whitelist.SimpleTagBasedWhitelist(TagNameSpec.TAG_IMG);
 Cleaner cleaner = new Cleaner(whitelist);

 Map<Tag, Map<String, AttributeSpec>> enforcedAttributes = whitelist.getEnforcedAttributes(TagNameSpec.TAG_IMG);

 Assert.assertEquals(1, enforcedAttributes.size());
 Assert.assertEquals(2, enforcedAttributes.get(Tag.IMG).size());

 Map<String, AttributeSpec> imgAttributes = enforcedAttributes.get(Tag.IMG);
 Assert.assertEquals("src", imgAttributes.keySet().iterator().next());
 Assert.assertEquals("alt", imgAttributes.keySet().iterator().next());

 AttributeSpec srcAttributeSpec = imgAttributes.get("src");
 AttributeSpec altAttributeSpec = imgAttributes.get("alt");

 Assert.assertNotNull(srcAttributeSpec);
 Assert.assertEquals("src", srcAttributeSpec.name());
 Assert.assertEquals("src", srcAttributeSpec.value());
 Assert.assertNull(srcAttributeSpec.scheme());

 Assert.assertNotNull(altAttributeSpec);
 Assert.assertEquals("alt", altAttributeSpec.name());
 Assert.assertEquals("alt", altAttributeSpec.value());
 Assert.assertNull(altAttributeSpec.scheme());
 }

}