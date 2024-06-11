import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(0, SmallCharMatcher.smear(0));
        assertEquals(265443576, SmallCharMatcher.smear(1));
        // Add more test cases for different inputs
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, false, "Test Description");
        assertTrue(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(0));
        // Add more test cases for various inputs
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        // Add more test cases for different input sizes
    }

    @Test
    public void testFrom() {
        // Create test for CharMatcher creation from BitSet
    }

    @Test
    public void testMatches() {
        // Create test for matches method, including edge and common cases
    }

    @Test
    public void testSetBits() {
        // Create test for setBits method, including edge and common cases
    }
}