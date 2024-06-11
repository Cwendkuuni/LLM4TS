import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        // Test basic functionality
        assertEquals(286291269, SmallCharMatcher.smear(123456));
        
        // Test edge case with maximum integer value
        assertEquals(-615228243, SmallCharMatcher.smear(Integer.MAX_VALUE));
        
        // Test negative input
        assertEquals(-413121409, SmallCharMatcher.smear(-123456));
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0x1L, true, "Test Matcher");
        
        // Test with character in filter
        assertTrue(matcher.checkFilter('b'));
        
        // Test with character not in filter
        assertFalse(matcher.checkFilter('d'));
    }
    
    @Test
    public void testChooseTableSize() {
        // Test with size of 1
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        
        // Test with size greater than 1
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
        
        // Test with size at maximum boundary
        assertEquals(2048, SmallCharMatcher.chooseTableSize(1023));
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0x1L, true, "Test Matcher");
        
        // Test matching character in table
        assertTrue(matcher.matches('b'));
        
        // Test matching character not in table
        assertFalse(matcher.matches('d'));
        
        // Test matching character '\0'
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0x1L, true, "Test Matcher");
        
        // Test setting bits in BitSet
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
        assertTrue(table.get('\0'));
    }
}