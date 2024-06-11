
import org.junit.Assert;
import org.junit.Test;
import java.util.BitSet;


public class SmallCharMatcherTest {
    
    @Test
    public void testMatches_WithEmptyCharMatcher_ShouldReturnFalse() {
        SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "EmptyCharMatcher");
        
        boolean result = charMatcher.matches('a');
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testMatches_WithNonEmptyCharMatcher_ContainingCharacter_ShouldReturnTrue() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher charMatcher = SmallCharMatcher.from(chars, "NonEmptyCharMatcher");
        
        boolean result = charMatcher.matches('a');
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testMatches_WithNonEmptyCharMatcher_NotContainingCharacter_ShouldReturnFalse() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher charMatcher = SmallCharMatcher.from(chars, "NonEmptyCharMatcher");
        
        boolean result = charMatcher.matches('b');
        
        Assert.assertFalse(result);
    }
   
    @Test
    public void testMatches_WithNonEmptyCharMatcher_ContainingZeroCharacter_ShouldReturnTrue() {
        BitSet chars = new BitSet();
        chars.set(0);
        SmallCharMatcher charMatcher = SmallCharMatcher.from(chars, "NonEmptyCharMatcher");
        
        boolean result = charMatcher.matches('\0');
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testSetBits_WithEmptyCharMatcher_ShouldSetBitsToZero() {
        SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "EmptyCharMatcher");
        
        BitSet table = new BitSet();
        charMatcher.setBits(table);
        
        Assert.assertEquals(0, table.cardinality());
    }
    
    @Test
    public void testSetBits_WithNonEmptyCharMatcher_ShouldSetBitsCorrectly() {
        BitSet chars = new BitSet();
        chars.set('a');
        SmallCharMatcher charMatcher = SmallCharMatcher.from(chars, "NonEmptyCharMatcher");
        
        BitSet table = new BitSet();
        charMatcher.setBits(table);
        
        Assert.assertTrue(table.get('a'));
    }
}
