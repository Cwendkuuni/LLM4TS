
import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

  @Test
  public void testMatches() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b111, true, "matcher");
    
    // Test matching characters in the table
    assertTrue(matcher.matches('a'));
    assertTrue(matcher.matches('b'));
    assertTrue(matcher.matches('c'));
    
    // Test non-matching characters
    assertFalse(matcher.matches('d'));
    assertFalse(matcher.matches('e'));
    
    // Test empty matcher
    SmallCharMatcher emptyMatcher = new SmallCharMatcher(new char[] {}, 0b0, false, "emptyMatcher");
    assertFalse(emptyMatcher.matches('a'));
    assertFalse(emptyMatcher.matches('b'));
  }
  
  @Test
  public void testSetBits() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b111, true, "matcher");
    BitSet table = new BitSet();
    matcher.setBits(table);
    
    // Test if table is set correctly
    assertTrue(table.get(0));
    assertTrue(table.get('a'));
    assertTrue(table.get('b'));
    assertTrue(table.get('c'));
    assertFalse(table.get('d'));
  }
}
