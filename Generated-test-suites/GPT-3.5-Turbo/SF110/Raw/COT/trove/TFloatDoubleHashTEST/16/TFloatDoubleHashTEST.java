
import static org.junit.Assert.*;
import org.junit.Test;
import gnu.trove.impl.hash.TFloatDoubleHash;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float val = 10.0f;
        assertFalse(hash.contains(val));
        hash.insertKey(val);
        assertTrue(hash.contains(val));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                System.out.println(val); // Do something with val
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float val = 10.0f;
        hash.insertKey(val);
        assertTrue(hash.contains(val));
        hash.removeAt(0);
        assertFalse(hash.contains(val));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float val = 10.0f;
        int index = hash.insertKey(val);
        assertTrue(hash.contains(val));
        assertEquals(index, 0);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float val = 10.0f;
        int index = hash.insertKeyRehash(val, 0, 1, (byte) 0);
        assertTrue(hash.contains(val));
        assertEquals(index, 0);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float val = 10.0f;
        hash.insertKeyAt(0, val);
        assertTrue(hash.contains(val));
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float val = 10.0f;
        int index = hash.XinsertKey(val);
        assertTrue(hash.contains(val));
        assertEquals(index, 0);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float no_entry_key = hash.getNoEntryKey();
        assertEquals(0.0f, no_entry_key, 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double no_entry_value = hash.getNoEntryValue();
        assertEquals(0.0, no_entry_value, 0.0);
    }

    @Test
    public void testWriteExternal() throws Exception {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.writeExternal(null); // To test the method without actually writing to an ObjectOutput
    }

    @Test
    public void testReadExternal() throws Exception {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.readExternal(null); // To test the method without actually reading from an ObjectInput
    }
}

