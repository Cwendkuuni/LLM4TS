package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("one", "two", "three");
 String expected = "one two three";
 String result = StringUtil.join(strings, " ");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinCollectionEmpty() {
 Collection<String> strings = Collections.emptyList();
 String result = StringUtil.join(strings, " ");
 assertEquals("", result);
 }

 @Test
 public void testJoinIterator() {
 List<String> strings = Arrays.asList("one", "two", "three");
 Iterator<String> iterator = strings.iterator();
 String expected = "one two three";
 String result = StringUtil.join(iterator, " ");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinIteratorEmpty() {
 Iterator<String> iterator = Collections.emptyIterator();
 String result = StringUtil.join(iterator, " ");
 assertEquals("", result);
 }

 @Test
 public void testJoinArray() {
 String[] strings = new String[]{"one", "two", "three"};
 String expected = "one two three";
 String result = StringUtil.join(strings, " ");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinArrayEmpty() {
 String[] strings = new String[]{};
 String result = StringUtil.join(strings, " ");
 assertEquals("", result);
 }

 @Test
 public void testPadding() {
 for (int i = 0; i < 21; i++) {
 String padding = StringUtil.padding(i);
 assertEquals(padding.length(), i);
 }
 }

 @Test
 public void testIsBlank() {
 String empty = "";
 String blank = " ";
 String notBlank = "not blank";
 assertTrue(StringUtil.isBlank(empty));
 assertTrue(StringUtil.isBlank(blank));
 assertFalse(StringUtil.isBlank(notBlank));
 }

 @Test
 public void testIsNumeric() {
 String number = "12345";
 String notNumber = "abc";
 String empty = "";
 assertTrue(StringUtil.isNumeric(number));
 assertFalse(StringUtil.isNumeric(notNumber));
 assertFalse(StringUtil.isNumeric(empty));
 }

 @Test
 public void testIsWhitespace() {
 char c1 = ' ';
 char c2 = '\t';
 char c3 = '\n';
 char c4 = '\f';
 char c5 = '\r';
 assertTrue(StringUtil.isWhitespace(c1));
 assertTrue(StringUtil.isWhitespace(c2));
 assertTrue(StringUtil.isWhitespace(c3));
 assertTrue(StringUtil.isWhitespace(c4));
 assertTrue(StringUtil.isWhitespace(c5));
 }

 @Test
 public void testIsActuallyWhitespace() {
 char c1 = ' ';
 char c2 = '\t';
 char c3 = '\n';
 char c4 = '\f';
 char c5 = '\r';
 char c6 = 160; // non-breaking space
 assertTrue(StringUtil.isActuallyWhitespace(c1));
 assertTrue(StringUtil.isActuallyWhitespace(c2));
 assertTrue(StringUtil.isActuallyWhitespace(c3));
 assertTrue(StringUtil.isActuallyWhitespace(c4));
 assertTrue(StringUtil.isActuallyWhitespace(c5));
 assertTrue(StringUtil.isActuallyWhitespace(c6));
 }

 @Test
 public void testNormaliseWhitespace() {
 String input = "  test string  \n with  tabs and \r newlines   ";
 String expected = "test string  with  tabs and  newlines";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals(expected, result);
 }

 @Test
 public void testIn() {
 String needle = "needle";
 String[] haystack = new String[]{"haystack1", "haystack2", "needle"};
 assertTrue(StringUtil.in(needle, haystack));
 }

 @Test
 public void testInSorted() {
 String needle = "needle";
 String[] haystack = new String[]{"haystack1", "needle", "haystack2"};
 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveInvalidBase() throws MalformedURLException {
 String relUrl = "relative url";
 String baseUrl = "invalid base url";
 String result = StringUtil.resolve(baseUrl, relUrl);
 fail("Expected MalformedURLException");
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveInvalidRel() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "relative url with invalid scheme: //other";
 String result = StringUtil.resolve(baseUrl, relUrl);
 fail("Expected MalformedURLException");
 }

 @Test
 public void testResolve() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "/path/to";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/path/to", result.toExternalForm());
 }

 @Test
 public void testResolveBaseAbsolute() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "http://example.org";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.org", result.toExternalForm());
 }

 @Test
 public void testResolveTrailingSlashBase() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = "/path/to";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/path/to", result.toExternalForm());
 }

 @Test
 public void testResolveTrailingSlashRel() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "/path/to/";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/path/to", result.toExternalForm());
 }
}