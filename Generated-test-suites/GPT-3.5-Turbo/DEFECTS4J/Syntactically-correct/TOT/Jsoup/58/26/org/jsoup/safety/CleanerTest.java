import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

   private Cleaner cleaner;
   private Document dirtyDocument;

   @Before
   public void setUp() {
       cleaner = new Cleaner(Whitelist.basic());
       dirtyDocument = Jsoup.parse("<html><head><title>Test Page</title></head><body><img src=\"image.jpg\"/><h1>Hello, World!</h1><script>alert('XSS attack');</script></body></html>");
   }

   @After
   public void tearDown() {
       cleaner = null;
       dirtyDocument = null;
   }

   @Test
   public void testClean() {
       Document cleanDocument = cleaner.clean(dirtyDocument);

       // Check if script tag is removed
       Elements scriptElements = cleanDocument.getElementsByTag("script");
       assertEquals(0, scriptElements.size());

       // Check if image tag is preserved
       Elements imageElements = cleanDocument.select("img[src]");
       assertEquals(1, imageElements.size());
   }

   @Test
   public void testIsValid() {
       boolean isValid = cleaner.isValid(dirtyDocument);

       assertFalse(isValid);
   }

   @Test
   public void testClean_EmptyBody() {
       Document dirtyDocumentWithEmptyBody = Jsoup.parse("<html><head><title>Test Page</title></head><body>   </body></html>");

       Document cleanDocument = cleaner.clean(dirtyDocumentWithEmptyBody);
       assertEquals("", cleanDocument.body().toString());
   }

   @Test
   public void testIsValid_EmptyBody() {
       Document dirtyDocumentWithEmptyBody = Jsoup.parse("<html><head><title>Test Page</title></head><body>   </body></html>");

       boolean isValid = cleaner.isValid(dirtyDocumentWithEmptyBody);
       assertTrue(isValid);
   }

   @Test
   public void testClean_NullDocument() {
       Document dirtyDocumentNull = null;
       Document cleanDocument = cleaner.clean(dirtyDocumentNull);

       assertEquals(null, cleanDocument);
   }

   @Test
   public void testIsValid_NullDocument() {
       Document dirtyDocumentNull = null;

       boolean isValid = cleaner.isValid(dirtyDocumentNull);
       assertTrue(isValid);
   }
}