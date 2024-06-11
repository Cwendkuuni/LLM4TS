package org.jsoup.safety;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {

 private Whitelist whiteList;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whiteList = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whiteList);
 }

 @After
 public void tearDown() {
 cleaner = null;
 whiteList = null;
 }

 /**
 * Testing cleaner with a valid document
 */
 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Parser parser = new Parser();
 String expected = "<html><body><h1></h1></body></html>";
 Document expectedDocument = parser.parse(expected);
 assertEquals(expectedDocument, cleanDocument);
 }

 /**
 * Testing cleaner with an invalid document - checking if the document is still cleaned
 */
 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('Hello')</script><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Parser parser = new Parser();
 String expected = "<html><body><h1></h1></body></html>";
 Document expectedDocument = parser.parse(expected);
 assertEquals(expectedDocument, cleanDocument);
 }

 /**
 * Testing isValid method with a valid document
 */
 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 boolean result = cleaner.isValid(dirtyDocument);
 assertTrue(result);
 }

 /**
 * Testing isValid method with an invalid document - checking if it returns false
 */
 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('Hello')</script><h1>Hello World</h1></body></html>");
 boolean result = cleaner.isValid(dirtyDocument);
 assertFalse(result);
 }
}