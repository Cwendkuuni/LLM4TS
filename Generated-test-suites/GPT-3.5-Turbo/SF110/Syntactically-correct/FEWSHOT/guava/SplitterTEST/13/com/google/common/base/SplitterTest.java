package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SplitterTest {

  @Test
  public void testOn_char() {
    Splitter splitter = Splitter.on(',');
    Iterable<String> result = splitter.split("a,b,c");
    assertEquals("a", result.iterator().next());
  }

  @Test
  public void testOn_CharMatcher() {
    Splitter splitter = Splitter.on(CharMatcher.is(':'));
    Iterable<String> result = splitter.split("a:b:c");
    assertEquals("a", result.iterator().next());
  }

  @Test
  public void testOn_String() {
    Splitter splitter = Splitter.on("|");
    Iterable<String> result = splitter.split("a|b|c");
    assertEquals("a", result.iterator().next());
  }

  @Test
  public void testOn_Pattern() {
    Splitter splitter = Splitter.on(Pattern.compile("\\s"));
    Iterable<String> result = splitter.split("a b c");
    assertEquals("a", result.iterator().next());
  }

  @Test
  public void testOnPattern_string() {
    Splitter splitter = Splitter.onPattern(",");
    Iterable<String> result = splitter.split("a,b,c");
    assertEquals("a", result.iterator().next());
  }

  @Test
  public void testFixedLength() {
    Splitter splitter = Splitter.fixedLength(3);
    Iterable<String> result = splitter.split("abcde");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testOmitEmptyStrings() {
    Splitter splitter = Splitter.on(":").omitEmptyStrings();
    Iterable<String> result = splitter.split(":abc::def");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testLimit() {
    Splitter splitter = Splitter.on(",").limit(2);
    Iterable<String> result = splitter.split("a,b,c,d");
    assertEquals("a", result.iterator().next());
  }

  @Test
  public void testTrimResults() {
    Splitter splitter = Splitter.on(",").trimResults();
    Iterable<String> result = splitter.split(" a , b , c ");
    assertEquals("a", result.iterator().next());
  }

  @Test
  public void testTrimResults_CharMatcher() {
    Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is('_'));
    Iterable<String> result = splitter.split("_a_,_b_,_c_");
    assertEquals("a", result.iterator().next());
  }

  @Test
  public void testSplit_CharSequence() {
    Splitter splitter = Splitter.on(",");
    Iterable<String> result = splitter.split("a,b,c");
    assertEquals("a", result.iterator().next());
  }

  @Test
  public void testSplitToList_CharSequence() {
    Splitter splitter = Splitter.on(",");
    List<String> result = splitter.splitToList("a,b,c");
    assertEquals("a", result.get(0));
  }

  @Test
  public void testWithKeyValueSeparator_String() {
    Splitter splitter = Splitter.on("-").withKeyValueSeparator(":");
    Map<String, String> result = splitter.split("a:b-c:d");
    assertEquals("a", result.keySet().iterator().next());
  }
  
  @Test
  public void testWithKeyValueSeparator_char() {
    Splitter splitter = Splitter.on("-").withKeyValueSeparator(':');
    Map<String, String> result = splitter.split("a:b-c:d");
    assertEquals("a", result.keySet().iterator().next());
  }
  
  @Test
  public void testWithKeyValueSeparator_Splitter() {
    Splitter splitter = Splitter.on("-").withKeyValueSeparator(Splitter.on(":"));
    Map<String, String> result = splitter.split("a:b-c:d");
    assertEquals("a", result.keySet().iterator().next());
  }
  
}