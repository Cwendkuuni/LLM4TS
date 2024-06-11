import org.junit.Before;
import org.junit.Test;
import java.util.BitSet;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {
    
    @Test
    public void testMatches_containsZero() {
        BitSet chars = new BitSet();
        chars.set(0);
        String description = "Contains Zero";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_doesNotContainZero() {
        BitSet chars = new BitSet();
        String description = "Does Not Contain Zero";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_checkFilter() {
        BitSet chars = new BitSet();
        chars.set(97);
        String description = "Check Filter";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
    }
    
    @Test
    public void testMatches_tableSize() {
        BitSet chars = new BitSet();
        String description = "Table Size";
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertEquals(2, matcher.table.length);
    }
    
    @Test
    public void testMatches_smear() {
        int hashCode = 97;
        assertEquals(-2038120328, SmallCharMatcher.smear(hashCode));
    }
    
    @Test
    public void testChoostTableSize() {
        int setSize = 1;
        assertEquals(2, SmallCharMatcher.chooseTableSize(setSize));
    }
    
    @Test
    public void testSetBits_containsZero() {
        BitSet table = new BitSet();
        String description = "Set Bits Contains Zero";
        CharMatcher matcher = new SmallCharMatcher(null, 0, true, description);
        matcher.setBits(table);
        assertTrue(table.get(0));
    }
    
    @Test
    public void testSetBits_doesNotContainZero() {
        BitSet table = new BitSet();
        String description = "Set Bits Does Not Contain Zero";
        CharMatcher matcher = new SmallCharMatcher(null, 0, false, description);
        matcher.setBits(table);
        assertFalse(table.get(0));
    }
}