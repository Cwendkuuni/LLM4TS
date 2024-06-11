package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(0.0f));

        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f));
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.println("Processing value: " + value);
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));

        hash.removeAt(1);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));

        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testInsertKey() {
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);

        index = hash.insertKey(2.0f);
        assertEquals(1, index);

        index = hash.insertKey(3.0f);
        assertEquals(2, index);
    }

    @Test
    public void testInsertKeyRehash() {
        hash.insertKey(0.0f);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);

        int index = hash.insertKeyRehash(1.0f, 0, 1, (byte) 1);
        assertEquals(-1, index);

        index = hash.insertKeyRehash(6.0f, 0, 6, (byte) 0);
        assertEquals(0, index);

        index = hash.insertKeyRehash(6.0f, 0, 6, (byte) 1);
        assertEquals(1, index);

        index = hash.insertKeyRehash(6.0f, 0, 6, (byte) 2);
        assertEquals(1, index);

        index = hash.insertKeyRehash(6.0f, 0, 6, (byte) 3);
        assertEquals(1, index);
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(0.0f));

        hash.insertKeyAt(1, 2.0f);
        assertTrue(hash.contains(2.0f));

        hash.insertKeyAt(0, 3.0f);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(3.0f));
    }

}