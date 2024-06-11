import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 1234;
        int expected = -862048943 * Integer.rotateLeft(1234 * -862048943, 15);
        int actual = SmallCharMatcher.smear(hashCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckFilter_whenFilterBitSet_ShouldReturnTrue() {
        int c = 5;
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x000000000000001FL, false, null);
        assertTrue(matcher.checkFilter(c));
    }

    @Test
    public void testCheckFilter_whenFilterBitNotSet_ShouldReturnFalse() {
        int c = 10;
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x0000000000000000L, false, null);
        assertFalse(matcher.checkFilter(c));
    }

    @Test
    public void testChooseTableSize_whenSetSizeIsOne_ShouldReturnTwo() {
        int setSize = 1;
        int expected = 2;
        int actual = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expected, actual);
    }

    @Test
    public void testChooseTableSize_whenSetSizeIsPositive_ShouldReturnTableSize() {
        int setSize = 10;
        int expected = 16;
        int actual = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expected, actual);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        chars.set(3);
        String description = "Test Description";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);

        // Typical use cases
        assertTrue(matcher.matches((char)1));
        assertTrue(matcher.matches((char)2));
        assertFalse(matcher.matches((char)0));
        
        // Edge cases
        assertFalse(matcher.matches((char)100));
        assertFalse(matcher.matches((char)1023));

        // Error scenarios
        assertFalse(matcher.matches((char)-1));
        assertFalse(matcher.matches((char)65536));
    }

    @Test
    public void testMatches_whenCharacterIsZero_ShouldReturnContainsZeroValue() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x000000000000001FL, true, null);
        assertTrue(matcher.matches((char)0));
    }

    @Test
    public void testMatches_whenCharacterNotInFilter_ShouldReturnFalse() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x000000000000001FL, true, null);
        assertFalse(matcher.matches((char)10));
    }

    @Test
    public void testMatches_whenCharacterInFilter_ShouldReturnTrue() {
        char[] table = {(char)10, (char)20};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x000000000000001FL, true, null);
        assertTrue(matcher.matches((char)10));
    }

    @Test
    public void testMatches_whenCharacterNotInTable_ShouldReturnFalse() {
        char[] table = {(char)10, (char)20};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x000000000000001FL, true, null);
        assertFalse(matcher.matches((char)15));
    }

    @Test
    public void testSetBits_shouldSetBitsInTable() {
        BitSet table = new BitSet();
        char[] inputTable = {(char)10, (char)20};
        SmallCharMatcher matcher = new SmallCharMatcher(inputTable, 0x000000000000001FL, true, null);
        matcher.setBits(table);

        assertTrue(table.get(0));
        assertTrue(table.get(10));
        assertTrue(table.get(20));
    }

}