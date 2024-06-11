import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 10;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(157690340, result);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0x1L, true, "test");
        assertTrue(matcher.checkFilter('a'));
        assertFalse(matcher.checkFilter('d'));
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 10;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(16, result);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        CharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches('b'));
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0x1L, true, "test");
        assertTrue(matcher.matches('b'));
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
    }
}