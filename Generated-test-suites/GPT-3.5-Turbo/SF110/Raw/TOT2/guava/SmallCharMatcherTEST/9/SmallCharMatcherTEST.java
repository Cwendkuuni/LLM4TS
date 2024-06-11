import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        int result = SmallCharMatcher.smear(5);
        assertEquals(123456789, result);
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[1], 1, true, "test");
        boolean result = smallCharMatcher.checkFilter(0);
        assertTrue(result);
    }
    
    @Test
    public void testChooseTableSize() {
        int result = SmallCharMatcher.chooseTableSize(5);
        assertEquals(8, result);
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher smallCharMatcher = SmallCharMatcher.from(new BitSet(), "test");
        boolean result = smallCharMatcher.matches('a');
        assertFalse(result);
    }
    
    @Test
    public void testSetBits() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{}, 0, false, "test");
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        assertEquals(0, table.size());
    }
}