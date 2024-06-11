
import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "");

        assertTrue(matcher.matches('\0'));
        assertFalse(matcher.matches('a'));
    }
    
    @Test
    public void testSmear() {
        int hashCode = 12345;
        int smeared = SmallCharMatcher.smear(hashCode);
        
        // Assert that the smeared value is not equal to the input value
        assertNotEquals(hashCode, smeared);
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0x1L, false, "");

        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
    }
    
    @Test
    public void testChooseTableSize() {
        int setSize = 5;
        int expectedTableSize = 8;
        
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        
        assertEquals(expectedTableSize, tableSize);
    }
    
    @Test
    public void testFrom() {
        String description = "test";
        BitSet chars = new BitSet();
        chars.set(1, 5);
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        // Assert that the containsZero flag is false since BitSet does not contain zero
        assertFalse(matcher.matches('\0'));
        
        // Assert that the matches method returns true for all characters in the BitSet
        assertTrue(matcher.matches('1'));
        assertTrue(matcher.matches('2'));
        assertTrue(matcher.matches('3'));
        assertTrue(matcher.matches('4'));
        
        // Assert that the matches method returns false for other characters not in the BitSet
        assertFalse(matcher.matches('0'));
        assertFalse(matcher.matches('5'));
        assertFalse(matcher.matches('a'));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'1', '2', '3', '4'}, 0L, false, "");

        matcher.setBits(table);
        
        // Assert that the table BitSet contains all characters in the SmallCharMatcher
        assertTrue(table.get('1'));
        assertTrue(table.get('2'));
        assertTrue(table.get('3'));
        assertTrue(table.get('4'));
        
        // Assert that the table BitSet does not contain other characters
        assertFalse(table.get('\0'));
        assertFalse(table.get('0'));
        assertFalse(table.get('5'));
        assertFalse(table.get('a'));
    }
}
