import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testDefaultConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testCapacityConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testCapacityAndLoadFactorConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertEquals(10, hash.size());
        assertEquals(0.75f, hash.getLoadFactor(), 0.0);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testFullConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        assertEquals(10, hash.size());
        assertEquals(0.75f, hash.getLoadFactor(), 0.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        hash.put(1.0f, 2.0);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        hash.put(1.0f, 2.0);
        hash.put(2.0f, 3.0);
        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(3.0, procedure.sum, 0.0);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        hash.put(1.0f, 2.0);
        hash.put(2.0f, 3.0);
        assertEquals(2, hash.size());
        hash.removeAt(0);
        assertEquals(1, hash.size());
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        hash.put(1.0f, 2.0);
        hash.put(2.0f, 3.0);
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        hash.put(1.0f, 2.0);
        hash.put(2.0f, 3.0);
        assertEquals(-2, hash.insertKey(1.0f));
        assertEquals(2, hash.size());
        assertFalse(hash.consumeFreeSlot);
        assertEquals(0, hash.insertKey(3.0f));
        assertEquals(3, hash.size());
        assertTrue(hash.consumeFreeSlot);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        hash.put(1.0f, 2.0);
        hash.put(2.0f, 3.0);
        assertEquals(-2, hash.insertKeyRehash(1.0f, 0, 0, (byte)1));
        assertEquals(2, hash.size());
        assertFalse(hash.consumeFreeSlot);
        assertEquals(1, hash.insertKeyRehash(3.0f, 2, 1, (byte)0));
        assertEquals(3, hash.size());
        assertTrue(hash.consumeFreeSlot);
    }

    @Test(expected=IllegalStateException.class)
    public void testInsertKeyRehashFull() {
        TFloatDoubleHash hash = new TFloatDoubleHash(2, 0.75f);
        hash.put(1.0f, 2.0);
        hash.put(2.0f, 3.0);
        hash.insertKeyRehash(3.0f, 0, 0, (byte)1);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        hash.insertKeyAt(0, 1.0f);
        hash.insertKeyAt(1, 2.0f);
        assertEquals(1.0f, hash.get(0), 0.0);
        assertEquals(2.0f, hash.get(1), 0.0);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        hash.writeExternal(oos);
        oos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TFloatDoubleHash newHash = new TFloatDoubleHash();
        newHash.readExternal(ois);
        assertEquals(10, newHash.size());
        assertEquals(0.75f, newHash.getLoadFactor(), 0.0);
        assertEquals(1.0f, newHash.getNoEntryKey(), 0.0);
        assertEquals(2.0, newHash.getNoEntryValue(), 0.0);
    }

}