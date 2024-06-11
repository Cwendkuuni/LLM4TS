import org.junit.Test;
import java.util.BitSet;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        assertEquals(matcher.matches('\0'), matcher.containsZero);
    }

    @Test
    public void testMatches_checkFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testMatches_validChar() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_invalidChar() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        matcher.setBits(table);
        assertEquals(table.get(0), matcher.containsZero);
    }

    @Test
    public void testSetBits_validChar() {
        BitSet table = new BitSet();
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        matcher.setBits(table);
        assertTrue(table.get('a'));
    }

    @Test
    public void testSetBits_invalidChar() {
        BitSet table = new BitSet();
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "test");
        matcher.setBits(table);
        assertFalse(table.get('b'));
    }
}