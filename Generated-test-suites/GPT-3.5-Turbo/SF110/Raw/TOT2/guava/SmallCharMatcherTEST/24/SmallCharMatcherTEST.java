import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        int hashCode = 10;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(276520759, result);
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[10], 12345L, false, "Test Description");
        assertTrue(charMatcher.checkFilter(3));
    }
    
    @Test
    public void testChooseTableSize() {
        int setSize = 7;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(8, result);
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "Test Description");
        assertTrue(charMatcher.matches('1'));
        assertTrue(charMatcher.matches('3'));
    }
    
    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[]{}, 12345L, true, "Test Description");
        assertTrue(charMatcher.matches('\0'));
    }
    
    @Test
    public void testMatches_checkFilterFalse() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[]{}, 5L, false, "Test Description");
        assertFalse(charMatcher.matches('3'));
    }
    
    @Test
    public void testMatches_notFound() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[]{1, 2}, 12345L, false, "Test Description");
        assertFalse(charMatcher.matches('3'));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[]{1, 2}, 12345L, true, "Test Description");
        charMatcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get(1));
        assertTrue(table.get(2));
        assertFalse(table.get(3));
    }
}