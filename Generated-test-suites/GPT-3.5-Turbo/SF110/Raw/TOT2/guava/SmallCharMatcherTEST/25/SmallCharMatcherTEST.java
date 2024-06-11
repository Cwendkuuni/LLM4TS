import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = -12345;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(123456789, result);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b101, true, "test");
        assertTrue(smallCharMatcher.checkFilter('b'));
        assertFalse(smallCharMatcher.checkFilter('d'));
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 3;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(4, result);
    }

    @Test
    public void testMatches() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b101, true, "test");
        assertTrue(smallCharMatcher.matches('a'));
        assertFalse(smallCharMatcher.matches('d'));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b101, true, "test");
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertFalse(table.get('d'));
    }
}