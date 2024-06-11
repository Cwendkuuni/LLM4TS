import org.junit.Test;
import org.junit.Assert.*; // for assertions
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

 // Test for join method with Collection as argument
 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("hello", "world", "junit");
 String expected = "helloworldjunit";
 String actual = StringUtil.join(strings, "");
 assertEquals(expected, actual);
 }

 // Test for join method with Iterator as argument
 @Test
 public void testJoinIterator() {
 Iterator<String> strings = Arrays.asList("hello", "world", "junit").iterator();
 String expected = "helloworldjunit";
 String actual = StringUtil.join(strings, "");
 assertEquals(expected, actual);
 }

 // Test for join method with Array as argument
 @Test
 public void testJoinArray() {
 String[] strings = new String[]{"hello", "world", "junit"};
 String expected = "helloworldjunit";
 String actual = StringUtil.join(strings, "");
 assertEquals(expected, actual);
 }

 // Test for padding method with valid width
 @Test
 public void testPaddingValidWidth() {
 String expected = StringUtil.padding(5);
 String actual = StringUtil.padding(5);
 assertEquals(expected, actual);
 }

 // Test for padding method with invalid width (negative value)
 @Test(expected = IllegalArgumentException.class)
 public void testPaddingInvalidWidth() {
 StringUtil.padding(-5);
 }

 // Test for isBlank method with null string
 @Test
 public void testIsBlankNullString() {
 boolean expected = true;
 boolean actual = StringUtil.isBlank(null);
 assertEquals(expected, actual);
 }

 // Test for isBlank method with empty string
 @Test
 public void testIsBlankEmptyString() {
 boolean expected = true;
 boolean actual = StringUtil.isBlank("");
 assertEquals(expected, actual);
 }

 // Test for isBlank method with whitespace string
 @Test
 public void testIsBlankWhitespaceString() {
 boolean expected = true;
 boolean actual = StringUtil.isBlank(" ");
 assertEquals(expected, actual);
 }

 // Test for isBlank method with non-blank string
 @Test
 public void testIsBlankNonBlankString() {
 boolean expected = false;
 boolean actual = StringUtil.isBlank("hello");
 assertEquals(expected, actual);
 }

 // Test for isNumeric method with null string
 @Test
 public void testIsNumericNullString() {
 boolean expected = false;
 boolean actual = StringUtil.isNumeric(null);
 assertEquals(expected, actual);
 }

 // Test for isNumeric method with empty string
 @Test
 public void testIsNumericEmptyString() {
 boolean expected = false;
 boolean actual = StringUtil.isNumeric("");
 assertEquals(expected, actual);
 }

 // Test for isNumeric method with numeric string
 @Test
 public void testIsNumericNumericString() {
 boolean expected = true;
 boolean actual = StringUtil.isNumeric("123456");
 assertEquals(expected, actual);
 }

 // Test for isNumeric method with non-numeric string
 @Test
 public void testIsNumericNonNumericString() {
 boolean expected = false;
 boolean actual = StringUtil.isNumeric("hello");
 assertEquals(expected, actual);
 }

 // Test for normaliseWhitespace method with no leading whitespace
 @Test
 public void testNormaliseWhitespaceNoLeading() {
 String expected = "hello world";
 String actual = StringUtil.normaliseWhitespace("hello world");
 assertEquals(expected, actual);
 }

 // Test for normaliseWhitespace method with leading whitespace
 @Test
 public void testNormaliseWhitespaceLeading() { // add test cases for other leading whitespace characters
 String expected = "hello world";
 String actual = StringUtil.normaliseWhitespace(" hello world ");
 assertEquals(expected, actual);
 }

 // Test for in method with array
 @Test
 public void testInArray() {
 String needle = "hello";
 String[] haystack = new String[]{"hello", "world", "junit"};
 assertTrue(StringUtil.in(needle, haystack));
 }

 // Test for in method with list
 @Test
 public void testInList() {
 String needle = "world";
 List<String> haystack = Arrays.asList("hello", "world", "junit");
 assertFalse(StringUtil.in(needle, haystack));
 }

 // Test for inSorted method with array
 @Test
 public void testInSortedArray() {
 String needle = "world";
 String[] haystack = new String[]{"hello", "world", "junit"};
 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 // Test for resolve method with absolute base URL and absolute rel URL
 @Test
 public void testResolveAbsoluteBaseAndRelative() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "http://example.org";
 String expected = "http://example.org";
 String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with absolute base URL and relative URL starting with "//"
 @Test
 public void testResolveAbsoluteBaseAndRelativeDoubleSlash() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "//example.org";
 String expected = "http://example.org";
 String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with absolute base URL and relative URL starting with "/"
 @Test
 public void testResolveAbsoluteBaseAndRelativeSlash() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "/example.org";
 String expected = "http://example.com/example.org";
 String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with absolute base URL and empty relative URL
 @Test
 public void testResolveAbsoluteBaseAndEmptyRelative() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "";
 String expected = baseUrl;
 String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with absolute base URL and null relative URL
 @Test
 public void testResolveAbsoluteBaseAndNullRelative() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = null;
 String expected = baseUrl;
 String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with absolute base URL and invalid relative URL
 @Test(expected = MalformedURLException.class)
 public void testResolveAbsoluteBaseAndInvalidRelative() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "/*this should throw an exception";
 String expected = "";
 String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with absolute base URL and empty string as base
 @Test
 public void testResolveAbsoluteEmptyBase() throws MalformedURLException {
 String baseUrl = "";
 String relUrl = "example.org";
 String expected = "";
 String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with absolute base URL and empty string as relative
 @Test
 public void testResolveAbsoluteEmptyRelative() throws MalformedURLException {
 String baseUrl = "http://example.com";
 String relUrl = "";
 String expected = baseUrl;
 String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 // Test for stringBuilder method
 @Test
 public void testStringBuilder() {
 StringBuilder sb = StringUtil.stringBuilder();
 String expected = "";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 // Test for stringBuilder method with some content
 @Test
 public void testStringBuilderSomeContent() {
 StringBuilder sb = StringUtil.stringBuilder();
 sb.append("hello");
 String expected = "hello";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 // Test for stringBuilder method with a lot of content
 @Test
 public void testStringBuilderALotOfContent() {
 StringBuilder sb = StringUtil.stringBuilder();
 for (int i = 0; i < 1000; i++) {
 sb.append(i);
 }
 String expected = String.join("", Arrays.asList(new String[1000]));
 String actual = sb.toString();
 assertEquals(expected, actual);
 }
}