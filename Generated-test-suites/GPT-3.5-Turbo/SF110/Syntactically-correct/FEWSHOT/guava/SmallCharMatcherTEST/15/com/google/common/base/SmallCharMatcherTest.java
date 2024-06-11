import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SmallCharMatcherTest {
    @Test
    public void testMatches_containsZero() {
        char[] table = { 'a', 'b', '\0', 'c', 'd' };
        long filter = 5L;
        boolean containsZero = true;
        String description = "Contains Zero";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_notContainsZero() {
        char[] table = { 'a', 'b', 'c', 'd' };
        long filter = 15L;
        boolean containsZero = false;
        String description = "Not Contains Zero";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        
        assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_match() {
        char[] table = { 'a', 'b', 'c', 'd' };
        long filter = 15L;
        boolean containsZero = false;
        String description = "Match";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertTrue(matcher.matches('d'));
    }
    
    @Test
    public void testMatches_notMatch() {
        char[] table = { 'a', 'b', 'c', 'd' };
        long filter = 15L;
        boolean containsZero = false;
        String description = "Not Match";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        
        assertFalse(matcher.matches('e'));
        assertFalse(matcher.matches('f'));
        assertFalse(matcher.matches('g'));
    }
    
    @Test
    public void testSmear() {
        assertEquals(-1928291915, SmallCharMatcher.smear(5));
        assertEquals(0, SmallCharMatcher.smear(0));
        assertEquals(461845907, SmallCharMatcher.smear(1));
    }
    
    @Test
    public void testCheckFilter() {
        char[] table = { 'a', 'b', 'c', 'd' };
        long filter = 11L;
        boolean containsZero = false;
        String description = "Check Filter";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        
        assertTrue(matcher.checkFilter(2));
        assertFalse(matcher.checkFilter(3));
    }
    
    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(8, SmallCharMatcher.chooseTableSize(3));
        assertEquals(16, SmallCharMatcher.chooseTableSize(8));
        assertEquals(32, SmallCharMatcher.chooseTableSize(16));
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        String description = "From";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('d'));
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('c'));
    }
    
    @Test
    public void testSetBits_containsZero() {
        char[] table = { 'a', 'b', 'c', 'd' };
        long filter = 15L;
        boolean containsZero = true;
        String description = "Set Bits Contains Zero";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        
        BitSet tableBits = new BitSet();
        matcher.setBits(tableBits);
        
        assertTrue(tableBits.get(0));
        assertTrue(tableBits.get('a'));
        assertTrue(tableBits.get('b'));
        assertTrue(tableBits.get('c'));
        assertTrue(tableBits.get('d'));
    }
    
    @Test
    public void testSetBits_notContainsZero() {
        char[] table = { 'a', 'b', 'c', 'd' };
        long filter = 15L;
        boolean containsZero = false;
        String description = "Set Bits Not Contains Zero";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        
        BitSet tableBits = new BitSet();
        matcher.setBits(tableBits);
        
        assertFalse(tableBits.get(0));
        assertTrue(tableBits.get('a'));
        assertTrue(tableBits.get('b'));
        assertTrue(tableBits.get('c'));
        assertTrue(tableBits.get('d'));
    }
}