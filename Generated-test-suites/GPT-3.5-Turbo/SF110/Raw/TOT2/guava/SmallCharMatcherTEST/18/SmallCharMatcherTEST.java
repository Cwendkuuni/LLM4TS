import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Description");

        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('z'));
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Description");
        matcher.setBits(table);

        assertEquals(1, table.cardinality());
        assertTrue(table.get(0));
    }

    @Test
    public void testSmear() {
        int hashCode = 12345;
        int result = SmallCharMatcher.smear(hashCode);

        // Check expected value
        assertEquals(-20447014, result);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Description");

        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(5));
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 10;
        int result = SmallCharMatcher.chooseTableSize(setSize);

        // Check expected value
        assertEquals(16, result);
    }

}