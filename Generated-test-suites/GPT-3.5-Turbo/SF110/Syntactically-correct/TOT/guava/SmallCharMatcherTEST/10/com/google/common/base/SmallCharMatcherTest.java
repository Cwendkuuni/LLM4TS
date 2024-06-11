
import org.junit.*;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        // Typical use case
        int hash = 12345;
        int result = SmallCharMatcher.smear(hash);
        assertEquals(-1915149385, result);
        
        // Edge case
        hash = 0;
        result = SmallCharMatcher.smear(hash);
        assertEquals(0, result);
    }

    @Test
    public void testCheckFilter() {
        // Typical use case - true
        SmallCharMatcher matcher = new SmallCharMatcher(null, 12345, true, null);
        boolean result = matcher.checkFilter(1);
        assertTrue(result);
        
        // Typical use case - false
        matcher = new SmallCharMatcher(null, 12345, false, null);
        result = matcher.checkFilter(1);
        assertFalse(result);
        
        // Edge case
        matcher = new SmallCharMatcher(null, 0, false, null);
        result = matcher.checkFilter(1);
        assertFalse(result);
    }

    @Test
    public void testChooseTableSize() {
        // Typical use case
        int setSize = 10;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(16, result);
        
        // Edge case
        setSize = 1;
        result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(2, result);
    }

    @Test
    public void testFrom() {
        // Typical use case
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        chars.set(2);
        String description = "Test SmallCharMatcher";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertTrue(matcher.matches('\0'));
        assertTrue(matcher.matches('A'));
        assertTrue(matcher.matches('B'));
        assertFalse(matcher.matches('C'));
        
        // Edge case - empty chars
        chars = new BitSet();
        description = "Test Empty SmallCharMatcher";
        matcher = SmallCharMatcher.from(chars, description);
        assertFalse(matcher.matches('\0'));
        assertFalse(matcher.matches('A'));
        assertFalse(matcher.matches('B'));
        assertFalse(matcher.matches('C'));
    }

    @Test
    public void testMatches() {
        // Typical use case - matches
        char c = '\0';
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0, true, null);
        assertTrue(matcher.matches(c));
        
        // Typical use case - does not match
        c = 'A';
        matcher = new SmallCharMatcher(null, 0, false, null);
        assertFalse(matcher.matches(c));
        
        // Edge case - empty table
        c = 'A';
        matcher = new SmallCharMatcher(new char[0], 0, false, null);
        assertFalse(matcher.matches(c));
    }

    @Test
    public void testSetBits() {
        // Typical use case
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'A','B','C'}, 0, true, null);
        matcher.setBits(table);
        assertFalse(table.get(0));
        assertTrue(table.get('A'));
        assertTrue(table.get('B'));
        assertTrue(table.get('C'));
        
        // Edge case - empty table
        table = new BitSet();
        matcher = new SmallCharMatcher(new char[0], 0, true, null);
        matcher.setBits(table);
        assertFalse(table.get(0));
    }
}

