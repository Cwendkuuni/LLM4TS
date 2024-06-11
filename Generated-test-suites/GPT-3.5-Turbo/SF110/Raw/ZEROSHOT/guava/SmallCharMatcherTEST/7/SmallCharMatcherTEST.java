import org.junit.*;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testMatches_notContainsZero() {
        BitSet chars = new BitSet();
        chars.set(1);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_checkFilter_true() {
        BitSet chars = new BitSet();
        chars.set(1);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches('1'));
    }

    @Test
    public void testMatches_checkFilter_false() {
        BitSet chars = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertFalse(matcher.matches('1'));
    }

    @Test
    public void testMatches_matchingChar() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches('2'));
    }

    @Test
    public void testMatches_notMatchingChar() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertFalse(matcher.matches('3'));
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "");
        matcher.setBits(table);
        assertTrue(table.get(0));
    }
    
    @Test
    public void testSetBits_notContainsZero() {
        BitSet table = new BitSet();
        BitSet chars = new BitSet();
        chars.set(1);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        matcher.setBits(table);
        assertFalse(table.get(0));
    }
}