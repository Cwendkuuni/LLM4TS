import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero_true() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 1L, true, "test");
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_containsZero_false() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "test");
        assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_checkFilter_true() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0x7L, false, "test");
        assertTrue(matcher.matches('a'));
    }
    
    @Test
    public void testMatches_checkFilter_false() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0x3L, false, "test");
        assertFalse(matcher.matches('d'));
    }
    
    @Test
    public void testMatches_tableMatch_true() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0x7L, false, "test");
        assertTrue(matcher.matches('b'));
    }
    
    @Test
    public void testMatches_tableMatch_false() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0x7L, false, "test");
        assertFalse(matcher.matches('d'));
    }
    
    @Test
    public void testMatches_tableWrap_false() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0x7L, false, "test");
        assertFalse(matcher.matches('a'));
    }
    
    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 1L, true, "test");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
    }
    
    @Test
    public void testSetBits_noZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0x7L, false, "test");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertFalse(table.get(0));
    }

}