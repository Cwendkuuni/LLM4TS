package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;
    private float no_entry_key = 0.0f;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    @Test
    public void testSetUp() {
        assertEquals(10, hash.setUp(10));
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));

        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));

        hash.remove(1.0f);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        final float[] sum = new float[1];
        hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                sum[0] += value;
                return true;
            }
        });

        assertEquals(3.0f, sum[0], 0.01f);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        assertEquals(1.0f, hash._set[0], 0.01f);

        hash.removeAt(0);
        assertEquals(no_entry_key, hash._set[0], 0.01f);
    }

    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(1.0f));

        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));

        hash.remove(1.0f);
        assertEquals(-1, hash.index(1.0f));
    }

    @Test
    public void testIndexRehashed() {
        assertEquals(-1, hash.indexRehashed(1.0f, 0, 0, (byte) 0));

        hash.insertKey(1.0f);
        assertEquals(0, hash.indexRehashed(1.0f, 0, 0, (byte) 1));

        hash.remove(1.0f);
        assertEquals(-1, hash.indexRehashed(1.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(0.0f, hash._set[0], 0.01f);

        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(2.0f, hash._set[1], 0.01f);
    }

    @Test
    public void testInsertKeyRehash() {
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 0, (byte) 0));
        assertEquals(0.0f, hash._set[0], 0.01f);

        assertEquals(1, hash.insertKeyRehash(2.0f, 1, 0, (byte) 0));
        assertEquals(2.0f, hash._set[1], 0.01f);
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.01f);
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(1.0f, hash._set[0], 0.01f);

        assertEquals(1, hash.XinsertKey(2.0f));
        assertEquals(2.0f, hash._set[1], 0.01f);
    }
    @Test
    public void testWriteExternal() {
        // Your test here
    }

    @Test
    public void testReadExternal() {
        // Your test here
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.01f);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0d, hash.getNoEntryValue(), 0.01d);
    }
    
    @Test
    public void testTFloatDoubleHash() {
        assertEquals(0.0f, hash.no_entry_key, 0.01f);
        assertEquals(0.0d, hash.no_entry_value, 0.01d);
    }
    
    @Test
    public void testTFloatDoubleHashInt() {
        hash = new TFloatDoubleHash(10);
        
        assertEquals(0.0f, hash.no_entry_key, 0.01f);
        assertEquals(0.0d, hash.no_entry_value, 0.01d);
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testTFloatDoubleHashIntFloat() {
        hash = new TFloatDoubleHash(10, 0.5f);
        
        assertEquals(0.0f, hash.no_entry_key, 0.01f);
        assertEquals(0.0d, hash.no_entry_value, 0.01d);
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testTFloatDoubleHashIntFloatFloatDouble() {
        hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0d);
        
        assertEquals(1.0f, hash.no_entry_key, 0.01f);
        assertEquals(2.0d, hash.no_entry_value, 0.01d);
        assertEquals(10, hash._set.length);
    }
}