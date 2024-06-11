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
        float result = tFloatCharHash.getNoEntryKey();
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        char expected = '\0';
        char result = tFloatCharHash.getNoEntryValue();
        assertEquals(expected, result);
    }

    @Test
    public void testContains() {
        assertFalse(tFloatCharHash.contains(0.0f));
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return true;
            }
        };
        assertTrue(tFloatCharHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tFloatCharHash.removeAt(0);
        float[] expectedSet = {0.0f};
        float[] resultSet = tFloatCharHash._set;
        assertArrayEquals(expectedSet, resultSet, 0.0f);
    }

    @Test
    public void testIndex() {
        int expected = 0;
        int result = tFloatCharHash.index(0.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testIndexRehashed() {
        int expected = -1;
        int result = tFloatCharHash.indexRehashed(0.0f, 0, 0, (byte) 0);
        assertEquals(expected, result);
    }

    @Test
    public void testInsertKey() {
        int expected = 0;
        int result = tFloatCharHash.insertKey(0.0f);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        tFloatCharHash.insertKeyRehash(0.0f, 0, 0, (byte) 0);
    }

    @Test
    public void testInsertKeyAt() {
        tFloatCharHash.insertKeyAt(0, 0.0f);
        float[] expectedSet = {0.0f};
        float[] resultSet = tFloatCharHash._set;
        assertArrayEquals(expectedSet, resultSet, 0.0f);
    }

    @Test
    public void testXinsertKey() {
        int expected = 0;
        int result = tFloatCharHash.XinsertKey(0.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testWriteExternal() {
        // TODO: Implement test
    }

    @Test
    public void testReadExternal() {
        // TODO: Implement test
    }
}