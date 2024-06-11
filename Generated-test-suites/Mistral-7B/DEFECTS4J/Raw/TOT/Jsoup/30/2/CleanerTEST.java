package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Whitelist.SimpleTextWhitelist;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist simpleWhitelist;

 @Before
 public void setup() {
 simpleWhitelist = new Whitelist.SimpleTextWhitelist();
 cleaner = new Cleaner(simpleWhitelist);
 }

 @Test
 public void testCreateCleaner() {
 Validate.notNull(cleaner);
 }

 @Test
 public void testClean_ValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello world!</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 @Test
 public void testClean_InvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValid_ValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello world!</h1></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 @Test
 public void testIsValid_InvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testClean_EmptyDocument() {
 Document dirtyDocument = Jsoup.parse("");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertSame(dirtyDocument, cleanDocument);
 }

 @Test
 public void testClean_FrameDocument() {
 Document dirtyDocument = Jsoup.parse("<html><frameset></frameset></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><frameset></frameset></html>", cleanDocument.html());
 }

 @Test
 public void testCopySafeNodes() {
 Element sourceElement = new Element(Tag.valueOf("div"), null, new Attributes().put("class", "test"));
 sourceElement.appendText("Hello world!");
 Element destinationElement = new Element(Tag.valueOf("p"));
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);

 assertEquals(1, numDiscarded);
 assertEquals("Hello world!", destinationElement.text());
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceElement = new Element(Tag.valueOf("div"), null, new Attributes().put("class", "test").put("id", "testid"));
 ElementMeta meta = cleaner.createSafeElement(sourceElement);

 assertEquals("div", meta.el.tagName());
 assertEquals(2, meta.numAttribsDiscarded);
 }

 @Test
 public void testIsSafeTag() {
 List<String> allowedTags = Arrays.asList("h1", "p", "div");
 Whitelist whitelist = new Whitelist(allowedTags);
 Cleaner cleaner = new Cleaner(whitelist);

 assertTrue(cleaner.whitelist.isSafeTag("h1"));
 assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttribute() {
 List<String> allowedTags = Arrays.asList("div");
 List<String> allowedAttributes = Arrays.asList("class");
 Whitelist whitelist = new Whitelist(allowedTags, allowedAttributes);
 Cleaner cleaner = new Cleaner(whitelist);

 Element sourceElement = new Element(Tag.valueOf("div"), null, new Attributes().put("class", "test").put("id", "testid"));

 assertTrue(cleaner.whitelist.isSafeAttribute("div", sourceElement, new Attribute("class", "test")));
 assertFalse(cleaner.whitelist.isSafeAttribute("div", sourceElement, new Attribute("id", "testid")));
 }

 @Test
 public void testGetEnforcedAttributes() {
 List<String> allowedTags = Arrays.asList("div");
 Map<String, String> enforcedAttributes = Map.of("class", "test");
 Whitelist whitelist = new Whitelist(allowedTags, null, enforcedAttributes);
 Cleaner cleaner = new Cleaner(whitelist);

 assertEquals("class", cleaner.whitelist.getEnforcedAttributes("div").get(0).getKey());
 }
}