import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {
  
  @Test
  public void testMatches_containsZero_returnsTrue() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "description");
    
    boolean result = matcher.matches('\0');
    
    assertTrue(result);
  }
  
  @Test
  public void testMatches_checkFilter_returnsFalse() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "description");
    
    boolean result = matcher.matches('a');
    
    assertFalse(result);
  }
  
  @Test
  public void testMatches_tableContainsCharacter_returnsTrue() {
    char[] table = new char[3];
    table[0] = 'a';
    table[1] = 'b';
    table[2] = 'c';
    SmallCharMatcher matcher = new SmallCharMatcher(table, 0L, false, "description");

    boolean result = matcher.matches('b');
    
    assertTrue(result);
  }
  
  @Test
  public void testMatches_tableDoesNotContainCharacter_returnsFalse() {
    char[] table = new char[3];
    table[0] = 'a';
    table[1] = 'b';
    table[2] = 'c';
    SmallCharMatcher matcher = new SmallCharMatcher(table, 0L, false, "description");

    boolean result = matcher.matches('d');
    
    assertFalse(result);
  }
  
  @Test
  public void testMatches_tableFullWithCharacter_returnsTrue() {
    char[] table = new char[3];
    table[0] = 'a';
    table[1] = 'b';
    table[2] = 'c';
    SmallCharMatcher matcher = new SmallCharMatcher(table, 0L, false, "description");

    boolean result = matcher.matches('a');
    
    assertTrue(result);
  }
  
  @Test
  public void testSetBits_containsZero_setsBitZero() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "description");
    BitSet table = new BitSet();
    
    matcher.setBits(table);
    
    assertTrue(table.get(0));
  }
  
  @Test
  public void testSetBits_tableNotEmpty_setsBits() {
    char[] table = new char[3];
    table[0] = 'a';
    table[1] = 'b';
    table[2] = 'c';
    SmallCharMatcher matcher = new SmallCharMatcher(table, 0L, false, "description");
    BitSet result = new BitSet();
    
    matcher.setBits(result);
    
    assertTrue(result.get('a'));
    assertTrue(result.get('b'));
    assertTrue(result.get('c'));
  }
}