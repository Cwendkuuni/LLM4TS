package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {
  @Test
  public void testJoinCollectionString() {
    Collection<String> strings = Arrays.asList("a", "b", "c");
    String sep = "-";
    String expected = "a-b-c";
    String result = StringUtil.join(strings, sep);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testJoinIteratorString() {
    Collection<String> strings = Arrays.asList("a", "b", "c");
    Iterator<String> iterator = strings.iterator();
    String sep = "-";
    String expected = "a-b-c";
    String result = StringUtil.join(iterator, sep);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testJoinArrayString() {
    String[] strings = {"a", "b", "c"};
    String sep = "-";
    String expected = "a-b-c";
    String result = StringUtil.join(strings, sep);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testPadding() {
    int width = 10;
    String expected = "          ";
    String result = StringUtil.padding(width);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testIsBlank() {
    String string = "";
    boolean expected = true;
    boolean result = StringUtil.isBlank(string);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testIsNumeric() {
    String string = "12345";
    boolean expected = true;
    boolean result = StringUtil.isNumeric(string);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testIsWhitespace() {
    int c = ' ';
    boolean expected = true;
    boolean result = StringUtil.isWhitespace(c);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testIsActuallyWhitespace() {
    int c = ' ';
    boolean expected = true;
    boolean result = StringUtil.isActuallyWhitespace(c);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testNormaliseWhitespace() {
    String string = "   hello  \t  world \n";
    String expected = " hello world ";
    String result = StringUtil.normaliseWhitespace(string);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testAppendNormalisedWhitespace() {
    StringBuilder accum = new StringBuilder();
    String string = "   hello  \t  world \n";
    boolean stripLeading = false;
    String expected = " hello world ";
    StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);
    String result = accum.toString();
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testIn() {
    String needle = "b";
    String[] haystack = {"a", "b", "c"};
    boolean expected = true;
    boolean result = StringUtil.in(needle, haystack);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testInSorted() {
    String needle = "b";
    String[] haystack = {"a", "b", "c"};
    boolean expected = true;
    boolean result = StringUtil.inSorted(needle, haystack);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testResolveURLString() {
    try {
      URL base = new URL("http://www.example.com");
      String relUrl = "/page.html";
      URL result = StringUtil.resolve(base, relUrl);
      URL expected = new URL("http://www.example.com/page.html");
      Assert.assertEquals(expected, result);
    } catch (MalformedURLException e) {
      Assert.fail("MalformedURLException: " + e.getMessage());
    }
  }

  @Test
  public void testResolveStringString() {
    String baseUrl = "http://www.example.com";
    String relUrl = "/page.html";
    String result = StringUtil.resolve(baseUrl, relUrl);
    String expected = "http://www.example.com/page.html";
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testStringBuilder() {
    StringBuilder sb = StringUtil.stringBuilder();
    String expected = "";
    String result = sb.toString();
    Assert.assertEquals(expected, result);
  }
}