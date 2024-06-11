package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TFloatCharHashTest {

    TFloatCharHash hash = new TFloatCharHash();

    @Test
    public void testContains() {
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                // Do something
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        // Test removeAt method
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0);
        assertEquals(hash.no_entry_key, hash._set[0], 0.0);
    }

    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(0.0f));
    }

    @Test
    public void testIndexRehashed() {
        assertEquals(-1, hash.indexRehashed(0.0f, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        hash.insertKey(0.0f);
        assertEquals(0.0f, hash._set[0], 0.0);
    }

    @Test
    public void testInsertKeyRehash() {
        hash.insertKeyRehash(0.0f, 0, 0, (byte) 0);
        assertEquals(0.0f, hash._set[0], 0.0);
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 0.0f);
        assertEquals(0.0f, hash._set[0], 0.0);
    }

    @Test
    public void testXinsertKey() {
        int result = hash.XinsertKey(0.0f);
        assertTrue(result >= 0);
    }

    @Test
    public void testWriteExternal() {
        // Test writeExternal method
        TFloatCharHash hash = new TFloatCharHash();
        hash.no_entry_key = 0.0f;
        hash.no_entry_value = '\0';
        byte[] byteArray = new byte[10];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        try {
            hash.writeExternal(oos);
            byteArray = baos.toByteArray();
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(byteArray);
    }

    @Test
    public void testReadExternal() {
        // Test readExternal method
        TFloatCharHash hash = new TFloatCharHash();
        hash.no_entry_key = 0.0f;
        hash.no_entry_value = '\0';

        byte[] byteArray = new byte[10];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        ObjectInputStream ois;
        try {
            hash.writeExternal(oos);
            byteArray = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            ois = new ObjectInputStream(bais);
            hash.readExternal(ois);
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(hash);
    }
    
}