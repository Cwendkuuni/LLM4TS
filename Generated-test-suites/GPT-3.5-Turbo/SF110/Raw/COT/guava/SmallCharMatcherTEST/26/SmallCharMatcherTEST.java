
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, true, "");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_noContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, false, "");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testMatches_checkFilterTrue() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 1L, false, "");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_checkFilterFalse() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, false, "");
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testMatches_tableNotEmptyAndMatch() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 0L, false, "");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_tableNotEmptyAndNotMatch() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 0L, false, "");
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testMatches_tableEmpty() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, false, "");
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 0L, true, "");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
    }

    @Test
    public void testSetBits_noContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 0L, false, "");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertFalse(table.get(0));
    }
}
