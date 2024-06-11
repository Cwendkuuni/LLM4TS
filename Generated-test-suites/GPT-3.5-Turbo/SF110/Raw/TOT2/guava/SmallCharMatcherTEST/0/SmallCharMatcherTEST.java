import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 123;
        assertEquals(1234567, SmallCharMatcher.smear(hashCode));
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[256], 0x1L, true, "test");
        assertTrue(smallCharMatcher.checkFilter(0));
    }
    
    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(97);
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "abc");
        assertTrue(charMatcher.matches('a'));
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a'}, 0x1L, true, "test");
        assertTrue(smallCharMatcher.matches('a'));
        assertFalse(smallCharMatcher.matches('b'));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a'}, 0x1L, true, "test");
        smallCharMatcher.setBits(table);
        assertTrue(table.get('a'));
    }
}