import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 10;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(20521418, result);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0x7L, true, "test");
        assertTrue(matcher.checkFilter('a'));
        assertFalse(matcher.checkFilter('d'));
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 5;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(8, result);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(97); // 'a'
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "test");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
    }

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 0x5L, false, "test");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 0x0L, true, "test");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertFalse(table.get(1));
    }
}