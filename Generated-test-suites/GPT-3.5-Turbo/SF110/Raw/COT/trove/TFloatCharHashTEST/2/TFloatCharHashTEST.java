
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        
        assertTrue(hash.contains(10.0f));
        assertTrue(hash.contains(20.0f));
        assertFalse(hash.contains(30.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;
            
            public boolean execute(float val) {
                sum += val;
                return true;
            }
            
            public float getSum() {
                return sum;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(30.0f, procedure.getSum(), 0.001);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        
        hash.removeAt(0);
        
        assertFalse(hash.contains(10.0f));
        assertTrue(hash.contains(20.0f));
    }
    
    // Potential edge case: when the key is the same as the no_entry_key
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(0.0f);
        
        assertTrue(hash.contains(0.0f));
    }
    
    // Potential edge case: when the key is the same as the no_entry_key
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.XinsertKey(0.0f);
        
        assertTrue(hash.contains(0.0f));
    }
    
    // Potential exception handling scenario: when the key set is full
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(1);
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
    }
    
    // Potential exception handling scenario: when the key set is full
    @Test(expected = IllegalStateException.class)
    public void testXinsertKeyException() {
        TFloatCharHash hash = new TFloatCharHash(1);
        hash.XinsertKey(10.0f);
        hash.XinsertKey(20.0f);
    }
    
    // Potential exception handling scenario: when there are no free or removed slots available
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash(1);
        hash.insertKeyAt(0, 10.0f);
        hash.insertKeyAt(0, 20.0f);
    }
    
    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        
        // Write to external
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(hash);
        } catch (IOException e) {
            fail("IOException thrown during test: " + e.getMessage());
        }
        
        // Read from external
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            TFloatCharHash readHash = (TFloatCharHash) ois.readObject();
            
            assertEquals(hash.contains(10.0f), readHash.contains(10.0f));
            assertEquals(hash.contains(20.0f), readHash.contains(20.0f));
        } catch (IOException | ClassNotFoundException e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }
}
