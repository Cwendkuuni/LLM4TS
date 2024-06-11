package com.imsmart.servlet;

import org.junit.*;
import static org.junit.Assert.*;

public class HTMLFilterTest {

  @Test
  public void testFilterNull() {
    assertNull(HTMLFilter.filter(null));
  }

  @Test
  public void testFilterEmpty() {
    assertEquals("", HTMLFilter.filter(""));
  }

  @Test
  public void testFilterNoSpecialCharacter() {
    String input = "Hello World";
    assertEquals(input, HTMLFilter.filter(input));
  }

  @Test
  public void testFilterContainsLessThan() {
    String input = "4 < 5";
    assertEquals("4 &lt; 5", HTMLFilter.filter(input));
  }

  @Test
  public void testFilterContainsGreaterThan() {
    String input = "10 > 5";
    assertEquals("10 &gt; 5", HTMLFilter.filter(input));
  }

  @Test
  public void testFilterContainsAmpersand() {
    String input = "AT&T";
    assertEquals("AT&amp;T", HTMLFilter.filter(input));
  }

  @Test
  public void testFilterContainsDoubleQuotes() {
    String input = "He said, \"Hello\"";
    assertEquals("He said, &quot;Hello&quot;", HTMLFilter.filter(input));
  }

  @Test
  public void testFilterContainsMixedSpecialCharacters() {
    String input = "<Hello & Goodbye>";
    assertEquals("&lt;Hello &amp; Goodbye&gt;", HTMLFilter.filter(input));
  }

}