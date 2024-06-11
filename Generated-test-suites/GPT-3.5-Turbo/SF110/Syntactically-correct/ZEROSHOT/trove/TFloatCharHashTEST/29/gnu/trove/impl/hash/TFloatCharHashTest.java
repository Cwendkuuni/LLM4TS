package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(-1, hash.index(1.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.0f);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0f);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(10, hash.setUp(10));
    }

    @Test
    public void testXInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.XinsertKey(1.0f));
    }

}