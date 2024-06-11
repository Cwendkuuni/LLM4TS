import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 12345;
        int expectedResult = -1353727285;
        assertEquals(expectedResult, SmallCharMatcher.smear(hashCode));
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0b101L, false, "test");
        assertTrue(smallCharMatcher.checkFilter(0));
        assertFalse(smallCharMatcher.checkFilter(1));
    }
    
    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "test");
        
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0b101L, true, "test");
        assertTrue(smallCharMatcher.matches('a'));
        assertFalse(smallCharMatcher.matches('d'));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0b101L, true, "test");
        smallCharMatcher.setBits(table);
        
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertFalse(table.get('d'));
    }
}