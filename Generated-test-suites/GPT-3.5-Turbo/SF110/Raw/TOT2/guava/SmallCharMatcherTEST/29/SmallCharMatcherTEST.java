import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(1517748373, SmallCharMatcher.smear(123456));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0B101L, true, "Test");
        assertTrue(matcher.checkFilter('a'));
        assertFalse(matcher.checkFilter('d'));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(16, SmallCharMatcher.chooseTableSize(8));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "Test");
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('a'));
    }

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0B101L, true, "Test");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0B101L, true, "Test");
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
        assertFalse(table.get('d'));
    }

}