package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 dirtyDocument = Jsoup.parse("<html><body><div id='test' name='test'><h1>Hello <script>alert('XSS')</script></h1></div></body></html>");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCreateCleaner() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanNullDocument() {
 assertThrows(NullPointerException.class, () -> new Cleaner(whitelist).clean(null));
 }

 @Test
 public void testCleanNullBodyDocument() {
 Document nullBodyDocument = Document.createShell(dirtyDocument.baseUri());
 nullBodyDocument.body(null);
 Cleaner cleaner = new Cleaner(whitelist);
 assertNull(cleaner.clean(nullBodyDocument));
 }

 @Test
 public void testCleanDocumentWithBody() {
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body().toString(), cleanedDocument.body().toString());
 }

 @Test
 public void testIsValidNullDocument() {
 assertThrows(NullPointerException.class, () -> new Cleaner(whitelist).isValid(null));
 }

 @Test
 public void testIsValidNullBodyDocument() {
 Document nullBodyDocument = Document.createShell(dirtyDocument.baseUri());
 nullBodyDocument.body(null);
 assertFalse(new Cleaner(whitelist).isValid(nullBodyDocument));
 }

 @Test
 public void testIsValidValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><div id='test'><h1>Hello</h1></div></body></html>");
 assertTrue(new Cleaner(whitelist).isValid(validDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><div id='test' name='test'><h1>Hello <script>alert('XSS')</script></h1></div></body></html>");
 assertFalse(new Cleaner(whitelist).isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesNullElement() {
 assertEquals(0, new Cleaner(whitelist).copySafeNodes(null, cleanDocument.body()));
 }

 @Test
 public void testCopySafeNodesNullDestinationElement() {
 Element nullDestinationElement = new Element(Tag.BODY, dirtyDocument.baseUri(), null);
 int numDiscarded = new Cleaner(whitelist).copySafeNodes(dirtyDocument.body(), nullDestinationElement);
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesValidElement() {
 Element validElement = dirtyDocument.selectFirst("div");
 Element destinationElement = new Element(Tag.DIV, dirtyDocument.baseUri(), null);
 int numDiscarded = new Cleaner(whitelist).copySafeNodes(validElement, destinationElement);
 assertEquals(0, numDiscarded);
 assertSame(validElement, destinationElement.child(0));
 }

 @Test
 public void testCopySafeNodesInvalidElement() {
 Element invalidElement = dirtyDocument.selectFirst("script");
 int numDiscarded = new Cleaner(whitelist).copySafeNodes(invalidElement, cleanDocument.body());
 assertEquals(1, numDiscarded);
 assertTrue(cleanDocument.body().children().size() == 0);
 }

 @Test
 public void testCreateSafeElementNullElement() {
 assertThrows(NullPointerException.class, () -> new Cleaner(whitelist).createSafeElement(null));
 }

 @Test
 public void testCreateSafeElementScriptTag() {
 Element invalidElement = dirtyDocument.selectFirst("script");
 ElementMeta meta = new Cleaner(whitelist).createSafeElement(invalidElement);
 assertNull(meta.el);
 assertEquals(1, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementDivTag() {
 Element validElement = dirtyDocument.selectFirst("div");
 ElementMeta meta = new Cleaner(whitelist).createSafeElement(validElement);
 assertNotNull(meta.el);
 assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementDivTagWithInvalidAttribute() {
 Element invalidElement = dirtyDocument.selectFirst("div[name]");
 ElementMeta meta = new Cleaner(whitelist).createSafeElement(invalidElement);
 assertNotNull(meta.el);
 assertEquals(1, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementDivTagWithValidAttributes() {
 Element validElement = dirtyDocument.selectFirst("div[id][class]");
 ElementMeta meta = new Cleaner(whitelist).createSafeElement(validElement);
 assertNotNull(meta.el);
 assertEquals(0, meta.numAttribsDiscarded);
 }
}