package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;

public class ParserTest {
  
  @Test
  public void testParse() {
    String html = "<html><body><h1>Hello World!</h1></body></html>";
    String baseUri = "https://www.example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertEquals("https://www.example.com", doc.baseUri());
    assertEquals("html", doc.tagName());
    assertNotNull(doc.body());
    assertNotNull(doc.head());
    assertNotNull(doc.selectFirst("h1"));
    assertEquals("Hello World!", doc.selectFirst("h1").text());
  }

  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<h1>Hello World!</h1>";
    String baseUri = "https://www.example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    
    assertNotNull(doc);
    assertEquals("https://www.example.com", doc.baseUri());
    assertNotNull(doc.body());
    assertNull(doc.head());
    assertNotNull(doc.selectFirst("h1"));
    assertEquals("Hello World!", doc.selectFirst("h1").text());
  }
  
  @Test
  public void testParseBodyFragmentRelaxed() {
    String bodyHtml = "<h1>Hello World!</h1>";
    String baseUri = "https://www.example.com";
    Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    
    assertNotNull(doc);
    assertEquals("https://www.example.com", doc.baseUri());
    assertNotNull(doc.body());
    assertNull(doc.head());
    assertNotNull(doc.selectFirst("h1"));
    assertEquals("Hello World!", doc.selectFirst("h1").text());
  }
  
  // Add more test methods for other functionalities in the Parser class

}