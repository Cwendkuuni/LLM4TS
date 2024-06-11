package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

public class JoinerTest {
	@Test
	public void testOnString() {
		Joiner joiner = Joiner.on(" ");
		StringBuilder result = joiner.appendTo(new StringBuilder(), Arrays.asList("Hello", "World"));
		assertEquals("Hello World", result.toString());
	}
	
	@Test
	public void testOnChar() {
		Joiner joiner = Joiner.on(',');
		StringBuilder result = joiner.appendTo(new StringBuilder(), Arrays.asList("A", "B", "C"));
		assertEquals("A,B,C", result.toString());
	}
	
	@Test
	public void testAppendToWithIterable() {
		Joiner joiner = Joiner.on(", ");
		StringBuilder result = joiner.appendTo(new StringBuilder(), Arrays.asList("One", "Two", "Three"));
		assertEquals("One, Two, Three", result.toString());
	}
	
	@Test
	public void testAppendToWithIterator() {
		Joiner joiner = Joiner.on("-");
		Iterator<String> iterator = Arrays.asList("A", "B", "C").iterator();
		StringBuilder result = joiner.appendTo(new StringBuilder(), iterator);
		assertEquals("A-B-C", result.toString());
	}
	
	@Test
	public void testAppendToWithObjectArray() {
		Joiner joiner = Joiner.on("#");
		StringBuilder result = joiner.appendTo(new StringBuilder(), new Object[] { "X", "Y", "Z" });
		assertEquals("X#Y#Z", result.toString());
	}
	
	@Test
	public void testAppendToWithObjects() {
		Joiner joiner = Joiner.on(" ");
		StringBuilder result = joiner.appendTo(new StringBuilder(), "This", "is", "a", "test");
		assertEquals("This is a test", result.toString());
	}
	
	@Test
	public void testAppendToWithStringBuilder() {
		Joiner joiner = Joiner.on("+");
		StringBuilder result = joiner.appendTo(new StringBuilder("Initial"), Arrays.asList("A", "B", "C"));
		assertEquals("Initial+A+B+C", result.toString());
	}
	
	@Test
	public void testJoinIterable() {
		Joiner joiner = Joiner.on("/");
		String result = joiner.join(Arrays.asList("path", "to", "directory"));
		assertEquals("path/to/directory", result);
	}
	
	@Test
	public void testJoinIterator() {
		Joiner joiner = Joiner.on("-");
		Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();
		String result = joiner.join(iterator);
		assertEquals("one-two-three", result);
	}
	
	@Test
	public void testJoinWithObjectArray() {
		Joiner joiner = Joiner.on(",");
		String result = joiner.join(new Object[] {1, 2, 3, 4});
		assertEquals("1,2,3,4", result);
	}
	
	@Test
	public void testJoinWithObjects() {
		Joiner joiner = Joiner.on(" ");
		String result = joiner.join("Hello", "World");
		assertEquals("Hello World", result);
	}
	
	@Test
	public void testUseForNull() {
		Joiner joiner = Joiner.on(",").useForNull("NULL");
		String result = joiner.join("A", null, "B");
		assertEquals("A,NULL,B", result);
	}
	
	@Test
	public void testSkipNulls() {
		Joiner joiner = Joiner.on(",").skipNulls();
		String result = joiner.join("A", null, "B");
		assertEquals("A,B", result);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testSkipNullsWithMaps() {
		Joiner joiner = Joiner.on(",").skipNulls();
		joiner.withKeyValueSeparator(":");
	}
	
	@Test
	public void testMapJoiner() {
		Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
		Map<String, Integer> map = Map.of("One", 1, "Two", 2, "Three", 3);
		String result = joiner.join(map);
		assertEquals("One:1,Two:2,Three:3", result);
	}
	
	@Test
	public void testMapJoinerAppendTo() {
		Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
		Map<String, Integer> map = Map.of("A", 1, "B", 2, "C", 3);
		StringBuilder result = joiner.appendTo(new StringBuilder(), map);
		assertEquals("A:1,B:2,C:3", result.toString());
	}
	
	@Test
	public void testMapJoinerJoin() {
		Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
		Map<String, Integer> map = Map.of("X", 10, "Y", 20, "Z", 30);
		String result = joiner.join(map);
		assertEquals("X:10,Y:20,Z:30", result);
	}
}