package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TFloatCharHashTest {
    private static final float DELTA = 0.000001;

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), DELTA);
        
        hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), DELTA);
        
        hash = new TFloatCharHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), DELTA);
        
        hash = new TFloatCharHash(10, 0.5f, -1.0f, 'a');
        assertEquals(-1.0f, hash.getNoEntryKey(), DELTA);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10, 0.5f);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10, 0.5f, -1.0f, 'a');
        assertEquals('a', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertFalse(hash.contains(1.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        
        hash.remove(1.0f);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.1f);
        hash.insertKey(2.2f);
        hash.insertKey(3.3f);

        final float[] sum = new float[1];
        sum[0] = 0.0f;
        hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                sum[0] += value;
                return true;
            }
        });

        assertEquals(1.1f + 2.2f + 3.3f, sum[0], DELTA);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        hash.removeAt(0);
        
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
        
        hash.removeAt(0);
        
        assertEquals(-1, hash.index(1.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(0, hash.index(1.0f));
        
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(1, hash.index(2.0f));
        
        assertEquals(-1, hash.insertKey(2.0f)); // duplicate key
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        hash.writeExternal(out);
        out.flush();
        
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bais);
        
        TFloatCharHash hash2 = new TFloatCharHash();
        hash2.readExternal(in);
        
        assertTrue(hash2.contains(1.0f));
        assertTrue(hash2.contains(2.0f));
        assertFalse(hash2.contains(3.0f));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], DELTA);
    }
}