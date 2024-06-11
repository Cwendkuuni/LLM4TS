import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b111L, true, "Test Matcher");
        
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b111L, true, "Test Matcher");
        
        BitSet expected = new BitSet();
        expected.set('a');
        expected.set('b');
        expected.set('c');
        
        BitSet actual = new BitSet();
        matcher.setBits(actual);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testSmear() {
        int hashCode = 123;
        int expected = 123;

        int actual = SmallCharMatcher.smear(hashCode);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0b111L, true, "Test Matcher");
        
        assertTrue(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));
        assertTrue(matcher.checkFilter(2));
        assertFalse(matcher.checkFilter(3));
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 10;
        int expected = 16;

        int actual = SmallCharMatcher.chooseTableSize(setSize);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        chars.set('c');
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test Matcher");
        
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('\0'));
    }
}
