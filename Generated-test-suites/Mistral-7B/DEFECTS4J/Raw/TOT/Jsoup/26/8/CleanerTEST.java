package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private static Whitelist getBasicWhitelist() {
 return Whitelist.relaxed().addTags("p", "a");
 }

 @Test
 public void testClean() {
 // given
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test'><script>alert('XSS')</script><a href='http://example.com'>Link</a></p></body></html>");
 Whitelist whitelist = getBasicWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 // when
 Document cleanDocument = cleaner.clean(dirtyDocument);
 // then
 assertNotNull(cleanDocument);
 assertEquals("<html><body><p id='test'></p></body></html>", cleanDocument.body().html());
 assertFalse(cleanDocument.body().child(0).hasText());
 assertNull(cleanDocument.body().child(0).child(0));
 }

 @Test
 public void testIsValidFalse() {
 // given
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test'><script>alert('XSS')</script><a href='http://example.com'>Link</a></p></body></html>");
 Whitelist whitelist = getBasicWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 // when
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidTrue() {
 // given
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test'>Hello World</p></body></html>");
 Whitelist whitelist = getBasicWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 // when
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes() {
 // given
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test'><a href='http://example.com'>Link</a></p></body></html>");
 Whitelist whitelist = getBasicWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 Element dirtyElement = dirtyDocument.body().child(0);
 // when
 int numDiscarded = cleaner.copySafeNodes(dirtyElement, null);
 // then
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithAttributes() {
 // given
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='test' style='color: red'><a href='http://example.com' title='Link Title'>Link</a></p></body></html>");
 Whitelist whitelist = getBasicWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 Element dirtyElement = dirtyDocument.body().child(0);
 // when
 int numDiscarded = cleaner.copySafeNodes(dirtyElement, null);
 // then
 assertEquals(3, numDiscarded);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 // given
 Whitelist whitelist = getBasicWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 // when, then
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidNullDocument() {
 // given
 Whitelist whitelist = getBasicWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 // when, then
 cleaner.isValid(null);
 }

}