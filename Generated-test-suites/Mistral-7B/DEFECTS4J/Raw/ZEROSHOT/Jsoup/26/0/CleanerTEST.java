package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleTextOnly();
 dirtyDocument = createDirtyDocument();
 cleanDocument = createCleanDocument();
 }

 @Test
 public void testCleanerWithValidWhitelist() {
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanedDocument.body().childNodeSize() > 0);
 assertFalse(cleanedDocument.body().getElementsByTag("script").size() > 0);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanerWithNullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test
 public void testIsValidWithValidDocument() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidWithInvalidDocument() {
 Whitelist invalidWhitelist = new Whitelist() {
 @Override
 public boolean isSafeTag(String name) {
 return false;
 }

 @Override
 public boolean isSafeAttribute(String tag, Element context, Attribute attribute) {
 return false;
 }
 };
 Cleaner cleaner = new Cleaner(invalidWhitelist);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodesWithEmptySource() {
 int numDiscarded = Cleaner.copySafeNodes(new Element(""), new Element(""));
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithEmptyDestination() {
 int numDiscarded = Cleaner.copySafeNodes(new Element(""), new Element(""));
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithValidSourceAndDestination() {
 ElementMeta meta = Cleaner.createSafeElement(new Element("div", null, new Attributes().put("class", "test")));
 Element dest = meta.el;
 int numDiscarded = Cleaner.copySafeNodes(new Element("div", new Attributes().put("class", "test"), null), dest);
 assertEquals(0, numDiscarded);
 assertEquals("div", dest.tagName());
 assertEquals(1, dest.childNodeSize());
 }

 @Test
 public void testCopySafeNodesWithInvalidSourceAndDestination() {
 int numDiscarded = Cleaner.copySafeNodes(new Element("script"), new Element("div"));
 assertEquals(1, numDiscarded);
 assertFalse(cleanDocument.body().getElementsByTag("script").size() > 0);
 }

 @Test
 public void testCreateSafeElementWithValidAttributes() {
 ElementMeta meta = Cleaner.createSafeElement(new Element("div", new Attributes().put("class", "test").put("id", "testId")));
 assertEquals("div", meta.el.tagName());
 assertEquals(2, meta.el.attributeSize());
 assertEquals("test", meta.el.getClassAttributeNames().get(0));
 assertEquals("testId", meta.el.idAttributeName());
 }

 @Test
 public void testCreateSafeElementWithInvalidAttributes() {
 ElementMeta meta = Cleaner.createSafeElement(new Element("div", new Attributes().put("onclick", "alert('test')")));
 assertEquals("div", meta.el.tagName());
 assertEquals(0, meta.el.attributeSize());
 assertEquals(1, meta.numAttribsDiscarded);
 }

 @Test
 private Document createDirtyDocument() {
 String html = "<html><head><script>alert('test');</script><body><div class=\"test\" id=\"testId\">Test<script>alert('test2');</script></div></body></html>";
 Document doc = Jsoup.parse(html);
 return doc;
 }

 @Test
 private Document createCleanDocument() {
 String html = "<html><head></head><body><div class=\"test\" id=\"testId\">Test</div></body></html>";
 Document doc = Jsoup.parse(html);
 return doc;
 }
}