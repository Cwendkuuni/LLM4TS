import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 1L, true, "description");
        
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches(' '));
    }

    @Test
    public void testSetBits() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 1L, true, "description");
        
        BitSet expected = new BitSet();
        expected.set(0);
        expected.set('a');
        expected.set('b');
        expected.set('c');
        
        BitSet actual = new BitSet();
        matcher.setBits(actual);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testSmear() {
        assertEquals(0, SmallCharMatcher.smear(0));
        assertEquals(-1362210353, SmallCharMatcher.smear(1));
        assertEquals(932071646, SmallCharMatcher.smear(2));
        assertEquals(3270560, SmallCharMatcher.smear(3));
        assertEquals(-1362210352, SmallCharMatcher.smear(4));
    }

    @Test
    public void testCheckFilter() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 1L, true, "description");
        
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(4));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(8, SmallCharMatcher.chooseTableSize(8));
        assertEquals(16, SmallCharMatcher.chooseTableSize(9));
        assertEquals(16, SmallCharMatcher.chooseTableSize(15));
        assertEquals(16, SmallCharMatcher.chooseTableSize(16));
        assertEquals(32, SmallCharMatcher.chooseTableSize(17));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        chars.set('c');
        String description = "description";
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        assertEquals('a', matcher.table[0]);
        assertEquals('b', matcher.table[1]);
        assertEquals('c', matcher.table[2]);
        assertEquals(description, matcher.description);
        assertTrue(matcher.containsZero);
        assertEquals(7L, matcher.filter);
    }
}