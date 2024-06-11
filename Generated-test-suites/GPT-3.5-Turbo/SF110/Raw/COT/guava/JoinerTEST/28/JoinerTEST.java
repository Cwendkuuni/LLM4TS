
package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class JoinerTest {
	
	@Test
	public void testOnString() {
		String separator = "/";
		Joiner joiner = Joiner.on(separator);
		assertEquals("a/b/c", joiner.join(Arrays.asList("a", "b", "c")));
	}
	
	@Test
	public void testOnChar() {
		char separator = '/';
		Joiner joiner = Joiner.on(separator);
		assertEquals("a/b/c", joiner.join(Arrays.asList("a", "b", "c")));
	}
	
	@Test
	public void testAppendToWithIterable() throws IOException {
		Joiner joiner = Joiner.on("/");
		assertEquals("a/b/c", joiner.appendTo(new StringBuilder(), Arrays.asList("a", "b", "c")).toString());
	}
	
	@Test
	public void testAppendToWithIterator() throws IOException {
		Joiner joiner = Joiner.on("/");
		Iterator<?> parts = Arrays.asList("a", "b", "c").iterator();
		StringBuilder builder = new StringBuilder();
		assertEquals(builder, joiner.appendTo(builder, parts));
	}
	
	@Test
	public void testAppendToWithArray() throws IOException {
		Joiner joiner = Joiner.on("/");
		assertEquals("a/b/c", joiner.appendTo(new StringBuilder(), new Object[] {"a", "b", "c"}).toString());
	}
	
	@Test
	public void testAppendToWithObjects() throws IOException {
		Joiner joiner = Joiner.on("/");
		assertEquals("a/b/c", joiner.appendTo(new StringBuilder(), "a", "b", "c").toString());
	}
	
	@Test
	public void testAppendToWithNullText() throws IOException {
		Joiner joiner = Joiner.on("/");
		joiner.useForNull("null");
		assertEquals("a/b/null/c", joiner.appendTo(new StringBuilder(), "a", "b", null, "c").toString());
	}
	
	@Test
	public void testSkipNulls() throws IOException {
		Joiner joiner = Joiner.on("/").skipNulls();
		assertEquals("a/b/c", joiner.join(Arrays.asList("a", "b", "c")));
	}
	
	@Test
	public void testMapJoiner() throws IOException {
		Joiner.MapJoiner mapJoiner = Joiner.on("/").withKeyValueSeparator(":");
		assertEquals("key1:value1/key2:value2", mapJoiner.join(Arrays.asList(
				new AbstractMap.SimpleEntry<>("key1", "value1"),
				new AbstractMap.SimpleEntry<>("key2", "value2"))));
	}
}
