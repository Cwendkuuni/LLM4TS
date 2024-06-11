package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {
    private TFloatDoubleHash tFloatDoubleHash;
    
    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0f);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testSetUp() {
        int expected = 10;
        int actual = tFloatDoubleHash.setUp(10);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContains() {
        float val = 10.0f;
        assertFalse(tFloatDoubleHash.contains(val));
        
        tFloatDoubleHash.insertKey(val);
        assertTrue(tFloatDoubleHash.contains(val));
    }
    
    @Test
    public void testForEach() {
        assertFalse(tFloatDoubleHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                return false;
            }
        }));
        
        float val = 10.0f;
        tFloatDoubleHash.insertKey(val);
        
        assertTrue(tFloatDoubleHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                return true;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        int index = 0;
        tFloatDoubleHash._set = new float[] {10.0f};
        tFloatDoubleHash._states = new byte[] {1};
        
        tFloatDoubleHash.removeAt(index);
        
        assertEquals(0.0f, tFloatDoubleHash._set[index], 0.0f);
        assertEquals(0, tFloatDoubleHash._states[index]);
    }
    
    @Test
    public void testIndex() {
        float key = 10.0f;
        tFloatDoubleHash._states = new byte[] {1};
        tFloatDoubleHash._set = new float[] {key};
        
        int expected = 0;
        int actual = tFloatDoubleHash.index(key);
        assertEquals(expected, actual);
        
        key = 20.0f;
        expected = -1;
        actual = tFloatDoubleHash.index(key);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexRehashed() {
        float key = 10.0f;
        int index = 0;
        int hash = 5;
        byte state = 1;
        
        int expected = -1;
        int actual = tFloatDoubleHash.indexRehashed(key, index, hash, state);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInsertKey() {
        float val = 10.0f;
        int expected = 0;
        int actual = tFloatDoubleHash.insertKey(val);
        assertEquals(expected, actual);
        
        expected = -1;
        actual = tFloatDoubleHash.insertKey(val);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInsertKeyRehash() {
        float val = 10.0f;
        int index = 0;
        int hash = 5;
        byte state = 1;
        
        int expected = -1;
        int actual = tFloatDoubleHash.insertKeyRehash(val, index, hash, state);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInsertKeyAt() {
        int index = 0;
        float val = 10.0f;
        
        tFloatDoubleHash.insertKeyAt(index, val);
        
        assertEquals(val, tFloatDoubleHash._set[index], 0.0f);
        assertEquals(1, tFloatDoubleHash._states[index]);
    }
    
    @Test
    public void testXinsertKey() {
        float key = 10.0f;
        tFloatDoubleHash._states = new byte[] {0};
        tFloatDoubleHash._set = new float[] {key};
        
        int expected = 0;
        int actual = tFloatDoubleHash.XinsertKey(key);
        assertEquals(expected, actual);
        
        key = 20.0f;
        expected = -1;
        actual = tFloatDoubleHash.XinsertKey(key);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        
        tFloatDoubleHash.writeExternal(out);
        
        byte[] expected = {0, 0, 0, 0, 0, 0, 0, 0};
        byte[] actual = outputStream.toByteArray();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0, 0, 0, 0, 0, 0, 0, 0});
        ObjectInput in = new ObjectInputStream(inputStream);
        
        tFloatDoubleHash.readExternal(in);
        
        assertEquals(0.0f, tFloatDoubleHash.no_entry_key, 0.0f);
        assertEquals(0.0, tFloatDoubleHash.no_entry_value, 0.0);
    }
}