import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZeroTrue() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1}), "test");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_containsZeroFalse() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{0}), "test");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testMatches_notInFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1}), "test");
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testMatches_inTable() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{97}), "test");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_notInTable() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{97}), "test");
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testMatches_tableFull() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{97, 98, 99}), "test");
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1}), "test");
        BitSet table = new BitSet(256);
        matcher.setBits(table);
        assertTrue(table.get(0));
    }

    @Test
    public void testSetBits_inTable() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{97}), "test");
        BitSet table = new BitSet(256);
        matcher.setBits(table);
        assertTrue(table.get(97));
    }

    @Test
    public void testSetBits_notInTable() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{97}), "test");
        BitSet table = new BitSet(256);
        matcher.setBits(table);
        assertFalse(table.get(98));
    }

}