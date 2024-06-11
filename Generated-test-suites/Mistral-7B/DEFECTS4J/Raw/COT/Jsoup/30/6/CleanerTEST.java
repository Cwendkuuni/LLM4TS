package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Cleaner cleaner;
 private Document dirtyDocument;
 private Whitelist whitelist;
 private Document cleanDocument;

 @Before
 public void setUp() {
 whitelist = new Whitelist();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = createDocument("dirty.html");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCleanerConstructor() {
 Validate.notNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 // Basic functionality test
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertSame(cleanDocument, cleanedDocument);

 // Edge case: empty input
 Document emptyDocument = Document.createShell("");
 cleanedDocument = cleaner.clean(emptyDocument);
 assertTrue(cleanDocument.body().childNodeSize() > cleanedDocument.body().childNodeSize());

 // Edge case: null input
 cleanDocument = Document.createShell(null);
 try {
 cleaner.clean(cleanDocument);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testIsValidMethod() {
 // Basic functionality test
 assertFalse(cleaner.isValid(dirtyDocument));

 // Edge case: empty input
 Document emptyDocument = Document.createShell("");
 assertTrue(cleaner.isValid(emptyDocument));

 // Edge case: null input
 Document nullDocument = Document.createShell(null);
 assertFalse(cleaner.isValid(nullDocument));
 }

 @Test
 public void testCopySafeNodesMethod() {
 // Basic functionality test
 Element root = dirtyDocument.body().child(0);
 int numDiscarded = cleaner.copySafeNodes(root, cleanDocument.body());
 assertTrue(numDiscarded > 0);

 // Edge case: null root
 root = null;
 numDiscarded = cleaner.copySafeNodes(root, cleanDocument.body());
 assertNull(numDiscarded);

 // Edge case: empty root
 root = new Element("", Tag.BODY);
 numDiscarded = cleaner.copySafeNodes(root, cleanDocument.body());
 assertTrue(numDiscarded == 0);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateSafeElementMethodNullSourceElement() {
 cleaner.createSafeElement(null);
 }

 @Test
 public void testCreateSafeElementMethod() {
 // Basic functionality test
 Element sourceElement = dirtyDocument.body().child(0);
 ElementMeta meta = cleaner.createSafeElement(sourceElement);
 assertSame(cleanDocument.body().child(0), meta.el);

 // Edge case: null tag name
 Element sourceElementNullTag = new Element("", Tag.BODY);
 ElementMeta metaNullTag = cleaner.createSafeElement(sourceElementNullTag);
 assertNull(metaNullTag.el);

 // Edge case: invalid tag name
 Element sourceElementInvalidTag = new Element("script", Tag.BODY);
 ElementMeta metaInvalidTag = cleaner.createSafeElement(sourceElementInvalidTag);
 assertNull(metaInvalidTag.el);
 }
}