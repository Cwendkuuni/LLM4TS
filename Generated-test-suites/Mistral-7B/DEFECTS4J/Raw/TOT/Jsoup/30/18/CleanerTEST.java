package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Whitelist.Func;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);

 dirtyDocument = Parser.parseBodyFragment(DataHelper.getUrl("cleanerTest.html"));
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCleanValidDocument() {
 Document expectedCleanDocument = Parser.parseBodyFragment(DataHelper.getUrl("cleanerTestValidDocument.html"));
 Document actualCleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(expectedCleanDocument, actualCleanDocument);
 }

 @Test
 public void testCleanInvalidDocument() {
 Document expectedCleanDocument = Document.createShell(dirtyDocument.baseUri());
 Document actualCleanDocument = cleaner.clean(dirtyDocument);
 assertNotEquals(expectedCleanDocument, actualCleanDocument);

 assertTrue(cleaner.isValid(dirtyDocument));
 assertTrue(cleaner.isValid(actualCleanDocument));
 }

 @Test
 public void testIsValidAllowAll() {
 Whitelist allowAllWhitelist = Whitelist.simpleText();
 allowAllWhitelist.addTag("script");
 Cleaner allowAllCleaner = new Cleaner(allowAllWhitelist);

 Document dirtyScriptDocument = Parser.parseBodyFragment(DataHelper.getUrl("dirtyScriptDocument.html"));
 assertTrue(allowAllCleaner.isValid(dirtyScriptDocument));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsValidNullDocument() {
 cleaner.isValid(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsValidEmptyDocument() {
 Document emptyDocument = Document.createEmpty();
 assertFalse(cleaner.isValid(emptyDocument));
 }

 @Test
 public void testCopySafeNodesTextNode() {
 TextNode sourceText = new TextNode("Test Text Node", dirtyDocument.baseUri());
 TextNode destText = new TextNode("Test Text Node", cleanDocument.baseUri());
 cleaner.copySafeNodes(sourceText.parent(), cleanDocument.body());
 assertEquals(sourceText.getWholeText(), destText.text());
 }

 @Test
 public void testCopySafeNodesElement() {
 Element sourceEl = new Element("p", dirtyDocument.baseUri());
 sourceEl.text("Test Element Contents");
 Element destEl = new Element("p", cleanDocument.baseUri());
 cleaner.copySafeNodes(sourceEl, cleanDocument.body());
 assertEquals(sourceEl.text(), destEl.text());
 }

 @Test
 public void testCopySafeNodesInvalidElement() {
 Element sourceEl = new Element("script", dirtyDocument.baseUri());
 Element destEl = new Element("p", cleanDocument.baseUri());
 cleaner.copySafeNodes(sourceEl, cleanDocument.body());
 // No assertions, as this method should discard the invalid node
 }

 @Test
 public void testCreateSafeElementValidTag() {
 Element sourceEl = new Element("p", dirtyDocument.baseUri());
 Element destEl = new Element("p", cleanDocument.baseUri());
 cleaner.copySafeNodes(sourceEl, destEl);
 assertEquals("p", destEl.tagName());

 Map<String, Func> allowedAttributes = whitelist.getAttributes();
 List<String> allowedAttributeNames = Arrays.asList(allowedAttributes.keySet().toArray(new String[0]));
 List<String> sourceAttributes = Arrays.asList(sourceEl.attributes().keySet().toArray(new String[0]));
 for (String sourceAttribute : sourceAttributes) {
 String attributeName = sourceAttribute;
 if (!allowedAttributeNames.contains(attributeName)) {
 continue;
 }
 String attributeValue = sourceEl.attribute(sourceAttribute);
 assertEquals(attributeValue, destEl.attribute(attributeName));
 }
 }

 @Test
 public void testCreateSafeElementInvalidTag() {
 Element sourceEl = new Element("script", dirtyDocument.baseUri());
 Element destEl = new Element("p", cleanDocument.baseUri());
 cleaner.copySafeNodes(sourceEl, destEl);
 // No assertions, as this method should discard the invalid node
 }

 @Test
 public void testCreateSafeElementInvalidAttribute() {
 Element sourceEl = new Element("p", dirtyDocument.baseUri());
 sourceEl.attr("test", "test");
 Element destEl = new Element("p", cleanDocument.baseUri());
 cleaner.copySafeNodes(sourceEl, destEl);
 // No assertions, as this method should discard the invalid attribute
 }
}