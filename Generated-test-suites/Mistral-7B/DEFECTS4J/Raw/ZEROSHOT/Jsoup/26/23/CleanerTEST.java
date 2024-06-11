package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = new Whitelist.SimpleTextWhitelist();
 cleaner = new Cleaner(whitelist);

 dirtyDocument = Document.createShell("http://example.com");
 cleanDocument = Document.createShell("http://example.com");
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test clean method
 */
 @Test
 public void testClean() {
 Element dirtyElement = new Element("html", "http://example.com");
 dirtyElement.appendText("Test");
 dirtyDocument.body().appendChild(dirtyElement);
 Element cleanElement = cleaner.clean(dirtyDocument).body().firstChild();
 assertNull(cleanElement.getPreviousSibling());
 assertNotNull(cleanElement.tagName());
 assertFalse(cleanElement.hasAttr("script"));
 assertFalse(cleanElement.hasAttr("style"));
 }

 /**
 * Test isValid method
 */
 @Test
 public void testIsValid() {
 Element invalidElement = new Element("html", "http://example.com");
 invalidElement.appendElement(new Element("script", "[\"alert('XSS')\"]"));
 dirtyDocument.body().appendChild(invalidElement);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with valid document
 */
 @Test
 public void testIsValidValidDocument() {
 Element validElement = new Element("html", "http://example.com");
 validElement.appendText("Test");
 dirtyDocument.body().appendChild(validElement);
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test copySafeNodes method
 */
 @Test
 public void testCopySafeNodes() {
 Element sourceElement = new Element("html", "http://example.com");
 sourceElement.appendText("Test1");
 sourceElement.appendElement(new Element("span", "test", Arrays.asList("class", "testClass")));
 sourceElement.appendText("Test2");
 sourceElement.appendElement(new Element("script", "[\"alert('XSS')\"]"));
 dirtyDocument.body().appendChild(sourceElement);

 Element destElement = cleanDocument.body().firstChild();
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
 assertNotNull(destElement.tagName());
 assertFalse(destElement.hasAttr("script"));
 assertTrue(destElement.tagName().equals("html"));
 assertTrue(numDiscarded > 1);
 }

}