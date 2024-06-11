import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {
    
    @Test
    public void testMatches_ContainsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_CheckFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        assertFalse(matcher.matches('A'));
    }
    
    @Test
    public void testMatches_NotInTable() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        assertFalse(matcher.matches('X'));
    }
    
    @Test
    public void testSetBits_ContainsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
    }
    
    @Test
    public void testChooseTableSize_SingleElement() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
    }
    
    @Test
    public void testChooseTableSize_MultipleElements() {
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
    }
    
    // Add more test cases for edge cases and exceptions
    
}