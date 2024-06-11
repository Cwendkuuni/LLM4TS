import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;

public class StringUtilTest {

 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("hello", "world", "junit");
 String expected = "helloworldjunit";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinIteratorEmpty() {
 Iterator<String> strings = Collections.emptyIterator();
 String result = StringUtil.join(strings, "");
 assertEquals("", result);
 }

 @Test
 public void testJoinIteratorOneElement() {
 Iterator<String> strings = Arrays.asList("hello").iterator();
 String expected = "hello";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinIteratorMultipleElements() {
 Iterator<String> strings = Arrays.asList("hello", "world", "junit").iterator();
 String expected = "helloworldjunit";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinArrayEmpty() {
 String[] strings = {};
 String result = StringUtil.join(strings, "");
 assertEquals("", result);
 }

 @Test
 public void testJoinArrayOneElement() {
 String[] strings = {"hello"};
 String expected = "hello";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinArrayMultipleElements() {
 String[] strings = {"hello", "world", "junit"};
 String expected = "helloworldjunit";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinArrayWithNull() {
 String[] strings = {"hello", null, "junit"};
 String result = StringUtil.join(strings, "");
 String expected = "hellojunit"; // null should be removed
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
 String string = "   ";
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlankNonBlank() {
 String string = "hello";
 boolean result = StringUtil.isBlank(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumericTrue() {
 String string = "123";
 boolean result = StringUtil.isNumeric(string);
 assertTrue(result);
 }

 @Test
 public void testIsNumericFalseEmpty() {
 String string = "";
 boolean result = StringUtil.isNumeric(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumericFalseNonDigit() {
 String string = "hello";
 boolean result = StringUtil.isNumeric(string);
 assertFalse(result);
 }

 @Test
 public void testIsWhitespaceSpace() {
 int codePoint = ' ';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceTab() {
 int codePoint = '\t';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceNewLine() {
 int codePoint = '\n';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceFormFeed() {
 int codePoint = '\f';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceCarriageReturn() {
 int codePoint = '\r';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceSpace() {
 int codePoint = ' ';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceTab() {
 int codePoint = '\t';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceNewLine() {
 int codePoint = '\n';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceFormFeed() {
 int codePoint = '\f';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceCarriageReturn() {
 int codePoint = '\r';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceNonBreakingSpace() {
 int codePoint = ' ';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testNormaliseWhitespaceEmpty() {
 String string = "";
 String expected = "";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespaceSingleSpace() {
 String string = " ";
 String expected = " ";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespaceMultipleSpaces() {
 String string = "   ";
 String expected = " ";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespaceTab() {
 String string = "\t";
 String expected = " ";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespaceNewLine() {
 String string = "\n";
 String expected = " ";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespaceFormFeed() {
 String string = "\f";
 String expected = " ";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespaceCarriageReturn() {
 String string = "\r";
 String expected = " ";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespaceNonBreakingSpace() {
 String string = " ";
 String expected = " ";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals(expected, result);
 }

 @Test
 public void testInNullNeedle() {
 String[] haystack = {};
 boolean result = StringUtil.in(null, haystack);
 assertFalse(result);
 }

 @Test
 public void testInEmptyNeedle() {
 String[] haystack = {"hello"};
 boolean result = StringUtil.in( "", haystack);
 assertFalse(result);
 }

 @Test
 public void testInMatch() {
 String[] haystack = {"hello", "world", "junit"};
 boolean result = StringUtil.in( "hello", haystack);
 assertTrue(result);
 }

 @Test
 public void testInMismatch() {
 String[] haystack = {"hello", "world", "junit"};
 boolean result = StringUtil.in( "junit", haystack);
 assertFalse(result);
 }

 @Test
 public void testInSortedNullNeedle() {
 String[] haystack = {};
 boolean result = StringUtil.inSorted(null, haystack);
 assertFalse(result);
 }

 @Test
 public void testInSortedEmptyNeedle() {
 String[] haystack = {"hello"};
 boolean result = StringUtil.inSorted( "", haystack);
 assertFalse(result);
 }

 @Test
 public void testInSortedMatch() {
 String[] haystack = {"hello", "world", "junit"};
 boolean result = StringUtil.inSorted( "hello", haystack);
 assertTrue(result);
 }

 @Test
 public void testInSortedMismatch() {
 String[] haystack = {"hello", "world", "junit"};
 boolean result = StringUtil.inSorted( "junit", haystack);
 assertFalse(result);
 }

 @Test
 public void testInSortedMatchAtBeginning() {
 String[] haystack = {"hello", "world", "junit"};
 boolean result = StringUtil.inSorted( "hello", haystack);
 assertTrue(result);
 }

 @Test
 public void testInSortedMatchAtEnd() {
 String[] haystack = {"hello", "world", "junit"};
 boolean result = StringUtil.inSorted( "junit", haystack);
 assertFalse(result);
 }

 @Test
 public void testResolveBaseNotNullRelNull() throws MalformedURLException {
 String baseUrl = "https://example.com/";
 String relUrl = null;
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNotNullRelAbsolute() throws MalformedURLException {
 String baseUrl = "https://example.com/";
 String relUrl = "https://example.com/about";
 String expected = "https://example.com/about";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNotNullRelRelative() throws MalformedURLException {
 String baseUrl = "https://example.com/";
 String relUrl = "/about";
 String expected = "https://example.com/about";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNullRelNotNull() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "https://example.com/about";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNullRelEmpty() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNullRelRelative() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "/about";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNotNullRelRelativeWithTrailingSlash() throws MalformedURLException {
 String baseUrl = "https://example.com/";
 String relUrl = "/about/";
 String expected = "https://example.com/about/";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNotNullRelRelativeWithLeadingSlash() throws MalformedURLException {
 String baseUrl = "https://example.com/";
 String relUrl = "/about";
 String expected = "https://example.com/about";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNotNullRelRelativeWithDoubleSlash() throws MalformedURLException {
 String baseUrl = "https://example.com/";
 String relUrl = "../about";
 String expected = "https://example.com/about";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNotNullRelRelativeWithDoubleSlashAtBeginning() throws MalformedURLException {
 String baseUrl = "https://example.com/";
 String relUrl = "../about";
 String expected = "https://example.com/about";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNullRelRelativeWithTrailingSlash() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "/about/";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNullRelRelativeWithLeadingSlash() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "/about";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNullRelRelativeWithDoubleSlash() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "../about";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNullRelRelativeWithDoubleSlashAtBeginning() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "../about";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveBaseNotNullRelRelativeWithDoubleSlashToParentOfParent() throws MalformedURLException {
 String baseUrl = "https://example.com/";
 String relUrl = "../../../about";
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveBaseNotNullRelRelativeWithDoubleSlashToParent() throws MalformedURLException {
 String baseUrl = "https://example.com/";
 String relUrl = "../../about";
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveBaseNotNullRelRelativeWithDoubleSlashToSelf() throws MalformedURLException {
 String baseUrl = "https://example.com/";
 String relUrl = "../about";
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResolveBaseNullRelAbsolute() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "https://example.com/about";
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResolveBaseNullRelRelativeWithTrailingSlash() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "/about/";
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResolveBaseNullRelRelativeWithLeadingSlash() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "/about";
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResolveBaseNullRelRelativeWithDoubleSlash() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "../about";
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResolveBaseNullRelRelativeWithDoubleSlashAtBeginning() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "../about";
 }

 @Test
 public void testStringBuilder() {
 StringBuilder sb = StringUtil.stringBuilder();
 assertTrue(sb.capacity() > 0);
 }

 @Test
 public void testStringBuilderLimit() {
 StringBuilder sb = StringUtil.stringBuilder();
 sb.setLength(MaxCachedBuilderSize);
 assertTrue(sb.length() > 0);
 assertTrue(sb.capacity() >= MaxCachedBuilderSize);
 }
}