import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash tFloatCharHash;

    @Before
    public void setUp() {
        tFloatCharHash = new TFloatCharHash();
    }

    @Test
    public void testGetNoEntryKey() {
       // Typical use case
        float expected = 0.0f;
        float actual = tFloatCharHash.getNoEntryKey();
        assertEquals(expected, actual, 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        // Typical use case
        char expected = '\0';
        char actual = tFloatCharHash.getNoEntryValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testContains() {
        // Typical use case - value exists in the hash
        tFloatCharHash.insertKey(1.0f);
        boolean expected = true;
        boolean actual = tFloatCharHash.contains(1.0f);
        assertEquals(expected, actual);

        // Typical use case - value does not exist in the hash
        expected = false;
        actual = tFloatCharHash.contains(2.0f);
        assertEquals(expected, actual);
    }

    @Test
    public void testForEach() {
        // Typical use case - execute procedure on all elements
        tFloatCharHash.insertKey(1.0f);
        tFloatCharHash.insertKey(2.0f);
        tFloatCharHash.insertKey(3.0f);

        final float[] expected = {1.0f, 2.0f, 3.0f};
        final float[] actual = new float[3];

        TFloatProcedure procedure = new TFloatProcedure() {
            int index = 0;

            @Override
            public boolean execute(float value) {
                actual[index++] = value;
                return true;
            }
        };

        boolean result = tFloatCharHash.forEach(procedure);
        assertTrue(result);
        assertArrayEquals(expected, actual, 0.0f);
    }

    @Test
    public void testRemoveAt() {
        // Typical use case - remove existing key-value pair
        tFloatCharHash.insertKey(1.0f);
        int initialSize = tFloatCharHash.size();

        tFloatCharHash.removeAt(0);

        int expectedSize = initialSize - 1;
        int actualSize = tFloatCharHash.size();
        assertEquals(expectedSize, actualSize);

        float expectedNoEntryKey = 0.0f;
        float actualNoEntryKey = tFloatCharHash.getNoEntryKey();
        assertEquals(expectedNoEntryKey, actualNoEntryKey, 0.0f);

        char expectedNoEntryValue = '\0';
        char actualNoEntryValue = tFloatCharHash.getNoEntryValue();
        assertEquals(expectedNoEntryValue, actualNoEntryValue);
    }

    @Test
    public void testIndex() {
        // Typical use case - value exists in the hash
        tFloatCharHash.insertKey(1.0f);
        int expected = 0;
        int actual = tFloatCharHash.index(1.0f);
        assertEquals(expected, actual);

        // Typical use case - value does not exist in the hash
        expected = -1;
        actual = tFloatCharHash.index(2.0f);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertKey() {
        // Typical use case - inserting new key-value pair
        int expectedIndex = 0;
        int actualIndex = tFloatCharHash.insertKey(1.0f);
        assertEquals(expectedIndex, actualIndex);
        assertTrue(tFloatCharHash.contains(1.0f));

        // Edge case - inserting a value that already exists
        expectedIndex = -1;
        actualIndex = tFloatCharHash.insertKey(1.0f);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testInsertKeyRehash() {
        // Typical use case - inserting new key-value pair
        int expectedIndex = 0;
        int actualIndex = tFloatCharHash.insertKeyRehash(1.0f, 0, 0, (byte) 0);
        assertEquals(expectedIndex, actualIndex);
        assertTrue(tFloatCharHash.contains(1.0f));

        // Edge case - inserting a value that already exists
        expectedIndex = -1;
        actualIndex = tFloatCharHash.insertKeyRehash(1.0f, 0, 0, (byte) 1);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testInsertKeyAt() {
        // Typical use case - inserting new key-value pair
        tFloatCharHash.insertKeyAt(0, 1.0f);
        assertTrue(tFloatCharHash.contains(1.0f));
    }

    @Test
    public void testWriteExternal() {
        // Typical use case
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            tFloatCharHash.writeExternal(oos);
            oos.flush();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);

            TFloatCharHash result = new TFloatCharHash();
            result.readExternal(ois);

            assertEquals(tFloatCharHash.getNoEntryKey(), result.getNoEntryKey(), 0.0f);
            assertEquals(tFloatCharHash.getNoEntryValue(), result.getNoEntryValue());

        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadExternal() {
        // Typical use case
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            tFloatCharHash.writeExternal(oos);
            oos.flush();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);

            TFloatCharHash result = new TFloatCharHash();
            result.readExternal(ois);

            assertEquals(tFloatCharHash.getNoEntryKey(), result.getNoEntryKey(), 0.0f);
            assertEquals(tFloatCharHash.getNoEntryValue(), result.getNoEntryValue());

        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

}