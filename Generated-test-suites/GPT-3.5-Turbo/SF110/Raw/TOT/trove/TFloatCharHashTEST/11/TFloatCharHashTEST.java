import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash tFloatCharHash;

    @Before
    public void setUp() {
        tFloatCharHash = new TFloatCharHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatCharHash.getNoEntryKey(), 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', tFloatCharHash.getNoEntryValue());
    }

    @Test
    public void testContains_ExistingValue() {
        float key = 1.23f;
        tFloatCharHash.insertKey(key);
        assertTrue(tFloatCharHash.contains(key));
    }

    @Test
    public void testContains_NonExistingValue() {
        assertFalse(tFloatCharHash.contains(4.56f));
    }

    @Test
    public void testForEach() {
        float[] keys = {1.23f, 4.56f, 7.89f};
        char[] values = {'a', 'b', 'c'};
        for (int i = 0; i < keys.length; i++) {
            tFloatCharHash.insertKey(keys[i]);
        }
        final StringBuilder result = new StringBuilder();
        tFloatCharHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                result.append(value);
                return true;
            }
        });
        assertEquals("1.234.567.89", result.toString());
    }

    @Test
    public void testRemoveAt_ExistingValue() {
        float key = 1.23f;
        tFloatCharHash.insertKey(key);
        int index = tFloatCharHash.index(key);
        assertEquals(key, tFloatCharHash._set[index], 0.0f);
        tFloatCharHash.removeAt(index);
        assertEquals(0.0f, tFloatCharHash._set[index], 0.0f);
    }

    @Test
    public void testRemoveAt_NonExistingValue() {
        tFloatCharHash.insertKey(1.23f);
        tFloatCharHash.insertKey(4.56f);
        tFloatCharHash.insertKey(7.89f);
        int index = 1;
        tFloatCharHash.removeAt(index);
        assertEquals(0.0f, tFloatCharHash._set[index], 0.0f);
    }

    @Test
    public void testIndex_ExistingValue() {
        float key = 1.23f;
        tFloatCharHash.insertKey(key);
        assertEquals(0, tFloatCharHash.index(key));
    }

    @Test
    public void testIndex_NonExistingValue() {
        assertEquals(-1, tFloatCharHash.index(4.56f));
    }

    @Test
    public void testIndexRehashed_ExistingValue() {
        float[] keys = {1.23f, 4.56f,7.89f};
        char[] values = {'a', 'b', 'c'};
        for (int i = 0; i < keys.length; i++) {
            tFloatCharHash.insertKey(keys[i]);
        }
        int index = tFloatCharHash.indexRehashed(1.23f, 0, 1, (byte)1);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed_NonExistingValue() {
        float[] keys = {1.23f, 4.56f, 7.89f};
        char[] values = {'a', 'b', 'c'};
        for (int i = 0; i < keys.length; i++) {
            tFloatCharHash.insertKey(keys[i]);
        }
        int index = tFloatCharHash.indexRehashed(0.12f, 0, 1, (byte)1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey_NewKey() {
        float key = 1.23f;
        int index = tFloatCharHash.insertKey(key);
        assertEquals(key, tFloatCharHash._set[index], 0.0f);
    }

    @Test
    public void testInsertKey_ExistingKey() {
        float key = 1.23f;
        tFloatCharHash.insertKey(key);
        int index = tFloatCharHash.insertKey(key);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash_NewKey() {
        float[] keys = {1.23f, 4.56f, 7.89f};
        char[] values = {'a', 'b', 'c'};
        for (int i = 0; i < keys.length; i++) {
            tFloatCharHash.insertKey(keys[i]);
        }
        int index = tFloatCharHash.insertKeyRehash(0.12f, 0, 1, (byte)0);
        assertFalse(tFloatCharHash.consumeFreeSlot);
        assertEquals(0.12f, tFloatCharHash._set[index], 0.0f);
    }

    @Test
    public void testInsertKeyRehash_ExistingKey() {
        float[] keys = {1.23f, 4.56f, 7.89f};
        char[] values = {'a', 'b', 'c'};
        for (int i = 0; i < keys.length; i++) {
            tFloatCharHash.insertKey(keys[i]);
        }
        int index = tFloatCharHash.insertKeyRehash(1.23f, 0, 1, (byte)0);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyAt() {
        float key = 1.23f;
        tFloatCharHash.insertKeyAt(0, key);
        assertEquals(key, tFloatCharHash._set[0], 0.0f);
        assertEquals(1, tFloatCharHash._states[0]);
    }

    @Test
    public void testXinsertKey_NewKey() {
        float key = 1.23f;
        int index = tFloatCharHash.XinsertKey(key);
        assertEquals(key, tFloatCharHash._set[index], 0.0f);
    }

    @Test
    public void testXinsertKey_ExistingKey() {
        float key = 1.23f;
        tFloatCharHash.insertKey(key);
        int index = tFloatCharHash.XinsertKey(key);
        assertEquals(-1, index);
    }

    @Test
    public void testWriteExternal() {
        // TODO: Implement test case for writeExternal()
    }

    @Test
    public void testReadExternal() {
        // TODO: Implement test case for readExternal()
    }

}