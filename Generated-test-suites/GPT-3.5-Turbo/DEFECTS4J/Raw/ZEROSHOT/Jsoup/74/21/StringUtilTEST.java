package org.jsoup.helper;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {
  
  //helper method to check if two lists are equal
  private <T> boolean listsEqual(List<T> list1, List<T> list2) {
    if (list1.size() != list2.size()) {
      return false;
    }
    for (int i = 0; i < list1.size(); i++) {
      if (!list1.get(i).equals(list2.get(i))) {
        return false;
      }
    }
    return true;
  }
  
  @Test
  public void testJoinCollection() {
    List<String> strings = Arrays.asList("hello", "world", "!");
    String result = StringUtil.join(strings, ",");
    assertEquals("hello,world,!", result);
  }
  
  @Test
  public void testJoinIterator() {
    List<String> strings = Arrays.asList("hello", "world", "!");
    Iterator<String> iterator = strings.iterator();
    String result = StringUtil.join(iterator, ",");
    assertEquals("hello,world,!", result);
  }
  
  @Test
  public void testJoinArray() {
    String[] strings = {"hello", "world", "!"};
    String result = StringUtil.join(strings, ",");
    assertEquals("hello,world,!", result);
  }
  
  @Test
  public void testPadding() {
    assertEquals("", StringUtil.padding(0));
    assertEquals(" ", StringUtil.padding(1));
    assertEquals("  ", StringUtil.padding(2));
    assertEquals("                   ", StringUtil.padding(19));
  }
  
  @Test
  public void testIsBlank() {
    assertTrue(StringUtil.isBlank(null));
    assertTrue(StringUtil.isBlank(""));
    assertTrue(StringUtil.isBlank(" "));
    assertFalse(StringUtil.isBlank("hello"));
  }
  
  @Test
  public void testIsNumeric() {
    assertTrue(StringUtil.isNumeric("12345"));
    assertFalse(StringUtil.isNumeric("12345a"));
    assertFalse(StringUtil.isNumeric(""));
  }
  
  @Test
  public void testIsWhitespace() {
    assertTrue(StringUtil.isWhitespace(' '));
    assertTrue(StringUtil.isWhitespace('\t'));
    assertTrue(StringUtil.isWhitespace('\n'));
    assertTrue(StringUtil.isWhitespace('\f'));
    assertTrue(StringUtil.isWhitespace('\r'));
  }
  
  @Test
  public void testIsActuallyWhitespace() {
    assertTrue(StringUtil.isActuallyWhitespace(' '));
    assertTrue(StringUtil.isActuallyWhitespace('\t'));
    assertTrue(StringUtil.isActuallyWhitespace('\n'));
    assertTrue(StringUtil.isActuallyWhitespace('\f'));
    assertTrue(StringUtil.isActuallyWhitespace('\r'));
    assertTrue(StringUtil.isActuallyWhitespace(160));
  }
  
  @Test
  public void testNormaliseWhitespace() {
    assertEquals("hello world !", StringUtil.normaliseWhitespace("hello   world \t!"));
    assertEquals("hello world !", StringUtil.normaliseWhitespace("hello \n world \f!"));
    assertEquals("hello world !", StringUtil.normaliseWhitespace("hello \r world \n!"));
    assertEquals("hello world !", StringUtil.normaliseWhitespace("hello  \t\n \f \r  world \n!"));
  }
  
  @Test
  public void testAppendNormalisedWhitespace() {
    StringBuilder sb = new StringBuilder();
    StringUtil.appendNormalisedWhitespace(sb, "hello", false);
    assertEquals("hello", sb.toString());
    
    sb = new StringBuilder();
    StringUtil.appendNormalisedWhitespace(sb, "   hello   ", true);
    assertEquals("hello ", sb.toString());
  }
  
  @Test
  public void testIn() {
    assertTrue(StringUtil.in("hello", "hello", "world", "!"));
    assertFalse(StringUtil.in("hello", "world", "!"));
    assertFalse(StringUtil.in("hello", new String[] {}));
  }
  
  @Test
  public void testInSorted() {
    assertTrue(StringUtil.inSorted("hello", new String[] {"hello", "world", "!"}));
    assertFalse(StringUtil.inSorted("hello", new String[] {"world", "!"}));
  }
  
  @Test
  public void testResolve() throws MalformedURLException {
    URL base = new URL("http://www.example.com");
    URL resolved = StringUtil.resolve(base, "path");
    assertEquals("http://www.example.com/path", resolved.toString());
    
    String resolvedStr = StringUtil.resolve("http://www.example.com", "path");
    assertEquals("http://www.example.com/path", resolvedStr);
  }
  
  @Test
  public void testResolveInvalid() {
    String resolvedStr = StringUtil.resolve("http://www.example.com", "invalid@@@");
    assertEquals("", resolvedStr);
  }
  
  @Test
  public void testStringBuilder() {
    StringBuilder sb1 = StringUtil.stringBuilder();
    StringBuilder sb2 = StringUtil.stringBuilder();
    assertNotSame(sb1, sb2);
    assertEquals(0, sb1.length());
    assertEquals(0, sb2.length());
  }
  
}