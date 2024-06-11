import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        
        // Test matches() for a small character
        assertTrue(matcher.matches('a'));
        
        // Test matches() for a character that is not in the matcher
        assertFalse(matcher.matches('b'));
        
        // Test matches() for the character '\0'
        assertFalse(matcher.matches('\0'));
        
        // Test matches() for a character that is in the matcher but not matching
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "test");
        
        // Test setBits() for an empty table
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertEquals(0, table.cardinality());
        
        // Test setBits() for a table with elements
        BitSet table2 = new BitSet();
        table2.set('a');
        table2.set('b');
        table2.set('c');
        matcher.setBits(table2);
        assertEquals(3, table2.cardinality());
        assertTrue(table2.get('a'));
        assertTrue(table2.get('b'));
        assertTrue(table2.get('c'));
    }

}