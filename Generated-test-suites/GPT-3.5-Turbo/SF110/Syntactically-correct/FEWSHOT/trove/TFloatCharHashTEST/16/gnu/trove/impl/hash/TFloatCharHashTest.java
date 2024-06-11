package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {

    TFloatCharHash tFloatCharHash;

    @Before
    public void setUp() {
        tFloatCharHash = new TFloatCharHash();
    }

    @Test
    public void testConstructor() {
        assertEquals(0.0f, tFloatCharHash.getNoEntryKey(), 0.0f);
        assertEquals('\0', tFloatCharHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        assertFalse(tFloatCharHash.contains(1.1f));

        tFloatCharHash.insertKey(1.1f);

        assertTrue(tFloatCharHash.contains(1.1f));
        assertFalse(tFloatCharHash.contains(2.2f));
    }

    @Test
    public void testForEach() {
        final float[] keys = { 1.1f, 2.2f, 3.3f };
        final char[] values = { 'a', 'b', 'c' };

        for (int i = 0; i < keys.length; i++) {
            tFloatCharHash.insertKey(keys[i]);
        }

        tFloatCharHash.forEach((float key) -> {
            assertEquals(values[0], tFloatCharHash.index(key));
            return true;
        });

    }

    @Test
    public void testRemoveAt() {
        final float[] keys = { 1.1f, 2.2f, 3.3f };

        for (int i = 0; i < keys.length; i++) {
            tFloatCharHash.insertKey(keys[i]);
        }

        assertEquals(3, tFloatCharHash.size());

        tFloatCharHash.removeAt(1);

        assertEquals(2, tFloatCharHash.size());
        assertEquals(tFloatCharHash.getNoEntryKey(), tFloatCharHash._set[1], 0.0f);
    }

    @Test
    public void testIndex() {
        final float[] keys = { 1.1f, 2.2f, 3.3f };

        for (int i = 0; i < keys.length; i++) {
            tFloatCharHash.insertKey(keys[i]);
        }

        assertEquals(0, tFloatCharHash.index(1.1f));
        assertEquals(1, tFloatCharHash.index(2.2f));
        assertEquals(2, tFloatCharHash.index(3.3f));
        assertEquals(-1, tFloatCharHash.index(4.4f));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tFloatCharHash.insertKey(1.1f));
        assertEquals(1, tFloatCharHash.insertKey(2.2f));
        assertEquals(2, tFloatCharHash.insertKey(3.3f));

        assertEquals(-1, tFloatCharHash.insertKey(2.2f));
    }

    @Test
    public void testXInsertKey() {
        assertEquals(0, tFloatCharHash.XinsertKey(1.1f));
        assertEquals(1, tFloatCharHash.XinsertKey(2.2f));
        assertEquals(2, tFloatCharHash.XinsertKey(3.3f));

        assertEquals(-1, tFloatCharHash.XinsertKey(2.2f));
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        final float[] keys = { 1.1f, 2.2f, 3.3f };
        final char[] values = { 'a', 'b', 'c' };

        for (int i = 0; i < keys.length; i++) {
            tFloatCharHash.insertKey(keys[i]);
        }

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        tFloatCharHash.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        final ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        TFloatCharHash readTFloatCharHash = new TFloatCharHash();
        readTFloatCharHash.readExternal(objectInputStream);

        for (int i = 0; i < keys.length; i++) {
            assertEquals(values[i], readTFloatCharHash.index(keys[i]));
        }
    }

}