import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SmallCharMatcherTest {
    
    private SmallCharMatcher smallCharMatcher;
    
    @Before
    public void setUp() {
        char[] table = new char[SmallCharMatcher.MAX_SIZE];
        long filter = 0L;
        boolean containsZero = true;
        smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, "Test Matcher");
    }
    
    @Test
    public void testMatches_WithCharacterZero_ShouldReturnTrue() {
        assertTrue(smallCharMatcher.matches('\0'));
    }
    
    @Test
    public void testMatches_WithCharacterNotPresent_ShouldReturnFalse() {
        assertFalse(smallCharMatcher.matches('a'));
    }
    
    @Test
    public void testMatches_WithCharacterPresentInTable_ShouldReturnTrue() {
        char[] table = new char[SmallCharMatcher.MAX_SIZE];
        table[42] = 'b';
        long filter = 0L;
        boolean containsZero = false;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Matcher");
        
        assertTrue(matcher.matches('b'));
    }
    
    @Test
    public void testMatches_WithCharacterPresentButNotInTable_ShouldReturnFalse() {
        char[] table = new char[SmallCharMatcher.MAX_SIZE];
        long filter = 0L;
        boolean containsZero = false;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test Matcher");
        
        assertFalse(matcher.matches('a'));
    }
    
    @Test
    public void testSetBits_ShouldSetBitsInTable() {
        BitSet expectedTable = new BitSet();
        expectedTable.set(0);
        expectedTable.set(42);
        
        BitSet actualTable = new BitSet();
        smallCharMatcher.setBits(actualTable);
        
        assertEquals(expectedTable, actualTable);
    }
}