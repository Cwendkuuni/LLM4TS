import com.google.common.base.CharMatcher;
import org.junit.Assert;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_WithZeroCharacter_ReturnsTrue() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{}, 0L, true, "description");
        boolean result = smallCharMatcher.matches('\0');
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_WithNonZeroCharacterNotInFilter_ReturnsFalse() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{}, 1L, false, "description");
        boolean result = smallCharMatcher.matches('c');
        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_WithNonZeroCharacterInFilter_ReturnsTrue() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'c'}, 1L, false, "description");
        boolean result = smallCharMatcher.matches('c');
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_WithNonZeroCharacterNotInTable_ReturnsFalse() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 1L, false, "description");
        boolean result = smallCharMatcher.matches('c');
        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_WithNonZeroCharacterInTable_ReturnsTrue() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L, false, "description");
        boolean result = smallCharMatcher.matches('c');
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_WithNonZeroCharacterInTableButNotMatching_ReturnsFalse() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L, false, "description");
        boolean result = smallCharMatcher.matches('d');
        Assert.assertFalse(result);
    }

    @Test
    public void testSetBits_WithContainsZero_SetsBitForZeroCharacter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L, true, "description");
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        Assert.assertTrue(table.get(0));
    }

    @Test
    public void testSetBits_WithoutContainsZero_DoesNotSetBitForZeroCharacter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L, false, "description");
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        Assert.assertFalse(table.get(0));
    }

    @Test
    public void testSetBits_SetsBitsForAllNonZeroCharactersInTable() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L, true, "description");
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        Assert.assertTrue(table.get('a'));
        Assert.assertTrue(table.get('b'));
        Assert.assertTrue(table.get('c'));
    }
}