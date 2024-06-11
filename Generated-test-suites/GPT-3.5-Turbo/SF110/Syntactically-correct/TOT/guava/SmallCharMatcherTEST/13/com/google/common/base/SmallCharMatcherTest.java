
import com.google.common.annotations.GwtIncompatible;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@GwtIncompatible("no precomputation is done in GWT")
public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "description");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_nullCharacter_notContainsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "description");
        assertFalse(matcher.matches(null));
    }

    @Test
    public void testMatches_noMatch() {
        BitSet bitSet = new BitSet();
        bitSet.set(2);
        SmallCharMatcher matcher = SmallCharMatcher.from(bitSet, "description");
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testMatches_match() {
        BitSet bitSet = new BitSet();
        bitSet.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(bitSet, "description");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testChooseTableSize_setSizeOne() {
        int tableSize = SmallCharMatcher.chooseTableSize(1);
        assertTrue(tableSize == 2);
    }

    @Test
    public void testChooseTableSize_setSizeGreaterThanOne() {
        int tableSize = SmallCharMatcher.chooseTableSize(10);
        int expectedSize = 16;
        assertTrue(tableSize == expectedSize);
    }

    @Test
    public void testChooseTableSize_zeroSetSize() {
        int tableSize = SmallCharMatcher.chooseTableSize(0);
        assertTrue(tableSize == 2);
    }

    @Test
    public void testCheckFilter_matchingFilter() {
        BitSet bitSet = new BitSet();
        bitSet.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(bitSet, "description");
        assertTrue(matcher.checkFilter('a'));
    }

    @Test
    public void testCheckFilter_notMatchingFilter() {
        BitSet bitSet = new BitSet();
        bitSet.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(bitSet, "description");
        assertFalse(matcher.checkFilter('b'));
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet bitSet = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(bitSet, "description");
        matcher.setBits(bitSet);
        assertTrue(bitSet.get(0));
    }

    @Test
    public void testSetBits_notContainsZero() {
        BitSet bitSet = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[2], 10, false, "description");
        matcher.setBits(bitSet);
        assertFalse(bitSet.get(0));
    }

    @Test
    public void testSetBits_setCharacters() {
        BitSet bitSet = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 10, true, "description");
        matcher.setBits(bitSet);
        assertTrue(bitSet.get('a'));
        assertTrue(bitSet.get('b'));
    }
}
