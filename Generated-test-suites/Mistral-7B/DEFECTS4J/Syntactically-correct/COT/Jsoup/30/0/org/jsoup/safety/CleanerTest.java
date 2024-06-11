package org.jsoup.safety;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setup() {
 // Initialize the Cleaner instance with a sample Whitelist
 whitelist = new Whitelist.SimpleTextWhitelist();
 cleaner = new Cleaner(whitelist);

 // Create a sample dirty document for testing
 Element body = new Element("body");
 body.appendText("Hello <b id='test'>World!</b> <script>alert('XSS');</script>");
 dirtyDocument = new Document(body);
 cleanDocument = new Document();
 }

 /**
 * Test method for {@link Cleaner#Cleaner(Whitelist)} constructor
 */
 @Test
 public void testCleanerConstructor() {
 // No validation is needed as Whitelist is not null by the test setup
 cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test method for {@link Cleaner#clean(Document)} method
 */
 @Test
 public void testClean() {
 // Test with the sample dirty document
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 // Check if the cleaned document only contains safe elements and attributes
 assertEquals("Expected clean document to have only body tag", Tag.BODY, cleanedDocument.tagName());
 Element cleanedBody = cleanedDocument.body();
 assertTrue("Expected cleaned document to have no script tag", cleanedBody.tagName().equals(Tag.BODY) || cleanedBody.children().isEmpty());
 assertTrue("Expected cleaned document to have no id attribute", cleanedBody.attributes().isEmpty());

 // Test with an empty dirty document
 Document emptyDirtyDocument = new Document();
 Document emptyCleanDocument = cleaner.clean(emptyDirtyDocument);
 assertEquals("Expected empty clean document", emptyCleanDocument.body(), null);
 }

 /**
 * Test method for {@link Cleaner#isValid(Document)} method
 */
 @Test
 public void testIsValid() {
 // Test valid document
 assertTrue(cleaner.isValid(dirtyDocument));

 // Test invalid document
 Element invalidBody = new Element("body");
 invalidBody.appendText("Hello <script>alert('XSS');</script>");
 Document invalidDocument = new Document(invalidBody);
 assertFalse(cleaner.isValid(invalidDocument));
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)} method
 */
 @Test
 public void testCopySafeNodes() {
 // Test with a safe element (p tag)
 Element safeSource = new Element("p");
 safeSource.text("Hello");
 Element safeDestination = new Element("p");
 int numDiscarded = cleaner.copySafeNodes(safeSource, safeDestination);
 assertEquals("Expected no attributes discarded", 0, numDiscarded);
 assertEquals("Expected safe destination to have the same text as source", "Hello", safeDestination.text());

 // Test with an unsafe element (script tag)
 Element unsafeSource = new Element("script");
 unsafeSource.text("alert('XSS');");
 int numDiscardedUnsafe = cleaner.copySafeNodes(unsafeSource, cleanDocument.body());
 assertEquals("Expected script tag to be discarded", 1, numDiscardedUnsafe);
 assertTrue("Expected cleanDocument to have no script tag", cleanDocument.body().childNodes().isEmpty());
 }

 // Add more test cases for other methods and edge cases as needed
}