import org.junit.Assert;import org.junit.Test;import java.util.Arrays;import static org.jsoup.helper.StringUtil.*;

public class StringUtilTest {

 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("apple", "banana", "cherry");
 String expected = "apple, banana, cherry";
 String result = join(strings, ", ");
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testJoinCollectionEmpty() {
 Collection<String> strings = Arrays.asList();
 String result = join(strings, ", ");
 Assert.assertEquals("", result);
 }

 @Test
 public void testJoinIterator() {
 Iterator<String> strings = Arrays.asList("apple", "banana", "cherry").iterator();
 String expected = "apple, banana, cherry";
 String result = join(strings, ", ");
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testJoinIteratorEmpty() {
 Iterator<String> strings = Arrays.asList().iterator();
 String result = join(strings, ", ");
 Assert.assertEquals("", result);
 }

 @Test
 public void testJoinArray() {
 String[] strings = {"apple", "banana", "cherry"};
 String expected = "apple, banana, cherry";
 String result = join(strings, ", ");
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testJoinArrayEmpty() {
 String[] strings = {};
 String result = join(strings, ", ");
 Assert.assertEquals("", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPaddingNegativeSize() {
 int width = -10;
 String result = padding(width);
 }

 @Test
 public void testIsBlank() {
 String string1 = " ";
 String string2 = "";
 String string3 = "   ";
 String string4 = null;

 Assert.assertTrue(isBlank(string2));
 Assert.assertTrue(isBlank(string3));
 Assert.assertTrue(isBlank(string4));
 Assert.assertFalse(isBlank(string1));
 }

 @Test
 public void testIsNumeric() {
 String string1 = "12345";
 String string2 = "123";
 String string3 = "123a";
 String string4 = "";
 String string5 = "abc";

 Assert.assertTrue(isNumeric(string1));
 Assert.assertTrue(isNumeric(string2));
 Assert.assertFalse(isNumeric(string3));
 Assert.assertFalse(isNumeric(string4));
 Assert.assertFalse(isNumeric(string5));
 }

 @Test
 public void testIsWhitespace() {
 int space = ' ';
 int tab = '\t';
 int cr = '\r';
 int lf = '\n';
 int nbsp = 160;

 Assert.assertTrue(isWhitespace(space));
 Assert.assertTrue(isWhitespace(tab));
 Assert.assertTrue(isWhitespace(cr));
 Assert.assertTrue(isWhitespace(lf));
 Assert.assertTrue(isWhitespace(nbsp));
 }

 @Test
 public void testIsActuallyWhitespace() {
 int space = ' ';
 int tab = '\t';
 int cr = '\r';
 int lf = '\n';
 int nbsp = 160;

 Assert.assertTrue(isActuallyWhitespace(space));
 Assert.assertTrue(isActuallyWhitespace(tab));
 Assert.assertTrue(isActuallyWhitespace(cr));
 Assert.assertTrue(isActuallyWhitespace(lf));
 Assert.assertTrue(isActuallyWhitespace(nbsp));
 }

 @Test
 public void testNormaliseWhitespaceBasic() {
 String string1 = "   apple   ";
 String expected = "apple";
 String result = normaliseWhitespace(string1);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespaceMultiSpaces() {
 String string1 = " apple \t\n  apple";
 String expected = "apple";
 String result = normaliseWhitespace(string1);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespaceNewLines() {
 String string1 = " apple\napple";
 String expected = "apple";
 String result = normaliseWhitespace(string1);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testNormaliseWhitespaceTab() {
 String string1 = " apple\tapple";
 String expected = "apple";
 String result = normaliseWhitespace(string1);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIn() {
 String needle = "apple";
 String[] haystack = {"apple", "banana", "cherry", "apple"};
 boolean expected = true;
 boolean result = in(needle, haystack);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testInEmptyArray() {
 String needle = "apple";
 String[] haystack = {};
 boolean result = in(needle, haystack);
 Assert.assertFalse(result);
 }

 @Test
 public void testInEmptyString() {
 String needle = "";
 String[] haystack = {"apple", "banana", "cherry"};
 boolean result = in(needle, haystack);
 Assert.assertFalse(result);
 }

 @Test
 public void testInSingleElementArray() {
 String needle = "apple";
 String[] haystack = {"apple"};
 boolean expected = true;
 boolean result = in(needle, haystack);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testInSorted() {
 String needle = "apple";
 String[] haystack = {"apple", "banana", "cherry"};
 boolean expected = true;
 boolean result = inSorted(needle, haystack);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseNull() {
 String baseUrl = null;
 String relUrl = "http://example.com/relative";
 String expected = "http://example.com/relative";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseEmpty() {
 String baseUrl = "";
 String relUrl = "http://example.com/relative";
 String expected = "http://example.com/relative";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseOnlyProtocol() {
 String baseUrl = "http:";
 String relUrl = "//example.com/relative";
 String expected = "http://example.com/relative";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseAbsolute() {
 String baseUrl = "http://example.com";
 String relUrl = "/path/to/file.html";
 String expected = "http://example.com/path/to/file.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveBaseAbsoluteWithFragment() {
 String baseUrl = "http://example.com#fragment";
 String relUrl = "/path/to/file.html";
 String expected = "http://example.com/path/to/file.html#fragment";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveRelativeOnlyFragment() {
 String baseUrl = "http://example.com";
 String relUrl = "#fragment";
 String expected = "http://example.com#fragment";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveRelative() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "relative.html";
 String expected = "http://example.com/relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveRelativeWithFragment() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "relative.html#fragment";
 String expected = "http://example.com/relative.html#fragment";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveRelativeStartsWithSlash() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "/relative.html";
 String expected = "http://example.com/relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveRelativeStartsWithTwoSlashes() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "//relative.html";
 String expected = "http://relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveRelativeWithLeadingDot() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "./relative.html";
 String expected = "http://example.com/relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveRelativeWithTwoLeadingDots() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "../relative.html";
 String expected = "http://example.com/relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveRelativeWithInvalidProtocol() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "//example:com/relative.html";
 String result = resolve(baseUrl, relUrl);
 }

 @Test
 public void testResolveRelativeWithValidProtocolButDifferentHost() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "//anotherhost.com/relative.html";
 String expected = "http://anotherhost.com/relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveRelativeWithDifferentPort() {
 String baseUrl = "http://example.com:8080/path/to/file.html";
 String relUrl = "http://example.com:80/relative.html";
 String expected = "http://example.com:80/relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveRelativeWithDifferentPortButSameHost() {
 String baseUrl = "http://example.com:8080/path/to/file.html";
 String relUrl = "http://example.com:80/relative.html";
 String expected = "http://example.com:80/relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveRelativeWithRelativeUrlFromAnotherScheme() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "mailto:user@example.com";
 String result = resolve(baseUrl, relUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveRelativeWithFragment() {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "relative.html";
 String result = resolve(baseUrl, relUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveRelativeWithFragmentAndFragmentInBase() {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "relative.html#fragment2";
 String result = resolve(baseUrl, relUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveRelativeWithRelativeUrlAndFragmentInBase() {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "relative.html";
 String result = resolve(baseUrl, relUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveRelativeWithRelativeUrlAndFragmentInBaseAndRelativeUrlHasFragment() {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "relative.html#fragment2";
 String result = resolve(baseUrl, relUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveRelativeWithRelativeUrlAndFragmentInBaseAndRelativeUrlHasNoFragment() {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "relative.html";
 String result = resolve(baseUrl, relUrl);
 }

 @Test
 public void testResolveRelativeWithRelativeUrlAndFragmentInBaseAndRelativeUrlHasNoFragmentButBaseHasNoFragment() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "relative.html";
 String expected = "http://example.com/relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveAbsoluteUrlWithFragment() {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "http://example.com/path/to/file.html#fragment2";
 String expected = "http://example.com/path/to/file.html#fragment2";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveAbsoluteUrlWithFragmentAndFragmentInBase() {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "http://example.com/path/to/file.html#fragment2";
 String expected = "http://example.com/path/to/file.html#fragment2";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveAbsoluteUrlWithRelativeUrlAndFragmentInBase() {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "relative.html";
 String expected = "http://example.com/relative.html#fragment";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveAbsoluteUrlWithRelativeUrlAndFragmentInBaseAndRelativeUrlHasFragment() {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "relative.html#fragment2";
 String expected = "http://example.com/relative.html#fragment2";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveAbsoluteUrlWithRelativeUrlAndNoFragmentInBase() {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "relative.html";
 String expected = "http://example.com/relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testResolveAbsoluteUrlWithRelativeUrlAndNoFragmentInBaseButBaseHasNoFragment() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "relative.html";
 String expected = "http://example.com/relative.html";
 String result = resolve(baseUrl, relUrl);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testStringBuilderCached() {
 StringBuilder sb1 = StringUtil.stringBuilder();
 StringBuilder sb2 = StringUtil.stringBuilder();
 StringBuilder sb3 = StringUtil.stringBuilder();
 StringBuilder sb4 = StringUtil.stringBuilder();

 // Test that different threads can safely access the same StringBuilder instance
 sb1.append("Thread 1");
 sb2.append("Thread 2");
 String result1 = sb1.toString();
 String result2 = sb2.toString();
 Assert.assertEquals("Thread 1Thread 2", result1 + result2);
 }
}