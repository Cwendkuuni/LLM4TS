package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TFloatCharHashTest {
    
    @Test
    public void testConstructor_noArgument() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor_initialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.size());
    }
    
    @Test
    public void testConstructor_initialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.size());
    }
    
    @Test
    public void testConstructor_initialCapacityAndLoadFactorAndNoEntryKeyAndNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('a', hash.getNoEntryValue());
        assertEquals(10, hash.size());
    }
    
    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(1000, hash.setUp(1000));
        assertEquals(1000, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.print(value);
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(2.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(-1, hash.indexRehashed(4.0f, 0, 0, (byte)0));
        assertEquals(1, hash.indexRehashed(2.0f, 0, 0, (byte)0));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash_exception() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyRehash(1.0f, 0, 0, (byte)1);
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(-1, hash.XinsertKey(1.0f));
    }
    
    @Test
    public void testWriteExternal() {
        try {
            TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
            ObjectOutput out = new ObjectOutputStream(new ByteArrayOutputStream());
            hash.writeExternal(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadExternal() {
        try {
            TFloatCharHash hash = new TFloatCharHash();
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(new byte[] {0}));
            hash.readExternal(in);
            in.close();
            assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
            assertEquals('\0', hash.getNoEntryValue());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}