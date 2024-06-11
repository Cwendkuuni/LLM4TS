
import org.junit.Test;
import com.google.common.base.SmallCharMatcher;

import java.util.BitSet;

import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    // Test matches method
    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");

        // Test for matching character
        assertTrue(matcher.matches('a'));

        // Test for non-matching character
        assertFalse(matcher.matches('b'));

        // Test for null character
        assertTrue(matcher.matches('\0'));
    }

    // Test setBits method
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");

        matcher.setBits(table);

        // Test for setting bits correctly
        // (assume matching character is 'a')
        assertFalse(table.get('b'));
        assertTrue(table.get('a'));
        assertTrue(table.get('\0'));
    }

    // Test checkFilter method
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");

        // Test for checking filter
        assertTrue(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(2));
    }

    // Test chooseTableSize method
    @Test
    public void testChooseTableSize() {
        // Test for set size of 1
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));

        // Test for set size of 5
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));

        // Test for set size of 10
        assertEquals(16, SmallCharMatcher.chooseTableSize(10));
    }

    // Test smear method
    @Test
    public void testSmear() {
        // Test for positive integer
        assertEquals(532532, SmallCharMatcher.smear(123456));

        // Test for negative integer
        assertEquals(-12182016, SmallCharMatcher.smear(-4321));
    }
}
