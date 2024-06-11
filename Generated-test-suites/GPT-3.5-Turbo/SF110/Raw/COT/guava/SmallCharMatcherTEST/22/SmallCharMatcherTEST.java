
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "description");
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_zeroFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "description");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testMatches_true() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x7L, false, "description");
        assertTrue(matcher.matches('b'));
    }

    @Test
    public void testMatches_false() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0x6L, false, "description");
        assertFalse(matcher.matches('d'));
    }     

    @Test
    public void testSetBits_containsZero() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "description");
        matcher.setBits(table);
        assertTrue(table.get(0));
    }
    
    @Test
    public void testSetBits_noContainsZero() {
        BitSet table = new BitSet();
        char[] chars = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(chars, 0x7L, false, "description");
        matcher.setBits(table);
        assertFalse(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
        assertFalse(table.get('d'));
    }
}
