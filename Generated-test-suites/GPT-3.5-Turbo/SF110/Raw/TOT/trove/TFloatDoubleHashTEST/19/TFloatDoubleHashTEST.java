import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testContains() {
        hash.insertKey(1.0f);

        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        final float[] sum = {0};
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                sum[0] += value;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(3.0f, sum[0], 0.0);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);

        hash.removeAt(0);

        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        hash.insertKey(1.0f);

        assertTrue(hash.contains(1.0f));
    }
}