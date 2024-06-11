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
        float expected = 0.0f;
        float actual = tFloatCharHash.getNoEntryKey();
        assertEquals(expected, actual, 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        char expected = '\0';
        char actual = tFloatCharHash.getNoEntryValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testContains() {
        float val = 1.0f;
        assertFalse(tFloatCharHash.contains(val));
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return true;
            }
        };
        boolean expected = true;
        boolean actual = tFloatCharHash.forEach(procedure);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveAt() {
        int index = 0;
        tFloatCharHash.removeAt(index);
        float expected = 0.0f;
        float actual = tFloatCharHash._set[index];
        assertEquals(expected, actual, 0.0f);
    }

    @Test
    public void testIndex() {
        float key = 1.0f;
        int expected = -1;
        int actual = tFloatCharHash.index(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testIndexRehashed() {
        float key = 1.0f;
        int index = 0;
        int hash = 0;
        byte state = 0;
        int expected = -1;
        int actual = tFloatCharHash.indexRehashed(key, index, hash, state);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertKey() {
        float val = 1.0f;
        int expected = 0;
        int actual = tFloatCharHash.insertKey(val);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertKeyRehash() {
        float val = 1.0f;
        int index = 0;
        int hash = 0;
        byte state = 0;
        int expected = -1;
        int actual = tFloatCharHash.insertKeyRehash(val, index, hash, state);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertKeyAt() {
        int index = 0;
        float val = 1.0f;
        tFloatCharHash.insertKeyAt(index, val);
        float expected = 1.0f;
        float actual = tFloatCharHash._set[index];
        assertEquals(expected, actual, 0.0f);
    }

    @Test
    public void testXinsertKey() {
        float key = 1.0f;
        int expected = 0;
        int actual = tFloatCharHash.XinsertKey(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteExternal() {
        // TODO Implement test case
    }

    @Test
    public void testReadExternal() {
        // TODO Implement test case
    }
}