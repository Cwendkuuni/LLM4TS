// Test methods for TFloatCharHash class

package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            int count = 0;

            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(2, procedure.count);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertEquals(hash.getNoEntryValue(), hash.get(0));

        hash.removeAt(0);

        assertEquals(hash.getNoEntryValue(), hash.get(0));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();

        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);

        hash = new TFloatCharHash(10, 0.5f, 1.0f, 'A');

        assertEquals(1.0f, hash.getNoEntryKey(), 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();

        assertEquals('\0', hash.getNoEntryValue());

        hash = new TFloatCharHash(10, 0.5f, 1.0f, 'A');

        assertEquals('A', hash.getNoEntryValue());
    }

    @Test
    public void testUp() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.up(100);

        assertEquals(100, hash.capacity());
    }

    @Test
    public void testClear() {
        TFloatCharHash hash = new TFloatCharHash();

        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        hash.clear();

        assertEquals(0, hash.size());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 1.0f, 'A');
        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteArrayOutput);

        hash.writeExternal(objectOutput);

        ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(byteArrayOutput.toByteArray());
        ObjectInputStream objectInput = new ObjectInputStream(byteArrayInput);

        TFloatCharHash newHash = new TFloatCharHash();
        newHash.readExternal(objectInput);

        assertEquals(1.0f, newHash.getNoEntryKey(), 0.0f);
        assertEquals('A', newHash.getNoEntryValue());
    }
}