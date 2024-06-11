package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.util.List;

public class CleanerTest {

 private static final String HTML_SAMPLE = "<html><head></head><body><div id='test' class='test' title='test' data-custom='custom'><h1>Test <script>alert('XSS')</script></h1><p>Safe text</p></div></body></html>";

 @Before
 public void setUp() {
 Whitelist whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 }

 private Cleaner cleaner;

 /**
 * Test constructor with valid Whitelist
 */
 @Test
 public void testCleanerConstruction() {
 Cleaner cleaner = new Cleaner(new Whitelist());
 assertNotNull(cleaner);
 }

 /**
 * Test clean method with valid Document
 */
 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><head></head><body><div id='test' class='test' title='test'><h1></h1><p>Safe text</p></div></body></html>", cleanDocument.html());
 }

 /**
 * Test clean method with invalid Document
 */
 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);

 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><head></head><body><div id='test' class='test' title='test'><h1></h1><p>Safe text</p></div></body></html>", cleanDocument.html());
 }

 /**
 * Test isValid method with valid Document
 */
 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><div class='test'></div></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }

 /**
 * Test isValid method with invalid Document
 */
 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 /**
 * Test copySafeNodes method with empty Element
 */
 @Test
 public void testCopySafeNodesEmptyElement() {
 Element source = Jsoup.parse("<html><body></body></html>").body();
 Element dest = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with safe Element
 */
 @Test
 public void testCopySafeNodesSafeElement() {
 Element source = Jsoup.parse("<html><body><h1>Test</h1></body></html>").body().getElementsByTag("h1").first();
 Element dest = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(0, numDiscarded);
 }

 /**
 * Test copySafeNodes method with unsafe Element
 */
 @Test(expected = RuntimeException.class)
 public void testCopySafeNodesUnsafeElement() {
 Element source = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>").body().getElementsByTag("script").first();
 Element dest = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 fail("Expected RuntimeException");
 }

 /**
 * Test createSafeElement method with safe Element
 */
 @Test
 public void testCreateSafeElementSafeElement() {
 Element source = Jsoup.parse("<html><body><h1 class='test'>Test</h1></body></html>").body().getElementsByTag("h1").first();
 Element dest = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(0, numDiscarded);
 }

 /**
 * Test createSafeElement method with unsafe Attribute
 */
 @Test
 public void testCreateSafeElementUnsafeAttribute() {
 Element source = Jsoup.parse("<html><body><div id='test' class='test' title='test' data-custom='custom'><h1>Test <script>alert('XSS')</script></h1></div></body></html>").body().getElementsByTag("h1").first();
 Element dest = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(source, dest);
 assertEquals(1, numDiscarded);
 }
}