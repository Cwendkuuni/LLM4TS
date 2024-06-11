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
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testContains() {
        assertFalse(tFloatDoubleHash.contains(1.0f));
        assertFalse(tFloatDoubleHash.contains(2.0f));
        assertFalse(tFloatDoubleHash.contains(3.0f));
    }

    @Test
    public void testInsertKey() {
        int index1 = tFloatDoubleHash.insertKey(1.0f);
        assertEquals(0, index1);

        int index2 = tFloatDoubleHash.insertKey(2.0f);
        assertEquals(1, index2);

        int index3 = tFloatDoubleHash.insertKey(3.0f);
        assertEquals(2, index3);
    }

    @Test
    public void testIndex() {
        assertEquals(-1, tFloatDoubleHash.index(1.0f));
        assertEquals(-1, tFloatDoubleHash.index(2.0f));
        assertEquals(-1, tFloatDoubleHash.index(3.0f));

        tFloatDoubleHash.insertKey(1.0f);
        assertEquals(0, tFloatDoubleHash.index(1.0f));

        tFloatDoubleHash.insertKey(2.0f);
        assertEquals(1, tFloatDoubleHash.index(2.0f));

        tFloatDoubleHash.insertKey(3.0f);
        assertEquals(2, tFloatDoubleHash.index(3.0f));
    }

    @Test
    public void testIndexRehashed() {
        assertEquals(-1, tFloatDoubleHash.indexRehashed(1.0f, 0, 0, (byte) 0));
        assertEquals(-1, tFloatDoubleHash.indexRehashed(2.0f, 0, 0, (byte) 0));
        assertEquals(-1, tFloatDoubleHash.indexRehashed(3.0f, 0, 0, (byte) 0));

        tFloatDoubleHash.insertKey(1.0f);
        assertEquals(0, tFloatDoubleHash.indexRehashed(1.0f, 0, 0, (byte) 1));

        tFloatDoubleHash.insertKey(2.0f);
        assertEquals(1, tFloatDoubleHash.indexRehashed(2.0f, 0, 0, (byte) 1));

        tFloatDoubleHash.insertKey(3.0f);
        assertEquals(2, tFloatDoubleHash.indexRehashed(3.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKeyRehash() {
        int index1 = tFloatDoubleHash.insertKeyRehash(1.0f, 0, 0, (byte) 0);
        assertEquals(0, index1);

        int index2 = tFloatDoubleHash.insertKeyRehash(2.0f, 1, 0, (byte) 0);
        assertEquals(1, index2);

        int index3 = tFloatDoubleHash.insertKeyRehash(3.0f, 2, 0, (byte) 0);
        assertEquals(2, index3);
    }

    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.insertKey(1.0f);
        assertEquals(0, tFloatDoubleHash.index(1.0f));

        tFloatDoubleHash.removeAt(0);
        assertEquals(-1, tFloatDoubleHash.index(1.0f));
    }

    @Test
    public void testForEach() {
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);
        tFloatDoubleHash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                System.out.println(val);
                return true;
            }
        };

        assertTrue(tFloatDoubleHash.forEach(procedure));
    }

    @Test
    public void testWriteExternal() {
        // Test writeExternal method here
    }

    @Test
    public void testReadExternal() {
        // Test readExternal method here
    }

    @Test
    public void testSetUp() {
        int capacity = tFloatDoubleHash.setUp(10);
        assertEquals(10, capacity);
    }

}