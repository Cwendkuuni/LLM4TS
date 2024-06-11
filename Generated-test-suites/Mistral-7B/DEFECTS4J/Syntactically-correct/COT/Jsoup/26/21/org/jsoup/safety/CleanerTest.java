package org.jsoup.safety;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleDefault();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanerConstructor() {
 // Test constructor with valid whitelist
 Assert.assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 // Test clean method with valid document
 Document dirtyDocument = createDocument("<html><body><p>Hello World</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertNotNull(cleanDocument);
 Assert.assertEquals("<html><body><p></p></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidMethod() {
 // Test isValid method with valid document
 Document validDocument = createDocument("<html><body><p>Hello World</p></body></html>");
 Assert.assertTrue(cleaner.isValid(validDocument));

 // Test isValid method with invalid document
 Document invalidDocument = createDocument("<html><body><p>Hello World<script>alert('XSS')</script></p></body></html>");
 Assert.assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesMethodWithSafeElement() {
 // Test copySafeNodes method with safe element
 Element sourceElement = createElement("div", null, Arrays.asList("class", "testClass"));
 Element destElement = new Element(Tag.DIV, null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

 Assert.assertEquals("testClass", destElement.attributes().get("class"));
 Assert.assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethodWithUnsafeElement() {
 // Test copySafeNodes method with unsafe element
 Element sourceElement = createElement("script", null, null);
 Element destElement = new Element(Tag.DIV, null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

 Assert.assertEquals(1, numDiscarded);
 Assert.assertNull(destElement.childNodes());
 }

 @Test
 public void testCopySafeNodesMethodWithElementContainingSafeChild() {
 // Test copySafeNodes method with element containing safe child
 Element sourceElement = createElement("div", null, Arrays.asList("class", "testClass"));
 Element safeChild = createElement("p", null, null);
 int numDiscarded = cleaner.copySafeNodes(sourceElement, null);
 Element destElement = new Element(Tag.DIV, null, new Attributes());
 cleaner.copySafeNodes(sourceElement, destElement);
 destElement.appendChild(safeChild);

 Assert.assertEquals(0, numDiscarded);
 Assert.assertEquals(1, destElement.childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesMethodWithNullSource() {
 // Test copySafeNodes method with null source
 int numDiscarded = cleaner.copySafeNodes(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopySafeNodesMethodWithNullDestination() {
 // Test copySafeNodes method with null destination
 int numDiscarded = cleaner.copySafeNodes(createElement("div", null, null), null);
 }

 @Test
 public void testCreateSafeElementMethod() {
 // Test createSafeElement method with safe element
 Element sourceElement = createElement("div", null, Arrays.asList("class", "testClass"));
 ElementMeta result = cleaner.createSafeElement(sourceElement);

 Assert.assertNotNull(result.el);
 Assert.assertEquals("div", result.el.tagName());
 Assert.assertEquals(0, result.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementMethodWithUnsafeElement() {
 // Test createSafeElement method with unsafe element
 Element sourceElement = createElement("script", null, null);
 ElementMeta result = cleaner.createSafeElement(sourceElement);

 Assert.assertNull(result.el);
 }

 private Document createDocument(String html) {
 return Jsoup.parseBodyFragment(html);
 }

 private Element createElement(String tagName, String id, List<String> classes) {
 return new Element(Tag.valueOf(tagName), null, new Attributes().put("id", id));
 if (classes != null) {
 for (String className : classes) {
 attributes.put(className);
 }
 }
 return new Element(tagName, null, attributes);
 }

}