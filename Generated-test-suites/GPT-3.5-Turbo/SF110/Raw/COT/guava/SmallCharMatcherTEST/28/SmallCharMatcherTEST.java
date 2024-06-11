
import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 1;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(-1835638318, result);
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
        chars.set(0);
        chars.set(1);

        CharMatcher matcher = SmallCharMatcher.from(chars, "description");

        assertTrue(matcher.matches('\0'));
        assertTrue(matcher.matches('\1'));
        assertFalse(matcher.matches('\2'));
    }

    @Test
    public void testMatches() {
        char[] table = { '\0', '\1', '\2' };
        long filter = 3L;
        boolean containsZero = true;
        String description = "description";

        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);

        assertTrue(matcher.matches('\0'));
        assertTrue(matcher.matches('\1'));
        assertTrue(matcher.matches('\2'));
        assertFalse(matcher.matches('\3'));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();

        char[] chars = { '\0', '\1', '\2' };
        long filter = 3L;
        boolean containsZero = true;
        String description = "description";

        SmallCharMatcher matcher = new SmallCharMatcher(chars, filter, containsZero, description);

        matcher.setBits(table);

        assertTrue(table.get(0));
        assertTrue(table.get(1));
        assertTrue(table.get(2));
        assertFalse(table.get(3));
    }
}

