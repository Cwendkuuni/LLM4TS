package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

 @Test
 public void testJoinCollection() {
 String[] strings = new String[]{"one", "two", "three"};
 String sep = ",";
 String expected = "one," + sep + "two," + sep + "three";
 String result = StringUtil.join(Arrays.asList(strings), sep);
 assertEquals(expected, result);

 List<String> list = Collections.singletonList("four");
 result = StringUtil.join(list.iterator(), sep);
 assertEquals("four", result);
 }

 @Test(expected = IllegalStateException.class)
 public void testJoinNullCollection() {
 Iterator<String> nullIterator = null;
 String sep = ",";
 String result = StringUtil.join(nullIterator, sep);
 }

 @Test
 public void testJoinArray() {
 String[] strings = new String[]{"one", "two", "three"};
 String sep = ",";
 String expected = "one," + sep + "two," + sep + "three";
 String result = StringUtil.join(strings, sep);
 assertEquals(expected, result);
 }

 @Test
 public void testPadding() {
 assertEquals("", StringUtil.padding(0));
 assertEquals(" ", StringUtil.padding(1));
 assertEquals("  ", StringUtil.padding(2));
 assertEquals("   ", StringUtil.padding(3));
 assertEquals("    ", StringUtil.padding(4));
 assertEquals("     ", StringUtil.padding(5));
 assertEquals("      ", StringUtil.padding(6));
 assertEquals("       ", StringUtil.padding(7));
 assertEquals("        ", StringUtil.padding(8));
 assertEquals("         ", StringUtil.padding(9));
 assertEquals("          ", StringUtil.padding(10));
 assertEquals("           ", StringUtil.padding(11));
 assertEquals("            ", StringUtil.padding(12));
 assertEquals("             ", StringUtil.padding(13));
 assertEquals("              ", StringUtil.padding(14));
 assertEquals("               ", StringUtil.padding(15));
 assertEquals("                ", StringUtil.padding(16));
 assertEquals("                 ", StringUtil.padding(17));
 assertEquals("                  ", StringUtil.padding(18));
 assertEquals("                 ", StringUtil.padding(19));
 assertEquals("                  ", StringUtil.padding(20));
 assertNull(StringUtil.padding(-1));
 }

 @Test
 public void testIsBlank() {
 assertFalse(StringUtil.isBlank(null));
 assertFalse(StringUtil.isBlank(""));
 assertFalse(StringUtil.isBlank(" "));
 assertFalse(StringUtil.isBlank(" \t\r\n"));
 assertFalse(StringUtil.isBlank("abc"));
 assertFalse(StringUtil.isBlank("abc def"));
 assertTrue(StringUtil.isBlank("   "));
 assertTrue(StringUtil.isBlank("   "));
 assertTrue(StringUtil.isBlank("   "));
 }

 @Test
 public void testIsNumeric() {
 assertFalse(StringUtil.isNumeric(null));
 assertFalse(StringUtil.isNumeric(""));
 assertFalse(StringUtil.isNumeric("abc"));
 assertFalse(StringUtil.isNumeric("123"));
 assertTrue(StringUtil.isNumeric("123"));
 assertTrue(StringUtil.isNumeric("1234"));
 assertTrue(StringUtil.isNumeric("12345"));
 }

 @Test
 public void testIsWhitespace() {
 assertFalse(StringUtil.isWhitespace('a'));
 assertFalse(StringUtil.isWhitespace('b'));
 assertFalse(StringUtil.isWhitespace('c'));
 assertTrue(StringUtil.isWhitespace(' '));
 assertTrue(StringUtil.isWhitespace('\t'));
 assertTrue(StringUtil.isWhitespace('\n'));
 assertTrue(StringUtil.isWhitespace('\r'));
 assertFalse(StringUtil.isWhitespace('\f'));
 }

 @Test
 public void testIsActuallyWhitespace() {
 assertFalse(StringUtil.isActuallyWhitespace('a'));
 assertFalse(StringUtil.isActuallyWhitespace('b'));
 assertFalse(StringUtil.isActuallyWhitespace('c'));
 assertTrue(StringUtil.isActuallyWhitespace(' '));
 assertTrue(StringUtil.isActuallyWhitespace('\t'));
 assertTrue(StringUtil.isActuallyWhitespace('\n'));
 assertTrue(StringUtil.isActuallyWhitespace('\r'));
 assertTrue(StringUtil.isActuallyWhitespace('\f'));
 assertTrue(StringUtil.isActuallyWhitespace(0x00A0));
 }

 @Test
 public void testNormaliseWhitespace() {
 String input = "abc def\t ghi\n jkl mno\r pqr\t stu";
 String expected = "abc def ghi jkl mno pqr stu";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals(expected, result);
 }

 @Test
 public void testIn() {
 String haystack = "one,two,three,four";
 String needle1 = "one";
 String needle2 = "four";
 String needle3 = "five";
 assertTrue(StringUtil.in(needle1, haystack.split(",")));
 assertTrue(StringUtil.in(needle2, haystack.split(",")));
 assertFalse(StringUtil.in(needle3, haystack.split(",")));
 }

 @Test
 public void testInSorted() {
 String[] haystack = {"one", "one", "two", "three", "four"};
 String needle = "one";
 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 @Test
 public void testResolve() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl1 = "path/file.html";
 String relUrl2 = "foo.html";
 String relUrl3 = ".";
 String relUrl4 = "../path/file.html";

 URL expectedUrl1 = new URL("http://example.com/path/file.html");
 URL expectedUrl2 = new URL("http://example.com/foo.html");
 URL expectedUrl3 = new URL("http://example.com/");
 URL expectedUrl4 = new URL("http://example.com/path/file.html");

 assertEquals(expectedUrl1.toString(), StringUtil.resolve(new URL(baseUrl), relUrl1).toString());
 assertEquals(expectedUrl2.toString(), StringUtil.resolve(new URL(baseUrl), relUrl2).toString());
 assertEquals(expectedUrl3.toString(), StringUtil.resolve(new URL(baseUrl), relUrl3).toString());
 assertEquals(expectedUrl4.toString(), StringUtil.resolve(new URL(baseUrl), relUrl4).toString());
 }

 @Test
 public void testResolveEmptyBaseUrl() throws MalformedURLException {
 String relUrl1 = "http://example.com/path/file.html";
 String relUrl2 = "foo.html";
 String relUrl3 = ".";
 String relUrl4 = "../path/file.html";

 URL expectedUrl1 = new URL("http://example.com/path/file.html");
 URL expectedUrl2 = new URL("http://example.com/foo.html");
 URL expectedUrl3 = new URL("http://example.com/");
 URL expectedUrl4 = new URL("http://example.com/path/file.html");

 assertEquals(expectedUrl1.toString(), StringUtil.resolve(null, relUrl1).toString());
 assertEquals(expectedUrl2.toString(), StringUtil.resolve(null, relUrl2).toString());
 assertEquals(expectedUrl3.toString(), StringUtil.resolve(null, relUrl3).toString());
 assertEquals(expectedUrl4.toString(), StringUtil.resolve(null, relUrl4).toString());
 }

 @Test
 public void testStringBuilder() {
 StringBuilder sb = StringUtil.stringBuilder();
 assertSame(sb, StringUtil.stringBuilder());
 assertSame(sb, StringUtil.stringBuilder());

 sb.append("abc");
 sb.append("def");
 assertEquals("abcdef", sb.toString());
 }
}