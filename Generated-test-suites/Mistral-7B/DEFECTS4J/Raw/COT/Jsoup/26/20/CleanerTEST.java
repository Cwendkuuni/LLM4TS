package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.Whitelist;
import org.jsoup.helper.Validate;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;
 private Element sourceElement;
 private Element destElement;

 @Before
 public void setUp() {
 // Initialize Whitelist for testing
 whitelist = Whitelist.simple();

 // Initialize dirtyDocument and sourceElement for testing
 String html = "<html><body><h1 id='title' class='title'><script>alert('XSS');</script>Hello World</h1><p id='paragraph'>This is a paragraph.</p></body></html>";
 dirtyDocument = Jsoup.parse(html);
 sourceElement = dirtyDocument.body().getElementsByTag("h1").first();
 }

 /**
 * Test constructor of Cleaner class
 */
 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 /**
 * Test clean method, valid input
 */
 @Test
 public void testCleanValidInput() {
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanDocument.body().toString().isEmpty());
 }

 /**
 * Test clean method, invalid input (null document)
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCleanNullDocument() {
 Cleaner cleaner = new Cleaner(whitelist);
 cleaner.clean(null);
 }

 /**
 * Test isValid method, valid input
 */
 @Test
 public void testIsValidValidInput() {
 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 /**
 * Test isValid method, invalid input (javascript tag)
 */
 @Test
 public void testIsValidInvalidInput() {
 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 /**
 * Test copySafeNodes method, safe tag and attributes
 */
 @Test
 public void testCopySafeNodesSafeTagAndAttributes() {
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element destElement = cleanDocument.body().getElementsByTag("h1").first();
 assertEquals(sourceElement.tagName(), destElement.tagName());
 assertEquals(sourceElement.attributes().size(), destElement.attributes().size());
 }

 /**
 * Test copySafeNodes method, unsafe tag but safe children
 */
 @Test
 public void testCopySafeNodesUnsafeTagButSafeChildren() {
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element destElement = cleanDocument.body().getElementsByTag("script").first();
 // script tag is not safe, but it doesn't have any children, so we don't expect any node to be copied
 assertTrue(destElement == null);
 }

 /**
 * Test copySafeNodes method, safe tag but unsafe attributes
 */
 @Test
 public void testCopySafeNodesSafeTagButUnsafeAttributes() {
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element destElement = cleanDocument.body().getElementsByTag("h1").first();
 String id = "title";
 String classAttribute = "title";
 String expectedId = id;
 String expectedClass = classAttribute;
 // id and class attributes are safe, but lets test if they are correctly copied
 assertEquals(expectedId, destElement.id());
 assertEquals(expectedClass, destElement.classes().get(0));

 // test if other unsafe attributes are discarded
 Attribute scriptAttribute = sourceElement.attr("onclick");
 assertNull(destElement.attr("onclick"));
 }

 /**
 * Test copySafeNodes method, recursive safe tag with children
 */
 @Test
 public void testCopySafeNodesRecursiveSafeTagWithChildren() {
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element destElement = cleanDocument.body().getElementsByTag("h1").first();
 Element childElement = destElement.child(0);
 // child of h1 tag should be a text node with "Hello World"
 assertEquals("Hello World", childElement.text());
 }

 /**
 * Test createSafeElement method, safe tag and attributes
 */
 @Test
 public void testCreateSafeElementSafeTagAndAttributes() {
 Cleaner cleaner = new Cleaner(whitelist);
 Element sourceElement = new Element("h1", "title");
 Element destElement = cleaner.createSafeElement(sourceElement);
 assertEquals("h1", destElement.tagName());
 assertEquals("title", destElement.id());
 }

 /**
 * Test createSafeElement method, unsafe tag but safe attributes
 */
 @Test
 public void testCreateSafeElementUnsafeTagButSafeAttributes() {
 Cleaner cleaner = new Cleaner(whitelist);
 Element sourceElement = new Element("script", "scriptTag");
 Element destElement = cleaner.createSafeElement(sourceElement);
 // script tag is not safe, but its attributes should be checked against whitelist and enforced if needed
 assertNull(destElement);
 }

 /**
 * Test createSafeElement method, safe tag but unsafe attributes
 */
 @Test
 public void testCreateSafeElementSafeTagButUnsafeAttributes() {
 Cleaner cleaner = new Cleaner(whitelist);
 Element sourceElement = new Element("h1", "title", "class", "titleClass");
 Element destElement = cleaner.createSafeElement(sourceElement);
 assertEquals("h1", destElement.tagName());
 assertEquals("title", destElement.id());
 assertEquals("titleClass", destElement.classes().get(0));
 }

 /**
 * Test createSafeElement method, recursive safe tag with children
 */
 @Test
 public void testCreateSafeElementRecursiveSafeTagWithChildren() {
 Cleaner cleaner = new Cleaner(whitelist);
 Element sourceElement = new Element("h1", null, null, null, new Element("p", "paragraph"));
 Element destElement = cleaner.createSafeElement(sourceElement);
 assertEquals("h1", destElement.tagName());
 Element childElement = destElement.child(0);
 assertEquals("p", childElement.tagName());
 }

 /**
 * Test ElementMeta class
 */
 @Test
 public void testElementMetaClass() {
 ElementMeta elementMeta = new ElementMeta(new Element("h1"), 3);
 assertEquals("h1", elementMeta.el.tagName());
 assertEquals(3, elementMeta.numAttribsDiscarded);
 }
}