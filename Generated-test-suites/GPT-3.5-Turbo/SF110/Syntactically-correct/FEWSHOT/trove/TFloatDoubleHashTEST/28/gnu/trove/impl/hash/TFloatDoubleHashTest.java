package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {
    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    @Test
    public void testConstructor() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        hash = new TFloatDoubleHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        hash = new TFloatDoubleHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructorWithAllParameters() {
        hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        assertEquals(10, hash.setUp(10));
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));

        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TestProcedure procedure = new TestProcedure();
        assertFalse(hash.forEach(procedure));

        hash.insertKey(1.0f);
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        assertEquals(1.0f, hash._set[0], 0.0f);

        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0f);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(1.0f));

        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1.0f, hash._set[0], 0.0f);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testXInsertKey() {
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(1.0f, hash._set[0], 0.0f);
        assertEquals(1, hash._states[0]);
    }

    private class TestProcedure implements TFloatProcedure {
        @Override
        public boolean execute(float value) {
            return false;
        }
    }
}