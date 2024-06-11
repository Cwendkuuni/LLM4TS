import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testConstructor() {
        char[] table = new char[] { 'a', 'b', 'c' };
        long filter = 0L;
        boolean containsZero = true;
        String description = "Test Description";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        assertEquals(table, matcher.table);
        assertEquals(filter, matcher.filter);
        assertEquals(containsZero, matcher.containsZero);
        assertEquals(description, matcher.getDescription());
    }

    @Test
    public void testSmear() {
        int hashCode = 12345;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(-187032711, result);
    }

    @Test
    public void testCheckFilter() {
        char[] table = new char[] { 'a', 'b', 'c' };
        long filter = 0x7L;
        boolean containsZero = true;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Description");
        assertTrue(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));
        assertTrue(matcher.checkFilter(2));
        assertFalse(matcher.checkFilter(3));
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 10;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(16, result);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        chars.set(3);
        String description = "Test Description";
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertTrue(matcher.checkFilter(1));
        assertTrue(matcher.checkFilter(2));
        assertTrue(matcher.checkFilter(3));
        assertFalse(matcher.checkFilter(4));
        assertEquals(description, matcher.getDescription());
    }

    @Test
    public void testMatches() {
        char[] table = new char[] { 'a', 'b', 'c' };
        long filter = 0x7L;
        boolean containsZero = false;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Description");
        assertFalse(matcher.matches('z'));
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
    }

    @Test
    public void testSetBits() {
        char[] table = new char[] { 'a', 'b', 'c' };
        long filter = 0x7L;
        boolean containsZero = true;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Description");
        BitSet expected = new BitSet();
        expected.set(0);
        expected.set('a');
        expected.set('b');
        expected.set('c');
        BitSet result = new BitSet();
        matcher.setBits(result);
        assertEquals(expected, result);
    }
}