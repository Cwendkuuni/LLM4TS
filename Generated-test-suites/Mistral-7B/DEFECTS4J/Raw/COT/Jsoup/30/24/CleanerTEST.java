package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * The JUnit test file for the Cleaner class.
 */
public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setup() {
 whitelist = new Whitelist();
 cleaner = new Cleaner(whitelist);

 DocumentBuilder docBuilder = new DocumentBuilder();
 dirtyDocument = docBuilder.parse("<html><body><div id='test'>Hello World! <script>alert('XSS');</script></div></body></html>");
 }

 @Test
 public void testCleanerConstruction() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().hasText());
 assertFalse(cleanDocument.body().hasMetaCharsequences());
 assertFalse(cleanDocument.body().hasForms());
 assertFalse(cleanDocument.body().hasLinks());
 assertFalse(cleanDocument.body().hasScripts());
 assertFalse(cleanDocument.body().hasStylesheets());

 Element body = cleanDocument.body();
 List<Node> children = body.childNodes();
 for (Node node : children) {
 if (node instanceof Element) {
 Element element = (Element) node;
 assertTrue(whitelist.isSafeTag(element.tagName()));
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanMethodNullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testIsValidMethod() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodInvalidDocument() {
 Document invalidDocument = Document.createShell(dirtyDocument.baseUri());
 Element invalidElement = new Element("script", null);
 invalidDocument.body().appendChild(invalidElement);
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesMethodEmptyRoot() {
 Element destination = new Element(Tag.BODY, dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(null, destination);
 assertNull(destination.childNodes());
 assertEq(numDiscarded, 0);
 }

 @Test
 public void testCopySafeNodesMethodEmptySource() {
 Element source = new Element(Tag.BODY, dirtyDocument.baseUri());
 Element destination = new Element(Tag.BODY, dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEq(numDiscarded, 0);
 }

 @Test
 public void testCopySafeNodesMethodSafeTag() {
 Element source = new Element("div", null);
 Element destination = new Element(Tag.DIV, dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEq(numDiscarded, 0);

 Element sourceEl = new Element("div", null);
 sourceEl.classes().add("test");
 Element destinationEl = new Element("div", null);
 numDiscarded = cleaner.copySafeNodes(sourceEl, destinationEl);
 assertEq(numDiscarded, 0);
 }

 @Test
 public void testCopySafeNodesMethodUnsafeTag() {
 Element source = new Element("script", null);
 Element destination = new Element(Tag.DIV, dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEq(numDiscarded, 1);
 assertNull(destination.childNodes());
 }

 @Test
 public void testCopySafeNodesMethodTextNode() {
 Element source = new TextNode("Hello World!");
 Element destination = new Element(Tag.BODY, dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(source, destination);
 assertEq(numDiscarded, 0);

 TextNode destTextNode = (TextNode) destination.childNodes().get(0);
 assertEquals("Hello World!", destTextNode.getWholeText());
 }

 @Test
 public void testCreateSafeElementMethodSafeTag() {
 Element sourceEl = new Element("div", null);
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNotNull(meta.el);
 assertEq(meta.numAttribsDiscarded, 0);
 }

 @Test
 public void testCreateSafeElementMethodUnsafeTag() {
 Element sourceEl = new Element("script", null);
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNull(meta.el);
 assertEq(meta.numAttribsDiscarded, 0);
 }

 @Test
 public void testCreateSafeElementMethodSafeTagWithAttributes() {
 Element sourceEl = new Element("div", null);
 sourceEl.classes().add("test");
 sourceEl.attr("id", "test");
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNotNull(meta.el);
 assertEq(meta.numAttribsDiscarded, 0);
 Element destEl = (Element) meta.el;
 assertEquals("div", destEl.tagName());
 assertEquals("test", destEl.classNames().get(0));
 assertEquals("test", destEl.id());
 }

 @Test
 public void testCreateSafeElementMethodUnsafeTagWithAttributes() {
 Element sourceEl = new Element("script", null);
 sourceEl.classes().add("test");
 sourceEl.attr("id", "test");
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNull(meta.el);
 assertEq(meta.numAttribsDiscarded, 2);
 }

 @Test
 public void testCreateSafeElementMethodWithEmptyTag() {
 Element sourceEl = new Element("img", null);
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNotNull(meta.el);
 assertEq(meta.numAttribsDiscarded, 0);
 }

 @Test
 public void testCreateSafeElementMethodWithEmptyTagWithAttributes() {
 Element sourceEl = new Element("img", null);
 sourceEl.attr("src", "image.jpg");
 ElementMeta meta = cleaner.createSafeElement(sourceEl);
 assertNotNull(meta.el);
 assertEq(meta.numAttribsDiscarded, 1);
 Element destEl = (Element) meta.el;
 assertEquals("img", destEl.tagName());
 assertEquals("image.jpg", destEl.attr("src"));
 }
}