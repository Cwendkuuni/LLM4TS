package org.jsoup.safety;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.Whitelist;
import org.jsoup.jsoup;

import java.util.List;

@RunWith(org.junit.runner.JUnit4.class)
public class CleanerTest {

 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Validate.notNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 // Given a dirty document
 Document dirtyDocument = jsoup.parse("<html><body><script>alert('XSS')</script><p>Hello</p></body></html>");

 // When cleaning the document using the cleaner
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Then the cleaned document should not contain the script tag
 Assert.assertFalse(cleanDocument.body().hasMetaElements());
 }

 @Test
 public void testIsValidMethodWithValidDocument() {
 // Given a valid document
 Document validDocument = jsoup.parse("<html><body><p>Hello</p></body></html>");

 // When validating the document using the cleaner
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 boolean isValid = cleaner.isValid(validDocument);

 // Then the document is valid
 Assert.assertTrue(isValid);
 }

 @Test
 public void testIsValidMethodWithInvalidDocument() {
 // Given an invalid document
 Document invalidDocument = jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");

 // When validating the document using the cleaner
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 boolean isValid = cleaner.isValid(invalidDocument);

 // Then the document is invalid
 Assert.assertFalse(isValid);
 }

 @Test
 public void testCopySafeNodesMethodWithEmptyDocument() {
 // Given an empty document
 Document emptyDocument = jsoup.parse("<html></html>");

 // When copying safe nodes from the empty document to a new document
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document newDocument = Document.createShell(emptyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(emptyDocument.body(), newDocument.body());

 // Then no nodes were discarded
 Assert.assertEquals(0, numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethodWithValidDocument() {
 // Given a valid document
 Document validDocument = jsoup.parse("<html><body><p id='test' class='test' title='Test'>Hello World</p></body></html>");

 // When copying safe nodes from the document to a new document
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document newDocument = Document.createShell(validDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(validDocument.body(), newDocument.body());

 // Then no nodes were discarded
 Assert.assertEquals(0, numDiscarded);

 // And the new document should contain the original node with its attributes
 Element originalNode = validDocument.body().getElementById("test");
 Element newNode = newDocument.body().getElementById("test");
 Assert.assertNotNull(newNode);
 Assert.assertEquals(originalNode.tagName(), newNode.tagName());
 Assert.assertEquals(originalNode.text(), newNode.text());
 Assert.assertEquals(originalNode.classes(), newNode.classes());
 Assert.assertEquals(originalNode.attr("id"), newNode.attr("id"));
 Assert.assertEquals(originalNode.attr("title"), newNode.attr("title"));
 }

 @Test
 public void testCopySafeNodesMethodWithInvalidDocument() {
 // Given an invalid document
 Document invalidDocument = jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");

 // When copying safe nodes from the document to a new document
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document newDocument = Document.createShell(invalidDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(invalidDocument.body(), newDocument.body());

 // Then all nodes were discarded
 Assert.assertEquals(invalidDocument.body().childNodeSize(), numDiscarded);

 // And the new document should be empty
 Assert.assertEquals(0, newDocument.body().childNodeSize());
 }

 @Test
 public void testIsValidMethodWithCleanDocument() {
 // Given a cleaned document
 Document dirtyDocument = jsoup.parse("<html><body><script>alert('XSS')</script><p>Hello</p></body></html>");
 Document cleanDocument = new Cleaner(Whitelist.simpleText()).clean(dirtyDocument);

 // When validating the cleaned document
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 boolean isValid = cleaner.isValid(cleanDocument);

 // Then the document is valid
 Assert.assertTrue(isValid);
 }
}