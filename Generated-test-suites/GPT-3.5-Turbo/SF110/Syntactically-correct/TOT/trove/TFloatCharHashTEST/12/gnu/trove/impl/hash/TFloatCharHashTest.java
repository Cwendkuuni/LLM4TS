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
    public void testGetNoEntryKey() {
        float expected = 0.0f;
        float actual = hash.getNoEntryKey();
        assertEquals(expected, actual, 0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        char expected = '\0';
        char actual = hash.getNoEntryValue();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int expectedCapacity = 16;
        int actualCapacity = hash.setUp(initialCapacity);
        assertEquals(expectedCapacity, actualCapacity);
    }
    
    @Test
    public void testContains() {
        float val = 5.0f;
        hash.insertKey(val);
        assertTrue(hash.contains(val));
    }
    
    @Test
    public void testForEach() {
        float val1 = 5.0f;
        float val2 = 10.0f;
        hash.insertKey(val1);
        hash.insertKey(val2);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            int count = 0;
            
            public boolean execute(float value) {
                count++;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(2, procedure.count);
    }
    
    @Test
    public void testRemoveAt() {
        float val = 5.0f;
        int index = hash.insertKey(val);
        hash.removeAt(index);
        
        assertFalse(hash.contains(val));
    }
    
    @Test
    public void testInsertKey() {
        float val = 5.0f;
        int index = hash.insertKey(val);
        assertEquals(val, hash._set[index], 0);
        assertEquals(1, hash._states[index]);
    }
    
    @Test
    public void testInsertKey_duplicate() {
        float val = 5.0f;
        hash.insertKey(val);
        int index = hash.insertKey(val);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKey_full() {
        int initialCapacity = 1;
        int loadFactor = 1;
        hash = new TFloatCharHash(initialCapacity, loadFactor);
        float val1 = 5.0f;
        float val2 = 10.0f;
        hash.insertKey(val1);
        
        try {
            hash.insertKey(val2);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception expected, test passed
        }
    }
    
    @Test
    public void testXinsertKey() {
        float val = 5.0f;
        int index = hash.XinsertKey(val);
        assertEquals(val, hash._set[index], 0);
        assertEquals(1, hash._states[index]);
    }
    
    @Test
    public void testXinsertKey_duplicate() {
        float val = 5.0f;
        hash.XinsertKey(val);
        int index = hash.XinsertKey(val);
        assertEquals(-1, index);
    }
    
    @Test
    public void testXinsertKey_full() {
        int initialCapacity = 1;
        int loadFactor = 1;
        hash = new TFloatCharHash(initialCapacity, loadFactor);
        float val1 = 5.0f;
        float val2 = 10.0f;
        hash.XinsertKey(val1);
        
        try {
            hash.XinsertKey(val2);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception expected, test passed
        }
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 1, 1};
        hash.no_entry_key = 0.0f;
        hash.no_entry_value = '\0';
        
        hash.writeExternal(oos);
        oos.flush();
        byte[] data = baos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        TFloatCharHash deserializedHash = new TFloatCharHash();
        deserializedHash.readExternal(ois);
        
        assertArrayEquals(hash._set, deserializedHash._set, 0);
        assertArrayEquals(hash._states, deserializedHash._states);
        assertEquals(hash.no_entry_key, deserializedHash.no_entry_key, 0);
        assertEquals(hash.no_entry_value, deserializedHash.no_entry_value);
    }
}