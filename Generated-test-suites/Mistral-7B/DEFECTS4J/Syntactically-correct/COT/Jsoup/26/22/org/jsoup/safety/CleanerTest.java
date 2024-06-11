package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

 private Document dirtyDocument;
 private Element dirtyElement;
 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 dirtyDocument = Jsoup.parse("<html><body><div id='testDiv' test='test'><script>alert('XSS')</script></div></body></html>");
 dirtyElement = dirtyDocument.body().child(0);
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanedDocument);
 assertFalse(cleanedDocument.body().text().contains("XSS"));
 }

 @Test
 public void testIsValidMethod() {
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testCopySafeNodesMethod_SafeElement() {
 Element safeElement = new Element(Tag.valueOf("p"), dirtyDocument.baseUri(), new Attributes());
 int numDiscarded = cleaner.copySafeNodes(dirtyElement, safeElement);
 assertNotNull(safeElement);
 assertFalse(numDiscarded > 0);
 }

 @Test
 public void testCopySafeNodesMethod_UnsafeTag() {
 Element unsafeElement = new Element(Tag.valueOf("script"), dirtyDocument.baseUri(), new Attributes());
 int numDiscarded = cleaner.copySafeNodes(dirtyElement, unsafeElement);
 assertTrue(numDiscarded > 0);
 assertNull(unsafeElement.child(0));
 }

 @Test
 public void testCopySafeNodesMethod_TextNode() {
 TextNode textNode = new TextNode("Test text", dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(dirtyElement, textNode);
 assertFalse(numDiscarded > 0);
 assertNotNull(textNode);
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesMethod_NullElement() {
 int numDiscarded = cleaner.copySafeNodes(null, dirtyElement);
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesMethod_NullDestination() {
 int numDiscarded = cleaner.copySafeNodes(dirtyElement, null);
 }

 @Test
 public void testCreateSafeElementMethod() {
 Element sourceElement = new Element(Tag.valueOf("div"), dirtyDocument.baseUri(), new Attributes().put("id", "testDiv"));
 ElementMeta meta = cleaner.createSafeElement(sourceElement);
 assertNotNull(meta);
 assertNotNull(meta.el);
 assertTrue(meta.numAttribsDiscarded == 0);
 }

 @Test
 public void testCreateSafeElementMethod_UnsafeAttribute() {
 Element sourceElement = new Element(Tag.valueOf("div"), dirtyDocument.baseUri(), new Attributes().put("test", "test"));
 ElementMeta meta = cleaner.createSafeElement(sourceElement);
 assertNotNull(meta);
 assertTrue(meta.numAttribsDiscarded > 0);
 assertNull(meta.el.attribute("test"));
 }

 @Test
 public void testCreateSafeElementMethod_SafeAttribute() {
 Element sourceElement = new Element(Tag.valueOf("div"), dirtyDocument.baseUri(), new Attributes().put("id", "testDiv"));
 ElementMeta meta = cleaner.createSafeElement(sourceElement);
 assertNotNull(meta);
 assertNotNull(meta.el);
 assertTrue(meta.numAttribsDiscarded == 0);
 assertNotNull(meta.el.attribute("id"));
 }

 @Test
 public void testCreateSafeElementMethod_EnforcedAttribute() {
 Element sourceElement = new Element(Tag.valueOf("p"), dirtyDocument.baseUri(), new Attributes().put("class", "test"));
 ElementMeta meta = cleaner.createSafeElement(sourceElement);
 assertNotNull(meta);
 assertNotNull(meta.el);
 assertTrue(meta.numAttribsDiscarded == 0);
 assertNotNull(meta.el.attribute("class"));
 }

}