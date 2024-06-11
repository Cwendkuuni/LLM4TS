
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {

    private TFloatCharHash testHash;

    @Before
    public void setUp() {
        testHash = new TFloatCharHash();
    }

    @Test
    public void testContains() {
        float key = 1.0f;
        assertFalse(testHash.contains(key));
        
        testHash.insertKey(key);
        assertTrue(testHash.contains(key));
    }

    @Test
    public void testForEach() {
        float[] keys = {1.0f, 2.0f, 3.0f};
        char[] values = {'a', 'b', 'c'};
        
        for (int i=0; i<keys.length; i++) {
            testHash.insertKey(keys[i]);
        }
        
        TFloatProcedure procedure = (val) -> {
            System.out.println("Value: " + val);
            return true;
        };
        
        assertTrue(testHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        float key = 1.0f;
        
        testHash.insertKey(key);
        assertTrue(testHash.contains(key));
        
        testHash.removeAt(0);
        assertFalse(testHash.contains(key));
    }
    
    @Test
    public void testInsertKey() {
        float key = 1.0f;
        
        int index = testHash.insertKey(key);
        assertTrue(testHash.contains(key));
        assertEquals(0, index);
        
        int newIndex = testHash.insertKey(key);
        assertFalse(newIndex >= 0);
    }

    @Test
    public void testXinsertKey() {
        float key = 1.0f;
        
        int index = testHash.XinsertKey(key);
        assertTrue(testHash.contains(key));
        assertEquals(0, index);
        
        int newIndex = testHash.XinsertKey(key);
        assertFalse(newIndex >= 0);
    }
    
}
