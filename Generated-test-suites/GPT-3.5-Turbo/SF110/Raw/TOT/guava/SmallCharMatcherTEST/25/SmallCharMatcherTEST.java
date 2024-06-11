import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 50;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(1056873519, result);
        
        hashCode = 0;
        result = SmallCharMatcher.smear(hashCode);
        assertEquals(0, result);
        
        hashCode = Integer.MAX_VALUE;
        result = SmallCharMatcher.smear(hashCode);
        assertEquals(-993445232, result);
    }
    
    @Test
    public void testCheckFilter_positive() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 1L, false, "test");
        
        boolean result = matcher.checkFilter(0);
        assertFalse(result);
        
        result = matcher.checkFilter(1);
        assertTrue(result);
        
        result = matcher.checkFilter(9);
        assertFalse(result);
    }
    
    @Test
    public void testCheckFilter_negative() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0L, false, "test");
        
        boolean result = matcher.checkFilter(0);
        assertFalse(result);
        
        result = matcher.checkFilter(1);
        assertFalse(result);
        
        result = matcher.checkFilter(9);
        assertFalse(result);
    }
    
    @Test
    public void testChooseTableSize() {
        int setSize = 1;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(2, result);
        
        setSize = 5;
        result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(8, result);
        
        setSize = 1023;
        result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(2048, result);
        
        setSize = 1024;
        result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(2048, result);
    }
    
    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, true, "test");
        
        boolean result = matcher.matches('\0');
        assertTrue(result);
    }
    
    @Test
    public void testMatches_notContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, false, "test");
        
        boolean result = matcher.matches('\0');
        assertFalse(result);
    }
    
    @Test
    public void testMatches_doesNotMatch() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, true, "test");
        
        boolean result = matcher.matches('d');
        assertFalse(result);
    }
    
    @Test
    public void testMatches_match() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, true, "test");
        
        boolean result = matcher.matches('a');
        assertTrue(result);
    }
    
    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, true, "test");
        
        BitSet table = new BitSet();
        
        matcher.setBits(table);
        
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
    }
    
    @Test
    public void testSetBits_notContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 0L, false, "test");
        
        BitSet table = new BitSet();
        
        matcher.setBits(table);
        
        assertFalse(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
    }
}