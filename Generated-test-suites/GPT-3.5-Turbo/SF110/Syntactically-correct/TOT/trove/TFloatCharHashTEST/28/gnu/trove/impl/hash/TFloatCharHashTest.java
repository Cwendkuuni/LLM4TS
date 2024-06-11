import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    
    private TFloatCharHash hash;
    
    @Before
    public void setup() {
        hash = new TFloatCharHash();
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001f);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testContains() {
        assertFalse(hash.contains(0.0f));
        
        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        final float[] values = { 1.0f, 2.0f, 3.0f };
        
        for (float value : values) {
            hash.insertKey(value);
        }
        
        TFloatProcedure procedure = new TFloatProcedure() {
            int i = 0;
            
            @Override
            public boolean execute(float value) {
                assertEquals(values[i], value, 0.0001f);
                i++;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        assertFalse(hash.contains(0.0f));
        
        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f));
        
        hash.removeAt(0);
        assertFalse(hash.contains(0.0f));
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(0.0f));
        
        hash.insertKey(0.0f);
        assertEquals(0, hash.index(0.0f));
        
        hash.insertKey(1.0f);
        assertEquals(1, hash.index(1.0f));
    }
    
    @Test
    public void testInsertKey() {
        int index = hash.insertKey(0.0f);
        assertEquals(0, index);
        
        index = hash.insertKey(1.0f);
        assertEquals(1, index);
    }
    
    @Test
    public void testXInsertKey() {
        int index = hash.XinsertKey(0.0f);
        assertEquals(0, index);
        
        index = hash.XinsertKey(1.0f);
        assertEquals(1, index);
    }
    
    @Test
    public void testWriteExternal() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testReadExternal() {
        fail("Not yet implemented");
    }

}