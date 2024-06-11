import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {
  
  @Test
  public void testMatches_containsZero() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "");
    assertTrue(matcher.matches('\0'));
  }
  
  @Test
  public void testMatches_notContainsZero() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "");
    assertFalse(matcher.matches('\0'));
  }
  
  @Test
  public void testMatches_checkFilter_true() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 1L, true, "");
    assertTrue(matcher.matches('A'));
  }
  
  @Test
  public void testMatches_checkFilter_false() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "");
    assertFalse(matcher.matches('A'));
  }
  
  @Test
  public void testMatches_tableContainsCharacter() {
    char[] table = new char[1];
    table[0] = 'A';
    SmallCharMatcher matcher = new SmallCharMatcher(table, 0L, true, "");
    assertTrue(matcher.matches('A'));
  }
  
  @Test
  public void testMatches_tableDoesNotContainCharacter() {
    char[] table = new char[1];
    table[0] = 'A';
    SmallCharMatcher matcher = new SmallCharMatcher(table, 0L, true, "");
    assertFalse(matcher.matches('B'));
  }
  
  @Test
  public void testMatches_emptyTable() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "");
    assertFalse(matcher.matches('A'));
  }
  
  @Test
  public void testSetBits_containsZero() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "");
    BitSet expected = new BitSet();
    expected.set(0);
    BitSet actual = new BitSet();
    matcher.setBits(actual);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetBits_doesNotContainZero() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "");
    BitSet expected = new BitSet();
    BitSet actual = new BitSet();
    matcher.setBits(actual);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetBits_tableNotEmpty() {
    char[] table = new char[2];
    table[0] = 'A';
    table[1] = 'B';
    SmallCharMatcher matcher = new SmallCharMatcher(table, 0L, false, "");
    BitSet expected = new BitSet();
    expected.set('A');
    expected.set('B');
    BitSet actual = new BitSet();
    matcher.setBits(actual);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFrom() {
    BitSet chars = new BitSet();
    chars.set('A');
    chars.set('B');
    SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
    assertTrue(matcher.matches('A'));
    assertTrue(matcher.matches('B'));
    assertFalse(matcher.matches('C'));
  }
  
  @Test
  public void testSmear() {
    assertEquals(2070383659, SmallCharMatcher.smear(1234567890));
  }
  
  @Test
  public void testChooseTableSize_setSize1() {
    assertEquals(2, SmallCharMatcher.chooseTableSize(1));
  }
  
  @Test
  public void testChooseTableSize_setSize2() {
    assertEquals(4, SmallCharMatcher.chooseTableSize(2));
  }
  
  @Test
  public void testChooseTableSize_setSize3() {
    assertEquals(4, SmallCharMatcher.chooseTableSize(3));
  }
  
  @Test
  public void testChooseTableSize_setSize8() {
    assertEquals(8, SmallCharMatcher.chooseTableSize(8));
  }
}