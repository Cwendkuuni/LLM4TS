package gnu.trove.impl.hash;

import org.junit.*;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals(0.0, hash.getNoEntryValue(), 0);
        
        hash = new TFloatDoubleHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals(0.0, hash.getNoEntryValue(), 0);
        
        hash = new TFloatDoubleHash(10, 0.75f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals(0.0, hash.getNoEntryValue(), 0);
        
        hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0);
        assertEquals(2.0, hash.getNoEntryValue(), 0);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertFalse(hash.contains(1.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Add some values
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        
        // Verify all values are processed by procedure
        final float[] sum = {0.0f};
        assertTrue(hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                sum[0] += value;
                return true;
            }
        }));
        assertEquals(10.0f, sum[0], 0);
        
        // Verify procedure can stop execution
        assertFalse(hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                return false;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Add some values
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        
        // Remove value
        hash.removeAt(1);
        
        // Verify value is removed and others are untouched
        assertFalse(hash.contains(2.0f));
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Add some values
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        
        // Verify index of values
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(2, hash.index(3.0f));
        assertEquals(3, hash.index(4.0f));
        
        // Verify index of non-existent value
        assertEquals(-1, hash.index(5.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Insert values
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(2, hash.insertKey(3.0f));
        assertEquals(3, hash.insertKey(4.0f));
        assertEquals(4, hash.insertKey(5.0f));
        
        // Verify values are inserted correctly
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        
        // Verify duplicate values are not inserted
        assertEquals(-1, hash.insertKey(4.0f));
        assertEquals(-6, hash.insertKey(1.0f));
    }
    
    @Test
    public void testWriteReadExternal() throws Exception {
        TFloatDoubleHash hash1 = new TFloatDoubleHash();
        
        // Add some values
        hash1.insertKey(1.0f);
        hash1.insertKey(2.0f);
        hash1.insertKey(3.0f);
        
        // Write hash to file
        FileOutputStream fos = new FileOutputStream("test.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(hash1);
        oos.close();
        
        // Read hash from file
        FileInputStream fis = new FileInputStream("test.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        TFloatDoubleHash hash2 = (TFloatDoubleHash) ois.readObject();
        ois.close();
        
        // Verify values in both hashes are the same
        assertEquals(hash1.contains(1.0f), hash2.contains(1.0f));
        assertEquals(hash1.contains(2.0f), hash2.contains(2.0f));
        assertEquals(hash1.contains(3.0f), hash2.contains(3.0f));
    }
}