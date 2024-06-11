import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        // Typical use case with positive integer
        assertEquals(123456789, SmallCharMatcher.smear(123456789));

        // Typical use case with negative integer
        assertEquals(-987654321, SmallCharMatcher.smear(-987654321));

        // Edge case with 0 as input
        assertEquals(0, SmallCharMatcher.smear(0));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        
        // Typical use case with filter bit set
        matcher = new SmallCharMatcher(new char[]{}, 0x1L, false, "Test");
        assertTrue(matcher.checkFilter(0));

        // Typical use case with filter bit unset
        matcher = new SmallCharMatcher(new char[]{}, 0x0L, false, "Test");
        assertFalse(matcher.checkFilter(0));

        // Edge case with filter bit set to -1
        matcher = new SmallCharMatcher(new char[]{}, 0xFFFFFFFFFFFFFFFFL, false, "Test");
        assertTrue(matcher.checkFilter(63));
    }

    @Test
    public void testChooseTableSize() {
        // Typical use case with setSize = 5
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));

        // Typical use case with setSize = 10
        assertEquals(16, SmallCharMatcher.chooseTableSize(10));

        // Edge case with setSize = 1
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
    }

    @Test
    public void testFrom() {
        // Typical use case with empty BitSet
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        assertFalse(matcher.matches('a'));

        // Typical use case with BitSet containing 'a' and 'b'
        BitSet bitSet = new BitSet();
        bitSet.set('a');
        bitSet.set('b');
        matcher = SmallCharMatcher.from(bitSet, "Test");
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));

        // Error scenario with BitSet exceeding MAX_SIZE
        bitSet = new BitSet(MAX_SIZE + 1);
        bitSet.set(0, MAX_SIZE + 1);
        try {
            matcher = SmallCharMatcher.from(bitSet, "Test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testMatches() {
        // Typical use case with SmallCharMatcher containing 'a' and 'b'
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0b11L, false, "Test");

        // Typical use case with char 'a'
        assertTrue(matcher.matches('a'));

        // Typical use case with char 'b'
        assertTrue(matcher.matches('b'));

        // Error scenario with char 'c'
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0b11L, false, "Test");

        // Typical use case
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));

        // Special case with containsZero set
        matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0b11L, true, "Test");
        table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
    }
}