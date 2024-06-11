import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        int hashCode = 10;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(748348695, result); // Expected smear result for input hash code
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(null, 0b101L, true, "test"); // Example matcher
        assertTrue(smallCharMatcher.checkFilter(0)); // Testing filter for character '0'
        assertFalse(smallCharMatcher.checkFilter(1)); // Testing filter for character '1'
    }
    
    @Test
    public void testChooseTableSize() {
        int setSize = 80;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(128, result); // Expected table size for given set size
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a'}, 0b10L, false, "test"); // Example matcher
        assertTrue(smallCharMatcher.matches('a')); // Testing match for character 'a'
        assertFalse(smallCharMatcher.matches('b')); // Testing match for character 'b'
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0b11L, true, "test"); // Example matcher
        smallCharMatcher.setBits(table);
        assertTrue(table.get(0)); // Testing bit set for character '0'
        assertTrue(table.get('a')); // Testing bit set for character 'a'
        assertTrue(table.get('b')); // Testing bit set for character 'b'
    }
}