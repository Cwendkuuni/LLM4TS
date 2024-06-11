import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashConstructor() {
        TCharHash charHash = new TCharHash();
        assertNotNull(charHash);
        
        charHash = new TCharHash(10);
        assertNotNull(charHash);
        
        charHash = new TCharHash(10, 0.5f);
        assertNotNull(charHash);
        
        charHash = new TCharHash(10, 0.5f, 'A');
        assertNotNull(charHash);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        assertEquals('\0', charHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        assertFalse(charHash.contains('A'));
        
        charHash.insertKey('A');
        assertTrue(charHash.contains('A'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                assertEquals('A', value);
                return true;
            }
        };
        
        assertTrue(charHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.removeAt(0);
        
        assertFalse(charHash.contains('A'));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        
        assertEquals(0, charHash.insertKey('A'));
    }

    // Add more test cases for other methods

}