package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanerConstructor() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 // Test case for valid input
 Document dirtyDocument = Jsoup.parse("<html><body><p>Valid Input</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 Element body = cleanDocument.body();
 assertNotNull(body);
 assertTrue(body.children().size() == 1);
 Element paragraph = body.children().get(0);
 assertNotNull(paragraph.text());
 assertNotNull(paragraph.text().getText());
 assertTrue(paragraph.tagName().equals("p"));

 // Test case for invalid input with script tag
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Document cleanInvalidDocument = cleaner.clean(invalidDocument);

 assertNotNull(cleanInvalidDocument);
 Element bodyInvalid = cleanInvalidDocument.body();
 assertNotNull(bodyInvalid);
 assertTrue(bodyInvalid.children().size() == 0);
 }

 @Test
 public void testIsValidMethod() {
 // Test case for valid input
 Document validDocument = Jsoup.parse("<html><body><p>Valid Input</p></body></html>");
 assertTrue(cleaner.isValid(validDocument));

 // Test case for invalid input with script tag
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesMethodWithSafeTag() {
 // Test case for safe tag with no attributes
 Element safeElement = Jsoup.parse("<html><body><p id='safe'>Safe Tag</p></body></html>").body().getElementsByTag("p").get(0);

 ElementMeta sourceElMeta = cleaner.createSafeElement(safeElement);
 Element destElement = sourceElMeta.el;
 int numDiscarded = sourceElMeta.numAttribsDiscarded;

 // Test case for safe tag with attributes
 safeElement = Jsoup.parse("<html><body><p id='safe' class='safeClass' style='color: red'>Safe Tag</p></body></html>").body().getElementsByTag("p").get(0);
 sourceElMeta = cleaner.createSafeElement(safeElement);
 destElement = sourceElMeta.el;
 numDiscarded = sourceElMeta.numAttribsDiscarded;

 assertTrue(destElement.tagName().equals("p"));
 assertNotNull(destElement.attributes().get("id"));
 assertNotNull(destElement.attributes().get("class"));
 assertNull(destElement.attributes().get("style"));
 assertTrue(numDiscarded == 0);

 // Test case for safe tag with children
 Element safeElementWithChildren = Jsoup.parse("<html><body><p id='safe'>Safe Tag<span>Child</span></p></body></html>").body().getElementsByTag("p").get(0);
 int numDiscardedChildren = copySafeNodes(safeElementWithChildren, destElement);
 assertTrue(numDiscardedChildren == 0);
 }

 @Test
 public void testCopySafeNodesMethodWithUnsafeTag() {
 // Test case for unsafe tag with no attributes
 Element unsafeElement = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>").body().getElementsByTag("script").get(0);
 int numDiscarded = copySafeNodes(unsafeElement, new Element("div"));
 assertTrue(numDiscarded > 0);

 // Test case for unsafe tag with attributes
 unsafeElement = Jsoup.parse("<html><body><script id='unsafe' src='script.js'>XSS</script></body></html>").body().getElementsByTag("script").get(0);
 numDiscarded = copySafeNodes(unsafeElement, new Element("div"));
 assertTrue(numDiscarded > 0);
 }

 @Test
 public void testCreateSafeElementMethod() {
 // Test case for safe tag with no attributes
 Element safeElement = Jsoup.parse("<html><body><p>Safe Tag</p></body></html>").body().getElementsByTag("p").get(0);
 ElementMeta sourceElMeta = cleaner.createSafeElement(safeElement);

 assertNotNull(sourceElMeta.el);
 assertTrue(sourceElMeta.numAttribsDiscarded == 0);

 // Test case for safe tag with attributes
 safeElement = Jsoup.parse("<html><body><p id='safe' class='safeClass' style='color: red'>Safe Tag</p></body></html>").body().getElementsByTag("p").get(0);
 sourceElMeta = cleaner.createSafeElement(safeElement);

 assertTrue(sourceElMeta.el.tagName().equals("p"));
 assertNotNull(sourceElMeta.el.attributes().get("id"));
 assertNotNull(sourceElMeta.el.attributes().get("class"));
 assertNull(sourceElMeta.el.attributes().get("style"));
 assertTrue(sourceElMeta.numAttribsDiscarded > 0);
 }

 // Add test cases for edge cases and exceptions handling here
}