package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.Node;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setup() throws IOException {
 whitelist = Mockito.mock(Whitelist.class);
 cleaner = new Cleaner(whitelist);

 dirtyDocument = Jsoup.parse("<html><body><div id='test'><script>alert('XSS')</script><p>Test Content</p></div></body></html>");
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 }

 @Test
 public void testCleaner_ValidDocument() {
 // Given: A valid document
 Element body = dirtyDocument.body();

 // When: Cleaning the document using the cleaner
 Document cleanedDocument = cleaner.clean(dirtyDocument);

 // Then: The cleaned document should be equal to the original document with no script tag
 assertEquals(dirtyDocument, cleanedDocument);
 assertEquals(body, cleanedDocument.body());
 assertFalse(cleanedDocument.body().getElementsByTag("script").isEmpty());
 assertNull(cleanedDocument.body().getElementsByTag("script").get(0).text());
 }

 @Test
 public void testCleaner_InvalidDocument() {
 // Given: An invalid document
 Element body = dirtyDocument.body();
 body.appendElement("script").text("alert('XSS')");

 // When: Trying to clean the document using the cleaner
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Then: The document is invalid and should be cleaned
 assertFalse(isValid);
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertSame(cleanDocument, cleanedDocument);
 assertNull(cleanedDocument.body().getElementsByTag("script").get(0).text());
 }

 @Test
 public void testCleaner_Whitelist_SafeTag() {
 // Given: A whitelist with a safe tag
 whitelist = new Whitelist.SimpleTagFilter(new HashSet<>(Arrays.asList("p", "h1")));
 cleaner = new Cleaner(whitelist);

 // When: Cleaning a document with only safe tags
 Document dirtyDocumentWithSafeTag = Jsoup.parse("<html><body><p>Test Content</p><h1>Test Heading</h1></body></html>");
 Document cleanedDocumentWithSafeTag = cleaner.clean(dirtyDocumentWithSafeTag);

 // Then: The cleaned document should be equal to the original document with the same safe tags
 assertEquals(dirtyDocumentWithSafeTag, cleanedDocumentWithSafeTag);
 assertEquals(dirtyDocumentWithSafeTag.body(), cleanedDocumentWithSafeTag.body());
 }

 @Test
 public void testCleaner_Whitelist_UnsafeTag() {
 // Given: A whitelist with an unsafe tag
 whitelist = new Whitelist.SimpleTagFilter(new HashSet<>(Arrays.asList("p")));
 cleaner = new Cleaner(whitelist);

 // When: Cleaning a document with an unsafe tag
 Document dirtyDocumentWithUnsafeTag = Jsoup.parse("<html><body><p>Test Content</p><script>alert('XSS')</script></body></html>");
 Document cleanedDocumentWithUnsafeTag = cleaner.clean(dirtyDocumentWithUnsafeTag);

 // Then: The cleaned document should not contain the unsafe tag
 assertEquals(cleanDocument, cleanedDocumentWithUnsafeTag);
 assertNull(cleanedDocumentWithUnsafeTag.body().getElementsByTag("script").get(0).text());
 }

 @Test
 public void testCleaner_Whitelist_SafeAttribute() {
 // Given: A whitelist with a safe tag and attribute
 whitelist = new Whitelist.SimpleAttributeFilter("class", "test");
 cleaner = new Cleaner(whitelist);

 // When: Cleaning a document with a safe attribute
 Document dirtyDocumentWithSafeAttribute = Jsoup.parse("<html><body><p class='test'>Test Content</p></body></html>");
 Document cleanedDocumentWithSafeAttribute = cleaner.clean(dirtyDocumentWithSafeAttribute);

 // Then: The cleaned document should be equal to the original document with the safe attribute
 assertEquals(dirtyDocumentWithSafeAttribute, cleanedDocumentWithSafeAttribute);
 assertEquals(dirtyDocumentWithSafeAttribute.body(), cleanedDocumentWithSafeAttribute.body());
 }

 @Test
 public void testCleaner_Whitelist_UnsafeAttribute() {
 // Given: A whitelist with an unsafe tag and attribute
 whitelist = new Whitelist.SimpleAttributeFilter("class", "test");
 cleaner = new Cleaner(whitelist);

 // When: Cleaning a document with an unsafe attribute
 Document dirtyDocumentWithUnsafeAttribute = Jsoup.parse("<html><body><p class='test unsafe'>Test Content</p></body></html>");
 Document cleanedDocumentWithUnsafeAttribute = cleaner.clean(dirtyDocumentWithUnsafeAttribute);

 // Then: The cleaned document should not contain the unsafe attribute
 assertEquals(cleanDocument, cleanedDocumentWithUnsafeAttribute);
 assertSame(cleanDocument.body(), cleanedDocumentWithUnsafeAttribute.body());
 }
}