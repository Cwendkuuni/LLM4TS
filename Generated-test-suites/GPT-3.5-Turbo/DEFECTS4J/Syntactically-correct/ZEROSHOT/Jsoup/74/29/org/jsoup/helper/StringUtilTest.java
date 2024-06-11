import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

  @Test
  public void testJoinCollection() {
    // Test with empty collection
    Collection<String> strings1 = new ArrayList<>();
    assertEquals("", StringUtil.join(strings1, ","));
    
    // Test with single string in collection
    Collection<String> strings2 = new ArrayList<>();
    strings2.add("Hello");
    assertEquals("Hello", StringUtil.join(strings2, ","));
    
    // Test with multiple strings in collection
    Collection<String> strings3 = new ArrayList<>();
    strings3.add("Hello");
    strings3.add("World");
    strings3.add("!");
    assertEquals("Hello,World,!", StringUtil.join(strings3, ","));
  }
  
  @Test
  public void testJoinIterator() {
    // Test with empty iterator
    Collection<String> strings1 = new ArrayList<>();
    Iterator<String> iterator1 = strings1.iterator();
    assertEquals("", StringUtil.join(iterator1, ","));
    
    // Test with single string in iterator
    Collection<String> strings2 = new ArrayList<>();
    strings2.add("Hello");
    Iterator<String> iterator2 = strings2.iterator();
    assertEquals("Hello", StringUtil.join(iterator2, ","));
    
    // Test with multiple strings in iterator
    Collection<String> strings3 = new ArrayList<>();
    strings3.add("Hello");
    strings3.add("World");
    strings3.add("!");
    Iterator<String> iterator3 = strings3.iterator();
    assertEquals("Hello,World,!", StringUtil.join(iterator3, ","));
  }
  
  @Test
  public void testJoinArray() {
    // Test with empty array
    String[] strings1 = {};
    assertEquals("", StringUtil.join(strings1, ","));
    
    // Test with single string in array
    String[] strings2 = {"Hello"};
    assertEquals("Hello", StringUtil.join(strings2, ","));
    
    // Test with multiple strings in array
    String[] strings3 = {"Hello", "World", "!"};
    assertEquals("Hello,World,!", StringUtil.join(strings3, ","));
  }
  
  @Test
  public void testPadding() {
    // Test with width less than 0
    try {
      StringUtil.padding(-1);
      fail("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // Expected
    }
    
    // Test with width less than padding length
    assertEquals("", StringUtil.padding(0));
    assertEquals(" ", StringUtil.padding(1));
    assertEquals("  ", StringUtil.padding(2));
    assertEquals("   ", StringUtil.padding(3));
    assertEquals("    ", StringUtil.padding(4));
    assertEquals("     ", StringUtil.padding(5));
    
    // Test with width greater than padding length
    assertEquals("          ", StringUtil.padding(10));
    assertEquals("                         ", StringUtil.padding(25));
  }
  
  @Test
  public void testIsBlank() {
    // Test with null string
    assertTrue(StringUtil.isBlank(null));
    
    // Test with empty string
    assertTrue(StringUtil.isBlank(""));
    
    // Test with whitespace string
    assertTrue(StringUtil.isBlank("   "));
    
    // Test with non-blank string
    assertFalse(StringUtil.isBlank("Hello"));
  }
  
  @Test
  public void testIsNumeric() {
    // Test with null string
    assertFalse(StringUtil.isNumeric(null));
    
    // Test with empty string
    assertFalse(StringUtil.isNumeric(""));
    
    // Test with numeric string
    assertTrue(StringUtil.isNumeric("12345"));
    
    // Test with non-numeric string
    assertFalse(StringUtil.isNumeric("Hello"));
    
    // Test with string containing both numeric and non-numeric characters
    assertFalse(StringUtil.isNumeric("12345Hello"));
  }
  
  @Test
  public void testIsWhitespace() {
    // Test with whitespace characters
    assertTrue(StringUtil.isWhitespace(' '));
    assertTrue(StringUtil.isWhitespace('\t'));
    assertTrue(StringUtil.isWhitespace('\n'));
    assertTrue(StringUtil.isWhitespace('\f'));
    assertTrue(StringUtil.isWhitespace('\r'));
    
    // Test with non-whitespace characters
    assertFalse(StringUtil.isWhitespace('a'));
    assertFalse(StringUtil.isWhitespace('1'));
  }
  
  @Test
  public void testIsActuallyWhitespace() {
    // Test with whitespace characters
    assertTrue(StringUtil.isActuallyWhitespace(' '));
    assertTrue(StringUtil.isActuallyWhitespace('\t'));
    assertTrue(StringUtil.isActuallyWhitespace('\n'));
    assertTrue(StringUtil.isActuallyWhitespace('\f'));
    assertTrue(StringUtil.isActuallyWhitespace('\r'));
    assertTrue(StringUtil.isActuallyWhitespace(160));
    
    // Test with non-whitespace characters
    assertFalse(StringUtil.isActuallyWhitespace('a'));
    assertFalse(StringUtil.isActuallyWhitespace('1'));
  }
  
  @Test
  public void testNormaliseWhitespace() {
    // Test with empty string
    assertEquals("", StringUtil.normaliseWhitespace(""));
    
    // Test with whitespace string
    assertEquals(" ", StringUtil.normaliseWhitespace("   "));
    
    // Test with string containing multiple spaces
    assertEquals("Hello World !", StringUtil.normaliseWhitespace("Hello    World      !"));
    
    // Test with string containing newline, tab, and space characters
    assertEquals("Hello World", StringUtil.normaliseWhitespace("Hello\n\tWorld"));
  }
  
  @Test
  public void testIn() {
    // Test with needle not in haystack
    String[] haystack1 = {"Hello", "World", "!"};
    assertFalse(StringUtil.in("Goodbye", haystack1));
    
    // Test with needle in haystack
    String[] haystack2 = {"Hello", "World", "!"};
    assertTrue(StringUtil.in("Hello", haystack2));
  }
  
  @Test
  public void testInSorted() {
    // Test with needle not in haystack
    String[] haystack1 = {"Hello", "World", "!"};
    assertFalse(StringUtil.inSorted("Goodbye", haystack1));
    
    // Test with needle in haystack
    String[] haystack2 = {"!", "Hello", "World"};
    assertTrue(StringUtil.inSorted("Hello", haystack2));
  }
  
  @Test
  public void testResolve() {
    // Test with relative URL and base URL
    try {
      URL base = new URL("http://example.com/");
      String relUrl = "index.html";
      URL resolvedUrl = StringUtil.resolve(base, relUrl);
      assertEquals("http://example.com/index.html", resolvedUrl.toString());
    } catch (MalformedURLException e) {
      fail("MalformedURLException thrown");
    }
    
    // Test with absolute URL and base URL
    try {
      URL base = new URL("http://example.com/");
      String relUrl = "http://example.com/index.html";
      URL resolvedUrl = StringUtil.resolve(base, relUrl);
      assertEquals("http://example.com/index.html", resolvedUrl.toString());
    } catch (MalformedURLException e) {
      fail("MalformedURLException thrown");
    }
    
    // Test with invalid relative URL and base URL
    try {
      URL base = new URL("http://example.com/");
      String relUrl = ":index.html";
      StringUtil.resolve(base, relUrl);
      fail("MalformedURLException not thrown");
    } catch (MalformedURLException e) {
      // Expected
    }
  }
  
  @Test
  public void testResolveWithString() {
    // Test with relative URL and base URL
    String baseUrl = "http://example.com/";
    String relUrl = "index.html";
    String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
    assertEquals("http://example.com/index.html", resolvedUrl);
    
    // Test with absolute URL and base URL
    String baseUrl2 = "http://example.com/";
    String relUrl2 = "http://example.com/index.html";
    String resolvedUrl2 = StringUtil.resolve(baseUrl2, relUrl2);
    assertEquals("http://example.com/index.html", resolvedUrl2);
    
    // Test with invalid relative URL and base URL
    String baseUrl3 = "http://example.com/";
    String relUrl3 = ":index.html";
    String resolvedUrl3 = StringUtil.resolve(baseUrl3, relUrl3);
    assertEquals("", resolvedUrl3);
  }

  @Test
  public void testStringBuilder() {
    // Test creating new StringBuilder
    StringBuilder sb1 = StringUtil.stringBuilder();
    assertNotNull(sb1);
    
    // Test clearing existing StringBuilder
    StringBuilder sb2 = StringUtil.stringBuilder();
    sb2.append("Hello");
    StringBuilder sb3 = StringUtil.stringBuilder();
    assertEquals("", sb3.toString());
  }

}