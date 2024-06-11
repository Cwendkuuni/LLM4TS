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
    public void testNoEntryKey() {
        float expectedKey = 0.0f;
        float actualKey = floatCharHash.getNoEntryKey();
        assertEquals(expectedKey, actualKey, 0.0001);
    }

    @Test
    public void testNoEntryValue() {
        char expectedValue = '\0';
        char actualValue = floatCharHash.getNoEntryValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testContainsWithEmptyHash() {
        assertFalse(floatCharHash.contains(10.5f));
    }

    @Test
    public void testContainsWithExistingValue() {
        floatCharHash.insertKey(10.5f);
        assertTrue(floatCharHash.contains(10.5f));
    }

    @Test
    public void testContainsWithNonExistingValue() {
        floatCharHash.insertKey(5.5f);
        assertFalse(floatCharHash.contains(10.5f));
    }

    @Test
    public void testForEachProcedure() {
        floatCharHash.insertKey(10.5f);
        floatCharHash.insertKey(5.5f);

        final StringBuilder stringBuilder = new StringBuilder();
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                stringBuilder.append(value);
                return true;
            }
        };

        assertTrue(floatCharHash.forEach(procedure));
        assertEquals("10.55.5", stringBuilder.toString());
    }

    @Test
    public void testRemoveAt() {
        floatCharHash.insertKey(10.5f);
        floatCharHash.insertKey(5.5f);

        assertTrue(floatCharHash.contains(10.5f));
        floatCharHash.removeAt(0);
        assertFalse(floatCharHash.contains(10.5f));
    }

    @Test
    public void testInsertKey() {
        float expectedKey = 10.5f;
        int index = floatCharHash.insertKey(expectedKey);
        assertTrue(index >= 0);
        assertTrue(floatCharHash.contains(expectedKey));
    }

    @Test
    public void testInsertKeyWithDuplicateKey() {
        float expectedKey = 10.5f;
        floatCharHash.insertKey(expectedKey);
        int index = floatCharHash.insertKey(expectedKey);
        assertTrue(index < 0);
    }

    @Test
    public void testInsertKeyWithRehash() {
        // Insert elements to fill up the Hash
        for (int i = 0; i < 100; i++) {
            floatCharHash.insertKey(i);
        }

        // Insert a new element which requires rehashing
        float expectedKey = 105.5f;
        int index = floatCharHash.insertKey(expectedKey);
        assertTrue(index >= 0);
        assertTrue(floatCharHash.contains(expectedKey));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHash() {
        // Insert elements to fill up the Hash
        for (int i = 0; i < 100; i++) {
            floatCharHash.insertKey(i);
        }

        // Insert a new element when the Hash is full
        floatCharHash.insertKey(105.5f);
    }

    @Test
    public void testExternalReadWrite() {
        floatCharHash.insertKey(10.5f);
        floatCharHash.insertKey(5.5f);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(floatCharHash);

            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            TFloatCharHash deserializedHash = (TFloatCharHash) objectInputStream.readObject();

            assertTrue(deserializedHash.contains(10.5f));
            assertTrue(deserializedHash.contains(5.5f));
            assertEquals(floatCharHash.getNoEntryKey(), deserializedHash.getNoEntryKey(), 0.0001);
            assertEquals(floatCharHash.getNoEntryValue(), deserializedHash.getNoEntryValue());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

}