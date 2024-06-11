package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CleanerTest {

 @InjectMocks
 private Cleaner cleaner;

 @Test
 public void testCreateCleaner() {
 // Given
 Whitelist whitelist = mock(Whitelist.class);

 // When
 Cleaner cleaner = new Cleaner(whitelist);

 // Then
 assertEquals(whitelist, cleaner.whitelist);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerNullWhitelist() {
 // Given
 Whitelist whitelist = null;

 // When
 Cleaner cleaner = new Cleaner(whitelist);

 // Then - should throw NPE
 }

 @Test
 public void testCleanDocument() {
 // Given
 Document dirtyDocument = createDocument("<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>");
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeTag("h1")).thenReturn(true);
 when(whitelist.isSafeTag("script")).thenReturn(false);

 // When
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Then
 assertNotNull(cleanDocument);
 assertEquals(1, cleanDocument.body().childNodeSize());
 assertEquals("Hello", cleanDocument.body().child(0).text());

 verify(whitelist, times(2)).isSafeTag(eq("h1"));
 verify(whitelist, times(1)).isSafeTag(eq("script"));
 }

 @Test
 public void testCleanDocumentEmptyDocument() {
 // Given
 Document dirtyDocument = createDocument("");

 // When
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Then
 assertNotNull(cleanDocument);
 assertEquals(0, cleanDocument.body().childNodeSize());
 }

 @Test
 public void testCleanDocumentWhitelistAllowsAllElements() {
 // Given
 Document dirtyDocument = createDocument("<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>");
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeTag(anyString())).thenReturn(true);

 // When
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Then
 assertNotNull(cleanDocument);
 assertEquals(2, cleanDocument.body().childNodeSize());
 assertEquals("<h1>Hello</h1>", cleanDocument.body().html());

 verify(whitelist, times(3)).isSafeTag(anyString());
 }

 @Test
 public void testCleanDocumentWithEmptyWhitelist() {
 // Given
 Document dirtyDocument = createDocument("<html><body><script>alert('XSS')</script><h1>Hello</h1></body></html>");
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeTag(anyString())).thenReturn(false);

 // When
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Then
 assertNotNull(cleanDocument);
 assertEquals(1, cleanDocument.body().childNodeSize());
 assertNull(cleanDocument.body().child(0).tagName());
 }

 @Test
 public void testIsDocumentValid() {
 // Given
 Document dirtyDocument = createDocument("<html><body><h1 id='test' onclick='alert(\"XSS\")'>Hello</h1></body></html>");
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeTag("h1")).thenReturn(true);
 when(whitelist.isSafeAttribute("h1", any(Element.class), eq("onclick"))).thenReturn(false);
 when(whitelist.isSafeAttribute("h1", any(Element.class), anyString())).thenReturn(true);

 // When
 boolean isValid = cleaner.isValid(dirtyDocument);
 // Then
 assertFalse(isValid);
 }

 @Test
 public void testIsDocumentValidEmptyDocument() {
 // Given
 Document dirtyDocument = Document.createShell("http://example.com");
 // When
 boolean isValid = cleaner.isValid(dirtyDocument);
 // Then
 assertTrue(isValid);
 }

 @Test
 public void testIsDocumentValidWhitelistAllowsAll() {
 // Given
 Document dirtyDocument = createDocument("<html><body><h1 id='test' onclick='alert(\"XSS\")'>Hello</h1></body></html>");
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeTag(anyString())).thenReturn(true);
 when(whitelist.isSafeAttribute(anyString(), any(Element.class), anyString())).thenReturn(true);

 // When
 boolean isValid = cleaner.isValid(dirtyDocument);
 // Then
 assertTrue(isValid);
 }

 @Test
 public void testIsDocumentValidWhitelistDisallowsOnclick() {
 // Given
 Document dirtyDocument = createDocument("<html><body><h1 id='test' onclick='alert(\"XSS\")'>Hello</h1></body></html>");
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeTag(anyString())).thenReturn(true);
 when(whitelist.isSafeAttribute(eq("h1"), any(Element.class), eq("onclick"))).thenReturn(false);

 // When
 boolean isValid = cleaner.isValid(dirtyDocument);
 // Then
 assertFalse(isValid);
 }

 @Test
 public void testCopySafeNodes() {
 // Given
 Element sourceEl = createElement("<div><script>alert('XSS')</script><h1>Hello</h1></div>");
 Element dest = createElement("div");
 int numDiscarded = 1;

 // When
 int numDiscardedActual = cleaner.copySafeNodes(sourceEl, dest);

 // Then
 assertEquals(numDiscarded, numDiscardedActual);
 assertEquals(1, dest.childNodeSize());
 assertEquals("<h1>Hello</h1>", dest.html());
 }

 @Test
 public void testCopySafeNodesScriptTag() {
 // Given
 Element sourceEl = createElement("<div><script>alert('XSS')</script></div>");
 Element dest = createElement("div");

 // When
 int numDiscardedActual = cleaner.copySafeNodes(sourceEl, dest);

 // Then
 assertEquals(1, numDiscardedActual);
 assertNotNull(dest.childNodes().get(0));
 assertTrue(dest.childNodes().get(0) instanceof TextNode);
 assertEquals("", dest.childNodes().get(0).text());
 }

 @Test
 public void testCopySafeNodesScriptTagWithWhitelist() {
 // Given
 Element sourceEl = createElement("<div><script>alert('XSS')</script></div>");
 Element dest = createElement("div");
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeTag("script")).thenReturn(true);

 // When
 int numDiscardedActual = cleaner.copySafeNodes(sourceEl, dest);

 // Then
 assertEquals(0, numDiscardedActual);
 assertNotNull(dest.childNodes().get(0));
 assertTrue(dest.childNodes().get(0) instanceof Element);
 }

 @Test
 public void testCopySafeNodesElementWithTextChild() {
 // Given
 Element sourceEl = createElement("<div><p>Hello</p></div>");
 Element dest = createElement("div");

 // When
 int numDiscardedActual = cleaner.copySafeNodes(sourceEl, dest);

 // Then
 assertEquals(0, numDiscardedActual);
 assertEquals(1, dest.childNodeSize());
 assertEquals("Hello", dest.child(0).text());
 }

 @Test
 public void testCopySafeNodesElementWithEmptyTextChild() {
 // Given
 Element sourceEl = createElement("<div><p></p></div>");
 Element dest = createElement("div");

 // When
 int numDiscardedActual = cleaner.copySafeNodes(sourceEl, dest);

 // Then
 assertEquals(0, numDiscardedActual);
 assertEquals(1, dest.childNodeSize());
 assertEquals("", dest.child(0).text());
 }

 @Test
 public void testCopySafeNodesElementWithAttributes() {
 // Given
 Element sourceEl = createElement("<a href='http://example.com'>Link</a>");
 Element dest = createElement("a");

 // When
 int numDiscardedActual = cleaner.copySafeNodes(sourceEl, dest);

 // Then
 assertEquals(0, numDiscardedActual);
 assertEquals("http://example.com", dest.attr("href"));
 }

 @Test
 public void testCopySafeNodesElementWithAttributesDiscarded() {
 // Given
 Element sourceEl = createElement("<a href='http://example.com'>Link</a>");
 Element dest = createElement("a");
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeAttribute("a", any(Element.class), eq("href"))).thenReturn(false);

 // When
 int numDiscardedActual = cleaner.copySafeNodes(sourceEl, dest);

 // Then
 assertEquals(1, numDiscardedActual);
 assertNull(dest.attr("href"));
 }

 @Test
 public void testCopySafeNodesElementWithNestedElements() {
 // Given
 Element sourceEl = createElement("<div><p><a href='http://example.com'>Link</a></p></div>");
 Element dest = createElement("div");

 // When
 int numDiscardedActual = cleaner.copySafeNodes(sourceEl, dest);

 // Then
 assertEquals(2, numDiscardedActual);
 assertEquals(1, dest.childNodeSize());
 assertEquals("<p></p>", dest.html());
 }

 @Test
 public void testCreateSafeElement() {
 // Given
 Element sourceEl = createElement("<h1 id='test' onclick='alert(\"XSS\")'>Hello</h1>");

 // When
 ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

 // Then
 assertEquals("<h1></h1>", elementMeta.el.html());
 assertEquals(2, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithEnforcedAttributes() {
 // Given
 Element sourceEl = createElement("<h1 id='test' onclick='alert(\"XSS\")' class='test'>Hello</h1>");
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeTag("h1")).thenReturn(true);
 when(whitelist.isSafeAttribute("h1", any(Element.class), eq("class"))).thenReturn(true);

 // When
 ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

 // Then
 assertEquals("<h1 class='test'></h1>", elementMeta.el.html());
 assertEquals(0, elementMeta.numAttribsDiscarded);
 }

 @Test
 public void testCreateSafeElementWithInvalidAttribute() {
 // Given
 Element sourceEl = createElement("<h1 id='test' onclick='alert(\"XSS\")'>Hello</h1>");
 Whitelist whitelist = mock(Whitelist.class);
 when(whitelist.isSafeTag("h1")).thenReturn(true);
 when(whitelist.isSafeAttribute("h1", any(Element.class), eq("onclick"))).thenReturn(false);

 // When
 ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

 // Then
 assertEquals("<h1></h1>", elementMeta.el.html());
 assertEquals(1, elementMeta.numAttribsDiscarded);
 }

 private Document createDocument(String html) {
 return Document.createShell(html);
 }

 private Element createElement(String html) {
 return Jsoup.parse(html).body().getFirstElement();
 }
}