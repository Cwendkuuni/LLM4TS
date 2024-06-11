import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

  @Test
  public void testMatches_withValidCharacter_returnsTrue() {
    SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{0x1001}), "Test Matcher");
    assertTrue(matcher.matches((char)0x1001));
  }

  @Test
  public void testMatches_withZeroCharacter_returnsContainsZero() {
    SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{0x1001}), "Test Matcher");
    assertTrue(matcher.matches((char)0));
  }

  @Test
  public void testMatches_withInvalidCharacter_returnsFalse() {
    SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{0x1001}), "Test Matcher");
    assertFalse(matcher.matches((char)0x1000));
  }

  @Test
  public void testSetBits_containsZero_setsZeroBit() {
    SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{0x1001}), "Test Matcher");
    BitSet table = new BitSet();
    matcher.setBits(table);
    assertTrue(table.get(0));
  }

  @Test
  public void testSetBits_noZero_doesNotSetZeroBit() {
    SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{0x1000}), "Test Matcher");
    BitSet table = new BitSet();
    matcher.setBits(table);
    assertFalse(table.get(0));
  }
}