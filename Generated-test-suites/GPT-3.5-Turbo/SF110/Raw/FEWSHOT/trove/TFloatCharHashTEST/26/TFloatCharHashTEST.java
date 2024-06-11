package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {

    private TFloatCharHash hash;
    
    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int capacity = hash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(new float[initialCapacity].length, hash._set.length);
    }
    
    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        final float[] elements = { 1.0f, 2.0f, 3.0f };
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        hash.forEach(new TFloatProcedure() {
            
            int index = 0;
            
            @Override
            public boolean execute(float value) {
                assertEquals(elements[index], value, 0.0f);
                index++;
                return true;
            }
        });
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        hash.removeAt(1);
        assertEquals(2.0f, hash._set[1], 0.0f);
        assertEquals(1, hash._states[1]);
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(1.0f));
        
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        assertEquals(-1, hash.indexRehashed(1.0f, 0, 0, (byte) 0));
        
        hash.insertKey(1.0f);
        assertEquals(0, hash.indexRehashed(1.0f, 0, 0, (byte) 1));
    }
    
    @Test
    public void testInsertKey() {
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
        
        index = hash.insertKey(2.0f);
        assertEquals(1, index);
        assertTrue(hash.contains(2.0f));
        
        index = hash.insertKey(3.0f);
        assertEquals(2, index);
        assertTrue(hash.contains(3.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        hash._set = new float[5];
        hash._states = new byte[5];
        hash._states[2] = 1;
        hash._states[3] = 2;
        hash._states[4] = 2;
        
        int index = hash.insertKeyRehash(1.0f, 0, 0, (byte) 0);
        assertEquals(1, index);
        
        index = hash.insertKeyRehash(2.0f, 0, 0, (byte) 1);
        assertEquals(0, index);
        
        index = hash.insertKeyRehash(3.0f, 0, 0, (byte) 1);
        assertEquals(3, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        hash._set = new float[5];
        hash._states = new byte[5];
        
        hash.insertKeyAt(2, 1.0f);
        assertEquals(1.0f, hash._set[2], 0.0f);
        assertEquals(1, hash._states[2]);
    }
    
    @Test
    public void testXinsertKey() {
        hash._set = new float[5];
        hash._states = new byte[5];
        
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
        
        index = hash.XinsertKey(2.0f);
        assertEquals(1, index);
        assertTrue(hash.contains(2.0f));
        
        index = hash.XinsertKey(3.0f);
        assertEquals(2, index);
        assertTrue(hash.contains(3.0f));
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        
        hash.writeExternal(out);
        
        byte[] byteArray = baos.toByteArray();
        assertEquals(12, byteArray.length);
        assertEquals(0, byteArray[0]);
        assertEquals(0, byteArray[1]);
        assertEquals(12, byteArray[2]);
        assertEquals(0, byteArray[3]);
        assertEquals(0, byteArray[4]);
        assertEquals(0, byteArray[5]);
        assertEquals(0, byteArray[6]);
        assertEquals(0, byteArray[7]);
        assertEquals(0, byteArray[8]);
        assertEquals(0, byteArray[9]);
        assertEquals(0, byteArray[10]);
        assertEquals(0, byteArray[11]);
    }
    
    @Test
    public void testReadExternal() throws ClassNotFoundException, IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ObjectInputStream in = new ObjectInputStream(bais);
        
        hash.readExternal(in);
        
        assertEquals(0.0f, hash.no_entry_key, 0.0f);
        assertEquals('\0', hash.no_entry_value);
    }
}