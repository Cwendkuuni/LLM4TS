import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        assertEquals(SmallCharMatcher.smear(5), -1808632427);
    }
    
    @Test
    public void testChooseTableSize() {
        assertEquals(SmallCharMatcher.chooseTableSize(2), 2);
        assertEquals(SmallCharMatcher.chooseTableSize(10), 16);
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "Test Matcher");
        
        assertTrue(charMatcher.matches('1'));
        assertFalse(charMatcher.matches('2'));
        assertTrue(charMatcher.matches('3'));
    }
    
    @Test
    public void testMatches() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "Test Matcher");
        
        assertTrue(charMatcher.matches('1'));
        assertFalse(charMatcher.matches('2'));
        assertTrue(charMatcher.matches('3'));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        
        SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Test Matcher");
        charMatcher.setBits(table);
        
        assertTrue(table.get(0));
    }
}