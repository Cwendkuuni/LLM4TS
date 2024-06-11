
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

  //Test the metaphone method with a null input
  @Test
  public void testMetaphoneWithNull() {
    Metaphone metaphone = new Metaphone();
    String result = metaphone.metaphone(null);
    Assert.assertEquals("", result);
  }
  
  //Test the metaphone method with an empty input
  @Test
  public void testMetaphoneWithEmptyInput() {
    Metaphone metaphone = new Metaphone();
    String result = metaphone.metaphone("");
    Assert.assertEquals("", result);
  }
  
  //Test the metaphone method with a single character input
  @Test
  public void testMetaphoneWithSingleCharacterInput() {
    Metaphone metaphone = new Metaphone();
    String result = metaphone.metaphone("a");
    Assert.assertEquals("A", result);
  }
  
  //Test the metaphone method with a word containing multiple characters
  @Test
  public void testMetaphoneWithMultipleCharacters() {
    Metaphone metaphone = new Metaphone();
    String result = metaphone.metaphone("hello");
    Assert.assertEquals("HL", result);
  }
  
  //Test the isMetaphoneEqual method with two identical words
  @Test
  public void testIsMetaphoneEqualWithIdenticalWords() {
    Metaphone metaphone = new Metaphone();
    boolean result = metaphone.isMetaphoneEqual("hello", "hello");
    Assert.assertTrue(result);
  }
  
  //Test the isMetaphoneEqual method with two different words
  @Test
  public void testIsMetaphoneEqualWithDifferentWords() {
    Metaphone metaphone = new Metaphone();
    boolean result = metaphone.isMetaphoneEqual("hello", "world");
    Assert.assertFalse(result);
  }
  
}
