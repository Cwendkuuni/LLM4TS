import static org.junit.Assert.*;
import org.junit.*;

public class SmallCharMatcherTest {

    // Test cases for smear() method
    
    @Test
    public void testSmearPositiveNumber() {
        int input = 12;
        int expectedOutput = -1411870764;
        int actualOutput = SmallCharMatcher.smear(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testSmearNegativeNumber() {
        int input = -5;
        int expectedOutput = 228104850;
        int actualOutput = SmallCharMatcher.smear(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    // Test cases for checkFilter() method
    
    @Test
    public void testCheckFilterTrue() {
        int input = 5;
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x1L, false, null);
        assertTrue(matcher.checkFilter(input));
    }
    
    @Test
    public void testCheckFilterFalse() {
        int input = 10;
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x0L, false, null);
        assertFalse(matcher.checkFilter(input));
    }
    
    // Test cases for chooseTableSize() method
    
    @Test
    public void testChooseTableSizeOne() {
        int setSize = 1;
        int expectedOutput = 2;
        int actualOutput = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testChooseTableSizeLessThanHalf() {
        int setSize = 5;
        int expectedOutput = 8;
        int actualOutput = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testChooseTableSizeExactlyHalf() {
        int setSize = 8;
        int expectedOutput = 16;
        int actualOutput = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testChooseTableSizeMoreThanHalf() {
        int setSize = 12;
        int expectedOutput = 16;
        int actualOutput = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expectedOutput, actualOutput);
    }
    
    // Test cases for from() method
    
    @Test
    public void testFromEmptyBitSet() {
        BitSet chars = new BitSet();
        String description = "Empty Bitset";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertFalse(matcher.matches('A'));
        assertFalse(matcher.matches('B'));
        assertFalse(matcher.matches('C'));
    }
    
    @Test
    public void testFromBitSetWithZero() {
        BitSet chars = new BitSet();
        chars.set(0);
        String description = "Bitset with zero";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertTrue(matcher.matches('\0'));
        assertFalse(matcher.matches('A'));
        assertFalse(matcher.matches('B'));
        assertFalse(matcher.matches('C'));
    }
    
    @Test
    public void testFromBitSetWithOneChar() {
        BitSet chars = new BitSet();
        chars.set('A');
        String description = "Bitset with one char";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertTrue(matcher.matches('A'));
        assertFalse(matcher.matches('B'));
        assertFalse(matcher.matches('C'));
    }
    
    @Test
    public void testFromBitSetWithMultipleChars() {
        BitSet chars = new BitSet();
        chars.set('A');
        chars.set('B');
        chars.set('C');
        String description = "Bitset with multiple chars";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertTrue(matcher.matches('A'));
        assertTrue(matcher.matches('B'));
        assertTrue(matcher.matches('C'));
        assertFalse(matcher.matches('D'));
    }
    
    // Test cases for matches() method
    
    @Test
    public void testMatchesZeroChar() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x1L, true, null);
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatchesPositiveChar() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'A', 'B', 'C'}, 0x7L, false, null);
        assertTrue(matcher.matches('B'));
    }
    
    @Test
    public void testMatchesNegativeChar() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'A', 'B', 'C'}, 0x7L, false, null);
        assertFalse(matcher.matches('D'));
    }
    
    // Test cases for setBits() method
    
    @Test
    public void testSetBitsWithZeroChar() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'A', '\0', 'C'}, 0x3L, true, null);
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('A'));
        assertFalse(table.get('B'));
        assertTrue(table.get('C'));
    }
    
    @Test
    public void testSetBitsWithoutZeroChar() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'A', 'B', 'C'}, 0x7L, false, null);
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertFalse(table.get(0));
        assertTrue(table.get('A'));
        assertTrue(table.get('B'));
        assertTrue(table.get('C'));
    }
}