
package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TFloatCharHashTest {

    private TFloatCharHash tFloatCharHash;

    @Before
    public void setUp() {
        tFloatCharHash = new TFloatCharHash();
    }

    @Test
    public void testNoEntryKey() {
        assertEquals(0.0f, tFloatCharHash.getNoEntryKey(), 0.01);
        tFloatCharHash = new TFloatCharHash(10, 0.5f, 5.0f, 'a');
        assertEquals(5.0f, tFloatCharHash.getNoEntryKey(), 0.01);
    }

    @Test
    public void testNoEntryValue() {
        assertEquals('\0', tFloatCharHash.getNoEntryValue());
        tFloatCharHash = new TFloatCharHash(10, 0.5f, 5.0f, 'a');
        assertEquals('a', tFloatCharHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        assertEquals(0, tFloatCharHash.setUp(10));
        assertEquals(10, tFloatCharHash._set.length);
    }

    @Test
    public void testContains() {
        assertFalse(tFloatCharHash.contains(1.0f));
        tFloatCharHash._set[0] = 1.0f;
        assertTrue(tFloatCharHash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        assertTrue(tFloatCharHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return true;
            }
        }));
        tFloatCharHash._set[0] = 1.0f;
        assertFalse(tFloatCharHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return false;
            }
        }));
    }

    @Test
    public void testRemoveAt() {
        tFloatCharHash._set[0] = 1.0f;
        tFloatCharHash.removeAt(0);
        assertEquals(0.0f, tFloatCharHash._set[0], 0.01);
    }

    @Test
    public void testIndex() {
        assertEquals(-1, tFloatCharHash.index(1.0f));
        tFloatCharHash._set[0] = 1.0f;
        assertEquals(0, tFloatCharHash.index(1.0f));
    }

    @Test
    public void testIndexRehashed() {
        assertEquals(-1, tFloatCharHash.indexRehashed(1.0f, 0, 0, (byte) 0));
        tFloatCharHash._set[0] = 1.0f;
        assertEquals(0, tFloatCharHash.indexRehashed(1.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tFloatCharHash.insertKey(1.0f));
        assertEquals(0.0f, tFloatCharHash._set[0], 0.01);
        assertEquals(1, tFloatCharHash.insertKey(1.0f));
        tFloatCharHash._set[1] = 2.0f;
        assertEquals(1, tFloatCharHash.insertKey(2.0f));
        tFloatCharHash._states[1] = 2;
        assertEquals(1, tFloatCharHash.insertKey(2.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        tFloatCharHash._states = new byte[]{1, 2, 1};
        tFloatCharHash.insertKeyRehash(1.0f, 1, 0, (byte) 1);
    }

    @Test
    public void testInsertKeyAt() {
        tFloatCharHash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, tFloatCharHash._set[0], 0.01);
        assertEquals(1, tFloatCharHash._states[0]);
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, tFloatCharHash.XinsertKey(1.0f));
        assertEquals(1.0f, tFloatCharHash._set[0], 0.01);
        assertEquals(1, tFloatCharHash._states[0]);
        assertEquals(-1, tFloatCharHash.XinsertKey(1.0f));
        tFloatCharHash._states[0] = 2;
        assertEquals(0, tFloatCharHash.XinsertKey(1.0f));
        tFloatCharHash._states = new byte[]{0, 0, 0};
        assertEquals(0, tFloatCharHash.XinsertKey(1.0f));
        assertEquals(1.0f, tFloatCharHash._set[0], 0.01);
        assertEquals(1, tFloatCharHash._states[0]);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        tFloatCharHash.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        assertEquals(0, bytes[0]);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayInpntStream byteArrayInpntStream = new ByteArrayInpntStream(new byte[]{0});
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInpntStream);
        tFloatCharHash.readExternal(objectInputStream);
        assertEquals(0.0f, tFloatCharHash.no_entry_key, 0.01);
        assertEquals('\0', tFloatCharHash.no_entry_value);
    }
}
