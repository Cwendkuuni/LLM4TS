import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {
  
  // Test case for the count method
  @Test
  public void testCount() {
    IndexedString indexedString = new IndexedString("aabbc");
    
    // Testing count of a character with multiple occurrences
    assertEquals(2, indexedString.count('a'));
    
    // Testing count of a character with single occurrence
    assertEquals(1, indexedString.count('b'));
    
    // Testing count of a character that does not exist in the string
    assertEquals(0, indexedString.count('z'));
  }
  
  // Test case for the constructor
  @Test
  public void testConstructor() {
    // Testing a basic string
    IndexedString indexedString1 = new IndexedString("abc");
    assertNotNull(indexedString1);
    
    // Testing an empty string
    IndexedString indexedString2 = new IndexedString("");
    assertNotNull(indexedString2);
  }
  
  // Test case for the indexOf method
  @Test
  public void testIndexOf() {
    IndexedString indexedString = new IndexedString("aabbc");
    
    // Testing the index of a target string with multiple occurrences
    char[] target1 = {'a', 'b'};
    assertEquals(1, indexedString.indexOf(target1));
    
    // Testing the index of a target string with single occurrence
    char[] target2 = {'b', 'c'};
    assertEquals(3, indexedString.indexOf(target2));
    
    // Testing the index of a target string that does not exist in the string
    char[] target3 = {'c', 'd'};
    assertEquals(-1, indexedString.indexOf(target3));
  }
  
  // Additional test cases for edge cases and exception handling scenarios
  
  // Testing the ctor with null string input
  @Test(expected = NullPointerException.class)
  public void testConstructorWithNullInput() {
    IndexedString indexedString = new IndexedString(null);
  }
  
  // Testing the indexOf method with null target input
  @Test(expected = NullPointerException.class)
  public void testIndexOfWithNullTarget() {
    IndexedString indexedString = new IndexedString("abc");
    char[] target = null;
    indexedString.indexOf(target);
  }
  
  // Testing the indexOf method with target string longer than source string
  @Test
  public void testIndexOfWithLongerTarget() {
    IndexedString indexedString = new IndexedString("abc");
    char[] target = {'a', 'b', 'c', 'd'};
    assertEquals(-1, indexedString.indexOf(target));
  }
  
  // Testing the indexOf method with empty target input
  @Test
  public void testIndexOfWithEmptyTarget() {
    IndexedString indexedString = new IndexedString("abc");
    char[] target = {};
    assertEquals(-1, indexedString.indexOf(target));
  }
  
  // Testing the indexOf method with all occurrences at the end of the string
  @Test
  public void testIndexOfWithOccurrencesAtEnd() {
    IndexedString indexedString = new IndexedString("abcabc");
    char[] target = {'c', 'a', 'b'};
    assertEquals(3, indexedString.indexOf(target));
  }
  
}