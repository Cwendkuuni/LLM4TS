
package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        // Test procedure that prints the value of each key
        TFloatProcedure procedure = (float value) -> {
            System.out.println(value);
            return true;
        };

        // Insert some keys
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        // Call forEach with the procedure
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
    }

    @Test
    public void testIndexRehashed() {
        hash.insertKey(1.0f);
        assertEquals(0, hash.indexRehashed(1.0f, 0, 1, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        int index = hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertEquals(0, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        // Fill up the hash map
        for (float i = 0; i < hash._set.length; i++) {
            hash.insertKey(i);
        }

        // Try inserting a new key, should throw IllegalStateException
        hash.insertKeyRehash(100.0f, 0, 100, (byte) 1);
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testWriteExternal() {
        // TODO: Implement the test for writeExternal method
    }

    @Test
    public void testReadExternal() {
        // TODO: Implement the test for readExternal method
    }
}

