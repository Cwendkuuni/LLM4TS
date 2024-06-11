import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

public class StringUtilTest {

 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("Hello", "World", "Java");
 String expected = "HelloWorldJava";
 String actual = StringUtil.join(strings, "");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testJoinIteratorWithOneElement() {
 Iterator<String> strings = Arrays.asList("Test").iterator();
 String expected = "Test";
 String actual = StringUtil.join(strings, "");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testJoinIteratorWithMultipleElements() {
 Iterator<String> strings = Arrays.asList("Test1", "Test2", "Test3").iterator();
 String expected = "Test1Test2Test3";
 String actual = StringUtil.join(strings, "");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testJoinArrayWithOneElement() {
 String[] strings = new String[]{"Test"};
 String expected = "Test";
 String actual = StringUtil.join(strings, "");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testJoinArrayWithMultipleElements() {
 String[] strings = new String[]{"Test1", "Test2", "Test3"};
 String expected = "Test1Test2Test3";
 String actual = StringUtil.join(strings, "");
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPaddingNegativeWidth() {
 String actual = StringUtil.padding(-1);
 Assert.fail();
 }

 @Test
 public void testPaddingZeroWidth() {
 String expected = "";
 String actual = StringUtil.padding(0);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPaddingWidthLessThanPaddingArraySize() {
 String expected = "   ";
 String actual = StringUtil.padding(3);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPaddingWidthGreaterThanPaddingArraySize() {
 String expected = "                          ";
 int width = 15;
 String actual = StringUtil.padding(width);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsBlankNullString() {
 boolean expected = true;
 boolean actual = StringUtil.isBlank(null);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsBlankEmptyString() {
 boolean expected = true;
 boolean actual = StringUtil.isBlank("");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsBlankWhitespaceString() {
 boolean expected = true;
 boolean actual = StringUtil.isBlank(" ");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsBlankNonBlankString() {
 boolean expected = false;
 boolean actual = StringUtil.isBlank("HelloWorld");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsNumericValidNumber() {
 boolean expected = true;
 boolean actual = StringUtil.isNumeric("12345");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsNumericInvalidNumber() {
 boolean expected = false;
 boolean actual = StringUtil.isNumeric("abcd");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsBlankAndIsNumeric() {
 String string = "";
 boolean isBlank = StringUtil.isBlank(string);
 boolean isNumeric = StringUtil.isNumeric(string);
 Assert.assertTrue(isBlank || isNumeric);
 }

 @Test
 public void testIsWhitespaceSpace() {
 boolean expected = true;
 boolean actual = StringUtil.isWhitespace(' ');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsWhitespaceTab() {
 boolean expected = true;
 boolean actual = StringUtil.isWhitespace('\t');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsWhitespaceNewLine() {
 boolean expected = true;
 boolean actual = StringUtil.isWhitespace('\n');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsWhitespaceNonWhitespace() {
 boolean expected = false;
 boolean actual = StringUtil.isWhitespace('A');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testNormaliseWhitespaceSingleSpace() {
 String input = " Hello ";
 String expected = "Hello";
 String actual = StringUtil.normaliseWhitespace(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testNormaliseWhitespaceMultipleSpaces() {
 String input = " Hello   World ";
 String expected = "Hello World";
 String actual = StringUtil.normaliseWhitespace(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testNormaliseWhitespaceNewLineTab() {
 String input = "Hello\n\tWorld";
 String expected = "HelloWorld";
 String actual = StringUtil.normaliseWhitespace(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testInSingleString() {
 String haystack = "TestString";
 String needle = "Test";
 boolean expected = true;
 boolean actual = StringUtil.in(needle, haystack);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testInMultipleStrings() {
 String haystack = "TestString1TestString2";
 String needle = "Test";
 boolean expected = true;
 boolean actual = StringUtil.in(needle, haystack.split(" "));
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testInSortedArray() {
 String[] haystack = {"Test1", "Test2", "TestString"};
 String needle = "Test";
 boolean expected = true;
 boolean actual = StringUtil.inSorted(needle, haystack);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testInSortedArrayWithMissingNeedle() {
 String[] haystack = {"Test1", "Test2", "TestString"};
 String needle = "Test3";
 boolean expected = false;
 boolean actual = StringUtil.inSorted(needle, haystack);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testResolveWithAbsoluteBaseUrl() {
 String baseUrl = "http://example.com/base";
 String relUrl = "http://example.com/relative";
 String expected = "http://example.com/relative";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testResolveWithRelativeUrl() {
 String baseUrl = "http://example.com/base";
 String relUrl = "/relative";
 String expected = "http://example.com/relative";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testResolveWithRelativeUrlStartingWithASlash() {
 String baseUrl = "http://example.com/base";
 String relUrl = "/";
 String expected = "";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveWithMalformedBaseUrl() {
 String baseUrl = "htp://example.com/base";
 String relUrl = "relative";
 String expected = "";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 Assert.fail();
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveWithMalformedRelativeUrl() {
 String baseUrl = "http://example.com/base";
 String relUrl = "http://example.com//relative";
 String expected = "";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 Assert.fail();
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveWithMalformedRelativeUrlStartingWithASlash() {
 String baseUrl = "http://example.com/base";
 String relUrl = "/http://example.com/relative";
 String expected = "";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 Assert.fail();
 }

 @Test
 public void testResolveWithInvalidBaseUrlAndValidRelativeUrl() {
 String baseUrl = "htp://example.com/base";
 String relUrl = "relative";
 String expected = "";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, actual);
 }

}