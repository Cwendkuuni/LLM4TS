import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

public class SplitterTest {
  
  // Test for the method public static Splitter on(char separator)
  @Test
  public void testOnCharSeparator() {
    Splitter splitter = Splitter.on(',');
    assertNotNull(splitter);
  }
  
  // Test for the method public static Splitter on(CharMatcher separatorMatcher)
  @Test
  public void testOnCharMatcherSeparator() {
    Splitter splitter = Splitter.on(CharMatcher.is(':'));
    assertNotNull(splitter);
  }
  
  // Test for the method public static Splitter on(String separator)
  @Test
  public void testOnStringSeparator() {
    Splitter splitter = Splitter.on(";");
    assertNotNull(splitter);
  }

  // More test methods for other public methods should be added here
  
  // Test for edge case where separator is empty string
  @Test
  public void testOnEmptyStringSeparator() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      Splitter.on("");
    });
    assertEquals("The separator may not be the empty string.", exception.getMessage());
  }
  
  // Test for exception handling when pattern matches empty string
  @Test
  public void testOnPatternEmptyString() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      Splitter.on(Pattern.compile(""));
    });
    assertEquals("The pattern may not match the empty string: ", exception.getMessage());
  }
  
  // More edge cases and exception handling scenarios should be tested here
  
  // Test for typical use case of splitting a comma separated string
  @Test
  public void testSplit() {
    Splitter splitter = Splitter.on(',');
    Iterable<String> result = splitter.split("apple,banana,orange");
    List<String> resultList = (List<String>)result;
    
    assertEquals(3, resultList.size());
    assertEquals("apple", resultList.get(0));
    assertEquals("banana", resultList.get(1));
    assertEquals("orange", resultList.get(2));
  }
  
  // Test for typical use case of splitting a string and obtaining a list
  @Test
  public void testSplitToList() {
    Splitter splitter = Splitter.on(',');
    List<String> resultList = splitter.splitToList("dog,cat,bird");
    
    assertEquals(3, resultList.size());
    assertEquals("dog", resultList.get(0));
    assertEquals("cat", resultList.get(1));
    assertEquals("bird", resultList.get(2));
  }
  
  // Test for edge case of splitting an empty string
  @Test
  public void testSplitEmptyString() {
    Splitter splitter = Splitter.on(',');
    Iterable<String> result = splitter.split("");
    List<String> resultList = (List<String>)result;
    
    assertTrue(resultList.isEmpty());
  }
  
  // More typical use cases, edge cases, and error scenarios should be tested here
  
}