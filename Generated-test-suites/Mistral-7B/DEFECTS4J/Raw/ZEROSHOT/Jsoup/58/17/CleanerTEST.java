package org.jsoup.safety;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;

public class CleanerTest {

 private static final String TEST_HTML_FILE = "test.html";

 @ org.junit.BeforeClass
 public static void setup() throws IOException {
 FileUtils.writeStringToFile(new File(TEST_HTML_FILE), "<html><body><h1 id='test' onclick='alert(\"XSS\")'>XSS test</h1></body></html>", "UTF-8");
 }

 @ org.junit.AfterClass
 public static void teardown() throws IOException {
 FileUtils.deleteQuietly(new File(TEST_HTML_FILE));
 }

 @ org.junit.Test
 public void testCreateCleaner() {
 // given
 Whitelist whitelist = Whitelist.simpleTextOnly();
 Cleaner cleaner = new Cleaner(whitelist);

 // when
 // no assertions here

 // then
 assertNotNull(cleaner);
 }

 @ org.junit.Test
 public void testCleanDocument() throws IOException {
 // given
 File htmlFile = new File(TEST_HTML_FILE);
 Document dirtyDocument = Jsoup.parse(htmlFile);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());

 // when
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // then
 assertEquals("<html><body><h1>XSS test</h1></body></html>", cleanDocument.html());
 }

 @ org.junit.Test
 public void testIsValidDocument() throws IOException {
 // given
 File htmlFile = new File(TEST_HTML_FILE);
 Document dirtyDocument = Jsoup.parse(htmlFile);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());

 // when
 boolean isValid = cleaner.isValid(dirtyDocument);

 // then
 assertFalse(isValid);
 }

 @ org.junit.Test
 public void testIsValidDocumentWithEmptyHead() throws IOException {
 // given
 String testHtml = "<html><body><h1>Test</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(testHtml);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());

 // when
 boolean isValid = cleaner.isValid(dirtyDocument);

 // then
 assertTrue(isValid);
 }

 @ org.junit.Test
 public void testCleanDocumentWithEmptyHead() throws IOException {
 // given
 String testHtml = "<html><body><h1>Test</h1></body></html>";
 Document dirtyDocument = Jsoup.parse(testHtml);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());

 // when
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // then
 assertEquals("<html><body><h1>Test</h1></body></html>", cleanDocument.html());
 }

 @ org.junit.Test
 public void testCleanDocumentWithScriptTag() throws IOException {
 // given
 File htmlFile = new File(TEST_HTML_FILE);
 Document dirtyDocument = Jsoup.parse(htmlFile);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());

 // when
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // then
 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testCreateCleanerWithNullWhitelist() {
 // given
 Cleaner cleaner = new Cleaner(null);

 // when & then
 // an exception is expected
 }

 @ org.junit.Test(expected = NullPointerException.class)
 public void testCleanDocumentWithNullDocument() {
 // given
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document dirtyDocument = null;

 // when & then
 // an exception is expected
 cleaner.clean(dirtyDocument);
 }
}