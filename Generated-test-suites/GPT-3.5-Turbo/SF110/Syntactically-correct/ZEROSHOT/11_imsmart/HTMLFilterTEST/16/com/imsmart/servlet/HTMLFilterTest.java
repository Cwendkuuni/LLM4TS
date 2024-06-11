package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
  
  @Test
  public void testFilterWithNullMessage() {
    assertNull(HTMLFilter.filter(null));
  }
  
  @Test
  public void testFilterWithEmptyMessage() {
    assertEquals("", HTMLFilter.filter(""));
  }
  
  @Test
  public void testFilterWithNoSpecialCharacters() {
    String input = "Hello, world!";
    assertEquals(input, HTMLFilter.filter(input));
  }
  
  @Test
  public void testFilterWithHtmlTags() {
    String input = "<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>";
    String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;p&gt;Hello, world!&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
    assertEquals(expectedOutput, HTMLFilter.filter(input));
  }
  
  @Test
  public void testFilterWithSpecialCharacters() {
    String input = "This & that \"here\"";
    String expectedOutput = "This &amp; that &quot;here&quot;";
    assertEquals(expectedOutput, HTMLFilter.filter(input));
  }
  
  @Test
  public void testFilterWithMixedMessage() {
    String input = "<h1>Title</h1><p>This is a paragraph &amp; quote \"example\"</p>";
    String expectedOutput = "&lt;h1&gt;Title&lt;/h1&gt;&lt;p&gt;This is a paragraph &amp;amp; quote &quot;example&quot;&lt;/p&gt;";
    assertEquals(expectedOutput, HTMLFilter.filter(input));
  }
  
}

