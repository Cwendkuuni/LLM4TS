
import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertEquals(table.size(), 0);
    }

    @Test
    public void testSmear() {
        int hashCode = 12345;
        int smeared = SmallCharMatcher.smear(hashCode);
        assertNotEquals(hashCode, smeared);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        assertTrue(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(2));
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 100;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertTrue(tableSize >= setSize);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
    }

}
