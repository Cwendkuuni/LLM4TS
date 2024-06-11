import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MetaphoneTest {
  
  @Test
  public void testMetaphone() {
    Metaphone metaphone = new Metaphone();
    
    // Basic test case
    String input1 = "hello";
    String expected1 = "HL";
    String result1 = metaphone.metaphone(input1);
    assertEquals(expected1, result1);
    
    // Test case with special characters
    String input2 = "ápplè";
    String expected2 = "APL";
    String result2 = metaphone.metaphone(input2);
    assertEquals(expected2, result2);
  }
  
  @Test
  public void testEncode() throws EncoderException {
    Metaphone metaphone = new Metaphone();
    
    // Test case with an object of type String
    String input1 = "hello";
    String expected1 = "HL";
    Object object1 = (Object) input1;
    Object result1 = metaphone.encode(object1);
    assertEquals(expected1, result1);
    
    // Test case with an object of type Integer
    Object object2 = (Object) new Integer(5);
    try {
      Object result2 = metaphone.encode(object2);
    } catch (EncoderException e) {
      assertEquals("Parameter supplied to Metaphone encode is not of type java.lang.String", e.getMessage());
    }
  }
  
  @Test
  public void testIsMetaphoneEqual() {
    Metaphone metaphone = new Metaphone();
    
    // Test case with equal metaphones
    String input1 = "hello";
    String input2 = "hellow";
    boolean expected1 = true;
    boolean result1 = metaphone.isMetaphoneEqual(input1, input2);
    assertEquals(expected1, result1);
    
    // Test case with different metaphones
    String input3 = "hello";
    String input4 = "world";
    boolean expected2 = false;
    boolean result2 = metaphone.isMetaphoneEqual(input3, input4);
    assertEquals(expected2, result2);
  }
  
}