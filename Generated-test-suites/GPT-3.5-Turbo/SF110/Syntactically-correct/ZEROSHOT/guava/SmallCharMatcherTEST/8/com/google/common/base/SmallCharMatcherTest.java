import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SmallCharMatcherTest {
    
    @Test
    public void testMatches_containsZero() {
        BitSet chars = new BitSet();
        chars.set(0);
        String description = "Test Matcher";
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_notContainsZero() {
        BitSet chars = new BitSet();
        String description = "Test Matcher";
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_checkFilter_true() {
        BitSet chars = new BitSet();
        chars.set(1);
        String description = "Test Matcher";
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        assertTrue(matcher.matches((char)1));
    }
    
    @Test
    public void testMatches_checkFilter_false() {
        BitSet chars = new BitSet();
        String description = "Test Matcher";
        
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        assertFalse(matcher.matches((char)1));
    }
    
    @Test
    public void testSetBits_containsZero() {
        BitSet table = new BitSet();
        String description = "Test Matcher";
        
        char[] chars = {'a', '\0', 'b'};
        SmallCharMatcher matcher = new SmallCharMatcher(chars, 0L, true, description);
        
        matcher.setBits(table);
        
        assertTrue(table.get(0));
    }
    
    @Test
    public void testSetBits_notContainsZero() {
        BitSet table = new BitSet();
        String description = "Test Matcher";
        
        char[] chars = {'a', 'b'};
        SmallCharMatcher matcher = new SmallCharMatcher(chars, 0L, false, description);
        
        matcher.setBits(table);
        
        assertFalse(table.get(0));
    }
}