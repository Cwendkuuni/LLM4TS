import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testConstructor() {
        char[] table = new char[] {'a', 'b', 'c'};
        long filter = 7;
        boolean containsZero = true;
        String description = "Test";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);

        assertArrayEquals(table, matcher.table);
        assertEquals(filter, matcher.filter);
        assertEquals(containsZero, matcher.containsZero);
    }

    @Test
    public void testSmear() {
        int hashCode = 42;
        int expectedResult = -1147759026;
        int result = SmallCharMatcher.smear(hashCode);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testCheckFilter_true() {
        int c = 1;
        boolean expectedResult = true;
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 1, true, "Test");

        boolean result = matcher.checkFilter(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testCheckFilter_false() {
        int c = 1;
        boolean expectedResult = false;
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0, true, "Test");

        boolean result = matcher.checkFilter(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testChooseTableSize_sizeIs1() {
        int setSize = 1;
        int expectedResult = 2;

        int result = SmallCharMatcher.chooseTableSize(setSize);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testChooseTableSize_sizeIsNot1() {
        int setSize = 5;
        int expectedResult = 8;

        int result = SmallCharMatcher.chooseTableSize(setSize);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        String description = "Test";
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);

        char[] expectedTable = new char[]{(char)97, (char)98, (char)0};
        long expectedFilter = 3;
        boolean expectedContainsZero = true;

        assertArrayEquals(expectedTable, matcher.table);
        assertEquals(expectedFilter, matcher.filter);
        assertEquals(expectedContainsZero, matcher.containsZero);
    }

    @Test
    public void testMatches_containsZero() {
        char c = 0;
        boolean expectedResult = true;
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 1, true, "Test");

        boolean result = matcher.matches(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testMatches_notContainsZero() {
        char c = 0;
        boolean expectedResult = false;
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 1, false, "Test");

        boolean result = matcher.matches(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testMatches_checkFilterTrue_charInTable() {
        char c = 'a';
        boolean expectedResult = true;
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 1, false, "Test");

        boolean result = matcher.matches(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testMatches_checkFilterFalse() {
        char c = 'a';
        boolean expectedResult = false;
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 1, false, "Test");

        boolean result = matcher.matches(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testMatches_charNotInTable() {
        char c = 'a';
        boolean expectedResult = false;
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'b'}, 1, false, "Test");

        boolean result = matcher.matches(c);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 1, true, "Test");
        BitSet expectedTable = new BitSet();
        expectedTable.set(0);

        BitSet table = new BitSet();

        matcher.setBits(table);

        assertEquals(expectedTable, table);
    }

    @Test
    public void testSetBits_notContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 1, false, "Test");
        BitSet expectedTable = new BitSet();

        BitSet table = new BitSet();

        matcher.setBits(table);

        assertEquals(expectedTable, table);
    }

    @Test
    public void testSetBits_tableNotEmpty() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 1, false, "Test");
        BitSet expectedTable = new BitSet();
        expectedTable.set('a');

        BitSet table = new BitSet();

        matcher.setBits(table);

        assertEquals(expectedTable, table);
    }
}