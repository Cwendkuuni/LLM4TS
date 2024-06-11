
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        // Typical use case with positive number
        int result1 = SmallCharMatcher.smear(5);
        assertEquals(1017241475, result1);

        // Typical use case with negative number
        int result2 = SmallCharMatcher.smear(-5);
        assertEquals(-2034482950, result2);

        // Edge case with the smallest possible integer
        int result3 = SmallCharMatcher.smear(Integer.MIN_VALUE);
        assertEquals(0, result3);

        // Edge case with the largest possible integer
        int result4 = SmallCharMatcher.smear(Integer.MAX_VALUE);
        assertEquals(2049360004, result4);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x03, true, null);

        // Typical use case with a character that exists in the filter
        boolean result1 = matcher.checkFilter(1);
        assertTrue(result1);

        // Typical use case with a character that does not exist in the filter
        boolean result2 = matcher.checkFilter(2);
        assertFalse(result2);

        // Edge case with the smallest possible character value
        boolean result3 = matcher.checkFilter(Character.MIN_VALUE);
        assertFalse(result3);

        // Edge case with the largest possible character value
        boolean result4 = matcher.checkFilter(Character.MAX_VALUE);
        assertFalse(result4);
    }

    @Test
    public void testChooseTableSize() {
        // Typical use case with a small set size
        int result1 = SmallCharMatcher.chooseTableSize(5);
        assertEquals(8, result1);

        // Typical use case with a large set size
        int result2 = SmallCharMatcher.chooseTableSize(100);
        assertEquals(128, result2);

        // Edge case with the smallest possible set size
        int result3 = SmallCharMatcher.chooseTableSize(1);
        assertEquals(2, result3);

        // Edge case with the largest possible set size
        int result4 = SmallCharMatcher.chooseTableSize(Integer.MAX_VALUE);
        assertEquals(1073741824, result4);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        String description = "Test";
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);

        // Typical use case with characters in the set
        assertTrue(matcher.matches((char)1));
        assertFalse(matcher.matches((char)2));
        assertTrue(matcher.matches((char)3));

        // Edge case with the smallest possible character value
        assertFalse(matcher.matches(Character.MIN_VALUE));

        // Edge case with the largest possible character value
        assertFalse(matcher.matches(Character.MAX_VALUE));
    }

    @Test
    public void testMatches() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        String description = "Test";
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);

        // Typical use case with a character in the set
        assertTrue(matcher.matches((char)1));

        // Typical use case with a character not in the set
        assertFalse(matcher.matches((char)2));

        // Edge case with the smallest possible character value and containsZero as true
        assertTrue(matcher.matches(Character.MIN_VALUE));

        // Edge case with the smallest possible character value and containsZero as false
        assertFalse(matcher.matches(Character.MIN_VALUE));

        // Edge case with the largest possible character value
        assertFalse(matcher.matches(Character.MAX_VALUE));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x03, true, null);
        matcher.setBits(table);

        // Typical use case with containsZero as true
        assertTrue(table.get(0));

        // Typical use case with containsZero as false
        assertFalse(table.get(1));

        // Edge case with a character value of 0
        assertFalse(table.get(Character.MIN_VALUE));

        // Edge case with the largest possible character value
        assertFalse(table.get(Character.MAX_VALUE));
    }
}
