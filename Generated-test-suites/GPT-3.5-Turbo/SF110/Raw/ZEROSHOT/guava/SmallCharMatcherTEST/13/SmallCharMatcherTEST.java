import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    private SmallCharMatcher matcher;

    @Before
    public void setUp() {
        char[] table = new char[SmallCharMatcher.MAX_SIZE];
        long filter = 0L;
        boolean containsZero = true;
        String description = "test";
        matcher = new SmallCharMatcher(table, filter, containsZero, description);
    }

    @Test
    public void testMatches_containsZero() {
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_checkFilter() {
        assertFalse(matcher.matches('a'));
    }

    @Test
    public void testMatches_notInTable() {
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testMatches_inTable() {
        char[] table = new char[]{ 'a', 'b', 'c' };
        long filter = 0L;
        boolean containsZero = true;
        String description = "test";
        matcher = new SmallCharMatcher(table, filter, containsZero, description);

        assertTrue(matcher.matches('b'));
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet table = new BitSet();
        matcher.setBits(table);

        assertTrue(table.get(0));
    }

    @Test
    public void testSetBits_notInTable() {
        BitSet table = new BitSet();
        matcher.setBits(table);

        assertFalse(table.get('a'));
    }

    @Test
    public void testSetBits_inTable() {
        char[] table = new char[]{ 'a', 'b', 'c' };
        long filter = 0L;
        boolean containsZero = true;
        String description = "test";
        matcher = new SmallCharMatcher(table, filter, containsZero, description);

        BitSet bitSet = new BitSet();
        matcher.setBits(bitSet);

        assertTrue(bitSet.get('a'));
        assertTrue(bitSet.get('b'));
        assertTrue(bitSet.get('c'));
    }
}