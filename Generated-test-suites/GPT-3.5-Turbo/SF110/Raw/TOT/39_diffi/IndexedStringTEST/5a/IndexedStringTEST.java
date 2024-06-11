import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {
  
  @Test
  public void testCount_NormalCase() {
    IndexedString indexedString = new IndexedString("stackoverflow");
    int result = indexedString.count('o');
    assertEquals(2, result);
  }
  
  @Test
  public void testCount_EmptyString() {
    IndexedString indexedString = new IndexedString("");
    int result = indexedString.count('o');
    assertEquals(0, result);
  }
  
  @Test
  public void testCount_NoOccurrences() {
    IndexedString indexedString = new IndexedString("stackoverflow");
    int result = indexedString.count('z');
    assertEquals(0, result);
  }
  
  @Test
  public void testIndexOf_NormalCase() {
    IndexedString indexedString = new IndexedString("stackoverflow");
    int result = indexedString.indexOf("erf".toCharArray());
    assertEquals(4, result);
  }
  
  @Test
  public void testIndexOf_MissingCharacter() {
    IndexedString indexedString = new IndexedString("stackoverflow");
    int result = indexedString.indexOf("z".toCharArray());
    assertEquals(-1, result);
  }
  
  @Test
  public void testIndexOf_EmptyString() {
    IndexedString indexedString = new IndexedString("");
    int result = indexedString.indexOf("erf".toCharArray());
    assertEquals(-1, result);
  }
  
  @Test
  public void testIndexOf_MatchingStringAtStart() {
    IndexedString indexedString = new IndexedString("stackoverflow");
    int result = indexedString.indexOf("stack".toCharArray());
    assertEquals(0, result);
  }
  
  @Test
  public void testIndexOf_MatchingStringAtEnd() {
    IndexedString indexedString = new IndexedString("stackoverflow");
    int result = indexedString.indexOf("flow".toCharArray());
    assertEquals(9, result);
  }
  
  @Test
  public void testIndexOf_LargeText() {
    String input = generateLargeText();
    IndexedString indexedString = new IndexedString(input);
    int result = indexedString.indexOf("index".toCharArray());
    assertEquals(200, result);
  }
  
  private String generateLargeText() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 1000; i++) {
      sb.append("text");
    }
    sb.append("index");
    return sb.toString();
  }
}