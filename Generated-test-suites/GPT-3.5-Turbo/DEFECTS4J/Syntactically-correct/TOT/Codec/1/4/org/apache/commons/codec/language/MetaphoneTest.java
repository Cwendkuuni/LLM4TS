import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {
  
  private Metaphone metaphone;
  
  @Before
  public void setUp() {
    metaphone = new Metaphone();
    metaphone.setMaxCodeLen(4);
  }
  
  @Test
  public void testMetaphone() {
    // Typical use case
    assertEquals("SLF", metaphone.metaphone("self"));
    
    // Edge case (single character)
    assertEquals("K", metaphone.metaphone("k"));
    
    // Error scenario (null input)
    assertEquals("", metaphone.metaphone(null));
    
    // Error scenario (empty input)
    assertEquals("", metaphone.metaphone(""));
  }
  
  @Test
  public void testEncode() throws EncoderException {
    // Typical use case
    assertEquals("MK", metaphone.encode("make"));
    
    // Error scenario (non-string input)
    try {
      metaphone.encode(123);
      fail("Expected EncoderException");
    } catch (EncoderException e) {
      // Pass
    }
  }
  
  @Test
  public void testIsMetaphoneEqual() {
    // Typical use case (Metaphones are equal)
    assertTrue(metaphone.isMetaphoneEqual("hello", "hell"));
    
    // Typical use case (Metaphones are not equal)
    assertFalse(metaphone.isMetaphoneEqual("hello", "world"));
  }
  
  @Test
  public void testGetMaxCodeLen() {
    assertEquals(4, metaphone.getMaxCodeLen());
    
    metaphone.setMaxCodeLen(3);
    assertEquals(3, metaphone.getMaxCodeLen());
  }
  
  @Test
  public void testSetMaxCodeLen() {
    metaphone.setMaxCodeLen(5);
    assertEquals(5, metaphone.getMaxCodeLen());
    
    metaphone.setMaxCodeLen(2);
    assertEquals(2, metaphone.getMaxCodeLen());
  }

}