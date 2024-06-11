package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {
	
	@Test
	public void testJoinCollectionString() {
		Collection<String> strings = new ArrayList<>(Arrays.asList("Hello", "World", "!"));
		assertEquals("Hello World !", StringUtil.join(strings, " "));
		assertEquals("Hello,World,!", StringUtil.join(strings, ","));
	}
	
	@Test
	public void testJoinIteratorString() {
		List<String> strings = Arrays.asList("Hello", "World", "!");
		Iterator<String> iterator = strings.iterator();
		assertEquals("Hello World !", StringUtil.join(iterator, " "));
		assertEquals("Hello,World,!", StringUtil.join(iterator, ","));
	}
	
	@Test
	public void testJoinStringArrayString() {
		String[] strings = {"Hello", "World", "!"};
		assertEquals("Hello World !", StringUtil.join(strings, " "));
		assertEquals("Hello,World,!", StringUtil.join(strings, ","));
	}
	
	@Test
	public void testPadding() {
		assertEquals("", StringUtil.padding(0));
		assertEquals("     ", StringUtil.padding(5));
	}
	
	@Test
	public void testIsBlank() {
		assertTrue(StringUtil.isBlank(null));
		assertTrue(StringUtil.isBlank(""));
		assertTrue(StringUtil.isBlank("   "));
		assertFalse(StringUtil.isBlank("Hello"));
	}
	
	@Test
	public void testIsNumeric() {
		assertFalse(StringUtil.isNumeric(null));
		assertFalse(StringUtil.isNumeric(""));
		assertTrue(StringUtil.isNumeric("123"));
		assertFalse(StringUtil.isNumeric("Hello"));
	}
	
	@Test
	public void testIsWhitespace() {
		assertTrue(StringUtil.isWhitespace(' '));
		assertTrue(StringUtil.isWhitespace('\t'));
		assertTrue(StringUtil.isWhitespace('\n'));
		assertTrue(StringUtil.isWhitespace('\f'));
		assertTrue(StringUtil.isWhitespace('\r'));
		assertFalse(StringUtil.isWhitespace('A'));
	}
	
	@Test
	public void testIsActuallyWhitespace() {
		assertTrue(StringUtil.isActuallyWhitespace(' '));
		assertTrue(StringUtil.isActuallyWhitespace('\t'));
		assertTrue(StringUtil.isActuallyWhitespace('\n'));
		assertTrue(StringUtil.isActuallyWhitespace('\f'));
		assertTrue(StringUtil.isActuallyWhitespace('\r'));
		assertFalse(StringUtil.isActuallyWhitespace('A'));
		assertTrue(StringUtil.isActuallyWhitespace(160));
	}
	
	@Test
	public void testNormaliseWhitespace() {
		assertEquals("Hello World !", StringUtil.normaliseWhitespace("Hello        World       !"));
	}
	
	@Test
	public void testAppendNormalisedWhitespace() {
		StringBuilder sb = StringUtil.stringBuilder();
		StringUtil.appendNormalisedWhitespace(sb, "Hello        World       !", false);
		assertEquals("Hello World !", sb.toString());
	}
	
	@Test
	public void testIn() {
		assertTrue(StringUtil.in("Hello", "Hello", "World", "!"));
		assertFalse(StringUtil.in("Hello", "World", "!"));
	}
	
	@Test
	public void testInSorted() {
		assertTrue(StringUtil.inSorted("Hello", new String[] {"Hello", "World", "!"}));
		assertFalse(StringUtil.inSorted("Hello", new String[] {"World", "!"}));
	}
	
	@Test
	public void testResolve() throws MalformedURLException {
		URL base = new URL("http://example.com/");
		assertEquals("http://example.com/foo", StringUtil.resolve(base, "/foo").toExternalForm());
		assertEquals("http://example.com/foo", StringUtil.resolve(base, "foo").toExternalForm());
	}
	
	@Test
	public void testResolve_withMalformedUrl() {
		String base = "http://example.com/";
		assertEquals("http://example.com/foo", StringUtil.resolve(base, "/foo"));
		assertEquals("http://example.com/foo", StringUtil.resolve(base, "foo"));
	}
	
	@Test
	public void testStringEmpty() {
		assertEquals("", StringUtil.stringBuilder().toString());
	}
	
	@Test
	public void testStringLengthGreaterThanMaxCachedBuilderSize() {
		StringUtil.stringBuilder().append(StringUtil.padding(1025));
		assertEquals(1025, StringUtil.stringBuilder().length());
	}
	
}