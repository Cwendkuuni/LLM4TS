import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilTest {

  // Test cases for join(Collection strings, String sep)
  @Test
  public void testJoinCollection() {
    List<String> strings = new ArrayList<>();
    strings.add("Hello");
    strings.add("World");
    strings.add("!");

    assertEquals("Hello World !", StringUtil.join(strings, " "));
  }

  // Test cases for join(Iterator strings, String sep)
  @Test
  public void testJoinIterator() {
    List<String> strings = new ArrayList<>();
    strings.add("Hello");
    strings.add("World");
    strings.add("!");

    assertEquals("Hello World !", StringUtil.join(strings.iterator(), " "));
  }

  // Test cases for join(String[] strings, String sep)
  @Test
  public void testJoinArray() {
    String[] strings = {"Hello", "World", "!"};

    assertEquals("Hello World !", StringUtil.join(strings, " "));
  }

  // Test cases for padding(int width)
  @Test
  public void testPadding() {
    assertEquals("        ", StringUtil.padding(8));
    assertEquals("", StringUtil.padding(0));
    assertEquals("", StringUtil.padding(-1));
  }

  // Test cases for isBlank(String string)
  @Test
  public void testIsBlank() {
    assertTrue(StringUtil.isBlank(null));
    assertTrue(StringUtil.isBlank(""));
    assertTrue(StringUtil.isBlank("     "));
    assertFalse(StringUtil.isBlank("Hello"));
  }

  // Test cases for isNumeric(String string)
  @Test
  public void testIsNumeric() {
    assertTrue(StringUtil.isNumeric("12345"));
    assertFalse(StringUtil.isNumeric(null));
    assertFalse(StringUtil.isNumeric(""));
    assertFalse(StringUtil.isNumeric("12345a"));
  }

  // Test cases for isWhitespace(int c)
  @Test
  public void testIsWhitespace() {
    assertTrue(StringUtil.isWhitespace(' '));
    assertTrue(StringUtil.isWhitespace('\t'));
    assertTrue(StringUtil.isWhitespace('\n'));
    assertTrue(StringUtil.isWhitespace('\f'));
    assertTrue(StringUtil.isWhitespace('\r'));

    assertFalse(StringUtil.isWhitespace('a'));
    assertFalse(StringUtil.isWhitespace('1'));
    assertFalse(StringUtil.isWhitespace('?'));
  }

  // Test cases for isActuallyWhitespace(int c)
  @Test
  public void testIsActuallyWhitespace() {
    assertTrue(StringUtil.isActuallyWhitespace(' '));
    assertTrue(StringUtil.isActuallyWhitespace('\t'));
    assertTrue(StringUtil.isActuallyWhitespace('\n'));
    assertTrue(StringUtil.isActuallyWhitespace('\f'));
    assertTrue(StringUtil.isActuallyWhitespace('\r'));
    assertTrue(StringUtil.isActuallyWhitespace(160));

    assertFalse(StringUtil.isActuallyWhitespace('a'));
    assertFalse(StringUtil.isActuallyWhitespace('1'));
    assertFalse(StringUtil.isActuallyWhitespace('?'));
  }

  // Test cases for normaliseWhitespace(String string)
  @Test
  public void testNormaliseWhitespace() {
    assertEquals("Hello World !", StringUtil.normaliseWhitespace("Hello  World  !"));
    assertEquals("", StringUtil.normaliseWhitespace(""));
    assertEquals("", StringUtil.normaliseWhitespace("     "));
    assertEquals("Hello", StringUtil.normaliseWhitespace("Hello"));
  }

  // Test cases for appendNormalisedWhitespace(StringBuilder accum, String string, boolean stripLeading)
  @Test
  public void testAppendNormalisedWhitespace() {
    StringBuilder sb = new StringBuilder();
    StringUtil.appendNormalisedWhitespace(sb, "Hello  World  !", false);
    assertEquals("Hello World !", sb.toString());

    sb = new StringBuilder();
    StringUtil.appendNormalisedWhitespace(sb, "", false);
    assertEquals("", sb.toString());

    sb = new StringBuilder();
    StringUtil.appendNormalisedWhitespace(sb, "     ", false);
    assertEquals("", sb.toString());

    sb = new StringBuilder();
    StringUtil.appendNormalisedWhitespace(sb, "Hello", false);
    assertEquals("Hello", sb.toString());

    sb = new StringBuilder();
    StringUtil.appendNormalisedWhitespace(sb, "  Hello  World  !", true);
    assertEquals("Hello World !", sb.toString());
  }

  // Test cases for in(String needle, String... haystack)
  @Test
  public void testIn() {
    assertTrue(StringUtil.in("Hello", "Hello", "World", "!"));
    assertFalse(StringUtil.in("Hello", "World", "!"));
  }

  // Test cases for inSorted(String needle, String[] haystack)
  @Test
  public void testInSorted() {
    String[] haystack = {"!", "Hello", "World"};
    assertTrue(StringUtil.inSorted("Hello", haystack));
    assertFalse(StringUtil.inSorted("Goodbye", haystack));
  }

  // Test cases for resolve(URL base, String relUrl)
  @Test
  public void testResolveURL() throws MalformedURLException {
    URL base = new URL("http://example.com");
    String relUrl = "./foo";
    URL resolved = StringUtil.resolve(base, relUrl);

    assertEquals("http://example.com/foo", resolved.toString());

    base = new URL("http://example.com/path/");
    relUrl = "?foo";
    resolved = StringUtil.resolve(base, relUrl);
    assertEquals("http://example.com/path/?foo", resolved.toString());
  }

  // Test cases for resolve(String baseUrl, String relUrl)
  @Test
  public void testResolveString() {
    String baseUrl = "http://example.com";
    String relUrl = "./foo";
    String resolved = StringUtil.resolve(baseUrl, relUrl);
    assertEquals("http://example.com/foo", resolved);

    baseUrl = "http://example.com/path/";
    relUrl = "?foo";
    resolved = StringUtil.resolve(baseUrl, relUrl);
    assertEquals("http://example.com/path/?foo", resolved);

    baseUrl = "invalid-url";
    relUrl = "https://example.com";
    resolved = StringUtil.resolve(baseUrl, relUrl);
    assertEquals("", resolved);
  }

  // Test cases for stringBuilder()
  @Test
  public void testStringBuilder() {
    StringBuilder sb1 = StringUtil.stringBuilder();
    StringBuilder sb2 = StringUtil.stringBuilder();

    assertNotSame(sb1, sb2); // Each thread should have its own StringBuilder instance
    assertEquals(0, sb1.length()); // The StringBuilder instance should be empty
    assertEquals(0, sb2.length()); // The StringBuilder instance should be empty
  }

}