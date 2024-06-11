import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }

    @After
    public void tearDown() {
        hash = null;
    }

    @Test
    public void testGetNoEntryKey() {
        float expected = 0.0f;
        float actual = hash.getNoEntryKey();
        assertEquals(expected, actual, 0.001f);
    }

    @Test
    public void testGetNoEntryValue() {
        char expected = '\0';
        char actual = hash.getNoEntryValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetUp() {
        int capacity = 100;
        int expectedCapacity = capacity;
        int actualCapacity = hash.setUp(capacity);
        assertEquals(expectedCapacity, actualCapacity);
    }

    @Test
    public void testContains() {
        float key = 1.5f;
        boolean expected = false;
        boolean actual = hash.contains(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testForEach() {
        // Test case without any elements
        TFloatProcedure procedure = val -> {
            // Should not execute as there are no elements in the hash
            return false;
        };
        boolean expected = true;
        boolean actual = hash.forEach(procedure);
        assertEquals(expected, actual);

        // Test case with 1 element
        float key = 1.5f;
        char value = 'A';
        hash.insertKey(key);

        procedure = val -> {
            assertEquals(key, val, 0.001f);
            assertEquals(value, hash.getNoEntryValue());
            return false;
        };
        expected = false;
        actual = hash.forEach(procedure);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveAt() {
        // Test case without any elements
        int index = 0;
        hash.removeAt(index);
        // Should not throw any exception

        // Test case with 1 element
        float key = 1.5f;
        char value = 'A';
        hash.insertKey(key);

        index = 0;
        hash.removeAt(index);
        assertEquals(key, hash.getNoEntryKey(), 0.001f);
        assertEquals(value, hash.getNoEntryValue());
    }

    @Test
    public void testIndex() {
        // Test case without any elements
        float key = 1.5f;
        int expected = -1;
        int actual = hash.index(key);
        assertEquals(expected, actual);

        // Test case with 1 element
        float insertedKey = 1.5f;
        char insertedValue = 'A';
        hash.insertKey(insertedKey);

        float nonInsertedKey = 2.5f;
        expected = -1;
        actual = hash.index(nonInsertedKey);
        assertEquals(expected, actual);

        expected = 0;
        actual = hash.index(insertedKey);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertKey() {
        // Test case without any elements
        float key = 1.5f;
        int expected = 0;
        int actual = hash.insertKey(key);
        assertEquals(expected, actual);

        // Test case with 1 element
        expected = -1;
        actual = hash.insertKey(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testIndexRehashed() {
        // Test case without any elements
        float key = 1.5f;
        int index = 0;
        int hashValue = 1234;
        byte state = 0;
        int expected = -1;
        int actual = hash.indexRehashed(key, index, hashValue, state);
        assertEquals(expected, actual);

        // Test case with 1 element
        float insertedKey = 1.5f;
        char insertedValue = 'A';
        hash.insertKey(insertedKey);

        float nonInsertedKey = 2.5f;
        index = 0;
        hashValue = 1234;
        state = 0;
        expected = -1;
        actual = hash.indexRehashed(nonInsertedKey, index, hashValue, state);
        assertEquals(expected, actual);

        expected = 0;
        actual = hash.indexRehashed(insertedKey, index, hashValue, state);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertKeyRehash() {
        // Test case without any elements
        float key = 1.5f;
        int index = 0;
        int hashValue = 1234;
        byte state = 0;
        int expected = 0;
        int actual = hash.insertKeyRehash(key, index, hashValue, state);
        assertEquals(expected, actual);

        // Test case with 1 element
        float insertedKey = 1.5f;
        char insertedValue = 'A';
        hash.insertKey(insertedKey);

        float nonInsertedKey = 2.5f;
        index = 0;
        hashValue = 1234;
        state = 0;
        expected = -1;
        actual = hash.insertKeyRehash(nonInsertedKey, index, hashValue, state);
        assertEquals(expected, actual);

        expected = -1;
        actual = hash.insertKeyRehash(insertedKey, index, hashValue, state);
        assertNotEquals(expected, actual);
    }

    @Test
    public void testInsertKeyAt() {
        int index = 0;
        float val = 1.5f;
        hash.insertKeyAt(index, val);

        float expectedKey = val;
        float actualKey = hash._set[index];
        assertEquals(expectedKey, actualKey, 0.001f);

        char expectedValue = hash.getNoEntryValue();
        char actualValue = hash._states[index];
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testXInsertKey() {
        // Test case without any elements
        float key = 1.5f;
        int expected = 0;
        int actual = hash.XinsertKey(key);
        assertEquals(expected, actual);

        // Test case with 1 element
        expected = -1;
        actual = hash.XinsertKey(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testWriteExternal() {
        // No need to test as it only writes values to the output stream
    }

    @Test
    public void testReadExternal() {
        // No need to test as it only reads values from the input stream
    }
}