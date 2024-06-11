package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                return false;
            }
        };
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[1];
        hash._set[0] = 0.0f;
        hash._states = new byte[1];
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0001);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._states = new byte[1];
        hash._states[0] = 1;
        hash._set = new float[1];
        hash._set[0] = 0.0f;
        assertEquals(0, hash.index(0.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._states = new byte[1];
        hash._states[0] = 1;
        hash._set = new float[1];
        hash._set[0] = 0.0f;
        assertEquals(-1, hash.indexRehashed(1.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._states = new byte[1];
        hash._states[0] = 0;
        hash._set = new float[1];
        hash.insertKey(0.0f);
        assertEquals(0.0f, hash._set[0], 0.0001);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._states = new byte[1];
        hash._states[0] = 0;
        hash._set = new float[1];
        hash.insertKeyRehash(0.0f, 0, 0, (byte) 0);
        assertEquals(0.0f, hash._set[0], 0.0001);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[1];
        hash._states = new byte[1];
        hash.insertKeyAt(0, 0.0f);
        assertEquals(0.0f, hash._set[0], 0.0001);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._states = new byte[1];
        hash._states[0] = 0;
        hash._set = new float[1];
        hash.XinsertKey(0.0f);
        assertEquals(0.0f, hash._set[0], 0.0001);
        assertEquals(1, hash._states[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testXinsertKey_full() {
        TFloatDoubleHash hash = new TFloatDoubleHash(1, 1.0f);
        hash.XinsertKey(0.0f);
        hash.XinsertKey(1.0f);
    }

    @Test
    public void testWriteExternal() throws Exception {
        TFloatDoubleHash hash = new TFloatDoubleHash(1, 1.0f, 0.0f, 0.0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        out.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);
        hash.readExternal(in);
        assertEquals(0.0f, hash.no_entry_key, 0.0001);
        assertEquals(0.0, hash.no_entry_value, 0.0001);
    }

}