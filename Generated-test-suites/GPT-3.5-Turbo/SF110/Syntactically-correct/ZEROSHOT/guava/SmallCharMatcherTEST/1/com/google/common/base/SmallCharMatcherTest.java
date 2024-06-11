import com.google.common.base.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[1], 0L, true, "");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_notContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[1], 0L, false, "");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testMatches_checkFilter_true() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[1], 0x1L, true, "");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_checkFilter_false() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[1], 0L, true, "");
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testMatches_singleChar_true() {
        char[] table = {'a'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x1L, true, "");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_singleChar_false() {
        char[] table = {'a'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x1L, true, "");
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testMatches_multipleChars_true() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x7L, true, "");
        assertTrue(matcher.matches('b'));
    }

    @Test
    public void testMatches_multipleChars_false() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x7L, true, "");
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testSetBits_containsZero() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x7L, true, "");
        BitSet expected = new BitSet();
        expected.set(0);
        expected.set(97);
        expected.set(98);
        expected.set(99);
        BitSet actual = new BitSet();
        matcher.setBits(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetBits_notContainsZero() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x7L, false, "");
        BitSet expected = new BitSet();
        expected.set(97);
        expected.set(98);
        expected.set(99);
        BitSet actual = new BitSet();
        matcher.setBits(actual);
        assertEquals(expected, actual);
    }
}