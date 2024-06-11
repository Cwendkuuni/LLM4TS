package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 dirtyDocument = Document.createFromString("<html><body><div id='test' onclick='alert(\"XSS\")'>Test<script>alert(\"XSS\")</script></div></body></html>", "http://test.com");
 cleanDocument = Document.createShell("http://test.com");
 cleaner = new Cleaner(new Whitelist());
 }

 @Test
 public void testClean() {
 assertEquals("<html><body></body></html>", cleaner.clean(dirtyDocument).body().html());
 }

 @Test
 public void testIsValid() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidWithEmptyDocument() {
 Document emptyDocument = Document.createShell("http://test.com");
 assertTrue(cleaner.isValid(emptyDocument));
 }

 @Test
 public void testIsValidWithAllowedTag() {
 Document allowedDocument = Document.createFromString("<html><body><p id='test'>Test</p></body></html>", "http://test.com");
 assertTrue(cleaner.isValid(allowedDocument));
 }

 @Test
 public void testIsValidWithAllowedAttribute() {
 Document allowedDocument = Document.createFromString("<html><body><p id='test' class='test'>Test</p></body></html>", "http://test.com");
 assertTrue(cleaner.isValid(allowedDocument));
 }

 @Test
 public void testIsValidWithDisallowedTag() {
 Document disallowedDocument = Document.createFromString("<html><body><script id='test'>Test</script></body></html>", "http://test.com");
 assertFalse(cleaner.isValid(disallowedDocument));
 }

 @Test
 public void testIsValidWithDisallowedAttribute() {
 Document disallowedDocument = Document.createFromString("<html><body><p id='test' onclick='alert(\"XSS\")'>Test</p></body></html>", "http://test.com");
 assertFalse(cleaner.isValid(disallowedDocument));
 }

 @Test
 public void testCleanWithAllowedTag() {
 Element allowedTag = new Element(Tag.P, dirtyDocument.baseUri(), new Attributes().put("id", "test"));
 Element cleanTag = cleaner.copySafeNodes(allowedTag, cleanDocument.body());
 assertNotNull(cleanTag);
 assertEquals("Test", cleanTag.text());
 }

 @Test
 public void testCleanWithDisallowedTag() {
 Element disallowedTag = new Element(Tag.SCRIPT, dirtyDocument.baseUri(), new Attributes().put("id", "test"));
 Element cleanTag = cleaner.copySafeNodes(disallowedTag, cleanDocument.body());
 assertNull(cleanTag);
 }

 @Test
 public void testCleanWithDisallowedAttribute() {
 Element allowedTag = new Element(Tag.P, dirtyDocument.baseUri(), new Attributes().put("id", "test").put("onclick", "alert(\"XSS\")"));
 Element cleanTag = cleaner.copySafeNodes(allowedTag, cleanDocument.body());
 assertNull(cleanTag);
 }
}