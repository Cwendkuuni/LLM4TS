package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CleanerTest {

 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Test
 void testCleanerConstruction() {
 // Test constructor with valid whitelist
 whitelist = new Whitelist.SimpleTextWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test
 void testCleanerInvalidWhitelist() {
 // Test constructor with null whitelist
 Validate.isNull(whitelist);
 assertThrows(NullPointerException.class, () -> new Cleaner(whitelist));

 // Test constructor with empty whitelist
 whitelist = new Whitelist.EmptyWhitelist();
 assertThrows(IllegalArgumentException.class, () -> new Cleaner(whitelist));
 }

 @Test
 void testCreateCleanDocument() {
 // Create a clean document with valid input
 dirtyDocument = Document.newDocument();
 Element body = new Element("body");
 body.text("Hello World!");
 dirtyDocument.setBody(body);
 cleanDocument = new Cleaner(whitelist).clean(dirtyDocument);

 // Check that the clean document has no body, only the root element
 assertEquals(1, cleanDocument.numChildren());
 assertNotNull(cleanDocument.head());
 assertNull(cleanDocument.body());
 }

 @Test
 void testCreateInvalidDocument() {
 // Create an invalid document with a script tag
 dirtyDocument = Document.newDocument();
 Element body = new Element("body");
 Element script = new Element("script");
 script.text("alert('XSS Attack!')");
 body.appendChild(script);
 dirtyDocument.setBody(body);

 // Test that the document is considered invalid
 assertFalse(new Cleaner(whitelist).isValid(dirtyDocument));
 }

 @Test
 void testCopySafeNodes() {
 // Test copying safe nodes
 Element root = new Element("html");
 Element body = new Element("body");
 Element div = new Element("div");
 div.text("Test");
 body.appendChild(div);
 root.appendChild(body);
 cleanDocument = Document.newDocument();
 cleanDocument.setBody(cleanDocument.createElement("body"));
 int numDiscarded = new Cleaner(whitelist).copySafeNodes(root.body(), cleanDocument.body());
 assertEquals(0, numDiscarded);
 }

 @Test
 void testDiscardInvalidNodes() {
 // Test discarding invalid nodes
 Element root = new Element("html");
 Element body = new Element("body");
 Element script = new Element("script");
 script.text("alert('XSS Attack!')");
 body.appendChild(script);
 root.appendChild(body);
 cleanDocument = Document.newDocument();
 cleanDocument.setBody(cleanDocument.createElement("body"));
 int numDiscarded = new Cleaner(whitelist).copySafeNodes(root.body(), cleanDocument.body());
 assertEquals(1, numDiscarded);
 }

 @Test
 void testDiscardInvalidAttributes() {
 // Test discarding invalid attributes
 Element root = new Element("html");
 Element body = new Element("body");
 body.text("Test <div data-xss='alert(\"XSS Attack!\")'>Test</div>");
 root.appendChild(body);
 cleanDocument = Document.newDocument();
 cleanDocument.setBody(cleanDocument.createElement("body"));
 int numDiscarded = new Cleaner(whitelist).copySafeNodes(root.body(), cleanDocument.body());
 assertEquals(1, numDiscarded);
 }

 @Test
 void testGetEnforcedAttributes() {
 // Test getting enforced attributes
 whitelist = new Whitelist.SimpleTextWhitelist().addTagRule("div", "class", true);
 int numAttributes = new Cleaner(whitelist).copySafeNodes(new Element("html"), cleanDocument.body()).numAttribsDiscarded;
 Element div = new Element("div").addClass("test");
 int numEnforced = new Cleaner(whitelist).copySafeNodes(div, new Element("div")).numAttribsDiscarded;
 assertEquals(numAttributes + 1, numEnforced);
 }
}