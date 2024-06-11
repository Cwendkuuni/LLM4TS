package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.Jsoup.parse;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 // setup the cleaner with a default whitelist
 Whitelist whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);

 // setup a dirty document with some unsafe tags and attributes
 String dirtyHtml = "<html><body><script src=\"malicious.js\"></script><div id=\"unsafe\" name=\"unsafe\">Malicious Content</div></body></html>";
 dirtyDocument = parse(dirtyHtml);
 }

 @Test
 public void testClean() {
 // test cleaning a document using the cleaner
 Document clean = cleaner.clean(dirtyDocument);

 // assert that the clean document only contains safe elements and attributes
 List<Element> cleanElements = clean.body().children();
 for (Element element : cleanElements) {
 assertSafeTag(element.tagName(), element);
 }
 }

 @Test
 public void testIsValid() {
 // test if the dirty document is valid against the whitelist
 boolean isValid = cleaner.isValid(dirtyDocument);

 // assert that the document is not valid, since it contains unsafe tags
 assertThat(isValid, is(false));
 }

 @Test
 public void testCopySafeNodes() {
 // test copying safe nodes from a dirty document to a clean document
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

 // assert that no nodes were discarded, since they were all safe
 assertThat(numDiscarded, is(0));

 // assert that the clean document only contains safe elements and attributes
 List<Element> cleanElements = cleanDocument.body().children();
 for (Element element : cleanElements) {
 assertSafeTag(element.tagName(), element);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testInvalidCleaner() {
 // test that a cleaner cannot be created with a null whitelist
 Whitelist nullWhitelist = null;
 Cleaner invalidCleaner = new Cleaner(nullWhitelist);
 }

 @Test(expected = NullPointerException.class)
 public void testInvalidDocument() {
 // test that a cleaner throws a NPE when passed a null document
 cleaner.clean(null);
 }

 @Test
 private void assertSafeTag(String tagName, Element element) {
 // helper method to assert that a given tag is safe
 if (!cleaner.whitelist.isSafeTag(tagName)) {
 throw new RuntimeException("Unexpected safe tag: " + tagName);
 }
 }

}