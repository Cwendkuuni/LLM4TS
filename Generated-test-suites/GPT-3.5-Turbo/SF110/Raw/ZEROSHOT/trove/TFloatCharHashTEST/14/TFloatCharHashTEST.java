package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.00001);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(4.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        final StringBuilder sb = new StringBuilder();
        hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                sb.append(value);
                return true;
            }
        });
        assertEquals("123.0", sb.toString());
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        hash._states = new byte[]{1, 1, 1};
        hash.removeAt(1);
        assertArrayEquals(new float[]{1.0f, 0.0f, 3.0f}, hash._set, 0.00001);
        assertArrayEquals(new byte[]{1, 0, 1}, hash._states);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        hash._states = new byte[]{1, 1, 1};
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        hash._states = new byte[]{1, 1, 1};
        assertEquals(1, hash.indexRehashed(2.0f, 1, 12345, (byte) 1));
        assertEquals(-1, hash.indexRehashed(4.0f, 1, 12345, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        hash._states = new byte[]{1, 1, 1};
        assertEquals(-1, hash.insertKey(2.0f));
        assertEquals(-4, hash.insertKey(4.0f));
        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, hash._set, 0.00001);
        assertArrayEquals(new byte[]{1, 1, 1, 1}, hash._states);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        hash._states = new byte[]{1, 1, 1};
        assertEquals(-1, hash.insertKeyRehash(2.0f, 1, 12345, (byte) 1));
        assertEquals(-4, hash.insertKeyRehash(4.0f, 1, 12345, (byte) 1));
        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, hash._set, 0.00001);
        assertArrayEquals(new byte[]{1, 1, 1, 1}, hash._states);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[3];
        hash._states = new byte[3];
        hash.insertKeyAt(1, 2.0f);
        assertArrayEquals(new float[]{0.0f, 2.0f, 0.0f}, hash._set, 0.00001);
        assertArrayEquals(new byte[]{0, 1, 0}, hash._states);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        hash._states = new byte[]{1, 1, 1};
        assertEquals(-1, hash.XinsertKey(2.0f));
        assertEquals(-4, hash.XinsertKey(4.0f));
        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, hash._set, 0.00001);
        assertArrayEquals(new byte[]{1, 1, 1, 1}, hash._states);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.no_entry_key = 1.0f;
        hash.no_entry_value = '\0';
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        hash.writeExternal(new ObjectOutputStream(out));
        byte[] expected = {0, 0, 0, 0, 63, -128, 0, 0, 0, 0};
        assertArrayEquals(expected, out.toByteArray());
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{0, 0, 0, 0, 63, -128, 0, 0, 0, 0});
        hash.readExternal(new ObjectInputStream(in));
        assertEquals(1.0f, hash.no_entry_key, 0.00001);
        assertEquals('\0', hash.no_entry_value);
    }
}