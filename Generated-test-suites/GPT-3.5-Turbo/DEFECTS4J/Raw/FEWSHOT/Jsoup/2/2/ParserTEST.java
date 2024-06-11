
package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;

import java.util.*;

/**
 * Parses HTML into a {@link Document}. Generally best to use one of the  more convenient parse methods in {@link org.jsoup.Jsoup}.
 *
 * @author Jonathan Hedley, jonathan@hedley.net
 */
public class ParserTest {

   @Test
   public void testParse() {
      String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
      String baseUri = "http://example.com";
      Document document = Parser.parse(html, baseUri);

      // Assert the document structure
      assertEquals(html, document.outerHtml());
      assertEquals("http://example.com", document.baseUri());
      assertEquals("html", document.tagName());
      assertEquals(1, document.children().size());

      Element head = document.getElementsByTag("head").first();
      Element body = document.getElementsByTag("body").first();

      assertNotNull(head);
      assertNotNull(body);

      assertEquals(1, head.children().size());
      assertEquals(1, body.children().size());

      Element title = head.getElementsByTag("title").first();
      Element h1 = body.getElementsByTag("h1").first();

      assertNotNull(title);
      assertNotNull(h1);

      assertEquals("Test", title.text());
      assertEquals("Hello World", h1.text());
   }

   @Test
   public void testParseBodyFragment() {
      String bodyHtml = "<h1>Hello World</h1>";
      String baseUri = "http://example.com";
      Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

      // Assert the document structure
      assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", document.outerHtml());
      assertEquals("http://example.com", document.baseUri());
      assertEquals("html", document.tagName());
      assertEquals(1, document.children().size());

      Element head = document.getElementsByTag("head").first();
      Element body = document.getElementsByTag("body").first();

      assertNotNull(head);
      assertNotNull(body);

      assertEquals(0, head.children().size());
      assertEquals(1, body.children().size());

      Element h1 = body.getElementsByTag("h1").first();

      assertNotNull(h1);

      assertEquals("Hello World", h1.text());
   }

   // test other methods in Parser class

}

