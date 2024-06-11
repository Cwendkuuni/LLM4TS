import static org.junit.Assert.*;
import org.junit.Test;
import java.util.BitSet;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        int hashCode = 123;
        assertEquals(987654321, SmallCharMatcher.smear(hashCode));
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[256], 0x1L, true, "Test");
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
    }
    
    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] { 'a', 'b', 'c' }, 0b101, true, "Test");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('z'));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] { 'd', 'e', 'f' }, 0b110, false, "Test");
        matcher.setBits(table);
        assertTrue(table.get('d'));
        assertTrue(table.get('e'));
        assertFalse(table.get('a'));
    }
    
    @Test
    public void testEmptyString() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0, false, "Test");
        assertFalse(matcher.matches('a'));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidChar() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[] { 'x', 'y', 'z' }, 0b11, false, "Test");
        matcher.matches('1');
    }
    
}