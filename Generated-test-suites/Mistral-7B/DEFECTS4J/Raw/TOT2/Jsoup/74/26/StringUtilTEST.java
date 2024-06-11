package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

 // Test join method with Collection
 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("hello", "world", "junit");
 String expected = "helloworldjunit";
 String actual = StringUtil.join(strings, "");
 assertEquals(expected, actual);
 }

 // Test join method with Iterator
 @Test
 public void testJoinIterator() {
 Iterator<String> strings = Arrays.asList("hello", "world", "junit").iterator();
 String expected = "helloworldjunit";
 String actual = StringUtil.join(strings, "");
 assertEquals(expected, actual);
 }

 // Test join method with Array
 @Test
 public void testJoinArray() {
 String[] strings = {"hello", "world", "junit"};
 String expected = "helloworldjunit";
 String actual = StringUtil.join(strings, "");
 assertEquals(expected, actual);
 }

 // Test padding method
 @Test
 public void testPadding() {
 String actual = StringUtil.padding(2);
 String expected = "   ";
 assertEquals(expected, actual);
 }

 // Test isBlank method with null string
 @Test
 public void testIsBlankNullString() {
 assertTrue(StringUtil.isBlank(null));
 }

 // Test isBlank method with empty string
 @Test
 public void testIsBlankEmptyString() {
 assertTrue(StringUtil.isBlank(""));
 }

 // Test isBlank method with whitespace only string
 @Test
 public void testIsBlankWhitespaceString() {
 String str = "   ";
 assertTrue(StringUtil.isBlank(str));
 }

 // Test isBlank method with non-whitespace string
 @Test
 public void testIsBlankNonWhitespaceString() {
 String str = "hello";
 assertFalse(StringUtil.isBlank(str));
 }

 // Test isNumeric method with empty string
 @Test
 public void testIsNumericEmptyString() {
 assertFalse(StringUtil.isNumeric(""));
 }

 // Test isNumeric method with null string
 @Test (expected = IllegalArgumentException.class)
 public void testIsNumericNullString() {
 StringUtil.isNumeric(null);
 }

 // Test isNumeric method with non-numeric string
 @Test
 public void testIsNumericNonNumericString() {
 String str = "hello";
 assertFalse(StringUtil.isNumeric(str));
 }

 // Test isNumeric method with numeric string
 @Test
 public void testIsNumericNumericString() {
 String str = "12345";
 assertTrue(StringUtil.isNumeric(str));
 }

 // Test normaliseWhitespace method
 @Test
 public void testNormaliseWhitespace() {
 String input = " hello world     \n\t foo\r bar";
 String expected = "hello worldfoobar";
 String actual = StringUtil.normaliseWhitespace(input);
 assertEquals(expected, actual);
 }

 // Test in method
 @Test
 public void testIn() {
 String haystack = "hello world junit";
 String needle1 = "world";
 String needle2 = "java";
 assertTrue(StringUtil.in(needle1, haystack.split(" ")));
 assertFalse(StringUtil.in(needle2, haystack.split(" ")));
 }

 // Test inSorted method
 @Test
 public void testInSorted() {
 String[] haystack = {"hello", "world", "junit"};
 String needle = "world";
 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 // Test resolve method with absolute base URL and relative URL
 @Test
 public void testResolveAbsoluteBaseAndRelative() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "/path/to/file.html";
 URL expected = new URL("http://example.com/path/to/file.html");
 URL actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 // Test resolve method with absolute base URL and absolute relative URL
 @Test
 public void testResolveAbsoluteBaseAndAbsoluteRelative() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to";
 String relUrl = "http://example.com/another/file.html";
 URL expected = new URL("http://example.com/another/file.html");
 URL actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 // Test resolve method with null base URL and null relative URL
 @Test (expected = MalformedURLException.class)
 public void testResolveNullBaseNullRelative() throws MalformedURLException {
 URL base = null;
 String relUrl = null;
 StringUtil.resolve(base, relUrl);
 }

 // Test resolve method with null base URL and non-null relative URL
 @Test (expected = MalformedURLException.class)
 public void testResolveNullBaseNonNullRelative() throws MalformedURLException {
 URL base = null;
 String relUrl = "http://example.com/file.html";
 StringUtil.resolve(base, relUrl);
 }

 // Test resolve method with non-null base URL and null relative URL
 @Test (expected = IllegalArgumentException.class)
 public void testResolveNonNullBaseNullRelative() throws MalformedURLException {
 URL base = new URL("http://example.com");
 String relUrl = null;
 StringUtil.resolve(base, relUrl);
 }

 // Test resolve method with MalformedURLException
 @Test (expected = MalformedURLException.class)
 public void testResolveMalformedURL() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "malformed:/path/to/file.html";
 StringUtil.resolve(new URL(baseUrl), relUrl);
 }
}