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
        hash.put(1.0f, 1.0);
        assertTrue(hash.contains(1.0f));

        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);

        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;

            public boolean execute(float value) {
                sum += value;
                return true;
            }

            public double getResult() {
                return sum;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(3.0, procedure.getResult(), 0.0);
    }

    @Test
    public void testRemoveAt() {
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);

        hash.remove(1.0f);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testInsertKey() {
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);

        index = hash.insertKey(2.0f);
        assertEquals(1, index);

        assertFalse(hash.insertKey(1.0f) >= 0);
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);
        hash.insertKeyAt(1, 2.0f);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }
}