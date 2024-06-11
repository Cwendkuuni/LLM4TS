import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        assertEquals(2147483647, SmallCharMatcher.smear(-1));
        assertEquals(2097152, SmallCharMatcher.smear(16));
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 3, true, "Test");
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
    }
    
    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 7, true, "Test");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('d'));
    }
    
    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 7, true, "Test");
        BitSet table = new BitSet(3);
        table.set(1);
        table.set(2);
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get(1));
        assertTrue(table.get(2));
    }
}