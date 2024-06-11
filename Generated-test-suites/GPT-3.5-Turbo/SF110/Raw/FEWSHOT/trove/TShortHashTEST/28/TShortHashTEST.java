package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {
    private TShortHash hash;

    @Before
    public void setUp() {
        hash = new TShortHash();
    }

    @Test
    public void testContains() {
        hash.insertKey((short) 1);
        assertTrue(hash.contains((short) 1));
        assertFalse(hash.contains((short) 2));
    }

    @Test
    public void testForEach() {
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);

        final boolean[] result = {false};
        hash.forEach((val) -> {
            if (val == 1) {
                result[0] = true;
            }
            return true;
        });

        assertTrue(result[0]);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);

        hash.removeAt(0);

        assertFalse(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
    }
    
    @Test
    public void testInsertKey() {
        int index = hash.insertKey((short) 1);
        assertTrue(hash.contains((short) 1));
        assertEquals(0, index);

        index = hash.insertKey((short) 2);
        assertTrue(hash.contains((short) 2));
        assertEquals(1, index);

        index = hash.insertKey((short) 3);
        assertTrue(hash.contains((short) 3));
        assertEquals(2, index);
    }
}