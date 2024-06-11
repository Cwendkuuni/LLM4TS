package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

 @Test
 public void testJoinCollection() {
 String[] strings = new String[]{"apple", "banana", "cherry"};
 String sep = ",";
 String expected = "apple,banana,cherry";
 String result = StringUtil.join(Arrays.asList(strings), sep);
 assertEquals(expected, result);
 }

 @Test
 public void testJoinIterator() {
 List<String> strings = Arrays.asList("apple", "banana", "cherry");
 Iterator<String> iterator = strings.iterator();
 String sep = ",";
 String expected = "apple,banana,cherry";
 String result = StringUtil.join(iterator, sep);
 assertEquals(expected, result);
 }

 @Test
 public void testJoinArray() {
 String[] strings = new String[]{"apple", "banana", "cherry"};
 String sep = ",";
 String expected = "apple,banana,cherry";
 String result = StringUtil.join(strings, sep);
 assertEquals(expected, result);
 }

 @Test
 public void testPadding() {
 for (int i = 0; i < padding.length; i++) {
 String paddingString = StringUtil.padding(i);
 String expected = padding[i];
 assertEquals(expected, paddingString);
 }
 }

 @Test
 public void testIsBlank() {
 String blank = "";
 String notBlank = "foo";
 String whitespace = " \t\n\r";
 String leadingWhitespace = " \t\napple";
 String trailingWhitespace = "apple\t ";
 String isBlank = null;

 assertTrue(StringUtil.isBlank(blank));
 assertFalse(StringUtil.isBlank(notBlank));
 assertFalse(StringUtil.isBlank(whitespace));
 assertFalse(StringUtil.isBlank(leadingWhitespace));
 assertFalse(StringUtil.isBlank(trailingWhitespace));
 assertFalse(StringUtil.isBlank(isBlank));
 }

 @Test
 public void testIsNumeric() {
 String numeric = "1234";
 String nonNumeric1 = "abc";
 String nonNumeric2 = "123a";
 String empty = "";
 String nullString = null;

 assertTrue(StringUtil.isNumeric(numeric));
 assertFalse(StringUtil.isNumeric(nonNumeric1));
 assertFalse(StringUtil.isNumeric(nonNumeric2));
 assertFalse(StringUtil.isNumeric(empty));
 assertNull(StringUtil.isNumeric(nullString));
 }

 @Test
 public void testIsWhitespace() {
 char space = ' ';
 char tab = '\t';
 char lf = '\n';
 char cr = '\r';

 assertTrue(StringUtil.isWhitespace(space));
 assertTrue(StringUtil.isWhitespace(tab));
 assertTrue(StringUtil.isWhitespace(lf));
 assertTrue(StringUtil.isWhitespace(cr));

 assertFalse(StringUtil.isWhitespace('\u00A0')); // non-breaking space
 }

 @Test
 public void testIsActuallyWhitespace() {
 char space = ' ';
 char tab = '\t';
 char lf = '\n';
 char cr = '\r';
 char nbsp = '\u00A0';

 assertTrue(StringUtil.isActuallyWhitespace(space));
 assertTrue(StringUtil.isActuallyWhitespace(tab));
 assertTrue(StringUtil.isActuallyWhitespace(lf));
 assertTrue(StringUtil.isActuallyWhitespace(cr));
 assertTrue(StringUtil.isActuallyWhitespace(nbsp));
 }

 @Test
 public void testNormaliseWhitespace() {
 String stringWithMultipleSpaces = " foo  bar ";
 String stringWithMixedWhitespace = "foo\n bar\t\r baz";
 String expected = "foo bar baz";
 String result = StringUtil.normaliseWhitespace(stringWithMultipleSpaces);
 assertEquals(expected, result);

 expected = "foo\nbartaz";
 result = StringUtil.normaliseWhitespace(stringWithMixedWhitespace);
 assertEquals(expected, result);
 }

 @Test
 public void testIn() {
 String needle = "apple";
 String[] haystack = {"apple", "banana", "cherry", "apple"};
 assertTrue(StringUtil.in(needle, haystack));
 }

 @Test
 public void testInSorted() {
 String needle = "apple";
 String[] haystack = {"cherry", "banana", "apple"};
 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 @Test
 public void testResolve() throws MalformedURLException {
 String baseUrl = "http://example.com/base";
 String relUrl = "/relative";
 URL expected = new URL("http://example.com/relative");
 URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected.toString(), result.toString());
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveMalformedUrl() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = "foo:bar";
 StringUtil.resolve(new URL(baseUrl), relUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveEmptyRelUrl() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = "";
 StringUtil.resolve(new URL(baseUrl), relUrl);
 }

 @Test
 public void testResolveNullBaseUrl() {
 String relUrl = "/relative";
 try {
 StringUtil.resolve(null, relUrl);
 fail("Expected MalformedURLException");
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test
 public void testResolveNullRelUrl() {
 String baseUrl = "http://example.com/base";
 try {
 StringUtil.resolve(new URL(baseUrl), null);
 fail("Expected MalformedURLException");
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test
 public void testResolveEmptyBaseUrl() {
 String relUrl = "/relative";
 try {
 StringUtil.resolve(new URL(""), relUrl);
 fail("Expected MalformedURLException");
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test
 public void testResolveEmptyRelUrl() {
 String baseUrl = "http://example.com/base";
 try {
 StringUtil.resolve(new URL(baseUrl), "");
 fail("Expected MalformedURLException");
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test
 public void testResolveRelativeUrlWithFragment() {
 String baseUrl = "http://example.com/base#fragment";
 String relUrl = "#fragment2";
 String expected = "http://example.com/base#fragment2";
 String result = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, result.toExternalForm());
 }

 @Test
 public void testStringBuilder() {
 StringBuilder sb = StringUtil.stringBuilder();
 assertNotNull(sb);
 }
}