import org.junit.*;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {
    @Test
    public void testMatches() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b101, true, "test");
        
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
    }
    
    @Test
    public void testSetBits() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0b101, true, "test");
        
        BitSet table = new BitSet();
        charMatcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('c'));
        assertFalse(table.get('d'));
    }
}