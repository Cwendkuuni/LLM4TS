import org.junit.*;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {
    
    @Test
    public void testMatchesContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, true, "");
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatchesNotContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, false, "");
        assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testMatchesCheckFilterTrue() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 1L, true, "");
        assertTrue(matcher.matches('a'));
    }
    
    @Test
    public void testMatchesCheckFilterFalse() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, true, "");
        assertFalse(matcher.matches('a'));
    }
    
    @Test
    public void testMatchesTrue() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 1L, true, "");
        assertTrue(matcher.matches('a'));
    }
    
    @Test
    public void testMatchesFalse() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 1L, true, "");
        assertFalse(matcher.matches('b'));
    }
    
    @Test
    public void testSetBitsContainsZero() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, true, "");
        matcher.setBits(table);
        assertTrue(table.get(0));
    }
    
    @Test
    public void testSetBitsNotContainsZero() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, false, "");
        matcher.setBits(table);
        assertFalse(table.get(0));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 1L, true, "");
        matcher.setBits(table);
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
    }
}