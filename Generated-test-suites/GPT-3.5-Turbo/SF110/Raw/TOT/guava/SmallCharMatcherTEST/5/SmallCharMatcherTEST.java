
import org.junit.Test;
import java.util.BitSet;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testConstructor() {
        char[] table = {'a', 'b', 'c', 'd', 'e'};
        long filter = 31L;
        boolean containsZero = true;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Description");

        assertArrayEquals(table, matcher.getTable());
        assertEquals(filter, matcher.getFilter());
        assertEquals(containsZero, matcher.containsZero());
    }

    @Test
    public void testSmear() {
        int hashCode = 12345;
        int result = SmallCharMatcher.smear(hashCode);

        // Test expected value for given hashCode
        assertEquals(-386050123, result);
    }

    @Test
    public void testCheckFilter_true() {
        int c = 5;
        int filter = 1;
        boolean result = new SmallCharMatcher(null, filter, false, null).checkFilter(c);

        assertTrue(result);
    }

    @Test
    public void testCheckFilter_false() {
        int c = 5;
        int filter = 0;
        boolean result = new SmallCharMatcher(null, filter, false, null).checkFilter(c);

        assertFalse(result);
    }

    @Test
    public void testChooseTableSize_setSizeIsOne() {
        int setSize = 1;
        int result = SmallCharMatcher.chooseTableSize(setSize);

        assertEquals(2, result);
    }

    @Test
    public void testChooseTableSize_setSizeIsGreaterThanOne() {
        int setSize = 4;
        int result = SmallCharMatcher.chooseTableSize(setSize);

        assertEquals(8, result);
    }

    @Test
    public void testFrom_singleCharBitSet() {
        BitSet chars = new BitSet();
        chars.set(5);
        String description = "Test Description";
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);

        assertTrue(matcher.matches('e'));
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testFrom_multipleCharBitSet() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        chars.set(5);
        String description = "Test Description";
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);

        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('d'));
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('c'));
        assertFalse(matcher.matches('e'));
    }

    @Test
    public void testMatches_containsZero() {
        char[] table = {'a', 'b', 'c', 'd', 'e'};
        long filter = 31L;
        boolean containsZero = true;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Description");

        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_matchingChar() {
        char[] table = {'a', 'b', 'c', 'd', 'e'};
        long filter = 31L;
        boolean containsZero = false;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Description");

        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_nonMatchingChar() {
        char[] table = {'a', 'b', 'c', 'd', 'e'};
        long filter = 31L;
        boolean containsZero = false;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Description");

        assertFalse(matcher.matches('f'));
    }

    @Test
    public void testMatches_circularTableTraversal() {
        char[] table = {'a', 'b', 'c', 'd', 'e'};
        long filter = 31L;
        boolean containsZero = false;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Description");

        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testSetBits_containsZero() {
        char[] table = {'a', 'b', 'c', 'd', 'e'};
        long filter = 31L;
        boolean containsZero = true;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Description");

        BitSet result = new BitSet();
        matcher.setBits(result);

        assertTrue(result.get(0));
        assertFalse(result.get(1));
        assertFalse(result.get(2));
        assertFalse(result.get(3));
        assertFalse(result.get(4));
    }

    @Test
    public void testSetBits_nonZeroChars() {
        char[] table = {'a', 'b', 'c', 'd', 'e'};
        long filter = 31L;
        boolean containsZero = false;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Description");

        BitSet result = new BitSet();
        matcher.setBits(result);

        assertFalse(result.get(0));
        assertTrue(result.get(1));
        assertTrue(result.get(2));
        assertTrue(result.get(3));
        assertTrue(result.get(4));
    }
}
