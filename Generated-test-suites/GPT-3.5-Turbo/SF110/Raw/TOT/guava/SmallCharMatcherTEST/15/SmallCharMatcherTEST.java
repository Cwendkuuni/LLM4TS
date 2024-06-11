

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    // Test cases for smear() method
    @Test
    public void testSmear_positiveHashCode_returnExpectedResult() {
        int hashCode = 10;
        int expectedResult = -511558270;
        
        int result = SmallCharMatcher.smear(hashCode);
        
        assertEquals(expectedResult, result);
    }
    
    // Test cases for checkFilter() method
    @Test
    public void testCheckFilter_containsFilterItem_returnsTrue() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 1L, false, null);
        int c = 2;
        
        boolean result = matcher.checkFilter(c);
        
        assertTrue(result);
    }
    
    @Test
    public void testCheckFilter_doesNotContainFilterItem_returnsFalse() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 1L, false, null);
        int c = 3;
        
        boolean result = matcher.checkFilter(c);
        
        assertFalse(result);
    }
    
    // Test cases for chooseTableSize() method
    @Test
    public void testChooseTableSize_setSizeIs1_returns2() {
        int setSize = 1;
        int expectedResult = 2;
        
        int result = SmallCharMatcher.chooseTableSize(setSize);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testChooseTableSize_setSizeIsOdd_returnExpectedResult() {
        int setSize = 7;
        int expectedResult = 8;
        
        int result = SmallCharMatcher.chooseTableSize(setSize);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testChooseTableSize_setSizeIsEven_returnExpectedResult() {
        int setSize = 8;
        int expectedResult = 16;
        
        int result = SmallCharMatcher.chooseTableSize(setSize);
        
        assertEquals(expectedResult, result);
    }
    
    // Test cases for from() method
    @Test
    public void testFrom_charsContainsZero_containsZeroExpectedToBeTrue() {
        BitSet chars = new BitSet();
        chars.set(0);
        String description = "dummy description";
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testFrom_charsDoesNotContainZero_containsZeroExpectedToBeFalse() {
        BitSet chars = new BitSet();
        String description = "dummy description";
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testFrom_charsNotEmpty_tableSizeShouldBeGreaterOrEqualToSetSize() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        chars.set(3);
        String description = "dummy description";
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        assertTrue(matcher.matches('\0'));
    }
    
    // Test cases for matches() method
    @Test
    public void testMatches_charIsZero_containsZeroExpectedToBeTrue() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 1L, true, null);
        char c = '\0';
        
        boolean result = matcher.matches(c);
        
        assertTrue(result);
    }
    
    @Test
    public void testMatches_charIsNotZero_doesNotContainChar_expectedToBeFalse() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 1L, false, null);
        char c = 'a';
        
        boolean result = matcher.matches(c);
        
        assertFalse(result);
    }
    
    @Test
    public void testMatches_charIsNotZero_containsChar_expectedToBeTrue() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a'}, 1L, false, null);
        char c = 'a';
        
        boolean result = matcher.matches(c);
        
        assertTrue(result);
    }
    
    @Test
    public void testMatches_charIsNotZero_doesNotContainChar_tableIsFull_returnFalse() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c', 'd'}, 1L, false, null);
        char c = 'e';
        
        boolean result = matcher.matches(c);
        
        assertFalse(result);
    }
    
    @Test
    public void testMatches_charIsNotZero_containsChar_tableIsFull_returnTrue() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b', 'c', 'd', 'e'}, 1L, false, null);
        char c = 'e';
        
        boolean result = matcher.matches(c);
        
        assertTrue(result);
    }
    
    // Test cases for setBits() method
    @Test
    public void testSetBits_containsZero_zeroExpectedToBeSet() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 1L, true, null);
        BitSet table = new BitSet();
        
        matcher.setBits(table);
        
        assertTrue(table.get(0));
    }
    
    @Test
    public void testSetBits_charIsNotZero_charExpectedToBeSet() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 1L, true, null);
        BitSet table = new BitSet();
        
        matcher.setBits(table);
        
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
    }
    
    @Test
    public void testSetBits_charIsZero_charExpectedToNotBeSet() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] {'a', 'b'}, 1L, true, null);
        BitSet table = new BitSet();
        
        matcher.setBits(table);
        
        assertFalse(table.get('c'));
    }
}

