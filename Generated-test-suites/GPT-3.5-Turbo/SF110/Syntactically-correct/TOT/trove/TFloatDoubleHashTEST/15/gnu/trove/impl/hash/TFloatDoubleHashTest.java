
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int result = tFloatDoubleHash.setUp(initialCapacity);

        assertEquals(initialCapacity, result);
        assertEquals(initialCapacity, tFloatDoubleHash._set.length);
    }

    @Test
    public void testContains() {
        float val = 5.0f;
        assertFalse(tFloatDoubleHash.contains(val));

        tFloatDoubleHash.insertKey(val);
        assertTrue(tFloatDoubleHash.contains(val));
    }

    @Test
    public void testForEach() {
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);
        tFloatDoubleHash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };

        assertTrue(tFloatDoubleHash.forEach(procedure));
        assertEquals(6.0f, procedure.sum, 0.0001);
    }

    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);

        tFloatDoubleHash.removeAt(0);

        assertFalse(tFloatDoubleHash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);

        assertEquals(0, tFloatDoubleHash.index(1.0f));
        assertEquals(1, tFloatDoubleHash.index(2.0f));
        assertEquals(-1, tFloatDoubleHash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        float val = 5.0f;
        int index = tFloatDoubleHash.insertKey(val);

        assertEquals(0, index);
        assertTrue(tFloatDoubleHash.contains(val));
    }

    @Test
    public void testXInsertKey() {
        float val = 5.0f;
        int index = tFloatDoubleHash.XinsertKey(val);

        assertEquals(0, index);
        assertTrue(tFloatDoubleHash.contains(val));
    }

    @Test
    public void testWriteExternal() {
        // To be implemented
    }

    @Test
    public void testReadExternal() {
        // To be implemented
    }
}

