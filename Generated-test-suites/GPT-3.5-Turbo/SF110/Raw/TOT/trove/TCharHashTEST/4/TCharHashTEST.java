import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {
    
    private TCharHash charHash;
    
    @Before
    public void setUp() {
        charHash = new TCharHash(5, 0.75f, 'X');
    }
    
    @Test
    public void testConstructor_DefaultValues() {
        assertEquals(TCharHash.Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
        assertNotNull(charHash._set);
        assertEquals('X', charHash._set[0]);
    }
    
    @Test
    public void testConstructor_SpecifiedValues() {
        TCharHash customCharHash = new TCharHash(10, 0.5f, 'Y');
        
        assertEquals('Y', customCharHash.getNoEntryValue());
        assertNotNull(customCharHash._set);
        assertEquals(10, customCharHash._states.length);
    }
    
    @Test
    public void testSetUp() {
        int capacity = charHash.setUp(7);
        
        assertNotNull(charHash._set);
        assertEquals(7, capacity);
    }
    
    @Test
    public void testContains_PositiveCase() {
        charHash.insertKey('A');
        charHash.insertKey('B');
        assertTrue(charHash.contains('A'));
    }
    
    @Test
    public void testContains_NegativeCase() {
        charHash.insertKey('A');
        charHash.insertKey('B');
        assertFalse(charHash.contains('C'));
    }
    
    @Test
    public void testForEach_ProcedureExecuted() {
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        charHash.insertKey('D');
        
        TCharProcedure procedure = new TCharProcedure() {
            StringBuilder stringBuilder = new StringBuilder();
            
            @Override
            public boolean execute(char value) {
                stringBuilder.append(value);
                return true;
            }
        };
        
        charHash.forEach(procedure);
        assertEquals("ABCD", procedure.stringBuilder.toString());
    }
    
    @Test
    public void testForEach_ProcedureStopped() {
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        charHash.insertKey('D');
        
        TCharProcedure procedure = new TCharProcedure() {
            int counter = 0;
            
            @Override
            public boolean execute(char value) {
                counter++;
                if (counter == 2) {
                    return false;
                }
                return true;
            }
        };
        
        assertFalse(charHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        charHash.insertKey('D');
        charHash.insertKey('E');
        charHash.removeAt(1);
        
        assertFalse(charHash.contains('B'));
        assertEquals('X', charHash._set[1]);
    }
    
    @Test
    public void testIndex_ExistingKey() {
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        
        int index = charHash.index('B');
        assertEquals(1, index);
    }
    
    @Test
    public void testIndex_NonExistingKey() {
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        
        int index = charHash.index('D');
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexRehashed_ExistingKey() {
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        
        int index = charHash.indexRehashed('B', 3, 20, (byte) 2);
        assertEquals(1, index);
    }
    
    @Test
    public void testIndexRehashed_NonExistingKey() {
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        
        int index = charHash.indexRehashed('D', 3, 20, (byte) 2);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKey_NewKey() {
        int index = charHash.insertKey('A');
        assertEquals(0, index);
        assertTrue(charHash.contains('A'));
    }
    
    @Test
    public void testInsertKey_ExistingKey() {
        charHash.insertKey('A');
        int index = charHash.insertKey('A');
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKeyRehash_NewKey() {
        int index = charHash.insertKeyRehash('A', 3, 20, (byte) 2);
        assertEquals(4, index);
        assertTrue(charHash.contains('A'));
    }
    
    @Test
    public void testInsertKeyRehash_ExistingKey() {
        charHash.insertKey('A');
        int index = charHash.insertKeyRehash('A', 3, 20, (byte) 2);
        assertEquals(-4, index);
    }
    
    @Test
    public void testInsertKeyRehash_NoFreeSlots() {
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        charHash.insertKey('D');
        charHash.insertKey('E');
        
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            charHash.insertKeyRehash('F', 3, 20, (byte) 2);
        });
        
        String expectedMessage = "No free or removed slots available. Key set full?!!";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    public void testInsertKeyAt() {
        charHash.insertKeyAt(2, 'B');
        assertEquals('B', charHash._set[2]);
        assertEquals(1, charHash._states[2]);
    }
}