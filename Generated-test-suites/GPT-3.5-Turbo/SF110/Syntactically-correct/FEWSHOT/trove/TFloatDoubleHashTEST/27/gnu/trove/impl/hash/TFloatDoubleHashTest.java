package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TFloatDoubleHashTest {
    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);

        hash = new TFloatDoubleHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);

        hash = new TFloatDoubleHash(10, 0.8f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);

        hash = new TFloatDoubleHash(10, 0.8f, 1.0f, 2.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(10, hash.setUp(10));

        hash = new TFloatDoubleHash(20);
        assertEquals(20, hash.setUp(10));
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(1.0f));

        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;
            
            public boolean execute(float value) {
                sum += (double)value;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(6.0, procedure.sum, 0.0);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assertEquals(1, hash.removeAt(1));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(-1, hash.index(1));

        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assertEquals(-1, hash.indexRehashed(4.0f, 0, 0, (byte)0));
        assertEquals(1, hash.indexRehashed(2.0f, 0, 0, (byte)0));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assertEquals(-1, hash.insertKeyRehash(4.0f, 0, 0, (byte)0));
        assertEquals(1, hash.insertKeyRehash(2.0f, 0, 0, (byte)0));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(-1, hash.XinsertKey(1.0f));
    }
}