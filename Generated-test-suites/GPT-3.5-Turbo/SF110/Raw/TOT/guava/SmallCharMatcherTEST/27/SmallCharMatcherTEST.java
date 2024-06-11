
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_WithEmptyTable_ReturnsFalse() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "empty");
        
        assertFalse(matcher.matches('a'));
    }
    
    @Test
    public void testMatches_WithTableSize1AndMatchChar_ReturnsTrue() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "singleItem");
        
        assertTrue(matcher.matches('a'));
    }
    
    @Test
    public void testMatches_WithTableSize1AndNotMatchChar_ReturnsFalse() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "singleItem");
        
        assertFalse(matcher.matches('b'));
    }
    
    @Test
    public void testMatches_WithMultipleItemsAndMatchCharInTable_ReturnsTrue() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        chars.set('c');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "multipleItems");
        
        assertTrue(matcher.matches('b'));
    }
    
    @Test
    public void testMatches_WithMultipleItemsAndNotMatchChar_ReturnsFalse() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        chars.set('c');
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "multipleItems");
        
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testMatches_WithTableSizeEquals1023_ReturnsTrue() {
        BitSet chars = new BitSet();
        for (int i = 0; i < 1023; i++) {
            chars.set(i);
        }
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "largeTable");
        
        assertTrue(matcher.matches(100));
    }
    
    @Test
    public void testMatches_WithTableSizeEquals1023_ReturnsFalse() {
        BitSet chars = new BitSet();
        for (int i = 0; i < 1023; i++) {
            chars.set(i);
        }
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "largeTable");
        
        assertFalse(matcher.matches(2000));
    }
    
    @Test
    public void testMatches_WithTableSizeGreaterThan1023_ReturnsTrue() {
        BitSet chars = new BitSet();
        for (int i = 0; i < 2000; i++) {
            chars.set(i);
        }
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "largeTable");
        
        assertTrue(matcher.matches(1500));
    }
    
    @Test
    public void testMatches_WithTableSizeGreaterThan1023_ReturnsFalse() {
        BitSet chars = new BitSet();
        for (int i = 0; i < 2000; i++) {
            chars.set(i);
        }
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "largeTable");
        
        assertFalse(matcher.matches(3000));
    }
}

