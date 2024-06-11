package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
  
  // Test cases for parse method
  
  // Typical use case
  @Test
  public void testParse() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertEquals("Test", doc.title());
    assertEquals("Hello World", doc.body().select("h1").text());
  }
  
  // Edge case: Empty HTML
  @Test
  public void testParseEmptyHTML() {
    String html = "";
    String baseUri = "http://example.com";
    Document doc = Parser.parse(html, baseUri);
    
    assertNotNull(doc);
    assertTrue(doc.children().isEmpty());
  }
  
  // Error scenario: Null HTML
  @Test(expected = IllegalArgumentException.class)
  public void testParseNullHTML() {
    String html = null;
    String baseUri = "http://example.com";
    Parser.parse(html, baseUri);
  }
  
  // Error scenario: Null base URI
  @Test(expected = IllegalArgumentException.class)
  public void testParseNullBaseURI() {
    String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
    String baseUri = null;
    Parser.parse(html, baseUri);
  }
  
  // Test cases for parseBodyFragment method
  
  // Typical use case
  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    
    assertNotNull(doc);
    assertEquals("Hello World", doc.body().select("h1").text());
  }
  
  // Edge case: Empty body HTML
  @Test
  public void testParseBodyFragmentEmptyHTML() {
    String bodyHtml = "";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
    
    assertNotNull(doc);
    assertTrue(doc.body().children().isEmpty());
  }
  
  // Error scenario: Null body HTML
  @Test(expected = IllegalArgumentException.class)
  public void testParseBodyFragmentNullHTML() {
    String bodyHtml = null;
    String baseUri = "http://example.com";
    Parser.parseBodyFragment(bodyHtml, baseUri);
  }
  
  // Error scenario: Null base URI
  @Test(expected = IllegalArgumentException.class)
  public void testParseBodyFragmentNullBaseURI() {
    String bodyHtml = "<h1>Hello World</h1>";
    String baseUri = null;
    Parser.parseBodyFragment(bodyHtml, baseUri);
  }
  
  // Test cases for parseBodyFragmentRelaxed method
  
  // Typical use case
  @Test
  public void testParseBodyFragmentRelaxed() {
    String bodyHtml = "<h1>Hello World";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    
    assertNotNull(doc);
    assertEquals("Hello World", doc.body().select("h1").text());
  }
  
  // Edge case: Empty body HTML
  @Test
  public void testParseBodyFragmentRelaxedEmptyHTML() {
    String bodyHtml = "";
    String baseUri = "http://example.com";
    Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    
    assertNotNull(doc);
    assertTrue(doc.body().children().isEmpty());
  }
  
  // Error scenario: Null body HTML
  @Test(expected = IllegalArgumentException.class)
  public void testParseBodyFragmentRelaxedNullHTML() {
    String bodyHtml = null;
    String baseUri = "http://example.com";
    Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
  }
  
  // Error scenario: Null base URI
  @Test(expected = IllegalArgumentException.class)
  public void testParseBodyFragmentRelaxedNullBaseURI() {
    String bodyHtml = "<h1>Hello World";
    String baseUri = null;
    Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
  }
}