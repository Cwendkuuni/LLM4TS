package org.jsoup.safety;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CleanerTest {

 private static final String SAMPLE_HTML = "" +
         "<html><head></head><body>" +
         "<div id=\"safeDiv\">Hello World!</div>" +
         "<script>alert('XSS Attack!')</script>" +
         "</body></html>";

 @Test
 public void testValidDocument() throws IOException {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse(SAMPLE_HTML);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assertions.assertFalse(cleanDocument.body().text().contains("alert('XSS Attack!'"));
 Assertions.assertEquals("<html><head></head><body><div id=\"safeDiv\">Hello World</div></body></html>", cleanDocument.body().html());
 }

 @Test
 public void testInvalidDocument() throws IOException {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS Attack!')</script></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assertions.assertFalse(isValid);
 }

 @Test
 public void testCopySafeNodes() throws IOException {
 Whitelist whitelist = new Whitelist.StaticWhitelist.Builder()
         .addTags("div")
         .addAttributes("id")
         .build();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse("<html><body><div id=\"safeDiv\">Hello <script>World</script></div></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Element cleanDiv = cleanDocument.body().getElementById("safeDiv");
 Assertions.assertEquals("Hello ", cleanDiv.text());
 Assertions.assertEquals("<div id=\"safeDiv\">Hello </div>", cleanDiv.html());
 }

 @Test
 public void testIsSafeTag() {
 Cleaner cleaner = new Cleaner(null);

 Assertions.assertTrue(cleaner.whitelist.isSafeTag("div"));
 Assertions.assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttribute() {
 Cleaner cleaner = new Cleaner(null);

 Assertions.assertTrue(cleaner.whitelist.isSafeAttribute("div", null, "id"));
 Assertions.assertFalse(cleaner.whitelist.isSafeAttribute("div", null, "src"));
 }
}