package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 // Initialize Whitelist and Document objects for testing
 whitelist = new Whitelist();
 dirtyDocument = Document.createShell("http://localhost");
 cleanDocument = Document.createShell("http://localhost");
 }

 /**
 * Test constructor method
 */
 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test clean method with valid input
 */
 @Test
 public void testCleanMethodValidInput() {
 // Arrange: setup valid input
 Element sourceElement = new Element(Tag.valueOf("div"), null, new Attributes());
 sourceElement.text("Test content");
 dirtyDocument.body().appendChild(sourceElement);

 // Act: call clean method
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 // Assert: check if cleaned document contains only allowed elements
 Element destElement = cleanedDocument.body().children().get(0);
 assertInstanceOf(Element.class, destElement);
 assertEquals("div", destElement.tagName());
 }

 /**
 * Test clean method with invalid input
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanMethodInvalidInput() {
 // Act: call clean method with null document
 Cleaner cleaner = new Cleaner(whitelist);
 cleaner.clean(null);
 }

 /**
 * Test isValid method with valid input
 */
 @Test
 public void testIsValidMethodValidInput() {
 // Arrange: setup valid input
 Element sourceElement = new Element(Tag.valueOf("div"), null, new Attributes());
 sourceElement.text("Test content");
 dirtyDocument.body().appendChild(sourceElement);

 // Act: call isValid method
 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Assert: check if document is valid
 assertTrue(isValid);
 }

 /**
 * Test isValid method with invalid input
 */
 @Test
 public void testIsValidMethodInvalidInput() {
 // Arrange: setup invalid input
 Element sourceElement = new Element(Tag.valueOf("script"), null, new Attributes().put("src", "malicious-script.js"));
 dirtyDocument.body().appendChild(sourceElement);

 // Act: call isValid method
 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Assert: check if document is invalid
 assertFalse(isValid);
 }

 /**
 * Test copySafeNodes method with valid input
 */
 @Test
 public void testCopySafeNodesMethodValidInput() {
 // Arrange: setup valid input
 Element sourceElement = new Element(Tag.valueOf("div"), null, new Attributes());
 sourceElement.text("Test content");
 sourceElement.tagName("p"); // Change tag name for testing
 dirtyDocument.body().appendChild(sourceElement);

 // Act: call copySafeNodes method
 Cleaner cleaner = new Cleaner(whitelist);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

 // Assert: check if number of discarded elements is zero
 assertEquals(0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with invalid input (unsafe tag)
 */
 @Test
 public void testCopySafeNodesMethodInvalidInputUnsafeTag() {
 // Arrange: setup invalid input (unsafe tag)
 Element sourceElement = new Element(Tag.valueOf("script"), null, new Attributes().put("src", "malicious-script.js"));
 dirtyDocument.body().appendChild(sourceElement);

 // Act: call copySafeNodes method
 Cleaner cleaner = new Cleaner(whitelist);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

 // Assert: check if number of discarded elements is greater than zero
 assertTrue(numDiscarded > 0);
 }

 /**
 * Test copySafeNodes method with invalid input (empty document)
 */
 @Test
 public void testCopySafeNodesMethodInvalidInputEmptyDocument() {
 // Act: call copySafeNodes method with empty document
 Cleaner cleaner = new Cleaner(whitelist);
 int numDiscarded = cleaner.copySafeNodes(new Document(), cleanDocument);

 // Assert: check if number of discarded elements is zero
 assertEquals(0, numDiscarded);
 }

}