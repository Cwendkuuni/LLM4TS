package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class JoinerTest {

	@Test
	public void testOnString() {
		Joiner joiner = Joiner.on(" ");
		assertEquals("Hello World", joiner.join("Hello", "World"));
	}

	@Test
	public void testOnChar() {
		Joiner joiner = Joiner.on(',');
		assertEquals("a,b,c,d", joiner.join("a", "b", "c", "d"));
	}

	@Test
	public void testAppendToIterable() {
		Joiner joiner = Joiner.on("-");
		assertEquals("1-2-3-4-5", joiner.appendTo(new StringBuilder(), Arrays.asList(1, 2, 3, 4, 5)).toString());
	}

	@Test
	public void testAppendToIterator() {
		Joiner joiner = Joiner.on("-");
		assertEquals("1-2-3-4-5", joiner.appendTo(new StringBuilder(), Arrays.asList(1, 2, 3, 4, 5).iterator()).toString());
	}

	@Test
	public void testAppendToObjectArray() {
		Joiner joiner = Joiner.on("-");
		assertEquals("1-2-3-4-5", joiner.appendTo(new StringBuilder(), new Object[] {1, 2, 3, 4, 5}).toString());
	}

	@Test
	public void testAppendToObjectVarargs() {
		Joiner joiner = Joiner.on("-");
		assertEquals("1-2-3-4-5", joiner.appendTo(new StringBuilder(), 1, 2, 3, 4, 5).toString());
	}

	@Test
	public void testAppendToBuilderIterable() {
		StringBuilder builder = new StringBuilder();
		Joiner joiner = Joiner.on("-");
		assertEquals("1-2-3-4-5", joiner.appendTo(builder, Arrays.asList(1, 2, 3, 4, 5)).toString());
		assertEquals("1-2-3-4-5", joiner.appendTo(builder, Arrays.asList(1, 2, 3, 4, 5)).toString());
	}

	@Test
	public void testAppendToBuilderIterator() {
		StringBuilder builder = new StringBuilder();
		Joiner joiner = Joiner.on("-");
		assertEquals("1-2-3-4-5", joiner.appendTo(builder, Arrays.asList(1, 2, 3, 4, 5).iterator()).toString());
		assertEquals("1-2-3-4-5", joiner.appendTo(builder, Arrays.asList(1, 2, 3, 4, 5).iterator()).toString());
	}

	@Test
	public void testAppendToBuilderObjectArray() {
		StringBuilder builder = new StringBuilder();
		Joiner joiner = Joiner.on("-");
		assertEquals("1-2-3-4-5", joiner.appendTo(builder, new Object[] {1, 2, 3, 4, 5}).toString());
		assertEquals("1-2-3-4-5", joiner.appendTo(builder, new Object[] {1, 2, 3, 4, 5}).toString());
	}

	@Test
	public void testAppendToBuilderObjectVarargs() {
		StringBuilder builder = new StringBuilder();
		Joiner joiner = Joiner.on("-");
		assertEquals("1-2-3-4-5", joiner.appendTo(builder, 1, 2, 3, 4, 5).toString());
		assertEquals("1-2-3-4-5", joiner.appendTo(builder, 1, 2, 3, 4, 5).toString());
	}

	@Test
	public void testJoinIterable() {
		Joiner joiner = Joiner.on(",");
		assertEquals("1,2,3,4,5", joiner.join(Arrays.asList(1, 2, 3, 4, 5)));
	}

	@Test
	public void testJoinIterator() {
		Joiner joiner = Joiner.on(",");
		assertEquals("1,2,3,4,5", joiner.join(Arrays.asList(1, 2, 3, 4, 5).iterator()));
	}

	@Test
	public void testJoinObjectArray() {
		Joiner joiner = Joiner.on(",");
		assertEquals("1,2,3,4,5", joiner.join(new Object[] {1, 2, 3, 4, 5}));
	}

	@Test
	public void testJoinObjectVarargs() {
		Joiner joiner = Joiner.on(",");
		assertEquals("1,2,3,4,5", joiner.join(1, 2, 3, 4, 5));
	}

	@Test
	public void testUseForNull() {
		Joiner joiner = Joiner.on(",").useForNull("NULL");
		assertEquals("1,NULL,3,NULL,5", joiner.join(1, null, 3, null, 5));
	}

	@Test
	public void testSkipNulls() {
		Joiner joiner = Joiner.on(",").skipNulls();
		assertEquals("1,3,5", joiner.join(1, null, 3, null, 5));
	}

	@Test
	public void testMapJoinerAppendToIterable() {
		Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
		Map<String, Integer> map = new HashMap<>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		assertEquals("a:1,b:2,c:3", joiner.appendTo(new StringBuilder(), map.entrySet()).toString());
	}

	@Test
	public void testMapJoinerAppendToIterator() {
		Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
		Map<String, Integer> map = new HashMap<>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		assertEquals("a:1,b:2,c:3", joiner.appendTo(new StringBuilder(), map.entrySet().iterator()).toString());
	}

	@Test
	public void testMapJoinerAppendToBuilderIterable() {
		StringBuilder builder = new StringBuilder();
		Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
		Map<String, Integer> map = new HashMap<>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		assertEquals("a:1,b:2,c:3", joiner.appendTo(builder, map.entrySet()).toString());
		assertEquals("a:1,b:2,c:3", joiner.appendTo(builder, map.entrySet()).toString());
	}

	@Test
	public void testMapJoinerAppendToBuilderIterator() {
		StringBuilder builder = new StringBuilder();
		Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
		Map<String, Integer> map = new HashMap<>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		assertEquals("a:1,b:2,c:3", joiner.appendTo(builder, map.entrySet().iterator()).toString());
		assertEquals("a:1,b:2,c:3", joiner.appendTo(builder, map.entrySet().iterator()).toString());
	}

	@Test
	public void testMapJoinerJoinIterable() {
		Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
		Map<String, Integer> map = new HashMap<>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		assertEquals("a:1,b:2,c:3", joiner.join(map.entrySet()));
	}

	@Test
	public void testMapJoinerJoinIterator() {
		Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
		Map<String, Integer> map = new HashMap<>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		assertEquals("a:1,b:2,c:3", joiner.join(map.entrySet().iterator()));
	}

	@Test
	public void testMapJoinerUseForNull() {
		Joiner joiner = Joiner.on(",").withKeyValueSeparator(":").useForNull("NULL");
		Map<String, Integer> map = new HashMap<>();
		map.put("a", 1);
		map.put("b", null);
		map.put("c", 3);
		assertEquals("a:1,b:NULL,c:3", joiner.join(map.entrySet()));
	}

}