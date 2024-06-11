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
    public void testSetUp() {
        int capacity = hash.setUp(5);
        assertEquals(5, capacity);
    }

    @Test
    public void testContains() {
        hash.insertKey(2.5f);
        assertTrue(hash.contains(2.5f));
    }

    @Test
    public void testContains_NotFound() {
        assertFalse(hash.contains(3.2f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(2.5f);
        hash.insertKey(1.8f);
        hash.insertKey(4.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0;

            @Override
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(8.3, procedure.sum, 0.0);
    }

    @Test
    public void testForEach_Interrupted() {
        hash.insertKey(2.5f);
        hash.insertKey(1.8f);
        hash.insertKey(4.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            int count = 0;

            @Override
            public boolean execute(float value) {
                count++;
                if (count >= 2) {
                    return false;
                }
                return true;
            }
        };

        assertFalse(hash.forEach(procedure));
        assertEquals(2, procedure.count);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(2.5f);
        hash.insertKey(1.8f);
        hash.insertKey(4.0f);

        hash.removeAt(1);
        assertFalse(hash.contains(1.8f));
    }

    @Test
    public void testIndex() {
        hash.insertKey(2.5f);
        hash.insertKey(1.8f);
        hash.insertKey(4.0f);

        int index = hash.index(1.8f);
        assertEquals(1, index);
    }

    @Test
    public void testIndex_KeyNotFound() {
        hash.insertKey(2.5f);
        hash.insertKey(1.8f);
        hash.insertKey(4.0f);

        int index = hash.index(3.2f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        hash.insertKey(2.5f);
        hash.insertKey(1.8f);
        hash.insertKey(4.0f);

        int index = hash.indexRehashed(1.8f, 1, 12, (byte) 1);
        assertEquals(1, index);
    }

    @Test
    public void testIndexRehashed_KeyNotFound() {
        hash.insertKey(2.5f);
        hash.insertKey(1.8f);
        hash.insertKey(4.0f);

        int index = hash.indexRehashed(3.2f, 1, 12, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        hash.insertKey(2.5f);
        assertTrue(hash.contains(2.5f));
    }

    @Test
    public void testInsertKey_AlreadyExists() {
        hash.insertKey(2.5f);
        hash.insertKey(1.8f);
        hash.insertKey(4.0f);

        int index = hash.insertKey(4.0f);
        assertEquals(-3, index);
    }

    @Test
    public void testInsertKeyRehash() {
        hash.insertKey(2.5f);
        hash.insertKey(1.8f);
        hash.insertKey(4.0f);

        int index = hash.insertKeyRehash(1.8f, 1, 12, (byte) 1);
        assertEquals(1, index);
    }

    @Test
    public void testInsertKeyRehash_AlreadyExists() {
        hash.insertKey(2.5f);
        hash.insertKey(1.8f);
        hash.insertKey(4.0f);

        int index = hash.insertKeyRehash(2.5f, 1, 12, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyAt() {
        byte[] states = new byte[]{0, 1, 2};
        float[] set = new float[3];
        hash._states = states;
        hash._set = set;

        hash.insertKeyAt(1, 3.2f);
        assertEquals(3.2f, set[1], 0.0);
        assertEquals(1, states[1]);
    }

    @Test
    public void testXinsertKey() {
        byte[] states = new byte[]{0, 1, 2};
        float[] set = new float[3];
        hash._states = states;
        hash._set = set;

        int index = hash.XinsertKey(2.5f);
        assertTrue(hash.contains(2.5f));
        assertEquals(1, states[index]);
    }

    @Test
    public void testWriteExternal() throws Exception {
        // Not testable
    }

    @Test
    public void testReadExternal() throws Exception {
        // Not testable
    }

}