package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import com.google.common.base.Joiner.MapJoiner;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JoinerTest {
   @Test
   public void testConstructor_onString() {
       Joiner joiner = Joiner.on(",");
       // assert something
   }

   @Test
   public void testConstructor_onChar() {
       Joiner joiner = Joiner.on('-');
       // assert something
   }

   @Test
   public void testAppendTo_withIterable() throws IOException {
       Joiner joiner = Joiner.on(",");
       StringBuilder builder = new StringBuilder();
       joiner.appendTo(builder, Arrays.asList("apple", "banana", "orange"));
       assertEquals("apple,banana,orange", builder.toString());
   }

   @Test
   public void testAppendTo_withIterator() throws IOException {
       Joiner joiner = Joiner.on(",");
       StringBuilder builder = new StringBuilder();
       joiner.appendTo(builder, Arrays.asList("apple", "banana", "orange").iterator());
       assertEquals("apple,banana,orange", builder.toString());
   }

   @Test
   public void testAppendTo_withObjectArray() throws IOException {
       Joiner joiner = Joiner.on(",");
       StringBuilder builder = new StringBuilder();
       joiner.appendTo(builder, new Object[] {"apple", "banana", "orange"});
       assertEquals("apple,banana,orange", builder.toString());
   }

   @Test
   public void testAppendTo_withFirstSecondRest() throws IOException {
       Joiner joiner = Joiner.on(",");
       StringBuilder builder = new StringBuilder();
       joiner.appendTo(builder, "apple", "banana", "orange");
       assertEquals("apple,banana,orange", builder.toString());
   }

   @Test
   public void testAppendTo_withNullAppendable() throws IOException {
       Joiner joiner = Joiner.on(",");
       StringBuilder builder = null;
       joiner.appendTo(builder, Arrays.asList("apple", "banana", "orange"));
       // assert something
   }

   @Test
   public void testAppendTo_withNullParts() throws IOException {
       Joiner joiner = Joiner.on(",");
       StringBuilder builder = new StringBuilder();
       joiner.appendTo(builder, Arrays.asList("apple", null, "orange"));
       assertEquals("apple,null,orange", builder.toString());
   }

   @Test
   public void testJoin_withIterable() {
       Joiner joiner = Joiner.on(",");
       String result = joiner.join(Arrays.asList("apple", "banana", "orange"));
       assertEquals("apple,banana,orange", result);
   }

   @Test
   public void testJoin_withIterator() {
       Joiner joiner = Joiner.on(",");
       String result = joiner.join(Arrays.asList("apple", "banana", "orange").iterator());
       assertEquals("apple,banana,orange", result);
   }

   @Test
   public void testJoin_withObjectArray() {
       Joiner joiner = Joiner.on(",");
       String result = joiner.join(new Object[] {"apple", "banana", "orange"});
       assertEquals("apple,banana,orange", result);
   }

   @Test
   public void testJoin_withFirstSecondRest() {
       Joiner joiner = Joiner.on(",");
       String result = joiner.join("apple", "banana", "orange");
       assertEquals("apple,banana,orange", result);
   }

   @Test
   public void testUseForNull_withNullText() {
       Joiner joiner = Joiner.on(",");
       Joiner result = joiner.useForNull("null");
       // assert something
   }

   @Test
   public void testSkipNulls() throws IOException {
       Joiner joiner = Joiner.on(",").skipNulls();
       StringBuilder builder = new StringBuilder();
       joiner.appendTo(builder, Arrays.asList("apple", null, "orange", null, "banana"));
       assertEquals("apple,orange,banana", builder.toString());
   }

   @Test
   public void testMapJoiner_appendTo_withMap() throws IOException {
       Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
       StringBuilder builder = new StringBuilder();
       Map<Object, Object> map = new HashMap<>();
       map.put("apple", 1);
       map.put("banana", 2);
       map.put("orange", 3);
       mapJoiner.appendTo(builder, map);
       assertEquals("apple:1,banana:2,orange:3", builder.toString());
   }

   @Test
   public void testMapJoiner_join_withMap() {
       Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
       Map<Object, Object> map = new HashMap<>();
       map.put("apple", 1);
       map.put("banana", 2);
       map.put("orange", 3);
       String result = mapJoiner.join(map);
       assertEquals("apple:1,banana:2,orange:3", result);
   }

   @Test
   public void testMapJoiner_useForNull_withNullText() {
       Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
       Joiner.MapJoiner result = mapJoiner.useForNull("null");
       // assert something
   }
}