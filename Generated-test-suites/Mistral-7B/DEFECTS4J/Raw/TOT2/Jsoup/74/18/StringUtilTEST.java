package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("hello", "world", "!");
 String expected = "helloworld!";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinIteratorWhenEmpty() {
 Iterator<String> iterator = Arrays.asList("", "", "").listIterator();
 String result = StringUtil.join(iterator, "");
 assertEquals("", result);
 }

 @Test
 public void testJoinIterator() {
 Iterator<String> iterator = Arrays.asList("hello", "world", "!").listIterator();
 String expected = "helloworld!";
 String result = StringUtil.join(iterator, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinArray() {
 String[] strings = {"hello", "world", "!"};
 String expected = "helloworld!";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPaddingNegativeWidth() {
 String result = StringUtil.padding(-1);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testIsBlankNull() {
 String string = null;
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlankEmpty() {
 String string = "";
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlankWhitespace() {
 String string = " ";
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlankNonWhitespace() {
 String string = "hello";
 boolean result = StringUtil.isBlank(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumericEmpty() {
 String string = "";
 boolean result = StringUtil.isNumeric(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumericNumber() {
 String string = "123";
 boolean result = StringUtil.isNumeric(string);
 assertTrue(result);
 }

 @Test
 public void testIsNumericLetter() {
 String string = "a";
 boolean result = StringUtil.isNumeric(string);
 assertFalse(result);
 }

 @Test
 public void testIsWhitespaceSpace() {
 boolean result = StringUtil.isWhitespace(' ');
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceTab() {
 boolean result = StringUtil.isWhitespace('\t');
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceLf() {
 boolean result = StringUtil.isWhitespace('\n');
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceCr() {
 boolean result = StringUtil.isWhitespace('\r');
 assertTrue(result);
 }

 @Test
 public void testNormaliseWhitespaceSingleSpace() {
 String string = "   hello world   ";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals("hello world", result);
 }

 @Test
 public void testNormaliseWhitespaceMultipleSpaces() {
 String string = "   hello   world   ";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals("hello world", result);
 }

 @Test
 public void testNormaliseWhitespaceTabToSpace() {
 String string = "\t   hello   world   \t";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals("hello world", result);
 }

 @Test
 public void testNormaliseWhitespaceNewlineToSpace() {
 String string = "hello\nworld";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals("helloworld", result);
 }

 @Test
 public void testNormaliseWhitespaceTabAndNewlineToSpace() {
 String string = "\thello\nworld";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals("hello world", result);
 }

 @Test
 public void testInEmptyArray() {
 String needle = "needle";
 boolean result = StringUtil.in(needle, new String[0]);
 assertFalse(result);
 }

 @Test
 public void testInSingleElementArray() {
 String needle = "needle";
 String[] haystack = {"hay"};
 boolean result = StringUtil.in(needle, haystack);
 assertFalse(result);
 }

 @Test
 public void testInArrayContainsNeedle() {
 String needle = "needle";
 String[] haystack = {"hay", "haystack", "needle"};
 boolean result = StringUtil.in(needle, haystack);
 assertTrue(result);
 }

 @Test
 public void testInCollectionEmpty() {
 String needle = "needle";
 boolean result = StringUtil.in(needle, Collections.emptyList());
 assertFalse(result);
 }

 @Test
 public void testInCollectionSingleElement() {
 String needle = "needle";
 List<String> collection = Arrays.asList("hay");
 boolean result = StringUtil.in(needle, collection);
 assertFalse(result);
 }

 @Test
 public void testInCollectionContainsNeedle() {
 String needle = "needle";
 List<String> collection = Arrays.asList("hay", "needle");
 boolean result = StringUtil.in(needle, collection);
 assertTrue(result);
 }

 @Test
 public void testResolveAbsoluteURL() throws MalformedURLException {
 URL base = new URL("http://example.com/");
 String relUrl = "/path/file.html";
 URL expected = new URL("http://example.com/path/file.html");
 URL result = StringUtil.resolve(base, relUrl);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testResolveAbsoluteURLWithTrailingSlash() throws MalformedURLException {
 URL base = new URL("http://example.com/");
 String relUrl = "/path/file.html/";
 URL expected = new URL("http://example.com/path/file.html/");
 URL result = StringUtil.resolve(base, relUrl);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testResolveAbsoluteURLWithTrailingSlashAndHash() throws MalformedURLException {
 URL base = new URL("http://example.com/");
 String relUrl = "/path/file.html/#fragment";
 URL expected = new URL("http://example.com/path/file.html/#fragment");
 URL result = StringUtil.resolve(base, relUrl);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testResolveAbsoluteURLWithRelativeURLThatIsAlreadyAbsolute() throws MalformedURLException {
 URL base = new URL("http://example.com/");
 String relUrl = "http://example.com/path/file.html";
 URL expected = new URL(relUrl);
 URL result = StringUtil.resolve(base, relUrl);
 assertEquals(expected.toString(), result.toString());
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveAbsoluteURLWithMalformedURL() throws MalformedURLException {
 URL base = new URL("http://example.com/");
 String relUrl = "//example.com/path/file.html";
 String result = StringUtil.resolve(base, relUrl);
 fail("Expected MalformedURLException");
 }

 @Test(expected = NullPointerException.class)
 public void testResolveAbsoluteURLWithNullBase() {
 String relUrl = "/path/file.html";
 String result = StringUtil.resolve(null, relUrl);
 fail("Expected NullPointerException");
 }
}