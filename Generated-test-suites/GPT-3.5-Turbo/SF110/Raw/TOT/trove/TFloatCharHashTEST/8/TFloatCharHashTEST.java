import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    
    private TFloatCharHash floatCharHash;
    
    @Before
    public void setUp() {
        floatCharHash = new TFloatCharHash();
    }
    
    @Test
    public void testInsertKey() {
        float key = 5.5f;
        
        int index = floatCharHash.insertKey(key);
        
        assertEquals(0, index);
        assertEquals(key, floatCharHash._set[0], 0.001);
        assertEquals((byte)1, floatCharHash._states[0]);
    }
    
    @Test
    public void testInsertKeyRehash() {
        float key = 20.2f;
        
        floatCharHash.insertKey(1.0f);
        int index = floatCharHash.insertKeyRehash(key, 0, 1, (byte)1);
        
        assertEquals(3, index);
        assertEquals(key, floatCharHash._set[3], 0.001);
        assertEquals((byte)1, floatCharHash._states[3]);
    }
    
    @Test
    public void testIndex() {
        float key = 10.1f;
        
        floatCharHash.insertKey(key);
        int index = floatCharHash.index(key);
        
        assertEquals(0, index);
    }
    
    @Test
    public void testIndexRehashed() {
        float key = 15.5f;
        
        floatCharHash.insertKey(key);
        int index = floatCharHash.indexRehashed(key, 0, 1, (byte)1);
        
        assertEquals(3, index);
    }
    
    @Test
    public void testContains() {
        float key = 25.5f;
        
        floatCharHash.insertKey(key);
        assertTrue(floatCharHash.contains(key));
    }
    
    @Test
    public void testRemoveAt() {
        float key = 30.2f;
        
        floatCharHash.insertKey(key);
        assertEquals((byte)1, floatCharHash._states[0]);
        
        floatCharHash.removeAt(0);
        assertEquals((byte)0, floatCharHash._states[0]);
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, floatCharHash.getNoEntryKey(), 0.001);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', floatCharHash.getNoEntryValue());
    }
    
    @Test
    public void testForEach() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);
        floatCharHash.insertKey(3.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            
            public boolean execute(float value) {
                System.out.println(value);
                return true;
            }
        };
        
        assertTrue(floatCharHash.forEach(procedure));
    }
    
    @Test
    public void testWriteExternal() {
        // Not implemented yet
    }
    
    @Test
    public void testReadExternal() {
        // Not implemented yet
    }
    
}