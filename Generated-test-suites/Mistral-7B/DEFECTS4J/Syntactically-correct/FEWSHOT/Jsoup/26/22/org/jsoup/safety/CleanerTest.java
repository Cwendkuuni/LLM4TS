package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CleanerTest {

 @Mock
 private Whitelist whitelist;

 @Test
 public void testCreateCleaner() {
 Validate.notNull(whitelist);
 Cleaner cleaner = new Cleaner(whitelist);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerNullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test
 public void testClean() {
 // Given
 Document dirtyDocument = Document.createShell("http://test.com");
 Element body = new Element("body");
 body.appendChild(new Element("script").text("alert('XSS')")); // unsafe tag
 body.appendChild(new Element("div").attr("id", "xss").attr("onclick", "alert('XSS')")); // unsafe tag with unsafe attribute
 body.appendChild(new TextNode("foo bar"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(mockWhitelist());

 // When
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Then
 assertTrue(cleanDocument.body().childNodeCount() == 1);
 assertTrue(cleanDocument.body().child(0) instanceof TextNode);
 assertTrue(cleanDocument.body().child(0).text().equals("foo bar"));
 }

 @Test
 public void testIsValid() {
 // Given
 Document dirtyDocument = Document.createShell("http://test.com");
 Element body = new Element("body");
 body.appendChild(new Element("script").text("alert('XSS')")); // unsafe tag
 body.appendChild(new Element("div").attr("id", "xss").attr("onclick", "alert('XSS')")); // unsafe tag with unsafe attribute
 body.appendChild(new TextNode("foo bar"));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(mockWhitelist());

 // When
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Then
 assertFalse(isValid);
 }

 @Test
 public void testIsValidValidDocument() {
 // Given
 Document dirtyDocument = Document.createShell("http://test.com");
 Element body = new Element("body");
 body.appendChild(new Element("p").attr("id", "test").text("This is a test."));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(mockWhitelist());

 // When
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Then
 assertTrue(isValid);
 }

 @Test
 public void testCopySafeNodes() {
 // Given
 Document dirtyDocument = Document.createShell("http://test.com");
 Element body = new Element("body");
 body.appendChild(new Element("script").text("alert('XSS')")); // unsafe tag
 body.appendChild(new Element("div").attr("id", "xss").attr("onclick", "alert('XSS')")); // unsafe tag with unsafe attribute
 body.appendChild(new TextNode("foo bar"));
 body.appendChild(new Element("p").attr("id", "safe").text("This is a safe tag."));
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(mockWhitelist());
 Element cleanBody = new Element("body");
 cleanDocument = Document.createShell("http://test.com");
 cleanDocument.setBody(cleanBody);

 // When
 int numDiscarded = cleaner.copySafeNodes(body, cleanBody);

 // Then
 assertTrue(cleanBody.childNodeCount() == 1);
 assertTrue(cleanBody.child(0) instanceof Element);
 assertTrue(cleanBody.child(0).tagName().equals("p"));
 assertTrue(cleanBody.child(0).childNodeCount() == 1);
 assertTrue(cleanBody.child(0).child(0) instanceof TextNode);
 assertTrue(cleanBody.child(0).child(0).text().equals("This is a safe tag."));
 assertTrue(numDiscarded == 2);
 }

 private Whitelist mockWhitelist() {
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeTag("script")).thenReturn(false);
 when(whitelist.isSafeTag("div")).thenReturn(true);
 when(whitelist.isSafeAttribute("div", null, "id")).thenReturn(true);
 when(whitelist.isSafeAttribute("div", null, "onclick")).thenReturn(false);
 when(whitelist.getEnforcedAttributes("div")).thenReturn(new Attributes());
 return whitelist;
 }
}