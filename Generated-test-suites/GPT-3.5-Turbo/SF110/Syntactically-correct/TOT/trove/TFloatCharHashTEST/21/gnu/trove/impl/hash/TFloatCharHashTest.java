import org.junit.*;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertNotNull(hash._set);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains_PositiveCase() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.5f);
        assertTrue(hash.contains(2.5f));
    }

    @Test
    public void testContains_NegativeCase() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach_EmptyHash() {
        TFloatCharHash hash = new TFloatCharHash();
        assertTrue(hash.forEach((float value) -> {
            fail("This should not be executed");
            return true;
        }));
    }

    @Test
    public void testForEach_NonEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertTrue(hash.forEach((float value) -> {
            assertTrue(value == 2.0f || value == 3.0f);
            return true;
        }));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);

        assertEquals('\0', hash._states[0]);
        assertEquals('\0', hash._states[1]);
        assertEquals(4.0f, hash._set[0], 0.0001);
        assertEquals(3.0f, hash._set[1], 0.0001);

        hash.removeAt(0);

        assertEquals(2.0f, hash._set[0], 0.0001);
        assertEquals('\2', hash._states[0]);
        assertEquals('\0', hash._states[1]);
    }

    @Test
    public void testIndex_ExistingKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(0, hash.index(2.0f));
        assertEquals(1, hash.index(3.0f));
    }

    @Test
    public void testIndex_NonExistingKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testIndexRehashed_ExistingKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._states[0] = 1;
        hash._states[1] = 1;
        hash._set[0] = 2.0f;
        hash._set[1] = 3.0f;

        assertEquals(0, hash.indexRehashed(2.0f, 0, 2, (byte) 1));
        assertEquals(1, hash.indexRehashed(3.0f, 1, 2, (byte) 1));
    }

    @Test
    public void testIndexRehashed_NonExistingKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._states[0] = 1;
        hash._states[1] = 1;
        hash._set[0] = 2.0f;
        hash._set[1] = 3.0f;

        assertEquals(-1, hash.indexRehashed(4.0f, 0, 2, (byte) 1));
    }

    @Test
    public void testInsertKey_NewKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(2.0f);

        assertEquals(0, index);
        assertEquals(2.0f, hash._set[0], 0.0001);
        assertEquals('\1', hash._states[0]);
    }

    @Test
    public void testInsertKey_ExistingKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index = hash.insertKey(2.0f);

        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash_NewKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKeyRehash(2.0f, 0, 2, (byte) 1);

        assertEquals(0, index);
        assertEquals(2.0f, hash._set[0], 0.0001);
        assertEquals('\1', hash._states[0]);
    }

    @Test
    public void testInsertKeyRehash_ExistingKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._states[0] = 1;
        hash._states[1] = 1;
        hash._set[0] = 2.0f;
        hash._set[1] = 3.0f;

        int index = hash.insertKeyRehash(2.0f, 0, 2, (byte) 1);

        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash_NoFreeSlot() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._states[0] = 1;
        hash._states[1] = 1;
        hash._set[0] = 2.0f;
        hash._set[1] = 3.0f;

        try {
            hash.insertKeyRehash(4.0f, 0, 2, (byte) 1);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 2.0f);

        assertEquals(2.0f, hash._set[0], 0.0001);
        assertEquals('\1', hash._states[0]);
    }

    @Test
    public void testXInsertKey_NewKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.XinsertKey(2.0f);

        assertEquals(0, index);
        assertEquals(2.0f, hash._set[0], 0.0001);
        assertEquals('\1', hash._states[0]);
    }

    @Test
    public void testXInsertKey_ExistingKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.XinsertKey(2.0f);
        hash.XinsertKey(3.0f);

        int index = hash.XinsertKey(2.0f);

        assertEquals(-1, index);
    }

    @Test
    public void testWriteExternal() throws Exception {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{2.0f, 3.0f};
        hash._states = new byte[]{1, 1};
        hash.no_entry_key = 5.0f;
        hash.no_entry_value = 'A';

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        hash.writeExternal(out);
        out.flush();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInput in = new ObjectInputStream(bis);
        hash.readExternal(in);

        assertEquals(5.0f, hash.no_entry_key, 0.0001);
        assertEquals('A', hash.no_entry_value);
    }
}