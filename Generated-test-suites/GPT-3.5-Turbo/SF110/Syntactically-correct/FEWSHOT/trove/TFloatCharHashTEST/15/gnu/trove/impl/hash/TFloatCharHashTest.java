package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertNotNull(hash._set);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float val) {
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.removeAt(1);
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(1, hash.index(2.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
    }

    @Test
    public void testWriteExternal() throws Exception {
        TFloatCharHash hash = new TFloatCharHash();
        ObjectOutputStream output = new ObjectOutputStream(new ByteArrayOutputStream());
        hash.writeExternal(output);
        assertTrue(true);
    }

    @Test
    public void testReadExternal() throws Exception {
        TFloatCharHash hash = new TFloatCharHash();
        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(new byte[] {0}));
        hash.readExternal(input);
        assertTrue(true);
    }

}