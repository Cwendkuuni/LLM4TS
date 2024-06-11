import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(0, SmallCharMatcher.smear(0));
        assertEquals(8555898, SmallCharMatcher.smear(5));
        assertEquals(-1593941, SmallCharMatcher.smear(-10));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x1L, true, "");
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(5));

        matcher = new SmallCharMatcher(null, 0x7L, false, "");
        assertTrue(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));
        assertTrue(matcher.checkFilter(5));
        assertFalse(matcher.checkFilter(10));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(2, SmallCharMatcher.chooseTableSize(2));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(4, SmallCharMatcher.chooseTableSize(4));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
        assertEquals(16, SmallCharMatcher.chooseTableSize(10));
        assertEquals(32, SmallCharMatcher.chooseTableSize(20));
        assertEquals(64, SmallCharMatcher.chooseTableSize(40));
        assertEquals(128, SmallCharMatcher.chooseTableSize(80));
        assertEquals(256, SmallCharMatcher.chooseTableSize(160));
        assertEquals(512, SmallCharMatcher.chooseTableSize(320));
        assertEquals(1024, SmallCharMatcher.chooseTableSize(640));
        assertEquals(2048, SmallCharMatcher.chooseTableSize(1280));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set('a');
        chars.set('b');
        chars.set('c');
        CharMatcher matcher = SmallCharMatcher.from(chars, "");

        assertTrue(matcher.matches((char)0));
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('e'));
    }

    @Test
    public void testMatches() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x7L, true, "");

        assertTrue(matcher.matches((char)0));
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('e'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        char[] chars = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(chars, 0x7L, true, "");

        matcher.setBits(table);

        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
        assertFalse(table.get('d'));
        assertFalse(table.get('e'));
    }

}