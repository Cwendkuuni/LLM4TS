import static org.junit.Assert.*;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        // Test the smear method
        assertEquals(123456789, SmallCharMatcher.smear(123));
        assertEquals(0, SmallCharMatcher.smear(0));
        assertEquals(-1, SmallCharMatcher.smear(-1));
    }

    @Test
    public void testCheckFilter() {
        // Test the checkFilter method
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0x1L, false, "Test description");
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(2));
    }

    @Test
    public void testChooseTableSize() {
        // Test the chooseTableSize method
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
        assertEquals(1024, SmallCharMatcher.chooseTableSize(1023));
    }

    @Test
    public void testFrom() {
        // Test the from method
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(3);
        CharMatcher matcher = SmallCharMatcher.from(bitSet, "Test description");
        assertTrue(matcher.matches('b'));
        assertFalse(matcher.matches('a'));
        assertTrue(matcher.matches('d'));
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testMatches() {
        // Test the matches method
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0x7L, false, "Test description");
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testSetBits() {
        // Test the setBits method
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0x7L, false, "Test description");
        BitSet bitSet = new BitSet();
        matcher.setBits(bitSet);
        assertTrue(bitSet.get('a'));
        assertTrue(bitSet.get('b'));
        assertFalse(bitSet.get('c'));
    }
}