package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.junit.runner.JUnit4.class)
public class CleanerTest {

 private Whitelist mockWhitelist;
 private Cleaner cleaner;
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 mockWhitelist = mock(Whitelist.class);
 cleaner = new Cleaner(mockWhitelist);
 dirtyDocument = Mockito.mock(Document.class);
 cleanDocument = Mockito.mock(Document.class);
 }

 @Test
 public void testCleanerConstructor() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 // Test with a valid document
 when(dirtyDocument.body()).thenReturn(Mockito.mock(Element.class));
 when(mockWhitelist.isSafeTag(Tag.valueOf("p"))).thenReturn(true);
 when(mockWhitelist.isSafeAttribute(Tag.valueOf("p"), null, null)).thenReturn(true);

 Element body = Mockito.mock(Element.class);
 when(body.tagName()).thenReturn(Tag.valueOf("p"));
 when(dirtyDocument.body()).thenReturn(body);
 when(mockWhitelist.getEnforcedAttributes(Tag.valueOf("p"))).thenReturn(new Attributes());

 Element cleanBody = Mockito.mock(Element.class);
 when(cleanDocument.body()).thenReturn(cleanBody);

 int numDiscarded = cleaner.copySafeNodes(body, cleanBody);
 assertSame(body, cleanBody);
 assertFalse(numDiscarded > 0);

 // Test with an invalid document
 when(dirtyDocument.body()).thenReturn(Mockito.mock(Element.class));
 when(mockWhitelist.isSafeTag(Tag.valueOf("script"))).thenReturn(false);

 Element scriptBody = Mockito.mock(Element.class);
 when(scriptBody.tagName()).thenReturn(Tag.valueOf("script"));
 when(dirtyDocument.body()).thenReturn(scriptBody);

 cleanDocument = Mockito.mock(Document.class);
 cleanBody = Mockito.mock(Element.class);
 when(cleanDocument.body()).thenReturn(cleanBody);

 numDiscarded = cleaner.copySafeNodes(scriptBody, cleanBody);
 assertFalse(numDiscarded < 0);
 }

 @Test
 public void testIsValidMethod() {
 // Test with a valid document
 when(dirtyDocument.body()).thenReturn(Mockito.mock(Element.class));
 when(mockWhitelist.isSafeTag(Tag.valueOf("p"))).thenReturn(true);
 when(mockWhitelist.isSafeAttribute(Tag.valueOf("p"), null, null)).thenReturn(true);

 Element body = Mockito.mock(Element.class);
 when(body.tagName()).thenReturn(Tag.valueOf("p"));
 when(dirtyDocument.body()).thenReturn(body);
 when(mockWhitelist.getEnforcedAttributes(Tag.valueOf("p"))).thenReturn(new Attributes());

 assertTrue(cleaner.isValid(dirtyDocument));

 // Test with an invalid document
 when(dirtyDocument.body()).thenReturn(Mockito.mock(Element.class));
 when(mockWhitelist.isSafeTag(Tag.valueOf("script"))).thenReturn(false);

 Element scriptBody = Mockito.mock(Element.class);
 when(scriptBody.tagName()).thenReturn(Tag.valueOf("script"));
 when(dirtyDocument.body()).thenReturn(scriptBody);

 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodesMethodWithTextNode() {
 // Test with a valid text node
 TextNode textNode = new TextNode("Hello World");
 Element dest = new Element(Tag.valueOf("p"), null);
 int numDiscarded = cleaner.copySafeNodes(null, dest);
 dest.appendChild(new TextNode("Hello World"));
 assertTrue(numDiscarded == 0);

 // Test with an invalid text node
 TextNode textNode2 = new TextNode("<script>alert('XSS')</script>");
 Element dest2 = new Element(Tag.valueOf("p"), null);
 int numDiscarded2 = cleaner.copySafeNodes(null, dest2);
 assertFalse(numDiscarded2 < 0);
 }

 @Test
 public void testCreateSafeElementMethod() {
 // Test with a valid element
 Element sourceEl = new Element(Tag.valueOf("p"), null);
 sourceEl.attributes().put("class", "class1 class2");
 Attribute classAttr = new Attribute("class", "class1 class2");
 List<Attribute> expectedAttributes = new ArrayList<>(Arrays.asList(classAttr));
 ElementMeta expectedMeta = new ElementMeta(sourceEl, 0);

 ElementMeta actualMeta = cleaner.createSafeElement(sourceEl);
 assertEquals(expectedMeta, actualMeta);
 MatcherAssert.assertThat(actualMeta.el.attributes(), CoreMatchers.hasItems(classAttr));

 // Test with an invalid element
 Element sourceEl2 = new Element(Tag.valueOf("script"), null);
 Attribute srcAttr = new Attribute("src", "javascript:alert('XSS')");
 List<Attribute> expectedAttributes2 = new ArrayList<>(Arrays.asList(srcAttr));
 ElementMeta expectedMeta2 = new ElementMeta(sourceEl2, 1);

 ElementMeta actualMeta2 = cleaner.createSafeElement(sourceEl2);
 assertEquals(expectedMeta2, actualMeta2);
 MatcherAssert.assertThat(actualMeta2.el.attributes(), CoreMatchers.hasItems(srcAttr));
 }

 private void assertEquals(Object expected, Object actual) {
 if (expected == null) {
 assertNull(actual);
 } else if (expected.getClass().isInstance(actual)) {
 assertSame(expected, actual);
 } else {
 super.assertEquals(expected, actual);
 }
 }
}