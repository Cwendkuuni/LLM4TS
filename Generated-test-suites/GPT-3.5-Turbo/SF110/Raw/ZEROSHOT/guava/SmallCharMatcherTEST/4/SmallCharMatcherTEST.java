import org.junit.*;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b1, true, "description");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_notContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b0, false, "description");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testMatches_containsChar() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b1, false, "description");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_notContainsChar() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b0, false, "description");
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b1, true, "description");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
    }

    @Test
    public void testSetBits_notContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b0, false, "description");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertFalse(table.get(0));
    }
}