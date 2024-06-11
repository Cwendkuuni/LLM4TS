package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

 // Test for join method with Collection
 @Test
 public void testJoinWithCollection() {
 Collections.addAll(list, "apple", "banana", "cherry");
 String result = StringUtil.join(list, ",");
 assertEquals("apple,banana,cherry", result);
 }

 // Test for join method with Iterator
 @Test
 public void testJoinWithIterator() {
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 String result = StringUtil.join(iterator, ",");
 assertEquals("apple,banana,cherry", result);
 }

 // Test for join method with Array
 @Test
 public void testJoinWithArray() {
 String[] strings = {"apple", "banana", "cherry"};
 String result = StringUtil.join(strings, ",");
 assertEquals("apple,banana,cherry", result);
 }

 // Test for padding method with valid input
 @Test
 public void testPaddingWithValidInput() {
 String result = StringUtil.padding(5);
 assertEquals("     ", result);
 }

 // Test for padding method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testPaddingWithInvalidInput() {
 StringUtil.padding(-5);
 }

 // Test for isBlank method with null input
 @Test
 public void testIsBlankWithNullInput() {
 assertTrue(StringUtil.isBlank(null));
 }

 // Test for isBlank method with empty string input
 @Test
 public void testIsBlankWithEmptyStringInput() {
 assertTrue(StringUtil.isBlank(""));
 }

 // Test for isBlank method with whitespace string input
 @Test
 public void testIsBlankWithWhitespaceStringInput() {
 String testString = "   ";
 assertTrue(StringUtil.isBlank(testString));
 }

 // Test for isNumeric method with valid input
 @Test
 public void testIsNumericWithValidInput() {
 String testString = "12345";
 assertTrue(StringUtil.isNumeric(testString));
 }

 // Test for isNumeric method with invalid input
 @Test
 public void testIsNumericWithInvalidInput() {
 String testString = "abcde";
 assertFalse(StringUtil.isNumeric(testString));
 }

 // Test for isWhitespace method with valid input
 @Test
 public void testIsWhitespaceWithValidInput() {
 int testCodePoint = ' ';
 assertTrue(StringUtil.isWhitespace(testCodePoint));
 }

 // Test for isWhitespace method with invalid input
 @Test
 public void testIsWhitespaceWithInvalidInput() {
 int testCodePoint = '!';
 assertFalse(StringUtil.isWhitespace(testCodePoint));
 }

 // Test for isActuallyWhitespace method with valid input
 @Test
 public void testIsActuallyWhitespaceWithValidInput() {
 int testCodePoint = ' ';
 assertTrue(StringUtil.isActuallyWhitespace(testCodePoint));
 }

 // Test for isActuallyWhitespace method with invalid input
 @Test
 public void testIsActuallyWhitespaceWithInvalidInput() {
 int testCodePoint = 'A';
 assertFalse(StringUtil.isActuallyWhitespace(testCodePoint));
 }

 // Test for normaliseWhitespace method with valid input
 @Test
 public void testNormaliseWhitespaceWithValidInput() {
 String testString = "hello world\n   \t\n how are you\t  ";
 String result = StringUtil.normaliseWhitespace(testString);
 assertEquals("hello world\nhow are you", result);
 }

 // Test for normaliseWhitespace method with empty input
 @Test
 public void testNormaliseWhitespaceWithEmptyInput() {
 String testString = "";
 String result = StringUtil.normaliseWhitespace(testString);
 assertEquals("", result);
 }

 // Test for in method with valid input
 @Test
 public void testInWithValidInput() {
 String haystack[] = {"apple", "banana", "cherry"};
 String needle = "banana";
 assertTrue(StringUtil.in(needle, haystack));
 }

 // Test for in method with invalid input
 @Test
 public void testInWithInvalidInput() {
 String haystack[] = {"apple", "banana", "cherry"};
 String needle = "orange";
 assertFalse(StringUtil.in(needle, haystack));
 }

 // Test for inSorted method with valid input
 @Test
 public void testInSortedWithValidInput() {
 String haystack[] = {"apple", "banana", "cherry"};
 String needle = "banana";
 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 // Test for inSorted method with invalid input
 @Test
 public void testInSortedWithInvalidInput() {
 String haystack[] = {"apple", "banana", "cherry"};
 String needle = "orange";
 assertFalse(StringUtil.inSorted(needle, haystack));
 }

 // Test for resolve method with valid input
 @Test
 public void testResolveWithValidInput() throws MalformedURLException {
 String baseUrl = "http://www.example.com/index.html";
 String relUrl = "/about.html";
 String expected = "http://www.example.com/about.html";
 URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, result.toExternalForm());
 }

 // Test for resolve method with invalid input
 @Test(expected = MalformedURLException.class)
 public void testResolveWithInvalidInput() throws MalformedURLException {
 String baseUrl = "http://www.example.com/index.html";
 String relUrl = "//about.html";
 StringUtil.resolve(new URL(baseUrl), relUrl);
 }
}