import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        // Typical use case
        assertEquals(12345, SmallCharMatcher.smear(12345));
        
        // Edge case
        assertEquals(0, SmallCharMatcher.smear(0));
    }

    @Test
    public void testCheckFilter() {
        // Typical use case
        SmallCharMatcher matcher = new SmallCharMatcher(new char[10], 0x1L, true, "Test");
        
        assertTrue(matcher.checkFilter(1));
        
        assertFalse(matcher.checkFilter(2));
    }

    @Test
    public void testChooseTableSize() {
        // Typical use case
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        
        assertEquals(16, SmallCharMatcher.chooseTableSize(6));
    }

    @Test
    public void testFrom() {
        // Typical use case
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "Test");
        
        assertTrue(charMatcher.matches('1'));
        
        assertFalse(charMatcher.matches('2'));
    }

    @Test
    public void testMatches() {
        // Typical use case
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{1, 3, 5}, 0x1L, true, "Test");
        
        assertTrue(matcher.matches('1'));
        
        assertFalse(matcher.matches('2'));
    }

    @Test
    public void testSetBits() {
        // Typical use case
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{1, 3, 5}, 0x1L, true, "Test");
        matcher.setBits(table);
        
        assertTrue(table.get(1));
        assertTrue(table.get(3));
        assertTrue(table.get(5));
        
        // Error scenario
        assertFalse(table.get(0));
    }
}