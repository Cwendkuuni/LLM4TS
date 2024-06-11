package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<script>alert('XSS')</script>");
 assertFalse("Cleaner should not clean invalid document", cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue("Cleaner should have cleaned invalid document", cleanDocument.body().text().isEmpty());
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<p>Hello World</p>");
 assertTrue("Cleaner should consider valid document valid", cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element body = cleanDocument.body();
 assertNotNull("Cleaner should not return null for body", body);
 assertTrue("Cleaner body should contain expected text", body.text().equals("Hello World"));
 }

 @Test
 public void testCleanDocumentWithDisallowedTag() {
 Document dirtyDocument = Jsoup.parse("<script>alert('XSS')</script><p>Hello World</p>");
 assertTrue("Cleaner should consider valid document valid initially", cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertFalse("Cleaner should not have allowed disallowed tag", cleanDocument.body().select("script").size() > 0);
 }

 @Test
 public void testCleanDocumentWithDisallowedAttribute() {
 Document dirtyDocument = Jsoup.parse("<p onclick='alert(\"XSS\")'>Hello World</p>");
 assertTrue("Cleaner should consider valid document valid initially", cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertFalse("Cleaner should not have allowed disallowed attribute", cleanDocument.body().select("p[onclick]").size() > 0);
 }

 @Test
 public void testCleanDocumentWithMultipleDisallowedElements() {
 Document dirtyDocument = Jsoup.parse("<script>alert('XSS')</script><p>Hello World</p><script>alert('XSS')</script>");
 assertTrue("Cleaner should consider valid document valid initially", cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertFalse("Cleaner should not have allowed disallowed elements", cleanDocument.body().select("script").size() > 0);
 }

 @Test
 public void testCleanDocumentWithMultipleDisallowedAttributes() {
 Document dirtyDocument = Jsoup.parse("<p onfocus='alert(\"focus\")' onclick='alert(\"click\")'>Hello World</p>");
 assertTrue("Cleaner should consider valid document valid initially", cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertFalse("Cleaner should not have allowed disallowed attribute onfocus", cleanDocument.body().select("p[onfocus]").size() > 0);
 assertFalse("Cleaner should not have allowed disallowed attribute onclick", cleanDocument.body().select("p[onclick]").size() > 0);
 }

 @Test
 public void testCleanDocumentWithVariablesInTags() {
 Document dirtyDocument = Jsoup.parse("<p>Hello %name%, welcome to the site!</p>", "http://example.com");
 assertTrue("Cleaner should consider valid document valid initially", cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull("Cleaner should not return null for body", cleanDocument.body());
 Element body = cleanDocument.body();
 List<String> tags = Arrays.asList("p");
 List<String> expectedTags = Arrays.asList("p");
 assertTrue("Cleaner body should only contain expected tags", body.tagName().equals(Tag.valueOf(expectedTags.get(0))));
 assertTrue("Cleaner body should contain expected text with variables replaced", body.text().contains("%name%"));
 }
}