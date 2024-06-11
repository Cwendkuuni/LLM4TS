package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.Whitelist;
import org.jsoup.helper.Validate;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Whitelist allowedTagsAndAttributesWhitelist;
 private Whitelist disallowedTagsAndAttributesWhitelist;
 private Document dirtyDocument;
 private Document cleanDocument;
 private Element dirtyElement;
 private Element cleanElement;

 @Before
 public void setUp() {
 allowedTagsAndAttributesWhitelist = Whitelist.simpleTagNames();
 allowedTagsAndAttributesWhitelist.addTags("script", "style");
 allowedTagsAndAttributesWhitelist.addAttribute("*", "id");
 disallowedTagsAndAttributesWhitelist = Whitelist.none();

 dirtyDocument = Document.createFromString("<html><body><script id='scriptTag'>alert('XSS');</script><div id='divTag'>Hello World</div></body></html>", "https://example.com");
 cleanDocument = Document.createShell("https://example.com");
 cleanElement = cleanDocument.createElement("div");
 cleanElement.setText("Hello World");
 }

 @Test
 public void testCreateCleaner() {
 // Test constructor
 Cleaner cleaner = new Cleaner(allowedTagsAndAttributesWhitelist);
 }

 @Test
 public void testCleanDocument() {
 // Test clean method with a valid document
 cleanDocument = new Cleaner(allowedTagsAndAttributesWhitelist).clean(dirtyDocument);
 assertEquals("<html><body><div id='divTag'>Hello World</div></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanDocumentWithInvalidDocument() {
 // Test clean method with an invalid document
 Document invalidDocument = Document.createFromString("<html><body><script id='scriptTag'>alert('XSS');</script></body></html>", "https://example.com");
 cleanDocument = new Cleaner(allowedTagsAndAttributesWhitelist).clean(invalidDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidDocument() {
 // Test isValid method with a valid document
 assertTrue(new Cleaner(allowedTagsAndAttributesWhitelist).isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithInvalidDocument() {
 // Test isValid method with an invalid document
 assertFalse(new Cleaner(allowedTagsAndAttributesWhitelist).isValid(invalidDocument));
 }

 @Test
 public void testIsSafeTag() {
 // Test isSafeTag method
 assertTrue(Cleaner.class.getEnclosingClass().getDeclaredMethod("isSafeTag", String.class, Element.class, Attribute.class).invoke(new Cleaner(allowedTagsAndAttributesWhitelist), "div", dirtyElement, null));
 assertFalse(Cleaner.class.getEnclosingClass().getDeclaredMethod("isSafeTag", String.class, Element.class, Attribute.class).invoke(new Cleaner(allowedTagsAndAttributesWhitelist), "script", dirtyElement, null));
 }

 @Test
 public void testCopySafeNodesWithEmptySource() {
 // Test copySafeNodes method with an empty source
 assertEquals(0, new Cleaner(allowedTagsAndAttributesWhitelist).copySafeNodes(null, cleanElement));
 }

 @Test
 public void testCopySafeNodesWithSafeSource() {
 // Test copySafeNodes method with a safe source
 Element safeSource = cleanDocument.createElement("div");
 safeSource.setText("Safe source");
 int numDiscarded = new Cleaner(allowedTagsAndAttributesWhitelist).copySafeNodes(safeSource, cleanElement);
 assertEquals("Safe source", cleanElement.text());
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithUnsafeSource() {
 // Test copySafeNodes method with an unsafe source
 Element unsafeSource = dirtyDocument.createElement("script");
 unsafeSource.setText("alert('XSS');");
 int numDiscarded = new Cleaner(allowedTagsAndAttributesWhitelist).copySafeNodes(unsafeSource, cleanElement);
 assertEquals("", cleanElement.text());
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCreateSafeElementWithSafeSource() {
 // Test createSafeElement method with a safe source
 Element safeSource = cleanDocument.createElement("div");
 safeSource.setText("Safe source");
 ElementMeta elementMeta = new Cleaner(allowedTagsAndAttributesWhitelist).createSafeElement(safeSource);
 assertEquals("div", elementMeta.el.tagName());
 assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithUnsafeSource() {
 // Test createSafeElement method with an unsafe source
 Element unsafeSource = dirtyDocument.createElement("script");
 unsafeSource.setText("alert('XSS');");
 ElementMeta elementMeta = new Cleaner(allowedTagsAndAttributesWhitelist).createSafeElement(unsafeSource);
 assertEquals(null, elementMeta.el);
 assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerWithNullWhitelist() {
 new Cleaner(null);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanWithNullDocument() {
 new Cleaner(allowedTagsAndAttributesWhitelist).clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateSafeElementWithNullElement() {
 new Cleaner(allowedTagsAndAttributesWhitelist).createSafeElement(null);
 }

}