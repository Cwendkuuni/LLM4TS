import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {
  
  @Test
  public void testMetaphone() {
    Metaphone metaphone = new Metaphone();
    
    // Typical use case
    String result1 = metaphone.metaphone("apple");
    Assert.assertEquals("APL", result1);
    
    // Edge case: Empty string
    String result2 = metaphone.metaphone("");
    Assert.assertEquals("", result2);
    
    // Edge case: Single character string
    String result3 = metaphone.metaphone("a");
    Assert.assertEquals("S", result3);
    
    // Error scenario: Input with special characters
    String result4 = metaphone.metaphone("apple@");
    Assert.assertEquals("APL", result4);
  }
  
  @Test
  public void testEncode() throws EncoderException {
    Metaphone metaphone = new Metaphone();
    
    // Typical use case
    Object result1 = metaphone.encode("apple");
    Assert.assertEquals("APL", result1);
    
    // Error scenario: Non-string object
    try {
      metaphone.encode(123);
      Assert.fail("EncoderException should have been thrown");
    } catch (EncoderException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testEncodeString() {
    Metaphone metaphone = new Metaphone();
    
    // Typical use case
    String result1 = metaphone.encode("apple");
    Assert.assertEquals("APL", result1);
  }
  
  @Test
  public void testIsMetaphoneEqual() {
    Metaphone metaphone = new Metaphone();
    
    // Typical use case: metaphones are equal
    boolean result1 = metaphone.isMetaphoneEqual("apple", "apl");
    Assert.assertTrue(result1);
    
    // Typical use case: metaphones are not equal
    boolean result2 = metaphone.isMetaphoneEqual("apple", "banana");
    Assert.assertFalse(result2);
  }
  
  @Test
  public void testGetMaxCodeLen() {
    Metaphone metaphone = new Metaphone();
    metaphone.setMaxCodeLen(5);
    
    // Typical use case
    int result1 = metaphone.getMaxCodeLen();
    Assert.assertEquals(5, result1);
  }
  
  @Test
  public void testSetMaxCodeLen() {
    Metaphone metaphone = new Metaphone();
    
    // Typical use case
    metaphone.setMaxCodeLen(5);
    int result1 = metaphone.getMaxCodeLen();
    Assert.assertEquals(5, result1);
  }
}