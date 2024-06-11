import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash(10, 0.75f, -1.0f, -1.0);
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(-1.0f, tFloatDoubleHash.getNoEntryKey(), 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(-1.0, tFloatDoubleHash.getNoEntryValue(), 0.001);
    }

    @Test
    public void testContains_existingKey_shouldReturnTrue() {
        tFloatDoubleHash.insertKey(2.5f);
        assertTrue(tFloatDoubleHash.contains(2.5f));
    }

    @Test
    public void testContains_nonExistingKey_shouldReturnFalse() {
        assertFalse(tFloatDoubleHash.contains(4.2f));
    }

    @Test
    public void testForEach_modifyValue_shouldUpdateValues() {
        final double[] values = new double[]{1.1, 2.2, 3.3};
        for (double value : values) {
            tFloatDoubleHash.insertKey((float) value);
        }
        
        tFloatDoubleHash.forEach((float val) -> {
            tFloatDoubleHash.insertKey(val + 1);
            return true;
        });

        for (double value : values) {
            assertFalse(tFloatDoubleHash.contains((float) value));
            assertTrue(tFloatDoubleHash.contains((float) (value + 1)));
        }
    }

    @Test
    public void testRemoveAt_existingKey_shouldRemoveKey() {
        tFloatDoubleHash.insertKey(3.14f);
        tFloatDoubleHash.removeAt(0);
        assertFalse(tFloatDoubleHash.contains(3.14f));
    }

    @Test
    public void testRemoveAt_nonExistingKey_shouldNotRemoveAnyKey() {
        tFloatDoubleHash.insertKey(1.5f);
        tFloatDoubleHash.removeAt(1); // No key at index 1
        assertTrue(tFloatDoubleHash.contains(1.5f));
    }

    @Test
    public void testIndex_existingKey_shouldReturnIndex() {
        tFloatDoubleHash.insertKey(2.2f);
        assertEquals(0, tFloatDoubleHash.index(2.2f));
    }

    @Test
    public void testIndex_nonExistingKey_shouldReturnNegativeIndex() {
        tFloatDoubleHash.insertKey(1.23f);
        assertEquals(-1, tFloatDoubleHash.index(9.99f));
    }

    @Test
    public void testInsertKey_noFreeSlots_shouldThrowException() {
        final int initialCapacity = tFloatDoubleHash._states.length;
        for (int i = 0; i < initialCapacity; i++) {
            tFloatDoubleHash.insertKey(i);
        }

        try {
            tFloatDoubleHash.insertKey(initialCapacity + 1);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }

    @Test
    public void testInsertKey_existingKey_shouldReturnNegativeIndex() {
        tFloatDoubleHash.insertKey(4.7f);
        assertEquals(-1, tFloatDoubleHash.insertKey(4.7f));
    }

    @Test
    public void testInsertKey_nonExistingKey_shouldInsertKey() {
        final int index = tFloatDoubleHash.insertKey(5.8f);
        assertTrue(tFloatDoubleHash.contains(5.8f));
        assertEquals(index, tFloatDoubleHash.index(5.8f));
    }

    @Test
    public void testXinsertKey_noFreeSlots_shouldThrowException() {
        final int initialCapacity = tFloatDoubleHash._states.length;
        for (int i = 0; i < initialCapacity; i++) {
            tFloatDoubleHash.XinsertKey(i);
        }

        try {
            tFloatDoubleHash.XinsertKey(initialCapacity + 1);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }

    @Test
    public void testXinsertKey_existingKey_shouldReturnNegativeIndex() {
        tFloatDoubleHash.XinsertKey(3.2f);
        assertEquals(-1, tFloatDoubleHash.XinsertKey(3.2f));
    }

    @Test
    public void testXinsertKey_nonExistingKey_shouldInsertKey() {
        final int index = tFloatDoubleHash.XinsertKey(2.4f);
        assertTrue(tFloatDoubleHash.contains(2.4f));
        assertEquals(index, tFloatDoubleHash.index(2.4f));
    }

    // Test serialization/deserialization

    @Test
    public void testWriteExternal_ValidInput_ShouldWriteToOutputStream() {
        TFloatDoubleHash clonedObject = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            tFloatDoubleHash.writeExternal(oos);
            oos.flush();
            
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedObject = new TFloatDoubleHash();
            clonedObject.readExternal(ois);
        } catch (Exception e) {
            fail("Exception during serialization/deserialization: " + e.getMessage());
        }

        assertNotNull(clonedObject);
        assertEquals(tFloatDoubleHash.getNoEntryKey(), clonedObject.getNoEntryKey(), 0.001);
        assertEquals(tFloatDoubleHash.getNoEntryValue(), clonedObject.getNoEntryValue(), 0.001);
        assertEquals(tFloatDoubleHash.contains(1.0f), clonedObject.contains(1.0f));
    }

    @Test
    public void testReadExternal_ValidInput_ShouldReadFromInputStream() {
        TFloatDoubleHash clonedObject = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeByte(0);
            tFloatDoubleHash.writeExternal(oos);
            oos.flush();
            
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedObject = new TFloatDoubleHash();
            clonedObject.readExternal(ois);
        } catch (Exception e) {
            fail("Exception during serialization/deserialization: " + e.getMessage());
        }

        assertNotNull(clonedObject);
        assertEquals(tFloatDoubleHash.getNoEntryKey(), clonedObject.getNoEntryKey(), 0.001);
        assertEquals(tFloatDoubleHash.getNoEntryValue(), clonedObject.getNoEntryValue(), 0.001);
        assertEquals(tFloatDoubleHash.contains(1.0f), clonedObject.contains(1.0f));
    }

}