import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(0, SmallCharMatcher.smear(0));
        assertEquals(1289099755, SmallCharMatcher.smear(5));
        assertEquals(-1404684386, SmallCharMatcher.smear(10));
    }

    @Test
    public void testCheckFilter() {
        char[] table = {0, 1, 2, 3, 4};
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, 10L, true, "test");
        assertTrue(smallCharMatcher.checkFilter(2));
        assertFalse(smallCharMatcher.checkFilter(5));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(16, SmallCharMatcher.chooseTableSize(7));
        assertEquals(32, SmallCharMatcher.chooseTableSize(16));
    }

    @Test
    public void testMatches() {
        char[] table = {0, 1, 2, 3, 4};
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, 10L, true, "test");
        assertTrue(smallCharMatcher.matches(2));
        assertFalse(smallCharMatcher.matches(5));
    }

    @Test
    public void testSetBits() {
        char[] table = {0, 1, 2, 3, 4};
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, 10L, true, "test");
        BitSet bitSet = new BitSet();
        smallCharMatcher.setBits(bitSet);
        assertTrue(bitSet.get(0));
        assertTrue(bitSet.get(1));
        assertTrue(bitSet.get(2));
        assertTrue(bitSet.get(3));
        assertTrue(bitSet.get(4));
        assertFalse(bitSet.get(5));
    }

}