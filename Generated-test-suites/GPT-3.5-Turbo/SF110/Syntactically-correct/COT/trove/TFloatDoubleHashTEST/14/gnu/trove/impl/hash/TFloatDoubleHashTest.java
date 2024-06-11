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
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
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
            public boolean execute(float value) {
                return value >= 0;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        hash._set[0] = 1.0f;
        hash._states[0] = 1;
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0001);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        hash._set[0] = 1.0f;
        hash._states[0] = 1;
        int index = hash.index(1.0f);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        hash._set[0] = 1.0f;
        hash._states[0] = 1;
        int index = hash.indexRehashed(1.0f, 0, 0, (byte)1);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        int index = hash.insertKeyRehash(1.0f, 0, 0, (byte)2);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.0001);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(hash);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            TFloatDoubleHash newHash = (TFloatDoubleHash) in.readObject();
            assertEquals(0.0f, newHash.no_entry_key, 0.0001);
            assertEquals(0.0, newHash.no_entry_value, 0.0001);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.no_entry_key = 1.0f;
        hash.no_entry_value = 2.0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(hash);
            out.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bais);
            TFloatDoubleHash newHash = (TFloatDoubleHash) in.readObject();
            assertEquals(1.0f, newHash.no_entry_key, 0.0001);
            assertEquals(2.0, newHash.no_entry_value, 0.0001);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}