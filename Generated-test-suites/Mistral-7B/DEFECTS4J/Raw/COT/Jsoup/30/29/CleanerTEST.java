package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setup() {
 whitelist = new Whitelist(); // Assuming you have a way to initialize a Whitelist object
 cleaner = new Cleaner(whitelist);
 dirtyDocument = createDocument("<html><body><h1 id='title'>Test</h1><script>alert('XSS')</script></body></html>");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 /**
 * Test constructor with null whitelist
 */
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullWhitelist() {
 cleaner = new Cleaner(null);
 }

 /**
 * Test clean method with null dirty document
 */
 @Test(expected = NullPointerException.class)
 public void testCleanWithNullDocument() {
 cleaner.clean(null);
 }

 /**
 * Test clean method with valid document
 */
 @Test
 public void testCleanWithValidDocument() {
 cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1 id='title'>Test</h1></body></html>", cleanDocument.body().html());
 }

 /**
 * Test clean method with invalid document
 * This test case assumes that the whitelist used in this test does not allow script tags
 */
 @Test
 public void testCleanWithInvalidDocument() {
 cleaner.clean(dirtyDocument);
 assertEquals("<html><body></body></html>", cleanDocument.body().html());
 }

 /**
 * Test isValid method with null document
 */
 @Test(expected = NullPointerException.class)
 public void testIsValidWithNullDocument() {
 cleaner.isValid(null);
 }

 /**
 * Test isValid method with valid document
 */
 @Test
 public void testIsValidWithValidDocument() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test isValid method with invalid document
 * This test case assumes that the whitelist used in this test does not allow script tags
 */
 @Test
 public void testIsValidWithInvalidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 /**
 * Test copySafeNodes method with null source and destination elements
 */
 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesWithNullSource() {
 copySafeNodes(null, cleanDocument.body());
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesWithNullDestination() {
 copySafeNodes(dirtyDocument.body(), null);
 }

 /**
 * Test copySafeNodes method with a safe tag and attributes
 */
 @Test
 public void testCopySafeNodesWithSafeTagAndAttributes() {
 Element safeElement = new Element("p", null);
 safeElement.attributes().put("id", "safeId");
 safeElement.text("Safe text");
 Element destElement = new Element("p", safeElement.baseUri(), new Attributes());
 int numDiscarded = copySafeNodes(safeElement, destElement);
 assertEquals(0, numDiscarded);
 assertEquals("Safe text", destElement.text());
 assertEquals("p", destElement.tagName());
 assertEquals("id", destElement.attribute(0).getName());
 assertEquals("safeId", destElement.attribute(0).getValue());
 }

 /**
 * Test copySafeNodes method with an unsafe tag
 */
 @Test
 public void testCopySafeNodesWithUnsafeTag() {
 Element unsafeElement = new Element("script", null);
 unsafeElement.text("Unsafe script");
 int numDiscarded = copySafeNodes(unsafeElement, cleanDocument.body());
 assertEquals(1, numDiscarded);
 assertTrue(cleanDocument.body().children().isEmpty());
 }

 /**
 * Test createSafeElement method with null source element
 */
 @Test(expected = NullPointerException.class)
 public void testCreateSafeElementWithNullSourceElement() {
 createSafeElement(null);
 }

 /**
 * Test createSafeElement method with a safe tag and attributes
 */
 @Test
 public void testCreateSafeElementWithSafeTagAndAttributes() {
 Element sourceElement = new Element("p", null);
 sourceElement.attributes().put("id", "safeId");
 ElementMeta meta = createSafeElement(sourceElement);
 assertEquals("p", meta.el.tagName());
 assertEquals("id", meta.el.attribute(0).getName());
 assertEquals("safeId", meta.el.attribute(0).getValue());
 assertEquals(0, meta.numAttribsDiscarded);
 }

 /**
 * Test createSafeElement method with an unsafe tag
 */
 @Test
 public void testCreateSafeElementWithUnsafeTag() {
 Element sourceElement = new Element("script", null);
 ElementMeta meta = createSafeElement(sourceElement);
 assertEquals(0, meta.numAttribsDiscarded);
 assertNull(meta.el);
 }

 /**
 * Test isSafeTag method with a safe tag
 */
 @Test
 public void testIsSafeTagWithSafeTag() {
 assertTrue(whitelist.isSafeTag("p"));
 }

 /**
 * Test isSafeTag method with an unsafe tag
 */
 @Test
 public void testIsSafeTagWithUnsafeTag() {
 assertFalse(whitelist.isSafeTag("script"));
 }

 /**
 * Test isSafeAttribute method with a safe tag and safe attribute
 */
 @Test
 public void testIsSafeAttributeWithSafeTagAndSafeAttribute() {
 assertTrue(whitelist.isSafeAttribute("p", null, new Attribute("class", "safeClass")));
 }

 /**
 * Test isSafeAttribute method with a safe tag and unsafe attribute
 */
 @Test
 public void testIsSafeAttributeWithSafeTagAndUnsafeAttribute() {
 assertFalse(whitelist.isSafeAttribute("p", null, new Attribute("onclick", "alert('XSS')")));
 }

 /**
 * Test isSafeAttribute method with an unsafe tag and safe attribute
 */
 @Test
 public void testIsSafeAttributeWithUnsafeTagAndSafeAttribute() {
 assertFalse(whitelist.isSafeAttribute("script", null, new Attribute("type", "text/plain")));
 }

 /**
 * Test isSafeAttribute method with an unsafe tag and unsafe attribute
 */
 @Test
 public void testIsSafeAttributeWithUnsafeTagAndUnsafeAttribute() {
 assertFalse(whitelist.isSafeAttribute("script", null, new Attribute("src", "malicious.js")));
 }

 /**
 * Test getEnforcedAttributes method with a safe tag
 */
 @Test
 public void testGetEnforcedAttributesWithSafeTag() {
 List<Attribute> attributes = whitelist.getEnforcedAttributes("p");
 assertEquals(0, attributes.size());
 }

 /**
 * Test getEnforcedAttributes method with an unsafe tag
 */
 @Test
 public void testGetEnforcedAttributesWithUnsafeTag() {
 List<Attribute> attributes = whitelist.getEnforcedAttributes("script");
 assertEquals(Arrays.asList(new Attribute("type", "text/javascript")), attributes);
 }

}