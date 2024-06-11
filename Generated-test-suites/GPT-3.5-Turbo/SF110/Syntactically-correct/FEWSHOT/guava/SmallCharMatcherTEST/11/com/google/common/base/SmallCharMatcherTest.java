import org.junit.Test;
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
    public void testMatchesTrue() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, true, "");
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
    }
    
    @Test
    public void testMatchesFalse() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, true, "");
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('e'));
        assertFalse(matcher.matches('f'));
    }
    
    @Test
    public void testMatchesWithTableSize() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, true, "");
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('e'));
        assertFalse(matcher.matches('f'));
    }
    
    @Test
    public void testSetBitsContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, true, "");
        BitSet expectedTable = new BitSet();
        expectedTable.set(0);
        expectedTable.set('a');
        expectedTable.set('b');
        expectedTable.set('c');
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertEquals(expectedTable, table);
    }
    
    @Test
    public void testSetBitsNotContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, false, "");
        BitSet expectedTable = new BitSet();
        expectedTable.set('a');
        expectedTable.set('b');
        expectedTable.set('c');
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertEquals(expectedTable, table);
    }
}