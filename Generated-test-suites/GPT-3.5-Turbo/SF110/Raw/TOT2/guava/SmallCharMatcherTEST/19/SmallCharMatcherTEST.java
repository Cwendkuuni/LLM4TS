import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(123456789, SmallCharMatcher.smear(123456789));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0x1L, true, "test");
        assertTrue(matcher.checkFilter('a'));
        assertFalse(matcher.checkFilter('d'));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(8, SmallCharMatcher.chooseTableSize(6));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        CharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches((char)1));
        assertFalse(matcher.matches((char)2));
        assertTrue(matcher.matches((char)3));
    }

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0x1L, true, "test");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0x1L, true, "test");
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
        assertFalse(table.get('d'));
    }
}