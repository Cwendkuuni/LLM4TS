import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() throws Exception {
        tFloatDoubleHash = new TFloatDoubleHash();
    }

    @Test
    public void testConstructor1() {
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0);
    }

    @Test
    public void testConstructor2() {
        tFloatDoubleHash = new TFloatDoubleHash(5);
        assertEquals(5, tFloatDoubleHash._set.length);
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0);
    }

    @Test
    public void testConstructor3() {
        tFloatDoubleHash = new TFloatDoubleHash(5, 0.5f);
        assertEquals(5, tFloatDoubleHash._set.length);
        assertEquals(0.5f, tFloatDoubleHash._loadFactor, 0);
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0);
    }

    @Test
    public void testConstructor4() {
        tFloatDoubleHash = new TFloatDoubleHash(5, 0.5f, 1.1f, 2.2);
        assertEquals(5, tFloatDoubleHash._set.length);
        assertEquals(0.5f, tFloatDoubleHash._loadFactor, 0);
        assertEquals(1.1f, tFloatDoubleHash.getNoEntryKey(), 0);
        assertEquals(2.2, tFloatDoubleHash.getNoEntryValue(), 0);
    }

    @Test
    public void testContains_ValidKey_ReturnsTrue() {
        tFloatDoubleHash.insertKey(1.1f);
        assertTrue(tFloatDoubleHash.contains(1.1f));
    }

    @Test
    public void testContains_InvalidKey_ReturnsFalse() {
        tFloatDoubleHash.insertKey(1.1f);
        assertFalse(tFloatDoubleHash.contains(2.2f));
    }

    @Test
    public void testForEach_ExecuteProcedure_ReturnsTrue() {
        tFloatDoubleHash.insertKey(1.1f);
        tFloatDoubleHash.insertKey(2.2f);
        tFloatDoubleHash.insertKey(3.3f);

        final boolean[] executed = {false};
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                executed[0] = true;
                return true;
            }
        };

        assertTrue(tFloatDoubleHash.forEach(procedure));
        assertTrue(executed[0]);
    }

    @Test
    public void testForEach_InterruptProcedure_ReturnsFalse() {
        tFloatDoubleHash.insertKey(1.1f);
        tFloatDoubleHash.insertKey(2.2f);
        tFloatDoubleHash.insertKey(3.3f);

        final boolean[] executed = {false};
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                executed[0] = true;
                return false;
            }
        };

        assertFalse(tFloatDoubleHash.forEach(procedure));
        assertTrue(executed[0]);
    }

    @Test
    public void testSetUp_WithInitialCapacity_ReturnsCapacity() {
        int capacity = tFloatDoubleHash.setUp(5);
        assertEquals(5, capacity);
        assertEquals(5, tFloatDoubleHash._set.length);
    }

    @Test
    public void testRemoveAt_RemovesValueAtIndex() {
        tFloatDoubleHash.insertKey(1.1f);
        tFloatDoubleHash.insertKey(2.2f);
        tFloatDoubleHash.insertKey(3.3f);

        tFloatDoubleHash.removeAt(1);

        assertFalse(tFloatDoubleHash.contains(2.2f));
        assertEquals(0.0f, tFloatDoubleHash._set[1], 0);
    }

    @Test
    public void testIndex_ValidKey_ReturnsIndex() {
        tFloatDoubleHash.insertKey(1.1f);
        tFloatDoubleHash.insertKey(2.2f);
        tFloatDoubleHash.insertKey(3.3f);

        assertEquals(1, tFloatDoubleHash.index(2.2f));
    }

    @Test
    public void testIndex_InvalidKey_ReturnsNegativeIndex() {
        tFloatDoubleHash.insertKey(1.1f);
        tFloatDoubleHash.insertKey(2.2f);
        tFloatDoubleHash.insertKey(3.3f);

        assertEquals(-1, tFloatDoubleHash.index(4.4f));
    }

    // Edge case: Inserting more keys than the capacity
    @Test(expected = IllegalStateException.class)
    public void testInsertKey_MaxCapacityReached_ExceptionThrown() {
        tFloatDoubleHash = new TFloatDoubleHash(3, 1.0f, 1.1f, 2.2);

        tFloatDoubleHash.insertKey(1.1f);
        tFloatDoubleHash.insertKey(2.2f);
        tFloatDoubleHash.insertKey(3.3f);
        tFloatDoubleHash.insertKey(4.4f);
    }

    // Error scenario: Inserting the same key twice
    @Test
    public void testInsertKey_DuplicateKey_ReturnsNegativeIndex() {
        tFloatDoubleHash.insertKey(1.1f);
        int index = tFloatDoubleHash.insertKey(1.1f);
        assertTrue(index < 0);
    }

    @Test
    public void testInsertKey_ValidKey_ReturnsIndex() {
        int index = tFloatDoubleHash.insertKey(1.1f);
        assertEquals(0, index);
        assertTrue(tFloatDoubleHash.contains(1.1f));
    }

    @Test
    public void testWriteExternal() throws Exception {
        tFloatDoubleHash = new TFloatDoubleHash(2, 1.0f, 1.1f, 2.2);
        tFloatDoubleHash.insertKey(1.1f);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);

        tFloatDoubleHash.writeExternal(objectOutput);

        objectOutput.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);

        assertEquals(0, objectInput.readByte());
        TFloatDoubleHash readTFloatDoubleHash = new TFloatDoubleHash();
        readTFloatDoubleHash.readExternal(objectInput);
        assertEquals(1.1f, readTFloatDoubleHash._set[0], 0);
    }

    @Test
    public void testReadExternal() throws Exception {
        tFloatDoubleHash = new TFloatDoubleHash(2, 1.0f, 1.1f, 2.2);
        tFloatDoubleHash.insertKey(1.1f);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);

        tFloatDoubleHash.writeExternal(objectOutput);

        objectOutput.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);

        assertEquals(0, objectInput.readByte());
        TFloatDoubleHash readTFloatDoubleHash = new TFloatDoubleHash();
        readTFloatDoubleHash.readExternal(objectInput);
        assertEquals(1.1f, readTFloatDoubleHash._set[0], 0);
    }

}