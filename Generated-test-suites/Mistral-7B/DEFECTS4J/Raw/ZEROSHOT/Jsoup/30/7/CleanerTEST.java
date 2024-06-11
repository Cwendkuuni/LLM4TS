package org.jsoup.safety;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;
 private Document dirtyDocument;
 private Document cleanDocument;
 private Element dirtyBody;
 private Element cleanBody;

 @Before
 public void setUp() {
 whitelist = Mockito.mock(Whitelist.class);
 cleaner = new Cleaner(whitelist);

 dirtyDocument = Mockito.mock(Document.class);
 cleanDocument = Mockito.mock(Document.class);
 dirtyBody = Mockito.mock(Element.class);
 cleanBody = Mockito.mock(Element.class);

 Mockito.when(dirtyDocument.baseUri()).thenReturn("http://test.com");
 Mockito.when(dirtyDocument.body()).thenReturn(dirtyBody);
 Mockito.when(cleanDocument.baseUri()).thenReturn("http://test.com");
 Mockito.when(cleanDocument.body()).thenReturn(cleanBody);
 }

 @Test
 public void testCleaner_validConstructor() {
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void testCleaner_invalidConstructor() {
 new Cleaner(null);
 }

 @Test
 public void testCleaner_cleanDocument_validDocument() {
 Mockito.when(dirtyBody.childNodes()).thenReturn( Arrays.asList(Mockito.mock(TextNode.class), Mockito.mock(Element.class), Mockito.mock(TextNode.class), Mockito.mock(Element.class)) );

 cleaner.clean(dirtyDocument);

 Mockito.verify(cleanDocument, Mockito.times(1)).appendChild(Mockito.any(Element.class));
 Mockito.verify(cleanDocument, Mockito.times(1)).appendChild(Mockito.any(TextNode.class));
 Mockito.verify(cleanDocument, Mockito.times(1)).appendChild(Mockito.any(Element.class));
 Mockito.verify(cleanDocument, Mockito.times(1)).appendChild(Mockito.any(TextNode.class));
 }

 @Test
 public void testCleaner_cleanDocument_invalidDocument() {
 Mockito.when(dirtyBody.childNodes()).thenReturn( Arrays.asList(Mockito.mock(TextNode.class), Mockito.mock(Element.class), Mockito.mock(TextNode.class), Mockito.mock(Element.class), Mockito.mock(Element.class)) );

 cleaner.clean(dirtyDocument);

 int actualNumDiscarded = Mockito.mockStatic(Validate.class).verifyZeroInteractions().notNull(cleanDocument);
 int expectedNumDiscarded = 3; // 3 unsafe tags or attributes have been discarded
 assertEquals(expectedNumDiscarded, actualNumDiscarded);
 }

 @Test
 public void testCleaner_isValidDocument_validDocument() {
 Mockito.when(dirtyBody.childNodes()).thenReturn( Arrays.asList(Mockito.mock(Element.class), Mockito.mock(Element.class)) );
 Mockito.when(whitelist.isSafeTag("div", dirtyBody, null)).thenReturn(true);
 Mockito.when(whitelist.isSafeTag("p", dirtyBody, null)).thenReturn(true);

 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleaner_isValidDocument_invalidDocument() {
 Mockito.when(dirtyBody.childNodes()).thenReturn( Arrays.asList(Mockito.mock(Element.class), Mockito.mock(Element.class)) );
 Mockito.when(whitelist.isSafeTag("div", dirtyBody, null)).thenReturn(false);
 Mockito.when(whitelist.isSafeTag("p", dirtyBody, null)).thenReturn(true);

 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleaner_isValidDocument_invalidTag() {
 Mockito.when(dirtyBody.childNodes()).thenReturn( Arrays.asList(Mockito.mock(Element.class), Mockito.mock(Element.class)) );
 Mockito.when(whitelist.isSafeTag("div", dirtyBody, null)).thenReturn(true);
 Mockito.when(whitelist.isSafeTag("script", dirtyBody, null)).thenReturn(false);

 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleaner_isValidDocument_invalidAttribute() {
 Mockito.when(dirtyBody.childNodes()).thenReturn( Arrays.asList(Mockito.mock(Element.class), Mockito.mock(Element.class)) );
 Mockito.when(whitelist.isSafeTag("div", dirtyBody, null)).thenReturn(true);
 Mockito.when(whitelist.isSafeAttribute("div", dirtyBody, "onclick", null)).thenReturn(false);

 assertFalse(cleaner.isValid(dirtyDocument));
 }
}