package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

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
        int initialCapacity = 10;
        int capacity = hash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertFalse(hash.contains(0.0f));
        
        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(0.0f);
        hash.insertKey(1.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            StringBuilder sb = new StringBuilder();
            @Override
            public boolean execute(float value) {
                sb.append(value);
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals("0.01.0", procedure.sb.toString());
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(0.0f);
        int index = hash.insertKey(1.0f);
        
        hash.removeAt(index);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        int index = hash.insertKey(0.0f);
        assertTrue(index >= 0);
        assertTrue(hash.contains(0.0f));

        index = hash.insertKey(0.0f);
        assertTrue(index < 0);
        assertFalse(hash.consumeFreeSlot);
        
        float val = Float.POSITIVE_INFINITY;
        index = hash.insertKey(val);
        assertTrue(index >= 0);
        assertTrue(hash.contains(val));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(10);

        float val = Float.NaN;
        hash.insertKey(val);
        assertTrue(hash.contains(val));

        hash.insertKeyRehash(val, 1, 1234, (byte) 0);
        assertFalse(hash.contains(val));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKeyAt(1, 0.0f);
        assertTrue(hash.contains(0.0f));
        assertFalse(hash.consumeFreeSlot);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        int index = hash.XinsertKey(0.0f);
        assertTrue(index >= 0);
        assertTrue(hash.contains(0.0f));

        index = hash.XinsertKey(0.0f);
        assertTrue(index < 0);
        assertFalse(hash.consumeFreeSlot);
        
        float val = Float.POSITIVE_INFINITY;
        index = hash.XinsertKey(val);
        assertTrue(index >= 0);
        assertTrue(hash.contains(val));
    }

    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(0.0f);
        hash.insertKey(1.0f);

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("temp.ser"));
            hash.writeExternal(os);
            os.close();

            ObjectInputStream is = new ObjectInputStream(new FileInputStream("temp.ser"));
            TFloatCharHash hash2 = new TFloatCharHash();
            hash2.readExternal(is);
            is.close();

            assertTrue(hash.contains(0.0f));
            assertTrue(hash.contains(1.0f));
            assertEquals(hash.getNoEntryKey(), hash2.getNoEntryKey(), 0.0);
            assertEquals(hash.getNoEntryValue(), hash2.getNoEntryValue());
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

}