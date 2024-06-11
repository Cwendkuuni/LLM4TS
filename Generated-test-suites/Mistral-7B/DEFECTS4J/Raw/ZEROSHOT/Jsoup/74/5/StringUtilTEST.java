package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

 @Test
 public void testJoinEmptyCollection() {
 String expected = "";
 String result = StringUtil.join(Collections.emptyList(), "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinSingleElementCollection() {
 String expected = "test";
 String result = StringUtil.join(Arrays.asList("test"), "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinCollectionWithSeparator() {
 String[] input = {"apple", "banana", "cherry"};
 String expected = "applebananacherry";
 String result = StringUtil.join(Arrays.asList(input), "-");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinIterator() {
 Iterator<String> iterator = Arrays.asList("apple", "banana", "cherry").iterator();
 String expected = "apple-banana-cherry";
 String result = StringUtil.join(iterator, "-");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinArrayWithSeparator() {
 String[] input = {"apple", "banana", "cherry"};
 String expected = "apple, banana, cherry";
 String result = StringUtil.join(input, ", ");
 assertEquals(expected, result);
 }

 @Test
 public void testPadding() {
 String expected = "   test";
 String result = StringUtil.padding(4) + "test";
 assertEquals(expected, result);
 }

 @Test
 public void testIsBlank() {
 String empty = "";
 String nullString = null;
 String test = "test";
 String whitespace = "   ";
 String multipleWhitespace = "   \n   ";

 assertTrue(StringUtil.isBlank(empty));
 assertTrue(StringUtil.isBlank(nullString));
 assertTrue(StringUtil.isBlank(whitespace));
 assertTrue(StringUtil.isBlank(multipleWhitespace));
 assertFalse(StringUtil.isBlank(test));
 }

 @Test
 public void testIsNumeric() {
 String test = "123";
 String notNumeric = "test";
 String numericWithLeadingZero = "0123";

 assertTrue(StringUtil.isNumeric(test));
 assertTrue(StringUtil.isNumeric(numericWithLeadingZero));
 assertFalse(StringUtil.isNumeric(notNumeric));
 }

 @Test
 public void testIsWhitespace() {
 char space = ' ';
 char tab = '\t';
 char newline = '\n';
 char formFeed = '\f';
 char carriageReturn = '\r';

 assertTrue(StringUtil.isWhitespace(space));
 assertTrue(StringUtil.isWhitespace(tab));
 assertTrue(StringUtil.isWhitespace(newline));
 assertTrue(StringUtil.isWhitespace(formFeed));
 assertFalse(StringUtil.isWhitespace('a'));
 }

 @Test
 public void testIsActuallyWhitespace() {
 char space = ' ';
 char tab = '\t';
 char newline = '\n';
 char formFeed = '\f';
 char carriageReturn = '\r';
 char nonBreakingSpace = '\u00A0';

 assertTrue(StringUtil.isActuallyWhitespace(space));
 assertTrue(StringUtil.isActuallyWhitespace(tab));
 assertTrue(StringUtil.isActuallyWhitespace(newline));
 assertTrue(StringUtil.isActuallyWhitespace(formFeed));
 assertTrue(StringUtil.isActuallyWhitespace(nonBreakingSpace));
 }

 @Test
 public void testNormaliseWhitespace() {
 String input = " test \n   hello     world \r\n test ";
 String expected = "test hello world test";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals(expected, result);
 }

 @Test
 public void testIn() {
 String needle = "test";
 String[] haystack = {"test1", "test2", "test"};

 assertTrue(StringUtil.in(needle, haystack));
 }

 @Test
 public void testInSorted() {
 String needle = "test";
 String[] haystack = {"test1", "test", "test2"};

 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 @Test
 public void testResolve() throws MalformedURLException {
 String baseUrl = "https://example.com/base";
 String relUrl = "/path/file.html";
 String expected = "https://example.com/path/file.html";
 String result = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, result.toExternalForm());
 }

}