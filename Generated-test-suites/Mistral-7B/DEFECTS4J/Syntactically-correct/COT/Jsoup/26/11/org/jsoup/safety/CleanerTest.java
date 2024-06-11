package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private static final String TEST_HTML = "<html><head></head><body>" +
         "<div id=\"test1\" class=\"testClass\" title=\"Test Title\">" +
         "Test Content <script>alert('XSS');</script></div>" +
         "<input type=\"text\" name=\"test2\" value=\"Test Value\"/>" +
         "<a href=\"http://test.com\">Test Link</a>" +
         "</body></html>";

 private static final String VALID_WHITELIST = Whitelist.relaxed().addTags("div", "input", "a");
 private static final String INVALID_WHITELIST = Whitelist.relaxed().addTags("div", "script");

 @Test
 public void testCreateCleaner() {
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 assertNotNull(cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerWithNullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testCleanMethod() {
 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Check if the clean document is valid
 assertTrue(cleaner.isValid(cleanDocument));

 // Check the structure of the clean document
 assertEquals(3, cleanDocument.body().children().size());

 Element divElement = cleanDocument.body().selectFirst("div");
 assertNotNull(divElement);
 assertEquals("test1", divElement.id());
 assertEquals("Test Content", divElement.text());
 assertFalse(divElement.hasAttr("title"));
 assertFalse(divElement.hasAttr("class"));
 assertFalse(divElement.hasAttr("title"));
 assertFalse(divElement.hasAttr("onclick"));
 assertFalse(divElement.hasAttr("onmouseover"));

 Element inputElement = cleanDocument.body().selectFirst("input");
 assertNotNull(inputElement);
 assertEquals("test2", inputElement.attr("name"));
 assertEquals("Test Value", inputElement.attr("value"));
 assertFalse(inputElement.hasAttr("type"));

 Element linkElement = cleanDocument.body().selectFirst("a");
 assertNotNull(linkElement);
 assertEquals("Test Link", linkElement.text());
 assertEquals("http://test.com", linkElement.attr("href"));
 assertFalse(linkElement.hasAttr("target"));
 assertFalse(linkElement.hasAttr("class"));
 }

 @Test
 public void testIsValidMethodWithValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><div>Valid Content</div></body></html>");
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testIsValidMethodWithInvalidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testIsValidMethodWithMixedDocument() {
 Document mixedDocument = Jsoup.parse("<html><body><div>Valid Content</div><script>alert('XSS');</script></body></html>");
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 assertFalse(cleaner.isValid(mixedDocument));
 }

 @Test
 public void testCopySafeNodesWithEmptyNode() {
 Element emptyNode = new Element("", "");
 int numDiscarded = Cleaner.copySafeNodes(emptyNode, new Element(""));
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithSafeTag() {
 Element safeTag = new Element("div", "");
 int numDiscarded = Cleaner.copySafeNodes(safeTag, new Element(""));
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesWithInvalidTag() {
 Element invalidTag = new Element("script", "");
 int numDiscarded = Cleaner.copySafeNodes(invalidTag, new Element(""));
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCreateSafeElementWithEmptyElement() {
 Element emptyElement = new Element("", "");
 ElementMeta emptyElementMeta = Cleaner.createSafeElement(emptyElement);
 assertNotNull(emptyElementMeta.el);
 assertEquals(0, emptyElementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithSafeElement() {
 Element safeElement = new Element("div", "");
 ElementMeta safeElementMeta = Cleaner.createSafeElement(safeElement);
 assertNotNull(safeElementMeta.el);
 assertEquals(0, safeElementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithInvalidElement() {
 Element invalidElement = new Element("script", "");
 ElementMeta invalidElementMeta = Cleaner.createSafeElement(invalidElement);
 assertNotNull(invalidElementMeta.el);
 assertEquals(1, invalidElementMeta.numAttribsDiscarded);
 }

 @Test
 public void testIsSafeTagWithSafeTag() {
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 assertTrue(cleaner.whitelist.isSafeTag("div"));
 }

 @Test
 public void testIsSafeTagWithInvalidTag() {
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttributeWithSafeAttribute() {
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 assertTrue(cleaner.whitelist.isSafeAttribute("div", null, null));
 }

 @Test
 public void testIsSafeAttributeWithInvalidAttribute() {
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 assertFalse(cleaner.whitelist.isSafeAttribute("div", "onclick", null));
 }

 @Test
 public void testGetEnforcedAttributesWithSafeTag() {
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 List<Attribute> enforcedAttributes = cleaner.whitelist.getEnforcedAttributes("div");
 assertTrue(enforcedAttributes.isEmpty());
 }

 @Test
 public void testGetEnforcedAttributesWithInvalidTag() {
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 List<Attribute> enforcedAttributes = cleaner.whitelist.getEnforcedAttributes("script");
 assertTrue(enforcedAttributes.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnforcedAttributesWithNullTag() {
 Cleaner cleaner = new Cleaner(VALID_WHITELIST);
 cleaner.whitelist.getEnforcedAttributes(null);
 fail("Expected IllegalArgumentException");
 }

}