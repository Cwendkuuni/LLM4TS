package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

  private Cleaner cleaner;
  
  @Before
  public void setUp() {
    Whitelist whitelist = Whitelist.relaxed();
    cleaner = new Cleaner(whitelist);
  }
  
  @Test
  public void testClean() {
    // Test typical input
    Document dirtyDocument = Jsoup.parseBodyFragment("<p>Hello, <b>world!</b></p>");
    Document cleanedDocument = cleaner.clean(dirtyDocument);
    String expected = "<html>\n" +
                      " <head></head>\n" +
                      " <body><p>Hello, <b>world!</b></p></body>\n" +
                      "</html>";
    assertEquals(expected, cleanedDocument.html());
    
    // Test empty input
    dirtyDocument = Jsoup.parseBodyFragment("");
    cleanedDocument = cleaner.clean(dirtyDocument);
    expected = "<html>\n" +
               " <head></head>\n" +
               " <body></body>\n" +
               "</html>";
    assertEquals(expected, cleanedDocument.html());
  }
  
  @Test
  public void testIsValid() {
    // Test valid document
    Document dirtyDocument = Jsoup.parseBodyFragment("<p>Hello, <b>world!</b></p>");
    boolean isValid = cleaner.isValid(dirtyDocument);
    assertTrue(isValid);
    
    // Test invalid document with disallowed tag
    dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS');</script>");
    isValid = cleaner.isValid(dirtyDocument);
    assertFalse(isValid);
    
    // Test invalid document with disallowed attribute
    dirtyDocument = Jsoup.parseBodyFragment("<p onclick=\"alert('XSS');\">Hello, world!</p>");
    isValid = cleaner.isValid(dirtyDocument);
    assertFalse(isValid);
    
  }
  
}