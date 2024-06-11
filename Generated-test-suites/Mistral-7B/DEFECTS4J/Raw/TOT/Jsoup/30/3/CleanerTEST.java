package org.jsoup.safety;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertEquals("<html><body></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanEmptyDocument() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertEquals("<html></html>", cleanDocument.html());
 }

 @Test
 public void testCleanWithAttributes() {
 Document dirtyDocument = Jsoup.parse("<html><body><button id=\"myButton\" class=\"myClass\" onclick=\"myFunction()\">Click me</button></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertEquals("<html><body><button id=\"myButton\" class=\"myClass\"></button></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assert.assertTrue(isValid);
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assert.assertFalse(isValid);
 }

 @Test
 public void testIsValidEmptyDocument() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assert.assertTrue(isValid);
 }

 @Test
 public void testIsValidWithAttributes() {
 Document dirtyDocument = Jsoup.parse("<html><body><button id=\"myButton\" class=\"myClass\" onclick=\"myFunction()\">Click me</button></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assert.assertFalse(isValid);
 }

 @Test
 public void testCopySafeNodesValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), Document.createShell(dirtyDocument.baseUri()).body());

 Assert.assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), Document.createShell(dirtyDocument.baseUri()).body());

 Assert.assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodesEmptyDocument() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), Document.createShell(dirtyDocument.baseUri()).body());

 Assert.assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithAttributes() {
 Document dirtyDocument = Jsoup.parse("<html><body><button id=\"myButton\" class=\"myClass\" onclick=\"myFunction()\">Click me</button></body></html>");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), Document.createShell(dirtyDocument.baseUri()).body());

 Assert.assertEquals(2, numDiscarded);
 }

 @Test
 public void testCreateSafeElementValidTag() {
 Element sourceEl = new Element(Tag.H1, null, new Attributes());
 ElementMeta meta = cleaner.createSafeElement(sourceEl);

 Assert.assertNotNull(meta.el);
 Assert.assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementInvalidTag() {
 Element sourceEl = new Element(Tag.SCRIPT, null, new Attributes());
 ElementMeta meta = cleaner.createSafeElement(sourceEl);

 Assert.assertNull(meta.el);
 Assert.assertEquals(1, sourceEl.childNodes().size());
 Assert.assertEquals("script", meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementEmptyAttributes() {
 Element sourceEl = new Element(Tag.BUTTON, null, new Attributes());
 ElementMeta meta = cleaner.createSafeElement(sourceEl);

 Assert.assertNotNull(meta.el);
 Assert.assertEquals(0, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementInvalidAttribute() {
 Element sourceEl = new Element(Tag.BUTTON, null, new Attributes().put("onclick", "myFunction()"));
 ElementMeta meta = cleaner.createSafeElement(sourceEl);

 Assert.assertNotNull(meta.el);
 Assert.assertEquals(1, meta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementValidAttributes() {
 Element sourceEl = new Element(Tag.BUTTON, null, new Attributes().put("id", "myButton").put("class", "myClass"));
 ElementMeta meta = cleaner.createSafeElement(sourceEl);

 Assert.assertNotNull(meta.el);
 Assert.assertEquals(0, meta.numAttribsDiscarded);
 }
}