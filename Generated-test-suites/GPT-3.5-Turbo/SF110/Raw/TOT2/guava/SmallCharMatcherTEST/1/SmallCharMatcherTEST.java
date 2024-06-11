import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 10;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(557496978, result);
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[2], 3L, true, "test");
        assertTrue(matcher.checkFilter(1));
    }
    
    @Test
    public void testChooseTableSize() {
        int setSize = 5;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(8, result);
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        CharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches('a'));
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{ 'a' }, 1L, false, "test");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet(2);
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{ 'a' }, 1L, true, "test");
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
    }
}