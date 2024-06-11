import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.function.TFloatProcedure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    
    private TFloatDoubleHash testObject;
    
    @Before
    public void setUp() {
        testObject = new TFloatDoubleHash();
    }
    
    @Test
    public void testConstructor1() {
        assertEquals(0.0f, testObject.getNoEntryKey(), 0.0);
        assertEquals(0.0, testObject.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        testObject = new TFloatDoubleHash(initialCapacity);
        assertEquals(initialCapacity, testObject.capacity());
        assertEquals(0.0f, testObject.getNoEntryKey(), 0.0);
        assertEquals(0.0, testObject.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        testObject = new TFloatDoubleHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, testObject.capacity());
        assertEquals(loadFactor, testObject.loadFactor(), 0.0);
        assertEquals(0.0f, testObject.getNoEntryKey(), 0.0);
        assertEquals(0.0, testObject.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        float noEntryKey = -1.0f;
        double noEntryValue = -1.0;
        testObject = new TFloatDoubleHash(initialCapacity, loadFactor, noEntryKey, noEntryValue);
        assertEquals(initialCapacity, testObject.capacity());
        assertEquals(loadFactor, testObject.loadFactor(), 0.0);
        assertEquals(noEntryKey, testObject.getNoEntryKey(), 0.0);
        assertEquals(noEntryValue, testObject.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        testObject.setUp(initialCapacity);
        assertEquals(initialCapacity, testObject.capacity());
        assertNotNull(testObject._set);
    }
    
    @Test
    public void testContains_true() {
        float val = 5.0f;
        testObject.add(val, 10.0);
        assertTrue(testObject.contains(val));
    }
    
    @Test
    public void testContains_false() {
        float val = 5.0f;
        assertFalse(testObject.contains(val));
    }
    
    @Test
    public void testForEach() {
        float val1 = 5.0f;
        float val2 = 10.0f;
        double value = 2.5;
        testObject.add(val1, value);
        testObject.add(val2, value);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                return val <= 5.0;
            }
        };
        
        assertFalse(testObject.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        float val = 5.0f;
        double value = 2.5;
        testObject.add(val, value);
        testObject.add(val + 1, value);
        testObject.removeAt(0);
        assertEquals(testObject.getNoEntryKey(), testObject.key(0), 0.0);
        assertEquals(testObject.getNoEntryValue(), testObject.value(0), 0.0);
    }
    
    @Test
    public void testIndex() {
        float val = 5.0f;
        double value = 2.5;
        testObject.add(val, value);
        testObject.add(val + 1, value);
        assertEquals(0, testObject.index(val));
        assertEquals(1, testObject.index(val + 1));
    }
    
    @Test
    public void testInsertKey() {
        float val = 5.0f;
        double value = 2.5;
        testObject.insertKey(val);
        assertEquals(1, testObject.size());
        assertEquals(val, testObject.key(0), 0.0);
        assertEquals(value, testObject.value(0), 0.0);
    }
    
    @Test
    public void testWriteExternal() {
        // Test writeExternal here
    }
    
    @Test
    public void testReadExternal() {
        // Test readExternal here
    }
    
}