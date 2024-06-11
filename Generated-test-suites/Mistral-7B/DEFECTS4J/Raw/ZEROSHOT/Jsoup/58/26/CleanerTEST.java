package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.CleaningVisitor;
import org.jsoup.safety.ElementMeta;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = mock(Whitelist.class);
 cleaner = new Cleaner(whitelist);

 Parser parser = new Parser();
 Element body = parser.parseBodyFragment("<html><body><div id='test'><h1>Hello World</h1><script>alert('XSS')</script></div></body></html>");
 dirtyDocument = new Document(parser.parseBodyFragment("<html><body></body></html>").outerHtml(), null);
 dirtyDocument.body().appendChild(body);

 cleanDocument = new Document();
 }

 @Test
 public void testCreateCleaner() {
 Validate.notNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document expectedCleanDocument = new Document();
 expectedCleanDocument.body().appendChild(new Element(Tag.H1, "Hello World"));

 Document actualCleanDocument = cleaner.clean(dirtyDocument);

 assertEquals(expectedCleanDocument.outerHtml(), actualCleanDocument.outerHtml());
 assertNull(actualCleanDocument.select("script").first());
 }

 @Test
 public void testIsValidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithEmptyBody() {
 Document emptyDocument = new Document(parser.parseBodyFragment("").outerHtml(), null);
 assertTrue(cleaner.isValid(emptyDocument));
 }

 @Test
 public void testIsValidDocumentWithValidBody() {
 Document validDocument = new Document(parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>").outerHtml(), null);
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testIsValidDocumentWithInvalidBody() {
 Document invalidDocument = new Document(parser.parseBodyFragment("<html><body><script>alert('XSS')</script></body></html>").outerHtml(), null);
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCleaningVisitor() {
 CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(dirtyDocument.body(), cleanDocument.body());
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(dirtyDocument.body());

 assertEquals(1, cleanDocument.body().childNodeSize());
 assertEquals("Hello World", cleanDocument.body().child(0).text());
 assertNull(cleanDocument.select("script").first());
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceElement = new Element(Tag.DIV, null);
 sourceElement.addClass("test");
 sourceElement.attr("data-test", "testData");
 sourceElement.text("Test Text");

 Element expectedSafeElement = new Element(Tag.DIV, null, new Attributes().addClass("test").put("data-test", "testData"));
 Element actualSafeElement = cleaner.createSafeElement(sourceElement);

 assertEquals(expectedSafeElement.outerHtml(), actualSafeElement.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateSafeElementNullSourceElement() {
 cleaner.createSafeElement(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateSafeElementInvalidTagName() {
 Element sourceElement = new Element("INVALID", null);
 cleaner.createSafeElement(sourceElement);
 }

 @Test
 public void testCreateSafeElementWithAllowedAttributes() {
 Element sourceElement = new Element(Tag.DIV, null);
 sourceElement.addClass("test");
 sourceElement.attr("data-test", "testData");

 Element expectedSafeElement = new Element(Tag.DIV, null, new Attributes().addClass("test").put("data-test", "testData"));
 Element actualSafeElement = cleaner.createSafeElement(sourceElement);

 assertEquals(expectedSafeElement.outerHtml(), actualSafeElement.outerHtml());
 }

 @Test
 public void testCreateSafeElementWithDisallowedAttributes() {
 Element sourceElement = new Element(Tag.DIV, null);
 sourceElement.addClass("test");
 sourceElement.attr("data-test", "testData");
 sourceElement.attr("onclick", "alert('XSS')");

 Element expectedSafeElement = new Element(Tag.DIV, null, new Attributes().addClass("test").put("data-test", "testData"));
 Element actualSafeElement = cleaner.createSafeElement(sourceElement);

 assertEquals(expectedSafeElement.outerHtml(), actualSafeElement.outerHtml());
 assertNull(actualSafeElement.attr("onclick"));
 }

 @Test
 public void testCreateSafeElementWithEnforcedAttributes() {
 when(whitelist.getEnforcedAttributes(Tag.DIV.name())).thenReturn(new Attributes().put("class", "test").put("data-test", "testData"));

 Element sourceElement = new Element(Tag.DIV, null);
 sourceElement.addClass("test");
 sourceElement.attr("data-test", "testData");

 Element expectedSafeElement = new Element(Tag.DIV, null, new Attributes().addClass("test").put("data-test", "testData"));
 Element actualSafeElement = cleaner.createSafeElement(sourceElement);

 assertEquals(expectedSafeElement.outerHtml(), actualSafeElement.outerHtml());
 }

 @Test
 public void testCopySafeNodes() {
 // Test copying empty body
 Document emptySourceDocument = new Document(parser.parseBodyFragment("<html><body></body></html>").outerHtml(), null);
 Document emptyDestinationDocument = new Document(parser.parseBodyFragment("<html><body></body></html>").outerHtml(), null);
 int numDiscarded = cleaner.copySafeNodes(emptySourceDocument.body(), emptyDestinationDocument.body());
 assertEquals(0, numDiscarded);
 assertEquals(0, emptyDestinationDocument.body().childNodeSize());

 // Test copying valid body
 Document validSourceDocument = new Document(parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>").outerHtml(), null);
 Document validDestinationDocument = new Document(parser.parseBodyFragment("<html><body></body></html>").outerHtml(), null);
 numDiscarded = cleaner.copySafeNodes(validSourceDocument.body(), validDestinationDocument.body());
 assertEquals(0, numDiscarded);
 assertEquals(1, validDestinationDocument.body().childNodeSize());
 assertEquals("Hello World", validDestinationDocument.body().child(0).text());

 // Test copying invalid body
 Document invalidSourceDocument = new Document(parser.parseBodyFragment("<html><body><div id='test'><h1>Hello World</h1><script>alert('XSS')</script></div></body></html>").outerHtml(), null);
 Document invalidDestinationDocument = new Document(parser.parseBodyFragment("<html><body></body></html>").outerHtml(), null);
 numDiscarded = cleaner.copySafeNodes(invalidSourceDocument.body(), invalidDestinationDocument.body());
 assertEquals(1, numDiscarded);
 assertNull(invalidDestinationDocument.select("script").first());
 assertEquals(1, invalidDestinationDocument.body().childNodeSize());
 assertEquals("Hello World", invalidDestinationDocument.body().child(0).text());

 // Test copying body with disallowed attributes
 Document disallowedAttributesSourceDocument = new Document(parser.parseBodyFragment("<html><body><div class='test' data-test='testData' onclick='alert(\"XSS\")'>Test</div></body></html>").outerHtml(), null);
 Document disallowedAttributesDestinationDocument = new Document(parser.parseBodyFragment("<html><body></body></html>").outerHtml(), null);
 numDiscarded = cleaner.copySafeNodes(disallowedAttributesSourceDocument.body(), disallowedAttributesDestinationDocument.body());
 assertEquals(1, numDiscarded);
 assertNull(disallowedAttributesDestinationDocument.select("script").first());
 assertEquals(1, disallowedAttributesDestinationDocument.body().childNodeSize());
 assertEquals("Test", disallowedAttributesDestinationDocument.body().child(0).text());
 assertNull(disallowedAttributesDestinationDocument.body().child(0).attr("onclick"));
 assertSame(Tag.DIV, disallowedAttributesDestinationDocument.body().child(0).tagName());
 assertEquals("test", disallowedAttributesDestinationDocument.body().child(0).classes().get(0));
 assertEquals("testData", disallowedAttributesDestinationDocument.body().child(0).attr("data-test"));

 // Test copying body with enforced attributes
 when(whitelist.getEnforcedAttributes(Tag.DIV.name())).thenReturn(new Attributes().put("class", "test").put("data-test", "testData"));
 Document enforcedAttributesSourceDocument = new Document(parser.parseBodyFragment("<html><body><div class='test' data-test='testData'>Test</div></body></html>").outerHtml(), null);
 Document enforcedAttributesDestinationDocument = new Document(parser.parseBodyFragment("<html><body></body></html>").outerHtml(), null);
 numDiscarded = cleaner.copySafeNodes(enforcedAttributesSourceDocument.body(), enforcedAttributesDestinationDocument.body());
 assertEquals(0, numDiscarded);
 assertEquals(1, enforcedAttributesDestinationDocument.body().childNodeSize());
 assertEquals("Test", enforcedAttributesDestinationDocument.body().child(0).text());
 assertSame(Tag.DIV, enforcedAttributesDestinationDocument.body().child(0).tagName());
 assertEquals("test", enforcedAttributesDestinationDocument.body().child(0).classes().get(0));
 assertEquals("testData", enforcedAttributesDestinationDocument.body().child(0).attr("data-test"));
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesNullSourceBody() {
 cleaner.copySafeNodes(null, cleanDocument.body());
 }

 @Test(expected = NullPointerException.class)
 public void testCopySafeNodesNullDestinationBody() {
 cleaner.copySafeNodes(dirtyDocument.body(), null);
 }

}