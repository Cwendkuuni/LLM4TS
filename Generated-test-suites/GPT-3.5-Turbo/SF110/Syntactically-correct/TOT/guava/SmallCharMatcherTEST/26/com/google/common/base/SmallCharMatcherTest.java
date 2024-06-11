import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(0, SmallCharMatcher.smear(0));
        assertEquals(632309861, SmallCharMatcher.smear(1));
        assertEquals(-166972876, SmallCharMatcher.smear(-1));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x00000001, true, null);
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(2));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(2, SmallCharMatcher.chooseTableSize(2));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(4, SmallCharMatcher.chooseTableSize(4));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(0, 2);
        chars.set(3, 5);
        CharMatcher matcher = SmallCharMatcher.from(chars, "Test SmallCharMatcher");
        
        assertTrue(matcher.matches('\0'));
        assertTrue(matcher.matches('A'));
        assertTrue(matcher.matches('B'));
        assertFalse(matcher.matches('C'));
        assertTrue(matcher.matches('D'));
    }

    @Test
    public void testMatches() {
        BitSet chars = new BitSet();
        chars.set(0, 2);
        chars.set(3, 5);
        CharMatcher matcher = SmallCharMatcher.from(chars, "Test SmallCharMatcher");

        assertTrue(matcher.matches('\0'));
        assertTrue(matcher.matches('A'));
        assertTrue(matcher.matches('B'));
        assertFalse(matcher.matches('C'));
        assertTrue(matcher.matches('D'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'A', 'B', 'C', '\0'}, 0x00000001, true, "Test SmallCharMatcher");
        matcher.setBits(table);

        assertTrue(table.get(0));
        assertTrue(table.get('A'));
        assertTrue(table.get('B'));
        assertTrue(table.get('C'));
        assertFalse(table.get('D'));
    }
}