import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SmallCharMatcherTest {
    @Test
    public void testMatches() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0b101, true, "");
        
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testSetBits() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0b101, true, "");
        
        BitSet tableBits = new BitSet();
        matcher.setBits(tableBits);
        
        assertTrue(tableBits.get('a'));
        assertTrue(tableBits.get('b'));
        assertTrue(tableBits.get('c'));
        assertFalse(tableBits.get('d'));
        assertTrue(tableBits.get('\0'));
    }
}