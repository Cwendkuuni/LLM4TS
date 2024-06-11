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
        float expected = 0.0f;
        float result = hash.getNoEntryKey();
        assertEquals(expected, result, 0);
    }

    @Test
    public void testGetNoEntryValue() {
        char expected = '\0';
        char result = hash.getNoEntryValue();
        assertEquals(expected, result);
    }

    @Test
    public void testContains_whenKeyExists() {
        float key = 5.5f;
        hash.insertKey(key);
        boolean result = hash.contains(key);
        assertTrue(result);
    }

    @Test
    public void testContains_whenKeyDoesNotExist() {
        float key = 5.5f;
        boolean result = hash.contains(key);
        assertFalse(result);
    }

    @Test
    public void testForEach() {
        final float[] values = {3.3f, 4.4f, 5.5f};
        final boolean[] executed = {false};

        TFloatProcedure procedure = new TFloatProcedure() {
            int index = 0;

            public boolean execute(float value) {
                assertEquals(values[index], value, 0);
                executed[0] = true;
                index++;
                return true;
            }
        };

        hash.insertKey(values[0]);
        hash.insertKey(values[1]);
        hash.insertKey(values[2]);

        hash.forEach(procedure);

        assertTrue(executed[0]);
    }

    @Test
    public void testRemoveAt() {
        float[] keys = {3.3f, 4.4f, 5.5f};
        hash.insertKey(keys[0]);
        hash.insertKey(keys[1]);
        hash.insertKey(keys[2]);

        int indexToRemove = hash.index(keys[1]);
        hash.removeAt(indexToRemove);

        boolean containsRemovedKey = hash.contains(keys[1]);
        assertFalse(containsRemovedKey);
    }

    @Test
    public void testInsertKey() {
        float key = 5.5f;
        int result = hash.insertKey(key);
        assertEquals(0, result);

        boolean containsKey = hash.contains(key);
        assertTrue(containsKey);
    }

    @Test
    public void testInsertKey_whenKeyAlreadyExists() {
        float key = 5.5f;
        hash.insertKey(key);
        int result = hash.insertKey(key);
        assertEquals(-1, result);
    }

    @Test
    public void testInsertKey_whenAllSlotsAreFull() {
        int capacity = 3;
        hash = new TFloatCharHash(capacity);

        float[] keys = {3.3f, 4.4f, 5.5f};
        hash.insertKey(keys[0]);
        hash.insertKey(keys[1]);
        hash.insertKey(keys[2]);

        float newKey = 6.6f;
        int result = hash.insertKey(newKey);
        assertEquals(1, result);

        boolean containsNewKey = hash.contains(newKey);
        assertTrue(containsNewKey);
    }

    @Test
    public void testXinsertKey() {
        float key = 5.5f;
        int result = hash.XinsertKey(key);
        assertEquals(0, result);

        boolean containsKey = hash.contains(key);
        assertTrue(containsKey);
    }

    @Test
    public void testWriteExternal() {
        // TODO: Test the writeExternal() method
    }

    @Test
    public void testReadExternal() {
        // TODO: Test the readExternal() method
    }

}