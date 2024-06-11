package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><script>alert('XSS')</script></html>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document clean = cleaner.clean(dirtyDocument);
 assertFalse(dirtyDocument.toString().equals(clean.toString()));
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndHtml());
 Document clean = cleaner.clean(dirtyDocument);
 assertTrue(dirtyDocument.toString().equals(clean.toString()));
 }

 @Test
 public void testIsValidDocument_Valid() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndHtml());
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocument_Invalid() {
 Document dirtyDocument = Jsoup.parse("<html><script>alert('XSS')</script></html>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndHtml());
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocument_Mixed() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p><script>alert('XSS')</script></body></html>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextAndHtml());
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes_Empty() {
 Element source = Jsoup.parseBodyFragment("<p></p>").firstChild();
 Element dest = new Element(Tag.P);
 int numDiscarded = Cleaner.copySafeNodes(source, dest);
 assertSame(source, dest);
 assertNull(numDiscarded);
 }

 @Test
 public void testCopySafeNodes_ValidText() {
 Element source = Jsoup.parseBodyFragment("<p>Hello World</p>").firstChild();
 Element dest = new Element(Tag.P);
 int numDiscarded = Cleaner.copySafeNodes(source, dest);
 assertSame(source, dest);
 assertNull(numDiscarded);
 }

 @Test
 public void testCopySafeNodes_ValidTextWithInvalidAttributes() {
 Element source = Jsoup.parseBodyFragment("<p id='badId' style='color:red'>Hello World</p>").firstChild();
 Element dest = new Element(Tag.P);
 int numDiscarded = Cleaner.copySafeNodes(source, dest);
 assertNotNull(numDiscarded);
 assertEquals(2, numDiscarded.intValue());
 }

 @Test
 public void testCopySafeNodes_ValidTextWithInvalidChild() {
 Element source = Jsoup.parseBodyFragment("<p><script>alert('XSS')</script></p>").firstChild();
 Element dest = new Element(Tag.P);
 int numDiscarded = Cleaner.copySafeNodes(source, dest);
 assertNotNull(numDiscarded);
 assertEquals(1, numDiscarded.intValue());
 }

 @Test
 public void testCopySafeNodes_ValidTextWithValidChild() {
 Element source = Jsoup.parseBodyFragment("<p><span>Hello World</span></p>").firstChild();
 Element dest = new Element(Tag.P);
 int numDiscarded = Cleaner.copySafeNodes(source, dest);
 assertSame(source, dest);
 assertNull(numDiscarded);
 }

 @Test
 public void testCopySafeNodes_ValidTextWithMixedChildren() {
 Element source = Jsoup.parseBodyFragment("<p><span>Hello World</span><script>alert('XSS')</script></p>").firstChild();
 Element dest = new Element(Tag.P);
 int numDiscarded = Cleaner.copySafeNodes(source, dest);
 assertNotNull(numDiscarded);
 assertEquals(1, numDiscarded.intValue());
 }

 @Test
 public void testCopySafeNodes_ValidWithAttributes() {
 Element source = Jsoup.parseBodyFragment("<p id='validId' style='color:blue'>Hello World</p>").firstChild();
 Element dest = new Element(Tag.P);
 int numDiscarded = Cleaner.copySafeNodes(source, dest);
 assertSame(source, dest);
 assertNull(numDiscarded);
 }

 @Test
 public void testCopySafeNodes_ValidWithAttributesAndInvalidChildren() {
 Element source = Jsoup.parseBodyFragment("<p id='validId' style='color:blue'>Hello World<script>alert('XSS')</script></p>").firstChild();
 Element dest = new Element(Tag.P);
 int numDiscarded = Cleaner.copySafeNodes(source, dest);
 assertNotNull(numDiscarded);
 assertEquals(1, numDiscarded.intValue());
 }
}