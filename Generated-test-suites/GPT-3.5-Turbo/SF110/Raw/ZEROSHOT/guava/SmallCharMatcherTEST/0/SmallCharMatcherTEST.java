import org.junit.*;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0, true, "test");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_zeroNotContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0, false, "test");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testMatches_notContainsZero() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0, false, "test");
        assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_containsChar() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0, true, "test");
        assertTrue(matcher.matches('a'));
    }
    
    @Test
    public void testMatches_notContainsChar() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0, true, "test");
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet expected = new BitSet();
        expected.set(0);
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0, true, "test");
        BitSet actual = new BitSet();
        matcher.setBits(actual);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetBits_notContainsZero() {
        BitSet expected = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0, false, "test");
        BitSet actual = new BitSet();
        matcher.setBits(actual);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetBits_containsChars() {
        char[] table = {'a', 'b', 'c'};
        BitSet expected = new BitSet();
        expected.set('a');
        expected.set('b');
        expected.set('c');
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0, true, "test");
        BitSet actual = new BitSet();
        matcher.setBits(actual);
        assertEquals(expected, actual);
    }
}