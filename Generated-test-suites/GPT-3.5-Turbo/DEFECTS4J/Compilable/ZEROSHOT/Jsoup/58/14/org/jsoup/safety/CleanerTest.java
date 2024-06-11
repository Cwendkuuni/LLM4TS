package org.jsoup.safety;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
  
  private Cleaner cleaner;
  
  @Before
  public void setUp() {
    cleaner = new Cleaner(Whitelist.basic());
  }
  
  @Test
  public void testClean() {
    // Test case 1: Valid input, expect a cleaned document
    Document dirtyDocument1 = Jsoup.parseBodyFragment("<div><p>Hello World</p></div>");
    Document cleanDocument1 = cleaner.clean(dirtyDocument1);
    Assert.assertEquals("<html><head></head><body><div><p>Hello World</p></div></body></html>", cleanDocument1.html());
    
    // Test case 2: Invalid input, expect an empty document
    Document dirtyDocument2 = Jsoup.parseBodyFragment("<div><script>alert('XSS')</script></div>");
    Document cleanDocument2 = cleaner.clean(dirtyDocument2);
    Assert.assertEquals("<html><head></head><body></body></html>", cleanDocument2.html());
  }
  
  @Test
  public void testIsValid() {
    // Test case 1: Valid input, expect true
    Document dirtyDocument1 = Jsoup.parseBodyFragment("<p>Hello World</p>");
    boolean isValid1 = cleaner.isValid(dirtyDocument1);
    Assert.assertTrue(isValid1);
    
    // Test case 2: Invalid input, expect false
    Document dirtyDocument2 = Jsoup.parseBodyFragment("<p>Hello World<script>alert('XSS')</script></p>");
    boolean isValid2 = cleaner.isValid(dirtyDocument2);
    Assert.assertFalse(isValid2);
  }
}