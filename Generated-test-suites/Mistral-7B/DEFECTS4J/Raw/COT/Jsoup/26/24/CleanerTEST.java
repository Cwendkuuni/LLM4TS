package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

/**
 * Tests for Cleaner class
 */
public class CleanerTest {

 private Whitelist getDefaultWhitelist() {
 return Whitelist.simpleText();
 }

 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 assertNotNull(cleaner);
 }

 @Test
 public void testClean() {
 String dirtyHtml = "<html><body><div id='test' onclick='alert(\"Hello World\")'><h1>Test</h1><script>alert(\"Hi\")</script></div></body></html>";
 Document dirtyDocument = Jsoup.parse(dirtyHtml);
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 String cleanHtml = cleanDocument.html();
 assertTrue(cleanHtml.contains("<html><body><h1>Test</h1></body></html>"));
 assertFalse(cleanHtml.contains("onclick"));
 assertFalse(cleanHtml.contains("script"));
 assertFalse(cleanHtml.contains("alert"));
 }

 @Test
 public void testIsValid() {
 String validHtml = "<html><body><div id='test'><h1>Test</h1></div></body></html>";
 Document validDocument = Jsoup.parse(validHtml);
 Cleaner cleaner = new Cleaner(getDefaultWhitelist());
 boolean isValid = cleaner.isValid(validDocument);
 assertTrue(isValid);

 String invalidHtml = "<html><body><div id='test' onclick='alert(\"Hello World\")'><h1>Test</h1><script>alert(\"Hi\")</script></div></body></html>";
 Document invalidDocument = Jsoup.parse(invalidHtml);
 boolean isInvalid = cleaner.isValid(invalidDocument);
 assertFalse(isInvalid);
 }

 @Test
 public void testCopySafeNodesWhenSafeTag() {
 String html = "<html><body><div id='test'><h1>Test</h1></div></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Element source = dirtyDocument.body().getElementsByTag("div").first();
 Element dest = new Element(Tag.DIV);
 int numDiscarded = CleanerTest.copySafeNodes(source, dest);

 assertNotNull(dest);
 assertTrue(dest.text().equals("Test"));
 assertTrue(numDiscarded == 0);
 }

 @Test
 public void testCopySafeNodesWhenUnsafeTag() {
 String html = "<html><body><div id='test' onclick='alert(\"Hello World\")'><h1>Test</h1></div></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Element source = dirtyDocument.body().getElementsByTag("div").first();
 Element dest = new Element(Tag.DIV);
 int numDiscarded = CleanerTest.copySafeNodes(source, dest);

 assertNotNull(dest);
 assertNull(dest.text());
 assertTrue(numDiscarded == 1); // for onclick
 }

 @Test
 public void testCreateSafeElementWhenSafeTag() {
 String html = "<html><body><div id='test'><h1>Test</h1></div></body></html>";
 Element source = Jsoup.parse(html).body().getElementsByTag("div").first();
 Element dest = new Element(Tag.DIV);
 int numDiscarded = CleanerTest.createSafeElement(source, dest).numAttribsDiscarded;

 assertNotNull(dest);
 assertTrue(dest.text().equals(""));
 assertTrue(numDiscarded == 0);
 }

 @Test
 public void testCreateSafeElementWhenUnsafeTag() {
 String html = "<html><body><div id='test' onclick='alert(\"Hello World\")'><h1>Test</h1></div></body></html>";
 Element source = Jsoup.parse(html).body().getElementsByTag("div").first();
 Element dest = new Element(Tag.DIV);
 int numDiscarded = CleanerTest.createSafeElement(source, dest).numAttribsDiscarded;

 assertNotNull(dest);
 assertTrue(dest.text().equals(""));
 assertTrue(numDiscarded == 1); // for onclick
 }

 private static int copySafeNodes(Element source, Element dest) {
 return new Cleaner(getDefaultWhitelist()).copySafeNodes(source, dest);
 }

 private static ElementMeta createSafeElement(Element source, Element dest) {
 return new Cleaner(getDefaultWhitelist()).createSafeElement(source, dest);
 }

}