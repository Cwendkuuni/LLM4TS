package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test file for Cleaner class
 */
public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = new Whitelist.SimpleText();
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test constructor
 */
 @Test
 public void testCleanerConstructor() {
 // Ensure whitelist is not null
 assertEquals("whitelist should not be null", whitelist, cleaner.getWhitelist());
 }

 /**
 * Test clean method with valid input
 */
 @Test
 public void testCleanValidInput() {
 Document dirtyDocument = Jsoup.parse("<div><p>Valid HTML</p></div>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("clean method should return expected document", "<div><p></p></div>", cleanDocument.body().html());
 }

 /**
 * Test clean method with invalid input
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanInvalidInput() {
 Document dirtyDocument = null; // Null input should throw an IllegalArgumentException
 cleaner.clean(dirtyDocument);
 }

 /**
 * Test isValid method with valid input
 */
 @Test
 public void testIsValidValidInput() {
 Document dirtyDocument = Jsoup.parse("<div><p>Valid HTML</p></div>");
 assertTrue("isValid method should return true for valid input", cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid input
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsValidInvalidInput() {
 Document dirtyDocument = null; // Null input should throw an IllegalArgumentException
 cleaner.isValid(dirtyDocument);
 }

 /**
 * Test isSafeTag method with valid tag
 */
 @Test
 public void testIsSafeTagValidTag() {
 assertTrue("isSafeTag should return true for valid tag", cleaner.whitelist.isSafeTag("p"));
 }

 /**
 * Test isSafeTag method with invalid tag
 */
 @Test
 public void testIsSafeTagInvalidTag() {
 assertFalse("isSafeTag should return false for invalid tag", cleaner.whitelist.isSafeTag("script"));
 }

 /**
 * Test isSafeAttribute method with valid tag and attribute
 */
 @Test
 public void testIsSafeAttributeValidTagAndAttribute() {
 Element sourceElement = Jsoup.parse("<p id='test' class='test'>Valid</p>").getElementsByTag("p").first();
 assertTrue("isSafeAttribute should return true for safe tag and attribute", cleaner.whitelist.isSafeAttribute(sourceElement.tagName(), sourceElement, sourceElement.attribute("id")));
 }

 /**
 * Test isSafeAttribute method with invalid tag and attribute
 */
 @Test
 public void testIsSafeAttributeInvalidTagAndAttribute() {
 Element sourceElement = Jsoup.parse("<script id='test' src='javascript:alert(\"Hello World\")'>").getElementsByTag("script").first();
 assertFalse("isSafeAttribute should return false for invalid tag and attribute", cleaner.whitelist.isSafeAttribute(sourceElement.tagName(), sourceElement, sourceElement.attribute("id")));
 }

 /**
 * Test copySafeNodes method with empty source and destination
 */
 @Test
 public void testCopySafeNodesEmptySourceAndDestination() {
 Element source = Jsoup.parse("").body();
 Element dest = Jsoup.parse("").body();
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals("Number of discarded elements should be 0", 0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with valid source and empty destination
 */
 @Test
 public void testCopySafeNodesValidSourceAndEmptyDestination() {
 Document dirtyDocument = Jsoup.parse("<div><p>Valid HTML</p></div>");
 Element source = dirtyDocument.body();
 Element dest = Jsoup.parse("").body();
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals("Number of discarded elements should be equal to the number of elements in the source", source.childNodes().size(), numDiscarded);
 }

 /**
 * Test copySafeNodes method with invalid source and empty destination
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesInvalidSourceAndEmptyDestination() {
 Element source = null; // Null input should throw an IllegalArgumentException
 Element dest = Jsoup.parse("").body();
 cleaner.copySafeNodes(source, dest);
 }

 /**
 * Test copySafeNodes method with empty source and valid destination
 */
 @Test
 public void testCopySafeNodesEmptySourceAndValidDestination() {
 Element source = Jsoup.parse("").body();
 Document dirtyDocument = Jsoup.parse("<div><p>Valid HTML</p></div>");
 Element dest = dirtyDocument.getElementsByTag("div").first().body();
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals("Number of discarded elements should be 0", 0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with valid source and valid destination
 */
 @Test
 public void testCopySafeNodesValidSourceAndValidDestination() {
 Document dirtyDocument = Jsoup.parse("<div><p id='test1'>Valid1</p><p id='test2'>Valid2</p></div>");
 Element source = dirtyDocument.body();
 Document cleanDocument = Jsoup.parse("<div></div>");
 Element dest = cleanDocument.getElementsByTag("div").first().body();
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals("Number of discarded elements should be equal to the number of discarded elements in the source", source.childNodes().size(), numDiscarded);
 }

 /**
 * Test copySafeNodes method with invalid source and valid destination
 */
 @Test
 public void testCopySafeNodesInvalidSourceAndValidDestination() {
 Document dirtyDocument = Jsoup.parse("<script src='javascript:alert(\"Hello World\")'>");
 Element source = dirtyDocument.body();
 Document cleanDocument = Jsoup.parse("<div></div>");
 Element dest = cleanDocument.getElementsByTag("div").first().body();
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals("Number of discarded elements should be equal to the number of elements in the source", 1, numDiscarded);
 }

}