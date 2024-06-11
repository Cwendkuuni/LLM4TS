package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 }

 /**
 * Test method for {@link Cleaner#Cleaner(Whitelist)} constructor.
 */
 @Test
 public void testCleanerConstructor() {
 Validate.notNull(whitelist);
 cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test method for {@link Cleaner#clean(Document)} method.
 */
 @Test
 public void testCleanMethod() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><div>Safe content</div></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().text().contains("alert('XSS');"));
 assertTrue(cleanDocument.body().text().contains("Safe content"));
 }

 /**
 * Test method for {@link Cleaner#isValid(Document)} method.
 */
 @Test
 public void testIsValidMethod() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><div>Safe content</div></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)} method with safe tag and safe attributes.
 */
 @Test
 public void testCopySafeNodesSafeTagAndAttributes() {
 Element sourceElement = new Element(Tag.valueOf("div"), null);
 sourceElement.attributes().put("class", "safe");
 sourceElement.appendText("Safe text");

 Element destElement = new Element(Tag.valueOf("p"), null);
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

 assertNotNull(destElement);
 assertNotNull(destElement.text());
 assertNotNull(destElement.tagName());
 assertFalse(destElement.tagName().equals("div")); // new tag should be created
 assertEquals(0, numDiscarded);
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)} method with safe tag but unsafe attributes.
 */
 @Test
 public void testCopySafeNodesUnsafeAttributes() {
 Element sourceElement = new Element(Tag.valueOf("div"), null);
 sourceElement.attributes().put("onclick", "alert('XSS');");
 sourceElement.appendText("Safe text");

 Element destElement = new Element(Tag.valueOf("p"), null);
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

 assertNotNull(destElement);
 assertNotNull(destElement.text());
 assertNotNull(destElement.tagName());
 assertFalse(destElement.tagName().equals("div")); // new tag should be created
 assertEquals(1, numDiscarded);
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)} method with unsafe tag and safe attributes.
 */
 @Test
 public void testCopySafeNodesUnsafeTagButSafeAttributes() {
 Element sourceElement = new Element(Tag.valueOf("script"), null);
 sourceElement.attributes().put("type", "text/javascript");
 sourceElement.appendText("Safe text");

 Element destElement = new Element(Tag.valueOf("p"), null);
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

 assertNotNull(destElement); // destElement should not be null
 assertNull(destElement.text()); // no text should be copied
 assertNull(destElement.tagName()); // no tag should be copied
 assertEquals(1, numDiscarded);
 }

 /**
 * Test method for {@link Cleaner#copySafeNodes(Element, Element)} method with unsafe tag and unsafe attributes.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesUnsafeTagAndUnsafeAttributes() {
 Element sourceElement = new Element(Tag.valueOf("script"), null);
 sourceElement.attributes().put("onclick", "alert('XSS');");
 sourceElement.appendText("Safe text");

 Element destElement = new Element(Tag.valueOf("p"), null);
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
 }
}