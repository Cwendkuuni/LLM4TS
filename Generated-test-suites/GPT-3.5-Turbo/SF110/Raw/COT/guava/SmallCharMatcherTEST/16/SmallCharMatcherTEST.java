
import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        
        assertFalse(matcher.matches('a')); // Check if matches method returns false for a character
        
        matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1}), "Test");
        assertTrue(matcher.matches('\0')); // Check if matches method returns true for '\0' character
        
        matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1}), "Test");
        assertFalse(matcher.matches('a')); // Check if matches method returns false for a character not present
        
        matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1, 2, 3}), "Test");
        assertTrue(matcher.matches((char)1)); // Check if matches method returns true for a character present
        
        matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1, 256}), "Test");
        assertFalse(matcher.matches((char)257)); // Check if matches method returns false for a character greater than 255
        
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        BitSet table = new BitSet();
        
        matcher.setBits(table);
        
        assertEquals(0, table.cardinality()); // Check if setBits method sets no bits with an empty character set
        
        matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1}), "Test");
        table.clear();
        
        matcher.setBits(table);
        
        assertEquals(2, table.cardinality()); // Check if setBits method sets the correct bits with a single character set
        
        matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1, 256}), "Test");
        table.clear();
        
        matcher.setBits(table);
        
        assertEquals(2, table.cardinality()); // Check if setBits method sets the correct bits with characters greater than 255
    }
    
    @Test
    public void testChooseTableSize() {
        int setSize = 1;
        int expectedTableSize = 2;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        
        assertEquals(expectedTableSize, tableSize); // Check if chooseTableSize method returns the correct table size for a set size of 1
        
        setSize = 7;
        expectedTableSize = 8;
        
        tableSize = SmallCharMatcher.chooseTableSize(setSize);
        
        assertEquals(expectedTableSize, tableSize); // Check if chooseTableSize method returns the correct table size for a set size greater than 1
        
        setSize = Integer.MAX_VALUE;
        expectedTableSize = 2147483648;
        
        tableSize = SmallCharMatcher.chooseTableSize(setSize);
        
        assertEquals(expectedTableSize, tableSize); // Check if chooseTableSize method returns the correct table size for a set size equal to the maximum integer value
    }

}
