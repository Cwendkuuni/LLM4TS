import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.impl.hash.TFloatDoubleHash;

public class TFloatDoubleHashTest {
    
    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash(10, 0.75f, Float.NaN, Double.NaN);
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(Float.NaN, hash.getNoEntryKey(), 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(Double.NaN, hash.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testSetUp() {
        assertEquals(10, hash.setUp(10));
        assertEquals(15, hash.setUp(15));
    }
    
    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        assertFalse(hash.forEach(new TFloatProcedure() {
            public boolean execute(float val) {
                return false;
            }
        }));
        hash.insertKey(1.0f);
        assertTrue(hash.forEach(new TFloatProcedure() {
            public boolean execute(float val) {
                return true;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.removeAt(1);
        assertTrue(hash.contains(3.0f));
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(4.0f));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }
    
    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(-1, hash.XinsertKey(1.0f));
    }
    
    @Test
    public void testWriteExternal() {
        // Test writeExternal method
    }
    
    @Test
    public void testReadExternal() {
        // Test readExternal method
    }
    
}