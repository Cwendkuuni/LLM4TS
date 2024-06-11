import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    // Test cases for constructor
    @Test
    public void testConstructor() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0x1L;
        boolean containsZero = true;
        String description = "SmallCharMatcher";
        
        SmallCharMatcher scm = new SmallCharMatcher(table, filter, containsZero, description);
        
        assertEquals(table, scm.table);
        assertEquals(filter, scm.filter);
        assertEquals(containsZero, scm.containsZero);
        assertEquals(description, scm.getDescription());
        
    }

    // Test cases for smear method
    @Test
    public void testSmear() {
        int hashCode = 1234;
        
        int result = SmallCharMatcher.smear(hashCode);
        
        // Perform expected calculation
        int expectedResult = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
        
        assertEquals(expectedResult, result);
    }

    // Test cases for checkFilter method
    @Test
    public void testCheckFilter_whenFilterContainsC_returnTrue() {
        SmallCharMatcher scm = new SmallCharMatcher(new char[1], 0x4L, false, "SmallCharMatcher");
        int c = 2;

        boolean result = scm.checkFilter(c);

        assertTrue(result);
    }

    @Test
    public void testCheckFilter_whenFilterDoesNotContainC_returnFalse() {
        SmallCharMatcher scm = new SmallCharMatcher(new char[1], 0x1L, false, "SmallCharMatcher");
        int c = 2;

        boolean result = scm.checkFilter(c);

        assertFalse(result);
    }

    // Test cases for chooseTableSize method
    @Test
    public void testChooseTableSize_whenSetSizeIs1_return2() {
        int setSize = 1;

        int result = SmallCharMatcher.chooseTableSize(setSize);

        assertEquals(2, result);
    }

    @Test
    public void testChooseTableSize_whenSetSizeIs5_return8() {
        int setSize = 5;

        int result = SmallCharMatcher.chooseTableSize(setSize);

        assertEquals(8, result);
    }

    @Test
    public void testChooseTableSize_whenSetSizeIs16_return32() {
        int setSize = 16;

        int result = SmallCharMatcher.chooseTableSize(setSize);

        assertEquals(32, result);
    }

    // Test cases for from method
    @Test
    public void testFrom_whenCharsIsEmpty_returnEmptySmallCharMatcher() {
        BitSet chars = new BitSet();
        String description = "Empty SmallCharMatcher";

        CharMatcher result = SmallCharMatcher.from(chars, description);

        assertEquals(description, result.getDescription());
        assertEquals(false, result.matches('a'));
    }

    @Test
    public void testFrom_whenCharsHasSingleElement_returnSmallCharMatcherWithSingleElement() {
        BitSet chars = new BitSet();
        chars.set('a');
        String description = "SmallCharMatcher with single element";

        CharMatcher result = SmallCharMatcher.from(chars, description);

        assertEquals(description, result.getDescription());
        assertEquals(true, result.matches('a'));
    }

    @Test
    public void testFrom_whenCharsHasMultipleElements_returnSmallCharMatcherWithMultipleElements() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        String description = "SmallCharMatcher with multiple elements";

        CharMatcher result = SmallCharMatcher.from(chars, description);

        assertEquals(description, result.getDescription());
        assertEquals(true, result.matches('a'));
        assertEquals(true, result.matches('b'));
    }

    // Test cases for matches method
    @Test
    public void testMatches_whenCharIsZeroAndContainsZeroIsTrue_returnTrue() {
        SmallCharMatcher scm = new SmallCharMatcher(new char[1], 0x1L, true, "SmallCharMatcher");
        char c = '\0';

        boolean result = scm.matches(c);

        assertTrue(result);
    }

    @Test
    public void testMatches_whenCharIsZeroAndContainsZeroIsFalse_returnFalse() {
        SmallCharMatcher scm = new SmallCharMatcher(new char[1], 0x1L, false, "SmallCharMatcher");
        char c = '\0';

        boolean result = scm.matches(c);

        assertFalse(result);
    }

    @Test
    public void testMatches_whenCharIsInFilterAndInTable_returnTrue() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher scm = new SmallCharMatcher(table, 0x7L, false, "SmallCharMatcher");
        char c = 'b';

        boolean result = scm.matches(c);

        assertTrue(result);
    }

    @Test
    public void testMatches_whenCharIsNotInFilter_returnFalse() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher scm = new SmallCharMatcher(table, 0x2L, false, "SmallCharMatcher");
        char c = 'b';

        boolean result = scm.matches(c);

        assertFalse(result);
    }

    @Test
    public void testMatches_whenCharIsInFilterButNotInTable_returnFalse() {
        char[] table = {'a', 'c'};
        SmallCharMatcher scm = new SmallCharMatcher(table, 0x5L, false, "SmallCharMatcher");
        char c = 'b';

        boolean result = scm.matches(c);

        assertFalse(result);
    }

    // Test cases for setBits method
    @Test
    public void testSetBits_whenContainsZeroIsTrue_setBitAtZeroIndex() {
        SmallCharMatcher scm = new SmallCharMatcher(new char[1], 0x1L, true, "SmallCharMatcher");
        BitSet table = new BitSet();

        scm.setBits(table);

        assertEquals(true, table.get(0));
    }

    @Test
    public void testSetBits_whenContainsZeroIsFalse_doNotSetBitAtZeroIndex() {
        SmallCharMatcher scm = new SmallCharMatcher(new char[1], 0x1L, false, "SmallCharMatcher");
        BitSet table = new BitSet();

        scm.setBits(table);

        assertEquals(false, table.get(0));
    }

    @Test
    public void testSetBits_whenTableHasElements_setBitsInTable() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher scm = new SmallCharMatcher(table, 0x7L, false, "SmallCharMatcher");
        BitSet result = new BitSet();

        scm.setBits(result);

        assertEquals(true, result.get('a'));
        assertEquals(true, result.get('b'));
        assertEquals(true, result.get('c'));
    }

}