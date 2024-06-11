import gnu.trove.procedure.TFloatProcedure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatCharHashTest {
    
    private TFloatCharHash hash;
    
    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }
    
    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value < 2.0f;
            }
        };
        
        assertFalse(hash.forEach(procedure));
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(1.0f));
        hash.insertKey(1.0f);
        assertTrue(hash.index(1.0f) >= 0);
    }
    
    @Test
    public void testReadExternal() {
        // TODO: Test reading serialized object with valid and invalid data
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testSetUp() {
        hash.setUp(10);
        assertEquals(10, hash._set.length);
        
        hash.setUp(0);
        assertEquals(0, hash._set.length);
        
        // TODO: Test with invalid initial capacities
    }
    
    @Test
    public void testWriteExternal() {
        // TODO: Test writing serialized object with valid and invalid data
    }
}