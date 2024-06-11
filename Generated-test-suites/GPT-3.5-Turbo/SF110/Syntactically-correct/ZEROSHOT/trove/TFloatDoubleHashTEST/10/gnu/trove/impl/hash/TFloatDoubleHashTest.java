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
        float no_entry_key = hash.getNoEntryKey();
        assertEquals(0.0f, no_entry_key, 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        double no_entry_value = hash.getNoEntryValue();
        assertEquals(0.0, no_entry_value, 0.001);
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(1.5f));
        hash.insertKey(1.5f);
        assertTrue(hash.contains(1.5f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.5f);
        hash.insertKey(2.5f);
        hash.insertKey(3.5f);

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

        boolean result = hash.forEach(procedure);
        assertTrue(result);

        double sum = ((TFloatProcedure)procedure).getSum();
        assertEquals(7.5, sum, 0.001);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.5f);
        assertTrue(hash.contains(1.5f));

        hash.removeAt(0);
        assertFalse(hash.contains(1.5f));
    }

    @Test
    public void testInsertKey() {
        int index = hash.insertKey(1.5f);
        assertTrue(index >= 0);

        int index2 = hash.insertKey(1.5f);
        assertTrue(index2 < 0);
    }

    @Test
    public void testIndex() {
        hash.insertKey(1.5f);
        int index = hash.index(1.5f);
        assertEquals(0, index);

        int index2 = hash.index(2.5f);
        assertEquals(-1, index2);
    }

    @Test
    public void testXinsertKey() {
        int index = hash.XinsertKey(1.5f);
        assertTrue(index >= 0);

        int index2 = hash.XinsertKey(1.5f);
        assertTrue(index2 < 0);
    }

    @Test
    public void testWriteExternal() {
        // Not implemented
    }

    @Test
    public void testReadExternal() {
        // Not implemented
    }

}