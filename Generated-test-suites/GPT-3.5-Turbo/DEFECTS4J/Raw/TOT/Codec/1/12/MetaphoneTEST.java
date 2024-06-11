import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {
  
  // Test cases for metaphone() method
  
  @Test
  public void testMetaphone() {
    Metaphone metaphone = new Metaphone();
    
    // Typical use case: testing a word with only consonants
    String result1 = metaphone.metaphone("EXAMPLE");
    assertEquals("EKSMPL", result1);
    
    // Typical use case: testing a word with only vowels
    String result2 = metaphone.metaphone("AOU");
    assertEquals("A", result2);
    
    // Edge case: testing an empty string
    String result3 = metaphone.metaphone("");
    assertEquals("", result3);
    
    // Error scenario: testing a null argument
    String result4 = metaphone.metaphone(null);
    assertEquals("", result4);
  }
  
  // Test cases for encode() method
  
  @Test
  public void testEncode() throws EncoderException {
    Metaphone metaphone = new Metaphone();
    
    // Typical use case: testing a string object to encode
    Object result1 = metaphone.encode("EXAMPLE");
    assertEquals("EKSMPL", result1);
    
    // Error scenario: testing an object that is not a string
    try {
      Object result2 = metaphone.encode(123);
      fail("EncoderException should have been thrown");
    } catch (EncoderException e) {
      // expected exception
    }
  }
  
  // Test cases for isMetaphoneEqual() method
  
  @Test
  public void testIsMetaphoneEqual() {
    Metaphone metaphone = new Metaphone();
    
    // Typical use case: testing two strings with equal metaphones
    boolean result1 = metaphone.isMetaphoneEqual("EXAMPLE", "EGZAMPLE");
    assertTrue(result1);
    
    // Typical use case: testing two strings with different metaphones
    boolean result2 = metaphone.isMetaphoneEqual("EXAMPLE", "EXPAMPLE");
    assertFalse(result2);
    
    // Edge case: testing two empty strings
    boolean result3 = metaphone.isMetaphoneEqual("", "");
    assertTrue(result3);
  }
  
  // Test cases for getMaxCodeLen() method
  
  @Test
  public void testGetMaxCodeLen() {
    Metaphone metaphone = new Metaphone();
    
    // Typical use case: default max code length is 4
    int result1 = metaphone.getMaxCodeLen();
    assertEquals(4, result1);
  }
  
  // Test cases for setMaxCodeLen() method
  
  @Test
  public void testSetMaxCodeLen() {
    Metaphone metaphone = new Metaphone();
    
    // Typical use case: setting a positive max code length
    metaphone.setMaxCodeLen(6);
    int result1 = metaphone.getMaxCodeLen();
    assertEquals(6, result1);
    
    // Error scenario: setting a negative max code length
    metaphone.setMaxCodeLen(-1);
    int result2 = metaphone.getMaxCodeLen();
    assertEquals(0, result2);
  }
}