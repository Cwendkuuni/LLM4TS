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
 Collection<String> strings = Arrays.asList("Hello", "World", "!");
 String expected = "HelloWorld!";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinIterator() {
 Iterator<String> strings = Arrays.asList("Hello", "World", "!").iterator();
 String expected = "HelloWorld!";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinArray() {
 String[] strings = new String[]{"Hello", "World", "!"};
 String expected = "HelloWorld!";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testPadding() {
 for (int i = 0; i < 21; i++) {
 String paddingResult = StringUtil.padding(i);
 assertEquals(StringUtil.padding(i).length(), paddingResult.length());
 }
 }

 @Test
 public void testIsBlank() {
 String testString1 = null;
 assertTrue(StringUtil.isBlank(testString1));

 String testString2 = "";
 assertTrue(StringUtil.isBlank(testString2));

 String testString3 = " ";
 assertTrue(StringUtil.isBlank(testString3));

 String testString4 = "   ";
 assertTrue(StringUtil.isBlank(testString4));

 String testString5 = "\r\n";
 assertTrue(StringUtil.isBlank(testString5));

 String testString6 = "Hello";
 assertFalse(StringUtil.isBlank(testString6));

 String testString7 = "Hello World";
 assertFalse(StringUtil.isBlank(testString7));

 String testString8 = "Hello World!";
 assertFalse(StringUtil.isBlank(testString8));

 String testString9 = "Hello World! \r\n";
 assertFalse(StringUtil.isBlank(testString9));
 }

 @Test
 public void testIsNumeric() {
 String testString1 = null;
 assertFalse(StringUtil.isNumeric(testString1));

 String testString2 = "123";
 assertTrue(StringUtil.isNumeric(testString2));

 String testString3 = "abcd";
 assertFalse(StringUtil.isNumeric(testString3));

 String testString4 = "123abcd";
 assertFalse(StringUtil.isNumeric(testString4));

 String testString5 = "123";
 assertTrue(StringUtil.isNumeric(testString5));
 }

 @Test
 public void testIsWhitespace() {
 char c = ' ';
 assertTrue(StringUtil.isWhitespace(c));

 char c1 = '\t';
 assertTrue(StringUtil.isWhitespace(c1));

 char c2 = '\n';
 assertTrue(StringUtil.isWhitespace(c2));

 char c3 = '\f';
 assertTrue(StringUtil.isWhitespace(c3));

 char c4 = '\r';
 assertTrue(StringUtil.isWhitespace(c4));

 char c5 = ' ';
 assertTrue(StringUtil.isWhitespace(c5));

 char c6 = 'A';
 assertFalse(StringUtil.isWhitespace(c6));
 }

 @Test
 public void testIsActuallyWhitespace() {
 char c = ' ';
 assertTrue(StringUtil.isActuallyWhitespace(c));

 char c1 = '\t';
 assertTrue(StringUtil.isActuallyWhitespace(c1));

 char c2 = '\n';
 assertTrue(StringUtil.isActuallyWhitespace(c2));

 char c3 = '\f';
 assertTrue(StringUtil.isActuallyWhitespace(c3));

 char c4 = '\r';
 assertTrue(StringUtil.isActuallyWhitespace(c4));

 char c5 = 160; // non-breaking space
 assertTrue(StringUtil.isActuallyWhitespace(c5));

 char c6 = 'A';
 assertFalse(StringUtil.isActuallyWhitespace(c6));
 }

 @Test
 public void testNormaliseWhitespace() {
 String testString1 = "  Hello World!";
 String expected = "Hello World!";
 String result = StringUtil.normaliseWhitespace(testString1);
 assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespace2() {
 String testString1 = "Hello\nWorld!";
 String expected = "Hello World!";
 String result = StringUtil.normaliseWhitespace(testString1);
 assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespace3() {
 String testString1 = "Hello\tWorld!\r\n";
 String expected = "Hello World!";
 String result = StringUtil.normaliseWhitespace(testString1);
 assertEquals(expected, result);
 }

 @Test
 public void testIn() {
 String needle = "world";
 String[] haystack = {"hello world", "world", "hello", "world!", "world wide", "hello world!"};
 List<String> expected = Arrays.asList("world", "hello world", "hello world!");
 List<String> result = Arrays.asList(StringUtil.in(needle, haystack));
 assertEquals(expected, result);
 }

 @Test
 public void testInSorted() {
 String needle = "world";
 String[] haystack = {"hello world", "world", "hello", "world!", "world wide", "hello world!"};
 boolean expected = true;
 boolean result = StringUtil.inSorted(needle, haystack);
 assertEquals(expected, result);
 }

 @Test
 public void testResolve() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "/foo.html";
 URL expected = new URL("http://example.com/foo.html");
 URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testResolveNullBaseUrl() throws MalformedURLException {
 String relUrl = "/foo.html";
 URL result = StringUtil.resolve(null, relUrl);
 assertEquals("", result.toString());
 }

 @Test
 public void testResolveAbsRelUrl() throws MalformedURLException {
 String baseUrl = "http://example.com/foo.html";
 String relUrl = "/bar.html";
 URL expected = new URL("http://example.com/bar.html");
 URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected.toString(), result.toString());
 }
}