import org.junit.Test;
import com.google.common.base.CharMatcher;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "matcher");
        
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "matcher");
        
        BitSet table = new BitSet();
        matcher.setBits(table);
        
        assertEquals(0, table.cardinality());
    }
    
    @Test
    public void testSmear() {
        int hashCode = 123456;
        int smeared = SmallCharMatcher.smear(hashCode);
        
        // Add appropriate assertions here
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "matcher");
        
        assertTrue(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(2));
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 3;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        
        // Add appropriate assertions here
    }
    
    // Add additional test cases for edge cases and exception handling scenarios if needed
    
}

