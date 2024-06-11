import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testNoEntryValueDefaultConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }
    
    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(10, 0.5f, 'X');
        charHash.insertKey('A');
        assertTrue(charHash.contains('A'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash(2, 0.5f, 'X');
        charHash.insertKey('A');
        charHash.insertKey('B');
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash(10, 0.5f, 'X');
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.removeAt(0);
        assertFalse(charHash.contains('A'));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash(2, 0.5f, 'X');
        charHash.insertKey('A');
        charHash.insertKey('B');
        int index = charHash.indexRehashed('B', 0, 1234, (byte) 1);
        assertEquals(1, index);
    }
    
    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.insertKey('B');
        
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return value != 'A'; // Break loop if 'A' is found
            }
        };
        
        assertFalse(charHash.forEach(procedure));
    }
}