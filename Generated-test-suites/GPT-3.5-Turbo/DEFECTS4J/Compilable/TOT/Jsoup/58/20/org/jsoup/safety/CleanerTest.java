package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
   private Cleaner cleaner;

   @Before
   public void setUp() {
      cleaner = new Cleaner(Whitelist.basic());
   }

   @Test
   public void testCleanMethod() {
      String dirtyHtml = "<html><head></head><body><div>This is a dirty HTML document</div></body></html>";
      Document dirtyDocument = Jsoup.parse(dirtyHtml);

      Document cleanDocument = cleaner.clean(dirtyDocument);

      assertEquals(dirtyDocument.baseUri(), cleanDocument.baseUri());
      assertEquals("div", cleanDocument.body().child(0).nodeName());
      assertEquals("This is a dirty HTML document", cleanDocument.body().text());
   }

   @Test
   public void testIsValidMethod_ValidDocument() {
      String validHtml = "<html><head></head><body><div>This is a valid HTML document</div></body></html>";
      Document validDocument = Jsoup.parse(validHtml);

      boolean isValid = cleaner.isValid(validDocument);

      assertTrue(isValid);
   }

   @Test
   public void testIsValidMethod_InvalidDocument() {
      String invalidHtml = "<html><head></head><body><img src=\"https://example.com/image\" /></body></html>";
      Document invalidDocument = Jsoup.parse(invalidHtml);

      boolean isValid = cleaner.isValid(invalidDocument);

      assertTrue(!isValid);
   }
}
