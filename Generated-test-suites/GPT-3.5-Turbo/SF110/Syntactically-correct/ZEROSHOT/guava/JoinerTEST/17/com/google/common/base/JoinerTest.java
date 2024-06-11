package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {
  
  @Test
  public void testOn() {
    Joiner joiner = Joiner.on(",");
    assertNotNull(joiner);
  }
  
  @Test
  public void testJoinWithIterable() {
    Joiner joiner = Joiner.on(",");
    Iterable<String> parts = Arrays.asList("one", "two", "three");
    String result = joiner.join(parts);
    assertEquals("one,two,three", result);
  }
  
  @Test
  public void testJoinWithIterator() {
    Joiner joiner = Joiner.on(",");
    List<String> parts = Arrays.asList("one", "two", "three");
    String result = joiner.join(parts.iterator());
    assertEquals("one,two,three", result);
  }
  
  @Test
  public void testJoinWithArray() {
    Joiner joiner = Joiner.on(",");
    String[] parts = {"one", "two", "three"};
    String result = joiner.join(parts);
    assertEquals("one,two,three", result);
  }
  
  @Test
  public void testJoinWithObjects() {
    Joiner joiner = Joiner.on(",");
    String result = joiner.join("one", "two", "three");
    assertEquals("one,two,three", result);
  }
  
  @Test
  public void testUseForNull() {
    Joiner joiner = Joiner.on(",").useForNull("null");
    String result = joiner.join("one", null, "three");
    assertEquals("one,null,three", result);
  }
  
  @Test
  public void testSkipNulls() {
    Joiner joiner = Joiner.on(",").skipNulls();
    String result = joiner.join("one", null, "three");
    assertEquals("one,three", result);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testSkipNullsOnMapJoiner() {
    Joiner joiner = Joiner.on(",").skipNulls();
    Map<String, String> map = new HashMap<>();
    map.put("key1", "value1");
    map.put("key2", null);
    joiner.withKeyValueSeparator("=").appendTo(new StringBuilder(), map);
  }
  
  @Test
  public void testWithKeyValueSeparator() {
    Joiner joiner = Joiner.on(",").withKeyValueSeparator("=");
    Map<String, String> map = new HashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    String result = joiner.join(map.entrySet());
    assertEquals("key1=value1,key2=value2", result);
  }
}