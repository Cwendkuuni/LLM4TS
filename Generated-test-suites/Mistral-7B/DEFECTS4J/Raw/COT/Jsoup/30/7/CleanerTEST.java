package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.ElementFilter;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;
 private Whitelist whitelist;

 @Before
 public void setup() {
 whitelist = new Whitelist();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Document.createShell("http://example.com");
 cleanDocument = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateCleaner() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 dirtyDocument.appendElement(new Element("script").html("<script>alert('XSS')</script>"));
 dirtyDocument.appendElement(new Element("div").text("Clean me!").attr("id", "danger"));

 cleanDocument = cleaner.clean(dirtyDocument);

 assertFalse(cleanDocument.body().getElementsByTag("script").isEmpty());
 assertFalse(cleanDocument.body().getElementsByTag("div").isEmpty());
 assertNull(cleanDocument.body().getElementById("danger"));
 }

 @Test
 public void testIsValidDocument() {
 dirtyDocument.appendElement(new Element("script").html("<script>alert('XSS')</script>"));
 dirtyDocument.appendElement(new Element("div").text("Clean me!").attr("id", "danger"));

 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerWithNullWhitelist() {
 cleaner = new Cleaner(null);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanDocumentWithNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidDocumentWithNullDocument() {
 cleaner.isValid(null);
 }

 @Test
 public void testCopySafeNodesWithEmptyElement() {
 Element emptyElement = new Element("p");
 int numDiscarded = cleaner.copySafeNodes(emptyElement, cleanDocument.body());
 assertNotNull(cleanDocument.body().getElementsByTag("p").get(0));
 assertEqualNumberOfElements(0, cleanDocument.body().getElementsByTag("*"));
 assertTrue(numDiscarded == 0);
 }

 @Test
 public void testCopySafeNodesWithUnsafeElement() {
 Element unsafeElement = new Element("script").html("<script>alert('XSS')</script>");
 int numDiscarded = cleaner.copySafeNodes(unsafeElement, cleanDocument.body());
 assertFalse(cleanDocument.body().getElementsByTag("script").isEmpty());
 assertTrue(numDiscarded > 0);
 }

 @Test
 public void testCopySafeNodesWithSafeElement() {
 Element safeElement = new Element("div").attr("id", "safe");
 int numDiscarded = cleaner.copySafeNodes(safeElement, cleanDocument.body());
 assertFalse(cleanDocument.body().getElementsByTag("div").isEmpty());
 assertTrue(numDiscarded == 0);
 }

 @Test
 public void testCreateSafeElementWithEmptyElement() {
 Element emptyElement = new Element("p");
 ElementMeta meta = cleaner.createSafeElement(emptyElement);
 assertNotNull(meta.el);
 assertEqualNumberOfElements(0, meta.el.getElementsByTag("*"));
 }

 @Test
 public void testCreateSafeElementWithUnsafeElement() {
 Element unsafeElement = new Element("script").html("<script>alert('XSS')</script>");
 ElementMeta meta = cleaner.createSafeElement(unsafeElement);
 assertNotNull(meta.el);
 assertFalse(meta.el.getElementsByTag("script").isEmpty());
 assertTrue(meta.numAttribsDiscarded > 0);
 }

 @Test
 public void testCreateSafeElementWithSafeElement() {
 Element safeElement = new Element("div").attr("id", "safe");
 ElementMeta meta = cleaner.createSafeElement(safeElement);
 assertNotNull(meta.el);
 assertFalse(meta.el.getElementsByTag("*").isEmpty());
 assertTrue(meta.numAttribsDiscarded == 0);
 }

 @Test
 public void testIsSafeTag() {
 assertTrue(cleaner.whitelist.isSafeTag("div"));
 assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttribute() {
 Element element = new Element("div").attr("class", "safe").attr("id", "test");
 assertTrue(cleaner.whitelist.isSafeAttribute("div", element, ElementFilter.attr("class")));
 assertFalse(cleaner.whitelist.isSafeAttribute("div", element, ElementFilter.attr("onclick")));
 }

 @Test
 public void testGetEnforcedAttributes() {
 assertTrue(cleaner.whitelist.getEnforcedAttributes("div").containsKey("class"));
 assertFalse(cleaner.whitelist.getEnforcedAttributes("div").containsKey("onclick"));
 }
}