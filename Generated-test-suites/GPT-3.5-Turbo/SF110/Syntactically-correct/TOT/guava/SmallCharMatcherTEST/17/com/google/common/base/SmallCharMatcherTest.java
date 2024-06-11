import org.junit.Before;
import org.junit.Test;
import java.util.BitSet;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {
  
  private SmallCharMatcher smallCharMatcher;
  
  @Before
  public void setUp() {
    BitSet chars = new BitSet();
    chars.set(50);
    chars.set(100);
    chars.set(150);
    String description = "Test Description";
    
    smallCharMatcher = SmallCharMatcher.from(chars, description);
  }
  
  @Test
  public void testMatches_WhenCharacterMatches_ReturnTrue() {
    assertTrue(smallCharMatcher.matches((char) 50)); // Typical use case, character is in the set
    assertTrue(smallCharMatcher.matches((char) 100)); // Typical use case, character is in the set
  }
  
  @Test
  public void testMatches_WhenCharacterDoesNotMatch_ReturnFalse() {
    assertFalse(smallCharMatcher.matches((char) 90)); // Typical use case, character is not in the set
    assertFalse(smallCharMatcher.matches((char) 0)); // Edge case, character is '\0'
    assertFalse(smallCharMatcher.matches((char) 160)); // Edge case, character is larger than MAX_SIZE
  }
  
  @Test
  public void testSetBits_WhenContainsZero_ShouldSetBitAtIndexZero() {
    BitSet expected = new BitSet();
    expected.set(0);
    
    BitSet table = new BitSet();
    smallCharMatcher.setBits(table);
    
    assertEquals(expected, table);
  }
  
  @Test
  public void testSetBits_WhenCharacterSet_ShouldSetBitsAtAssociatedIndexes() {
    BitSet expected = new BitSet();
    expected.set(50);
    expected.set(100);
    expected.set(150);
    
    BitSet table = new BitSet();
    smallCharMatcher.setBits(table);
    
    assertEquals(expected, table);
  }
}