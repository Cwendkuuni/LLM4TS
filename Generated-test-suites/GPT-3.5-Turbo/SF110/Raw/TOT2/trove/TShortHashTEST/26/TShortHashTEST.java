import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    
    @Test
    public void testTShortHashDefaultConstructor() {
        TShortHash tShortHash = new TShortHash();
        assertNotNull(tShortHash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testTShortHashConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash(initialCapacity);
        assertNotNull(tShortHash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testContainsMethod() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 5);
        assertTrue(tShortHash.contains((short) 5));
        assertFalse(tShortHash.contains((short) 10));
    }
    
    @Test
    public void testForEachMethod() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 5);
        
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value == 5;
            }
        };
        
        assertTrue(tShortHash.forEach(procedure));
    }
    
    // Additional test cases for edge cases and exceptions can be added here
    
}