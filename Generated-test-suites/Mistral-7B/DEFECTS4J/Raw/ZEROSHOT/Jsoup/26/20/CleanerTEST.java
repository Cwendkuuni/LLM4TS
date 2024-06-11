package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Whitelist.Func;
import org.jsoup.safety.Whitelist.SimpleText;

import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCreateCleaner() {
 Validate.notNull(cleaner, "cleaner should not be null");
 Validate.notNull(whitelist, "whitelist should not be null");
 }

 @Test
 public void testCreateCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test Title</h1><script>alert('XSS');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1 id='title'></h1></body></html>", cleanDocument.body().html());
 assertFalse(cleanDocument.body().hasMetaCharset());
 }

 @Test
 public void testIsValidCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test Title</h1></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test Title</h1><script>alert('XSS');</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodesWithSafeTag() {
 Element sourceElement = Jsoup.parse("<html><body><h1 id='title'>Test Title</h1></body></html>").body().getElementsByTag("h1").get(0);
 Element destElement = new Element(Tag.H1, null, new Attributes());
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
 cleaner.copySafeNodes(sourceElement, destElement);

 assertEquals(sourceElement.html(), elementMeta.el.html());
 assertNull(elementMeta.el.baseUri());
 assertNotNull(elementMeta.el.attributes().getClass());
 assertFalse(elementMeta.el.hasMetaCharset());
 }

 @Test
 public void testCopySafeNodesWithUnsafeTag() {
 Element sourceElement = Jsoup.parse("<html><body><h1 id='title'>Test Title</h1><script>alert('XSS');</script></body></html>").body().getElementsByTag("script").get(0);
 int numDiscarded = cleaner.copySafeNodes(sourceElement, new Element(Tag.SCRIPT, null, new Attributes()));

 assertEquals(0, numDiscarded, "No safe nodes copied as the tag is not allowed by the whitelist.");
 }

 @Test
 public void testCopySafeNodesWithTextNode() {
 Element sourceElement = new Element(Tag.BODY, null, null);
 TextNode sourceTextNode = new TextNode("Hello World", null);
 sourceElement.appendChild(sourceTextNode);
 Element destElement = new Element(Tag.BODY, null, null);
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

 assertEquals("Hello World", destElement.text());
 assertEquals(0, numDiscarded);
 }

 @Test
 public void testCreateSafeElementWithSafeTag() {
 Element sourceElement = Jsoup.parse("<html><body><h1 id='title'>Test Title</h1></body></html>").body().getElementsByTag("h1").get(0);
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

 assertEquals(sourceElement.html(), elementMeta.el.html());
 assertNull(elementMeta.el.baseUri());
 assertNotNull(elementMeta.el.attributes().getClass());
 assertFalse(elementMeta.el.hasMetaCharset());
 }

 @Test
 public void testCreateSafeElementWithUnsafeTag() {
 Element sourceElement = Jsoup.parse("<html><body><h1 id='title'>Test Title</h1><script>alert('XSS');</script></body></html>").body().getElementsByTag("script").get(0);
 int numDiscarded = cleaner.copySafeNodes(sourceElement, new Element(Tag.SCRIPT, null, new Attributes()));
 int numSafeNodes = cleaner.copySafeNodes(sourceElement, new Element(Tag.H1, null, new Attributes()));

 assertEquals(1, numDiscarded, "One unsafe node discarded.");
 assertEquals(0, numSafeNodes, "No safe nodes copied as the tag is not allowed by the whitelist.");
 }

 @Test
 public void testIsValidDocumentWithEmptyWhitelist() {
 Cleaner emptyWhitelistCleaner = new Cleaner(new Whitelist());
 Document dirtyDocument = Jsoup.parse("<html><body>Hello World</body></html>");

 assertFalse(emptyWhitelistCleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithSimpleTextWhitelist() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Test Title</h1>Hello World</body></html>");
 Whitelist simpleTextWhitelist = Whitelist.simpleText();
 Cleaner simpleTextWhitelistCleaner = new Cleaner(simpleTextWhitelist);

 assertTrue(simpleTextWhitelistCleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCreateSafeElementWithAttribute() {
 Element sourceElement = Jsoup.parse("<html><body><h1 id='title' title='Test Title'>Test Title</h1></body></html>").body().getElementsByTag("h1").get(0);
 ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

 assertEquals(sourceElement.html(), elementMeta.el.html());
 assertNull(elementMeta.el.baseUri());
 assertNotNull(elementMeta.el.attributes().getClass());
 assertEquals("Test Title", elementMeta.el.attr("title"));
 }

 @Test
 public void testCreateSafeElementWithUnsafeAttribute() {
 Element sourceElement = Jsoup.parse("<html><body><h1 id='title' title='Test Title' onclick='alert(\"XSS\")'>Test Title</h1></body></html>").body().getElementsByTag("h1").get(0);
 int numDiscarded = cleaner.copySafeNodes(sourceElement, new Element(Tag.H1, null, new Attributes()));
 int numSafeNodes = cleaner.copySafeNodes(sourceElement, new Element(Tag.H1, null, new Attributes()).attr("onclick", ""));

 assertEquals(1, numDiscarded, "One unsafe attribute discarded.");
 assertEquals(0, numSafeNodes, "No safe nodes copied as the tag and attributes are not allowed by the whitelist.");
 }

 @Test
 public void testCreateSafeElementWithAllowedAttribute() {
 Element sourceElement = Jsoup.parse("<html><body><h1 id='title' title='Test Title'>Test Title</h1></body></html>").body().getElementsByTag("h1").get(0);
 Whitelist whitelistWithTitleAllowed = Whitelist.simpleText().addAllowedElements("h1", new Func() {
 @Override
 public void allow(Tag tag, String name, Attributes attrs) {
 attrs.put("title", new SimpleText());
 }
 });
 Cleaner whitelistWithTitleAllowedCleaner = new Cleaner(whitelistWithTitleAllowed);
 ElementMeta elementMeta = whitelistWithTitleAllowedCleaner.createSafeElement(sourceElement);

 assertEquals(sourceElement.html(), elementMeta.el.html());
 assertNull(elementMeta.el.baseUri());
 assertNotNull(elementMeta.el.attributes().getClass());
 assertEquals("Test Title", elementMeta.el.attr("title"));
 }
}