package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeTraversor;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 private static final String TEST_HTML = "<html><head><title>Test</title><script>alert('XSS')</script></head>"
 + "<body><h1 id='title' onclick='alert(\"click\")'>Click me</h1>"
 + "<p>Hello <i>world</i></p>"
 + "<p>Safe attribute: <span data-custom-safe=\"safe\">Attribute</span></p>"
 + "</body></html>";

 @org.junit.BeforeClass
 public static void setup() {
 Whitelist whitelist = Whitelist.relaxed();
 whitelist.addTags("h1", "p", "span");
 whitelist.addAttributes("h1", "onclick");
 whitelist.addAttributeFilter("h1", "id", true);
 whitelist.addAttributeFilter("h1", "data-custom-safe", true);
 Cleaner cleaner = new Cleaner(whitelist);
 }

 @org.junit.Test
 public void testClean() {
 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><head></head><body><h1 id='title'>Click me</h1><p>Hello <i>world</i></p><p>Safe attribute: <span data-custom-safe=\"safe\">Attribute</span></p></body></html>",
 cleanDocument.html());
 }

 @org.junit.Test
 public void testIsValid() {
 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 @org.junit.Test
 public void testInvalid() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @org.junit.Test
 public void testCleanWithScript() {
 Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title><script>alert('XSS')</script></head>"
 + "<body><h1 id='title'>Click me</h1>"
 + "<p>Hello <i>world</i></p>"
 + "</body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><head></head><body><h1 id='title'>Click me</h1><p>Hello <i>world</i></p></body></html>", cleanDocument.html());
 }

 @org.junit.Test
 public void testCopySafeNodes() {
 Document dirtyDocument = Jsoup.parse("<html><head></head><body>"
 + "<h1 id='title' onclick='alert(\"click\")'>Click me</h1>"
 + "<p>Hello <i>world</i></p>"
 + "<p>Safe attribute: <span data-custom-safe=\"safe\">Attribute</span></p>"
 + "</body></html>");
 Element source = dirtyDocument.body();
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(source, destination);

 assertEquals(3, numDiscarded);
 assertEquals("<html><head></head><body><div></div></body></html>", destination.html());
 }
}