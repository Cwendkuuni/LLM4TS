
package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash(10, 0.5f, -1.0f, -1.0);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);
    }

    @Test
    public void testContains() {
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(6.0f));
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;

            @Override
            public boolean execute(float value) {
                sum += value;
                return true;
            }

            public double getSum() {
                return sum;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(15.0, procedure.getSum(), 0.01);
    }

    @Test
    public void testRemoveAt() {
        hash.remove(2.0f);
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(6.0f));
    }

    @Test
    public void testInsertKey() {
        assertEquals(5, hash.insertKey(6.0f));
        assertTrue(hash.contains(6.0f));
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(-1.0f, hash.getNoEntryKey(), 0.01);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(-1.0, hash.getNoEntryValue(), 0.01);
    }

    @Test
    public void testWriteExternal() {
        // TODO: Implement this test
    }

    @Test
    public void testReadExternal() {
        // TODO: Implement this test
    }
}
