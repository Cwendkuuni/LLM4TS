import org.junit.*;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    /**
     * Test case for the constructor TFloatDoubleHash()
     */
    @Test
    public void testConstructor1() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals(0.0, hash.getNoEntryValue(), 0);
    }

    /**
     * Test case for the constructor TFloatDoubleHash(int)
     */
    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TFloatDoubleHash hash = new TFloatDoubleHash(initialCapacity);
        assertEquals(initialCapacity, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals(0.0, hash.getNoEntryValue(), 0);
    }

    /**
     * Test case for the constructor TFloatDoubleHash(int, float)
     */
    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TFloatDoubleHash hash = new TFloatDoubleHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, hash.capacity());
        assertEquals(loadFactor, hash.loadFactor(), 0);
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals(0.0, hash.getNoEntryValue(), 0);
    }

    /**
     * Test case for the constructor TFloatDoubleHash(int, float, float, double)
     */
    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        float noEntryKey = -1.0f;
        double noEntryValue = -1.0;
        TFloatDoubleHash hash = new TFloatDoubleHash(initialCapacity, loadFactor, noEntryKey, noEntryValue);
        assertEquals(initialCapacity, hash.capacity());
        assertEquals(loadFactor, hash.loadFactor(), 0);
        assertEquals(noEntryKey, hash.getNoEntryKey(), 0);
        assertEquals(noEntryValue, hash.getNoEntryValue(), 0);
    }

    /**
     * Test case for the method contains(float)
     */
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(10.0f);
        assertTrue(hash.contains(10.0f));
        assertFalse(hash.contains(20.0f));
    }

    /**
     * Test case for the method forEach(TFloatProcedure)
     */
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(30.0, procedure.sum, 0);
    }

    /**
     * Test case for the method removeAt(int)
     */
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(10.0f));
    }

    /**
     * Test case for the method index(float)
     */
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        assertEquals(0, hash.index(10.0f));
        assertEquals(1, hash.index(20.0f));
        assertEquals(-1, hash.index(30.0f));
    }

    /**
     * Test case for the method insertKey(float)
     */
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.insertKey(10.0f));
        assertEquals(1, hash.insertKey(20.0f));
        assertEquals(-1, hash.insertKey(10.0f));
    }

    /**
     * Test case for the method insertKeyAt(int, float)
     */
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 10.0f);
        assertEquals(10.0f, hash.get(0), 0);
    }

}