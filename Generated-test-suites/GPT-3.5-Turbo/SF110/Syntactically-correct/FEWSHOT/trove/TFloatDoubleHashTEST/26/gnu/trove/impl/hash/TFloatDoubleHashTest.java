import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
   }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        final float[] expected = new float[]{1.0f, 2.0f, 3.0f};
        final float[] actual = new float[hash.size()];
        int i = 0;

        hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                actual[i++] = value;
                return true;
        }});

        assertArrayEquals(expected, actual, 0.0f);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(3, hash.size());

        hash.removeAt(1);

        assertEquals(2, hash.size());
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
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
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(-2, hash.insertKey(2.0f));

        assertEquals(2, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }
}