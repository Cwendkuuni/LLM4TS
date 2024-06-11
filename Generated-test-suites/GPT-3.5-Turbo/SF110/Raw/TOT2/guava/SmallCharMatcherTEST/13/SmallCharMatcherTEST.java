import static org.junit.Assert.*;
import org.junit.Test;
import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 12345;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(830502460, result);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[2], 1L, true, "test");
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(8, SmallCharMatcher.chooseTableSize(6));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        CharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches((char) 1));
        assertTrue(matcher.matches((char) 3));
        assertFalse(matcher.matches((char) 5));
    }

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{1, 3, 5}, 23L, true, "test");
        assertTrue(matcher.matches((char) 1));
        assertTrue(matcher.matches((char) 3));
        assertFalse(matcher.matches((char) 0));
        assertFalse(matcher.matches((char) 2));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{1, 3, 5}, 23L, true, "test");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get(1));
        assertTrue(table.get(3));
        assertTrue(table.get(5));
        assertFalse(table.get(2));
    }
}