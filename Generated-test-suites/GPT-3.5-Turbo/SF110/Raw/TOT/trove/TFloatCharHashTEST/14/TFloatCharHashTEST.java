import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash floatCharHash;

    @Before
    public void setUp() {
        floatCharHash = new TFloatCharHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, floatCharHash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', floatCharHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int capacity = floatCharHash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(initialCapacity, floatCharHash._set.length);
    }

    @Test
    public void testContains() {
        floatCharHash.insertKey(1.0f);
        assertTrue(floatCharHash.contains(1.0f));
        assertFalse(floatCharHash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);
        floatCharHash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;

            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };

        assertTrue(floatCharHash.forEach(procedure));
        assertEquals(6.0f, procedure.sum, 0.0001);
    }

    @Test
    public void testRemoveAt() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);

        floatCharHash.removeAt(0);
        assertFalse(floatCharHash.contains(1.0f));
        assertEquals(2.0f, floatCharHash._set[0], 0.0001);
    }

    @Test
    public void testIndex() {
        floatCharHash.insertKey(1.0f);
        assertEquals(0, floatCharHash.index(1.0f));

        floatCharHash.removeAt(0);
        assertEquals(-1, floatCharHash.index(1.0f));
    }

    @Test
    public void testInsertKey() {
        floatCharHash.insertKey(1.0f);
        assertTrue(floatCharHash.contains(1.0f));

        floatCharHash.insertKey(2.0f);
        assertTrue(floatCharHash.contains(2.0f));

        floatCharHash.insertKey(3.0f);
        assertTrue(floatCharHash.contains(3.0f));

        assertEquals(1, floatCharHash.index(2.0f));
    }

    @Test
    public void testInsertKeyAtFullCapacity() {
        floatCharHash = new TFloatCharHash(3);
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);
        floatCharHash.insertKey(3.0f);

        try {
            floatCharHash.insertKey(4.0f);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testXinsertKey() {
        floatCharHash.XinsertKey(1.0f);
        assertTrue(floatCharHash.contains(1.0f));

        floatCharHash.XinsertKey(2.0f);
        assertTrue(floatCharHash.contains(2.0f));

        floatCharHash.XinsertKey(3.0f);
        assertTrue(floatCharHash.contains(3.0f));

        assertEquals(1, floatCharHash.index(2.0f));
    }

    @Test
    public void testXinsertKeyAtFullCapacity() {
        floatCharHash = new TFloatCharHash(3);
        floatCharHash.XinsertKey(1.0f);
        floatCharHash.XinsertKey(2.0f);
        floatCharHash.XinsertKey(3.0f);

        try {
            floatCharHash.XinsertKey(4.0f);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception caught, test passed
        }
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