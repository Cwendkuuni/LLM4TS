package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for StringUtil class.
 */
public class StringUtilTest {

 @Test
 public void testJoin() {
 String[] strings = {"Hello", "World", "!"};
 String expected = "HelloWorld!";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);

 strings = new String[0];
 result = StringUtil.join(strings, "");
 assertEquals("", result);
 }

 @Test
 public void testJoin_iterator() {
 Iterator<String> strings = Arrays.asList("Hello", "World", "!").iterator();
 String expected = "HelloWorld!";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);

 strings = Arrays.asList().iterator();
 result = StringUtil.join(strings, "");
 assertEquals("", result);
 }

 @Test
 public void testJoin_collection() {
 Collection<String> strings = Arrays.asList("Hello", "World", "!");
 String expected = "HelloWorld!";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);

 strings = Arrays.asList();
 result = StringUtil.join(strings, "");
 assertEquals("", result);
 }

 @Test
 public void testPadding() {
 for (int i = 0; i <= padding.length; i++) {
 String result = StringUtil.padding(i);
 assertEquals(padding[i], result);
 }
 }

 @Test
 public void testIsBlank() {
 String empty = "";
 String whitespace = " ";
 String whitespaceOnly = "   ";
 String notBlank = "Hello";

 assertFalse(StringUtil.isBlank(notBlank));
 assertTrue(StringUtil.isBlank(empty));
 assertTrue(StringUtil.isBlank(whitespace));
 assertTrue(StringUtil.isBlank(whitespaceOnly));
 }

 @Test
 public void testIsNumeric() {
 String notNumeric = "abc";
 String numeric = "123";
 String numericWithLetters = "123abc";

 assertFalse(StringUtil.isNumeric(notNumeric));
 assertTrue(StringUtil.isNumeric(numeric));
 assertTrue(StringUtil.isNumeric(numericWithLetters));
 }

 @Test
 public void testIsWhitespace() {
 int space = ' ';
 int tab = '\t';
 int lf = '\n';
 int cr = '\r';

 assertTrue(StringUtil.isWhitespace(space));
 assertTrue(StringUtil.isWhitespace(tab));
 assertTrue(StringUtil.isWhitespace(lf));
 assertTrue(StringUtil.isWhitespace(cr));

 assertFalse(StringUtil.isWhitespace('\u0000'));
 assertFalse(StringUtil.isWhitespace('\u0001'));
 assertFalse(StringUtil.isWhitespace('\u0002'));
 assertFalse(StringUtil.isWhitespace('\u007F'));
 }

 @Test
 public void testIsActuallyWhitespace() {
 int space = ' ';
 int tab = '\t';
 int lf = '\n';
 int cr = '\r';
 int nbsp = '\u00A0';

 assertTrue(StringUtil.isActuallyWhitespace(space));
 assertTrue(StringUtil.isActuallyWhitespace(tab));
 assertTrue(StringUtil.isActuallyWhitespace(lf));
 assertTrue(StringUtil.isActuallyWhitespace(cr));
 assertTrue(StringUtil.isActuallyWhitespace(nbsp));

 assertFalse(StringUtil.isActuallyWhitespace('\u0000'));
 assertFalse(StringUtil.isActuallyWhitespace('\u0001'));
 assertFalse(StringUtil.isActuallyWhitespace('\u0002'));
 assertFalse(StringUtil.isActuallyWhitespace('\u007F'));
 }

 @Test
 public void testNormaliseWhitespace() {
 String multipleSpaces = " Hello   World  ";
 String multipleTabs = "\t  \tHey\t";
 String multipleNewlines = "Line\nAnother\nLine";
 String leadingNewline = "\nLine";
 String trailingNewline = "Line\n";
 String expected = "Hello World";
 String result = StringUtil.normaliseWhitespace(multipleSpaces);
 assertEquals(expected, result);

 result = StringUtil.normaliseWhitespace(multipleTabs);
 assertEquals(expected, result);

 result = StringUtil.normaliseWhitespace(multipleNewlines);
 assertEquals(expected, result);

 result = StringUtil.normaliseWhitespace(leadingNewline);
 assertEquals(expected, result);

 result = StringUtil.normaliseWhitespace(trailingNewline);
 assertEquals(expected, result);
 }

 @Test
 public void testIn() {
 String[] haystack = {"apple", "banana", "cherry"};
 String needle = "banana";
 assertTrue(StringUtil.in(needle, haystack));

 needle = "orange";
 assertFalse(StringUtil.in(needle, haystack));
 }

 @Test
 public void testInSorted() {
 String[] haystack = {"apple", "banana", "cherry"};
 String needle = "banana";
 assertTrue(StringUtil.inSorted(needle, haystack));

 needle = "orange";
 assertFalse(StringUtil.inSorted(needle, haystack));
 }

 @Test
 public void testResolve() {
 String baseUrl = "http://example.com";
 String relUrl = "/test.html";
 String expected = "http://example.com/test.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);

 baseUrl = "http://example.com/test.html";
 relUrl = "/test2.html";
 expected = "http://example.com/test2.html";
 result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);

 baseUrl = "http://example.com/test.html";
 relUrl = "./test.html";
 expected = "http://example.com/test.html";
 result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);

 baseUrl = "http://example.com/test.html";
 relUrl = "../test.html";
 expected = "http://example.com/test.html";
 result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);

 baseUrl = "http://example.com/test.html";
 relUrl = "../test2.html";
 expected = "http://example.com/test2.html";
 result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);

 baseUrl = "http://example.com/test.html";
 relUrl = "../../test.html";
 expected = "http://example.com/test.html";
 result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);

 baseUrl = "http://example.com/test.html";
 relUrl = "../../test2.html";
 expected = "http://example.com/test2.html";
 result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);

 baseUrl = "http://example.com/test.html";
 relUrl = "test.html";
 assertNull(StringUtil.resolve(baseUrl, relUrl));
 }

 @Test
 public void testMalformedURL() {
 String baseUrl = "http://example.com";
 String relUrl = "//test.html";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }
}