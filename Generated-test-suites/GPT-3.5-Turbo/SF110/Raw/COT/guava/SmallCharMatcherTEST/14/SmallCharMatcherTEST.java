import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 123L, true, "description");

        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_notContained() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 123L, true, "description");

        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testMatches_contained() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 123L, true, "description");

        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 123L, true, "description");
        BitSet expected = new BitSet();
        expected.set(0);

        BitSet table = new BitSet();
        matcher.setBits(table);

        assertEquals(expected, table);
    }

}