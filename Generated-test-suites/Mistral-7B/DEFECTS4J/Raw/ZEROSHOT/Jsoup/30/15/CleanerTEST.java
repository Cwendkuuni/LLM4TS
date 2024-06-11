package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Helper.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test file for Cleaner class
 */
public class CleanerTest {

 private Whitelist getDefaultWhitelist() {
 return Whitelist.simpleText();
 }

 @Test
 public void testCreateCleaner() {
 // Test creating a cleaner with a valid whitelist
 Whitelist whitelist = getDefaultWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 assertTrue(cleaner instanceof Cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerWithNullWhitelist() {
 Whitelist whitelist = null;
 Cleaner cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanDocumentWithValidInput() {
 Whitelist whitelist = getDefaultWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<html>\n" +
 "<body>\n" +
 "<p>This is a <strong>safe</strong> input.</p>\n" +
 "</body>\n" +
 "</html>");

 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html>\n<body>\n<p>\n<p>\n</body>\n</html>", cleanDocument.html());
 }

 @Test
 public void testCleanDocumentWithInvalidInput() {
 Whitelist whitelist = getDefaultWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<html>\n" +
 "<body>\n" +
 "<script src=\"malicious_script.js\"></script>\n" +
 "</body>\n" +
 "</html>");

 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html>\n<body>\n</body>\n</html>", cleanDocument.html());
 }

 @Test
 public void testIsValidDocumentWithValidInput() {
 Whitelist whitelist = getDefaultWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<html>\n" +
 "<body>\n" +
 "<p>This is a <strong>safe</strong> input.</p>\n" +
 "</body>\n" +
 "</html>");

 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithInvalidInput() {
 Whitelist whitelist = getDefaultWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = new Document("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<html>\n" +
 "<body>\n" +
 "<script src=\"malicious_script.js\"></script>\n" +
 "</body>\n" +
 "</html>");

 assertFalse(cleaner.isValid(dirtyDocument));
 }
}