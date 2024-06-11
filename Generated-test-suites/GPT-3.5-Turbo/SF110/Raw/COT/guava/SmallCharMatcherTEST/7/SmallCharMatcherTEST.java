
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0b01L, true, "matcher");
        assertTrue("Matches should return true for null character", matcher.matches('\0'));
    }

    @Test
    public void testMatches_notContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0b01L, false, "matcher");
        assertFalse("Matches should return false for null character", matcher.matches('\0'));
    }

    @Test
    public void testMatches_containsElement() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b011L, false, "matcher");
        assertTrue("Matches should return true for contained element", matcher.matches('b'));
    }

    @Test
    public void testMatches_notContainsElement() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b011L, false, "matcher");
        assertFalse("Matches should return false for non-contained element", matcher.matches('d'));
    }

    @Test
    public void testMatches_emptyTable() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0b011L, false, "matcher");
        assertFalse("Matches should return false for empty table", matcher.matches('a'));
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet expectedSet = new BitSet();
        expectedSet.set(0);

        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0b01L, true, "matcher");
        BitSet actualSet = new BitSet();
        matcher.setBits(actualSet);

        assertEquals("SetBits should set the bit for null character", expectedSet, actualSet);
    }

    @Test
    public void testSetBits_notContainsZero() {
        BitSet expectedSet = new BitSet();

        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0b01L, false, "matcher");
        BitSet actualSet = new BitSet();
        matcher.setBits(actualSet);

        assertEquals("SetBits should not set the bit for null character", expectedSet, actualSet);
    }

    @Test
    public void testSetBits_containsElements() {
        BitSet expectedSet = new BitSet();
        expectedSet.set('a');
        expectedSet.set('b');
        expectedSet.set('c');

        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b011L, false, "matcher");
        BitSet actualSet = new BitSet();
        matcher.setBits(actualSet);

        assertEquals("SetBits should set the bits for contained elements", expectedSet, actualSet);
    }

    @Test
    public void testSetBits_emptyTable() {
        BitSet expectedSet = new BitSet();

        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0b011L, false, "matcher");
        BitSet actualSet = new BitSet();
        matcher.setBits(actualSet);

        assertEquals("SetBits should not set any bits for empty table", expectedSet, actualSet);
    }
}
