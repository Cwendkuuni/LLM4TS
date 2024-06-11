import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        // Typical use case
        int hashCode = 12345;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(543621286, result);

        // Edge case
        hashCode = 0;
        result = SmallCharMatcher.smear(hashCode);
        assertEquals(0, result);

        // Error scenario
        hashCode = -12345;
        result = SmallCharMatcher.smear(hashCode);
        assertEquals(-1900553927, result);
    }

    @Test
    public void testCheckFilter() {
        // Typical use case
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        boolean result = matcher.checkFilter(5);
        assertFalse(result);

        // Edge case
        result = matcher.checkFilter(0);
        assertFalse(result);

        // Error scenario
        result = matcher.checkFilter(-5);
        assertFalse(result);
    }

    @Test
    public void testChooseTableSize() {
        // Typical use case
        int setSize = 10;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(16, result);

        // Edge case
        setSize = 1;
        result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(2, result);

        // Error scenario
        setSize = -10;
        result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(0, result);
    }

    @Test
    public void testFrom() {
        // Typical use case
        BitSet chars = new BitSet();
        chars.set(5);
        chars.set(10);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        boolean result = matcher.matches((char)5);
        assertTrue(result);
        result = matcher.matches((char)10);
        assertTrue(result);
        result = matcher.matches((char)15);
        assertFalse(result);

        // Edge case
        chars.clear();
        chars.set(0);
        matcher = SmallCharMatcher.from(chars, "test");
        result = matcher.matches((char)0);
        assertTrue(result);
        result = matcher.matches((char)1);
        assertFalse(result);

        // Error scenario
        matcher = SmallCharMatcher.from(null, "test");
        result = matcher.matches((char)5);
        assertFalse(result);
    }

    @Test
    public void testMatches() {
        // Typical use case
        BitSet chars = new BitSet();
        chars.set(5);
        chars.set(10);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        boolean result = matcher.matches((char)5);
        assertTrue(result);
        result = matcher.matches((char)10);
        assertTrue(result);
        result = matcher.matches((char)15);
        assertFalse(result);

        // Edge case
        matcher = SmallCharMatcher.from(chars, "test");
        result = matcher.matches((char)0);
        assertFalse(result);

        // Error scenario
        matcher = SmallCharMatcher.from(chars, "test");
        result = matcher.matches('\0');  // containsZero = true
        assertTrue(result);
    }

    @Test
    public void testSetBits() {
        // Typical use case
        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        matcher.setBits(table);
        for (int i = 0; i < table.length(); i++) {
            if (i == 0) {
                assertTrue(table.get(i));
            } else {
                assertFalse(table.get(i));
            }
        }

        // Edge case
        table.clear();
        matcher = SmallCharMatcher.from(new BitSet(), "test");
        matcher.setBits(table);
        for (int i = 0; i < table.length(); i++) {
            assertFalse(table.get(i));
        }

        // Error scenario
        table = null;
        matcher = SmallCharMatcher.from(new BitSet(), "test");
        matcher.setBits(table);
        assertNull(table);
    }
}