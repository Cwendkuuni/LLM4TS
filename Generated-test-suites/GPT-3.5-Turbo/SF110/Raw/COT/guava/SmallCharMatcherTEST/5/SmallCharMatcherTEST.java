import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {
  
  @Test
  public void testMatches_containsZero() {
    SmallCharMatcher matcher = new SmallCharMatcher(
        new char[] {'a', 'b', 'c'}, 0b101, true, "ContainsZero");
    
    assertTrue(matcher.matches('\0'));
    assertFalse(matcher.matches('a'));
    assertFalse(matcher.matches('b'));
    assertFalse(matcher.matches('c'));
    assertFalse(matcher.matches('d'));
  }
  
  @Test
  public void testMatches_checkFilter() {
    SmallCharMatcher matcher = new SmallCharMatcher(
        new char[] {'a', 'b', 'c'}, 0b101, false, "CheckFilter");
    
    assertTrue(matcher.matches('a'));
    assertFalse(matcher.matches('b'));
    assertTrue(matcher.matches('c'));
    assertFalse(matcher.matches('d'));
  }
  
  @Test
  public void testMatches_table() {
    SmallCharMatcher matcher = new SmallCharMatcher(
        new char[] {'a', 'b', 'c'}, 0b101, true, "Table");
    
    assertFalse(matcher.matches('\0'));
    assertTrue(matcher.matches('a'));
    assertTrue(matcher.matches('b'));
    assertTrue(matcher.matches('c'));
    assertFalse(matcher.matches('d'));
  }
  
  @Test
  public void testMatches_smear() {
    int hashCode = 123456;
    int result = SmallCharMatcher.smear(hashCode);
    assertEquals(240366913, result);
  }
  
  @Test
  public void testChooseTableSize() {
    int setSize = 10;
    int result = SmallCharMatcher.chooseTableSize(setSize);
    assertEquals(16, result);
  }
  
  @Test
  public void testFrom() {
    BitSet chars = new BitSet();
    chars.set(0);
    chars.set(3);
    chars.set(5);
    String description = "From";
    
    CharMatcher matcher = SmallCharMatcher.from(chars, description);
    
    assertTrue(matcher.matches('\0'));
    assertFalse(matcher.matches('a'));
    assertFalse(matcher.matches('b'));
    assertTrue(matcher.matches('c'));
    assertFalse(matcher.matches('d'));
  }
  
  @Test
  public void testSetBits() {
    BitSet table = new BitSet();
    SmallCharMatcher matcher = new SmallCharMatcher(
        new char[] {'a', 'b', 'c'}, 0b101, true, "SetBits");
    
    matcher.setBits(table);
    
    assertEquals(4, table.cardinality());
    assertTrue(table.get(0));
    assertTrue(table.get('a'));
    assertTrue(table.get('b'));
    assertTrue(table.get('c'));
  }
}