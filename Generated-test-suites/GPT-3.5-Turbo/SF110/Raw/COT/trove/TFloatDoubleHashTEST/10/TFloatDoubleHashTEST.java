import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(0.5f));

        hash.insertKey(0.5f);
        assertTrue(hash.contains(0.5f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        final float[] sum = {0.0f};
        hash.forEach(value -> {
            sum[0] += value;
            return true;
        });

        assertEquals(6.0f, sum[0], 0.01);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(0.5f);

        hash.removeAt(0);
        assertFalse(hash.contains(0.5f));
    }

    @Test
    public void testIndex() {
        hash.insertKey(0.5f);

        int index = hash.index(0.5f);
        assertEquals(0, index);

        index = hash.index(1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        int index = hash.insertKey(0.5f);
        assertEquals(0, index);
        assertTrue(hash.contains(0.5f));

        index = hash.insertKey(0.5f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 0.5f);
        assertTrue(hash.contains(0.5f));
    }

    @Test
    public void testXinsertKey() {
        int index = hash.XinsertKey(0.5f);
        assertEquals(0, index);
        assertTrue(hash.contains(0.5f));

        index = hash.XinsertKey(0.5f);
        assertEquals(-1, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        for (int i = 0; i < 1025; i++) {
            hash.insertKey(i);
        }
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.01);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, hash.getNoEntryValue(), 0.01);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        out.flush();
        byte[] bytes = baos.toByteArray();

        assertTrue(bytes.length > 0);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        out.flush();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bais);
        hash.readExternal(in);

        assertEquals(0.0f, hash.getNoEntryKey(), 0.01);
        assertEquals(0.0, hash.getNoEntryValue(), 0.01);
    }

}