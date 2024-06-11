import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

public class TCharHashTest {
    
    private TCharHash tCharHash;
    
    @Before
    public void setUp() {
        tCharHash = new TCharHash(10);
    }
    
    @Test
    public void testContains_WhenValueExists_ReturnTrue() {
        tCharHash.insertKey('x');
        assertTrue(tCharHash.contains('x'));
    }
    
    @Test
    public void testContains_WhenValueDoesNotExist_ReturnFalse() {
        assertFalse(tCharHash.contains('x'));
    }
    
    @Test
    public void testForEach_WhenProcedureReturnsTrue_ReturnTrue() {
        tCharHash.insertKey('x');
        tCharHash.insertKey('y');
        tCharHash.insertKey('z');
        
        ArrayList<Character> list = new ArrayList<>();
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                list.add(value);
                return true;
            }
        };
        
        assertTrue(tCharHash.forEach(procedure));
        assertEquals(Arrays.asList('x', 'y', 'z'), list);
    }
    
    @Test
    public void testForEach_WhenProcedureReturnsFalse_ReturnFalse() {
        tCharHash.insertKey('x');
        tCharHash.insertKey('y');
        tCharHash.insertKey('z');
        
        ArrayList<Character> list = new ArrayList<>();
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                list.add(value);
                return false;
            }
        };
        
        assertFalse(tCharHash.forEach(procedure));
        assertEquals(Arrays.asList('x'), list);
    }
    
    @Test
    public void testRemoveAt_WhenIndexIsValid_RemoveValueAtIndex() {
        tCharHash.insertKey('x');
        tCharHash.insertKey('y');
        tCharHash.removeAt(0);
        
        assertFalse(tCharHash.contains('x'));
    }
    
    @Test
    public void testIndex_WhenValueExists_ReturnIndex() {
        tCharHash.insertKey('x');
        assertEquals(0, tCharHash.index('x'));
    }
    
    @Test
    public void testIndex_WhenValueDoesNotExist_ReturnNegativeOne() {
        assertEquals(-1, tCharHash.index('x'));
    }
    
    @Test
    public void testIndexRehashed_WhenValueExists_ReturnIndex() {
        tCharHash.insertKey('x');
        assertEquals(0, tCharHash.indexRehashed('x', 0, 0, (byte)1));
    }
    
    @Test
    public void testIndexRehashed_WhenValueDoesNotExist_ReturnNegativeOne() {
        assertEquals(-1, tCharHash.indexRehashed('x', 0, 0, (byte)1));
    }
    
    @Test
    public void testInsertKey_WhenValueDoesNotExist_InsertValueAndReturnIndex() {
        assertEquals(0, tCharHash.insertKey('x'));
        assertTrue(tCharHash.contains('x'));
    }
    
    @Test
    public void testInsertKey_WhenValueExists_ReturnNegativeIndexMinusOne() {
        tCharHash.insertKey('x');
        assertEquals(-1, tCharHash.insertKey('x'));
    }
    
    @Test
    public void testInsertKeyRehash_WhenValueDoesNotExist_InsertValueAndReturnIndex() {
        assertEquals(0, tCharHash.insertKeyRehash('x', 0, 0, (byte)1));
        assertTrue(tCharHash.contains('x'));
    }
    
    @Test
    public void testInsertKeyRehash_WhenValueExists_ReturnNegativeIndexMinusOne() {
        tCharHash.insertKey('x');
        assertEquals(-1, tCharHash.insertKeyRehash('x', 0, 0, (byte)1));
    }
    
    @Test
    public void testInsertKeyRehash_WhenNoFreeSlotAvailable_ThrowException() {
        tCharHash.insertKey('x');
        tCharHash.insertKey('y');
        tCharHash.insertKey('z');
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        tCharHash.insertKey('d');
        tCharHash.insertKey('e');
        tCharHash.insertKey('f');
        tCharHash.insertKey('g');
        
        try {
            tCharHash.insertKeyRehash('h', 0, 0, (byte)1);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }
    
    @Test
    public void testInsertKeyAt_InsertValueAtSpecifiedIndex() {
        tCharHash.insertKeyAt(0, 'x');
        assertTrue(tCharHash.contains('x'));
    }
}