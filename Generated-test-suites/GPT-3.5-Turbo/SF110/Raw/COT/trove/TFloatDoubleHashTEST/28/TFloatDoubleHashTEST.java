import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.0);
    }

    @Test
    public void testNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double noEntryValue = hash.getNoEntryValue();
        assertEquals(0.0, noEntryValue, 0.0);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.index(1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.indexRehashed(1.0f, 0, 0, (byte) 0);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1.0f, hash._set[0], 0.0);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.insertKeyRehash(1.0f, 0, 0, (byte) 0);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.0);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1.0f, hash._set[0], 0.0);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutputStream);
        objectOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        assertEquals(17, byteArray.length);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        byte[] byteArray = {0};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        hash.readExternal(objectInputStream);
        objectInputStream.close();
        assertEquals(0.0f, hash.no_entry_key, 0.0);
        assertEquals(0.0, hash.no_entry_value, 0.0);
    }
}