import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SmallCharMatcherTest {
    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(2, SmallCharMatcher.chooseTableSize(2));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(4, SmallCharMatcher.chooseTableSize(4));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
        assertEquals(16, SmallCharMatcher.chooseTableSize(10));
        assertEquals(32, SmallCharMatcher.chooseTableSize(20));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(2);
        chars.set(10);
        chars.set(20);
        String description = "Test CharMatcher";
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);

        assertTrue(matcher.matches((char) 0));
        assertTrue(matcher.matches((char) 1));
        assertTrue(matcher.matches((char) 2));
        assertFalse(matcher.matches((char) 3));
        assertFalse(matcher.matches((char) 5));
        assertFalse(matcher.matches((char) 9));
        assertTrue(matcher.matches((char) 10));
        assertFalse(matcher.matches((char) 15));
        assertFalse(matcher.matches((char) 19));
        assertTrue(matcher.matches((char) 20));
    }

    @Test
    public void testMatches() {
        char[] table = {'a', 'b', 'c', '\0', 'e'};
        long filter = 7L;
        boolean containsZero = true;
        String description = "Test CharMatcher";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);

        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertTrue(matcher.matches((char) 0));
        assertFalse(matcher.matches('f'));
    }

    @Test
    public void testSetBits() {
        char[] table = {'a', 'b', 'c', '\0', 'e'};
        long filter = 7L;
        boolean containsZero = true;
        String description = "Test CharMatcher";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);

        BitSet tableBits = new BitSet();
        matcher.setBits(tableBits);

        assertTrue(tableBits.get('a'));
        assertTrue(tableBits.get('b'));
        assertTrue(tableBits.get('c'));
        assertFalse(tableBits.get('d'));
        assertTrue(tableBits.get((char) 0));
        assertFalse(tableBits.get('f'));
    }
}