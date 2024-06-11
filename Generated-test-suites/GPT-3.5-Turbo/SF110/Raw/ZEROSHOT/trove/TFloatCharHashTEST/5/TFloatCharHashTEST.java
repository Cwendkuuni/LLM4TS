package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor1() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.size());
    }

    @Test
    public void testConstructor3() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f);
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.size());
        assertEquals(0.5f, hash.getLoadFactor(), 0.001);
    }

    @Test
    public void testConstructor4() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 2.0f, 'a');
        assertNotNull(hash);
        assertEquals(2.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('a', hash.getNoEntryValue());
        assertEquals(10, hash.size());
        assertEquals(0.5f, hash.getLoadFactor(), 0.001);
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertNotNull(hash._set);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.println(value);
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
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(1, hash.indexRehashed(2.0f, 1, 123, (byte)1));
        assertEquals(-1, hash.indexRehashed(4.0f, 1, 456, (byte)1));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(2, hash.insertKey(2.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(1, hash.insertKeyRehash(2.0f, 1, 123, (byte)1));
        assertEquals(2, hash.insertKeyRehash(3.0f, 1, 123, (byte)2));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(1, 2.0f);
        assertEquals(2.0f, hash._set[1], 0.001);
        assertEquals(1, hash._states[1]);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.XinsertKey(1.0f);
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(2, hash.XinsertKey(2.0f));
    }

    @Test(expected = IOException.class)
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash();
        ObjectOutputStream out = new ObjectOutputStream(new ByteArrayOutputStream());
        hash.writeExternal(out);
    }

    @Test(expected = IOException.class)
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(new byte[] {0}));
        hash.readExternal(in);
    }

}