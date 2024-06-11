import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

  @Test
  public void testSmear() {
    int hashCode = 12345;
    int expected = -123456789;
    int actual = SmallCharMatcher.smear(hashCode);
    assertEquals(expected, actual);
  }

  @Test
  public void testCheckFilter() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 5L, true, "testing");
    assertTrue(matcher.checkFilter(0));
  }

  @Test
  public void testChooseTableSize() {
    assertEquals(2, SmallCharMatcher.chooseTableSize(1));
    assertEquals(4, SmallCharMatcher.chooseTableSize(2));
    assertEquals(8, SmallCharMatcher.chooseTableSize(4));
    assertEquals(16, SmallCharMatcher.chooseTableSize(8));
    assertEquals(32, SmallCharMatcher.chooseTableSize(9));
  }

  @Test
  public void testFrom() {
    BitSet chars = new BitSet();
    chars.set(0);
    chars.set(1);
    chars.set(2);
    chars.set(9);
    SmallCharMatcher matcher = SmallCharMatcher.from(chars, "testing");
    assertTrue(matcher.matches('\0'));
    assertTrue(matcher.matches('A'));
    assertFalse(matcher.matches('B'));
    assertTrue(matcher.matches('J'));
    assertFalse(matcher.matches('K'));
  }

  @Test
  public void testMatches() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'A', 'B', 'C'}, 14L, false, "testing");
    assertTrue(matcher.matches('A'));
    assertTrue(matcher.matches('B'));
    assertTrue(matcher.matches('C'));
    assertFalse(matcher.matches('D'));
  }

  @Test
  public void testSetBits() {
    SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'A', 'B', 'C'}, 14L, false, "testing");
    BitSet table = new BitSet();
    table.set(1);
    table.set(2);
    matcher.setBits(table);
    assertFalse(table.get(0));
    assertTrue(table.get(1));
    assertTrue(table.get(2));
    assertFalse(table.get(3));
  }

}