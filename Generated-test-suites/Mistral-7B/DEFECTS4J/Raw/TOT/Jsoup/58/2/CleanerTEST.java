package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;

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
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 String html = "<html><body><p>Hello, <b>World!</b></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element root = cleanDocument.body();
 Element p = root.getFirstElementChild();
 Element b = p.getFirstElementChild();

 assertEquals("Hello, <b>World!</b>", p.outerHtml());
 assertEquals("World!", b.text());
 }

 @Test
 public void testCleanInvalidDocument() {
 String html = "<html><body><p>Hello, <script>alert('XSS!')</script>World!</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);

 int numDiscarded = cleaner.isValid(dirtyDocument) ? 0 : dirtyDocument.body().numChildren();
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element root = cleanDocument.body();
 Element p = root.getFirstElementChild();

 assertTrue(numDiscarded > 0);
 assertNull(p.getElementsByTag("script").first());
 }

 @Test
 public void testCleanEmptyDocument() {
 Document dirtyDocument = Document.createEmpty();
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertNotNull(cleanDocument.body());
 }

 @Test
 public void testCleanDocumentWithFrameset() {
 String html = "<html><frameset><frame src='frame1.html'></frameset></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertNull(cleanDocument.body());
 }

 @Test
 public void testCleanDocumentWithEnforcedAttributes() {
 String html = "<html><body><p id='test' class='test' title='test'>Hello, <b>World!</b></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element p = cleanDocument.body().getFirstElementChild();

 assertEquals("p", p.tagName());
 assertEquals("test", p.classes().get(0));
 assertEquals("test", p.id());
 }
}