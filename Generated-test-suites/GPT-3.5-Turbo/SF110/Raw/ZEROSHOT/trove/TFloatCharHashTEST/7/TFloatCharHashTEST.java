package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest
{
    private TFloatCharHash tFloatCharHash;

    @Before
    public void setUp() throws Exception
    {
        tFloatCharHash = new TFloatCharHash();
    }

    @After
    public void tearDown() throws Exception
    {
        tFloatCharHash = null;
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatCharHash.getNoEntryKey(), 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', tFloatCharHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        assertFalse(tFloatCharHash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value > 0.0f;
            }
        };

        assertFalse(tFloatCharHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tFloatCharHash._set[0] = 3.0f;
        tFloatCharHash.removeAt(0);

        assertEquals(0.0f, tFloatCharHash._set[0], 0.001);
    }

    @Test
    public void testIndex() {
        tFloatCharHash._set[0] = 3.0f;

        assertEquals(0, tFloatCharHash.index(3.0f));
    }

    @Test
    public void testIndexRehashed() {
        tFloatCharHash._set[0] = 3.0f;

        assertEquals(0, tFloatCharHash.indexRehashed(3.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tFloatCharHash.insertKey(3.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        assertEquals(0, tFloatCharHash.insertKeyRehash(3.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKeyAt() {
        tFloatCharHash.insertKeyAt(0, 3.0f);

        assertEquals(3.0f, tFloatCharHash._set[0], 0.001);
        assertEquals((byte) 1, tFloatCharHash._states[0]);
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, tFloatCharHash.XinsertKey(3.0f));
    }

    @Test
    public void testWriteExternal() {
        fail("Not yet implemented");
    }

    @Test
    public void testReadExternal() {
        fail("Not yet implemented");
    }
}