import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(15579342, SmallCharMatcher.smear(123));
    }

    @Test
    public void testCheckFilter() {
        char[] table = {'a', 'b', 'c', 'd'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x1L, true, "Test");
        assertTrue(matcher.checkFilter('c'));
        assertFalse(matcher.checkFilter('e'));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
    }

    @Test
    public void testFrom() {
        BitSet set = new BitSet();
        set.set(1);
        set.set(3);
        CharMatcher matcher = SmallCharMatcher.from(set, "Test");
        assertTrue(matcher.matches('b'));
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testMatches() {
        char[] table = {'a', 'b', 'c', 'd'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x1L, true, "Test");
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('e'));
    }

    @Test
    public void testSetBits() {
        char[] table = {'a', 'b', 'c', 'd'};
        BitSet set = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x1L, true, "Test");
        matcher.setBits(set);
        assertTrue(set.get('a'));
        assertFalse(set.get('e'));
    }
}