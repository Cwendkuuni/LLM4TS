import static org.junit.Assert.*;
import org.junit.Test;
import gnu.trove.impl.hash.TFloatCharHash;

public class TFloatCharHashTest {

    // Test case for getNoEntryKey() method
    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float expectedKey = 0.0f;
        float actualKey = hash.getNoEntryKey();
        assertEquals(expectedKey, actualKey, 0.0f);
    }

    // Test case for getNoEntryValue() method
    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        char expectedValue = '\0';
        char actualValue = hash.getNoEntryValue();
        assertEquals(expectedValue, actualValue);
    }

    // Test case for contains(float val) method
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.5f);
        hash.insertKey(20.7f);
        hash.insertKey(30.9f);

        assertTrue(hash.contains(20.7f));
        assertFalse(hash.contains(15.3f));
        assertFalse(hash.contains(40.2f));
    }

    // Test case for forEach(TFloatProcedure procedure) method
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;

            public boolean execute(float value) {
                sum += value;
                return true;
            }

            public float getSum() {
                return sum;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(60.0f, procedure.getSum(), 0.0f);
    }

    // Test case for removeAt(int index) method
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);

        hash.removeAt(1);
        assertFalse(hash.contains(20.0f));
    }

    // Test case for index(float key) method
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);

        int expectedIndex = 1;
        int actualIndex = hash.index(20.0f);
        assertEquals(expectedIndex, actualIndex);

        expectedIndex = -1;
        actualIndex = hash.index(15.0f);
        assertEquals(expectedIndex, actualIndex);
    }

    // Test case for insertKey(float val) method
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(10.0f);
        assertEquals(0, index);

        index = hash.insertKey(20.0f);
        assertEquals(1, index);

        index = hash.insertKey(30.0f);
        assertEquals(2, index);
    }

    // Test case for XinsertKey(float key) method
    @Test
    public void testXInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.XinsertKey(10.0f);
        assertEquals(0, index);

        index = hash.XinsertKey(20.0f);
        assertEquals(1, index);

        index = hash.XinsertKey(30.0f);
        assertEquals(2, index);
    }

    // Test case for writeExternal(ObjectOutput out) method
    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);

        // Test writing to object output
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(hash);
            out.flush();
            byte[] byteArray = bos.toByteArray();
            assertNotNull(byteArray);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    // Test case for readExternal(ObjectInput in) method
    @Test
    public void testReadExternal() {
        TFloatCharHash expectedHash = new TFloatCharHash();
        expectedHash.insertKey(10.0f);
        expectedHash.insertKey(20.0f);
        expectedHash.insertKey(30.0f);

        // Test reading from object input
        ByteArrayInputStream bis = new ByteArrayInputStream(expectedHash);
        ObjectInput in;
        try {
            in = new ObjectInputStream(bis);
            TFloatCharHash actualHash = (TFloatCharHash) in.readObject();
            assertNotNull(actualHash);
            assertEquals(expectedHash.size(), actualHash.size());
        } catch (IOException | ClassNotFoundException e) {
            fail(e.getMessage());
        }
    }
}