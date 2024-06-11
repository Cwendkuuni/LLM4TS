import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

   @Test
   public void testMatches_withNullChar_returnsFalse() {
      SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test Matcher");
      assertFalse(matcher.matches('\0'));
   }

   @Test
   public void testMatches_withEmptyMatcher_returnsFalse() {
      BitSet chars = new BitSet();
      SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
      assertFalse(matcher.matches('a'));
   }

   @Test
   public void testMatches_withMatcherContainingChar_returnsTrue() {
      BitSet chars = new BitSet();
      chars.set('a');
      SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
      assertTrue(matcher.matches('a'));
   }

   @Test
   public void testMatches_withMatcherNotContainingChar_returnsFalse() {
      BitSet chars = new BitSet();
      chars.set('a');
      SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
      assertFalse(matcher.matches('b'));
   }

   @Test
   public void testMatches_withZeroCharInMatcher_returnsTrue() {
      BitSet chars = new BitSet();
      chars.set('a');
      chars.set(0);
      SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
      assertTrue(matcher.matches('\0'));
   }

   @Test
   public void testSetBits_withEmptyMatcher_setsNoBits() {
      BitSet expected = new BitSet();
      SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test Matcher");
      BitSet actual = new BitSet();
      matcher.setBits(actual);
      assertEquals(expected, actual);
   }

   @Test
   public void testSetBits_withMatcherContainingChars_setsCorrectBits() {
      BitSet chars = new BitSet();
      chars.set('a');
      chars.set('b');
      chars.set('c');
      SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
      BitSet expected = new BitSet();
      expected.set('a');
      expected.set('b');
      expected.set('c');
      BitSet actual = new BitSet();
      matcher.setBits(actual);
      assertEquals(expected, actual);
   }

   @Test
   public void testChooseTableSize_withSetSizeOfOne_returnsTwo() {
      int setSize = 1;
      int expected = 2;
      int actual = SmallCharMatcher.chooseTableSize(setSize);
      assertEquals(expected, actual);
   }

   @Test
   public void testChooseTableSize_withSetSizeGreaterThanOne_returnsExpectedTableSize() {
      int setSize = 5;
      int expected = 8;
      int actual = SmallCharMatcher.chooseTableSize(setSize);
      assertEquals(expected, actual);
   }

   @Test
   public void testSmear_withSmearableInt_returnsSmearValue() {
      int hashCode = 123;
      int expected = -1660678091;
      int actual = SmallCharMatcher.smear(hashCode);
      assertEquals(expected, actual);
   }
}