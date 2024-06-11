import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        // Typical use case
        assertEquals(1476856093, SmallCharMatcher.smear(123456789));
        // Edge case
        assertEquals(0, SmallCharMatcher.smear(0));
    }

    @Test
    public void testCheckFilter() {
        // Typical use cases
        assertTrue(new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b101L, true, "Test").checkFilter('a'));
        assertFalse(new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b101L, true, "Test").checkFilter('d'));
    }

    @Test
    public void testChooseTableSize() {
        // Typical use case
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        // Edge case
        assertEquals(1024, SmallCharMatcher.chooseTableSize(1023));
    }

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b101L, true, "Test");
        // Typical use cases
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('x'));
        // Edge case
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b101L, true, "Test");
        matcher.setBits(table);
        // Typical use cases
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertFalse(table.get('d'));
        // Edge case
        assertTrue(table.get('\0'));
    }
}