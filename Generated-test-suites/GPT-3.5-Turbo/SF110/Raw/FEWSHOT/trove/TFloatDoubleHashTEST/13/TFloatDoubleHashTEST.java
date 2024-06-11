package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    private TFloatDoubleHash testObject;

    @Before
    public void setUp() {
        testObject = new TFloatDoubleHash();
    }

    @Test
    public void testConstructor() {
        testObject = new TFloatDoubleHash();
        assertNotNull(testObject);
        assertEquals(0.0f, testObject.getNoEntryKey(), 0.0);
        assertEquals(0.0, testObject.getNoEntryValue(), 0.0);

        testObject = new TFloatDoubleHash(10);
        assertNotNull(testObject);
        assertEquals(0.0f, testObject.getNoEntryKey(), 0.0);
        assertEquals(0.0, testObject.getNoEntryValue(), 0.0);

        testObject = new TFloatDoubleHash(10, 0.5f);
        assertNotNull(testObject);
        assertEquals(0.0f, testObject.getNoEntryKey(), 0.0);
        assertEquals(0.0, testObject.getNoEntryValue(), 0.0);

        testObject = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertNotNull(testObject);
        assertEquals(1.0f, testObject.getNoEntryKey(), 0.0);
        assertEquals(2.0, testObject.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        int capacity = testObject.setUp(10);
        assertEquals(10, capacity);
        float[] set = testObject._set;
        assertNotNull(set);
        assertEquals(10, set.length);
    }

    @Test
    public void testContains() {
        testObject._set = new float[]{1.0f, 2.0f, 3.0f};
        assertTrue(testObject.contains(2.0f));
        assertFalse(testObject.contains(4.0f));
    }

    @Test
    public void testForEach() {
        testObject._set = new float[]{1.0f, 2.0f, 3.0f};
        TFloatProcedure procedure = new TFloatProcedure() {
            int count = 0;

            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };
        assertTrue(testObject.forEach(procedure));
        assertEquals(3, procedure.count);
    }

    @Test
    public void testRemoveAt() {
        testObject._set = new float[]{1.0f, 2.0f, 3.0f};
        testObject._states = new byte[]{1, 1, 2};
        testObject.removeAt(1);
        assertEquals(0.0f, testObject._set[1], 0.0);
        assertEquals(0, testObject._states[1]);
    }

    @Test
    public void testIndex() {
        testObject._set = new float[]{1.0f, 2.0f, 3.0f};
        testObject._states = new byte[]{1, 1, 2};
        assertEquals(1, testObject.index(2.0f));
        assertEquals(-1, testObject.index(4.0f));
    }

    @Test
    public void testIndexRehashed() {
        testObject._set = new float[]{1.0f, 2.0f, 3.0f};
        testObject._states = new byte[]{1, 1, 2};
        int index = testObject.indexRehashed(2.0f, 1, 2, (byte) 1);
        assertEquals(1, index);
        index = testObject.indexRehashed(4.0f, 1, 2, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        testObject._states = new byte[]{1, 0, 2};
        testObject._set = new float[]{1.0f, 0.0f, 3.0f};
        int index = testObject.insertKey(2.0f);
        assertEquals(-2, index);
        assertEquals(2.0f, testObject._set[1], 0.0);
        assertEquals(1, testObject._states[1]);
        index = testObject.insertKey(4.0f);
        assertEquals(1, index);
        assertEquals(4.0f, testObject._set[1], 0.0);
        assertEquals(1, testObject._states[1]);
    }

    @Test
    public void testInsertKeyRehash() {
        testObject._states = new byte[]{1, 0, 2};
        testObject._set = new float[]{1.0f, 0.0f, 3.0f};
        int index = testObject.insertKeyRehash(2.0f, 1, 2, (byte) 1);
        assertEquals(1, index);
        assertEquals(2.0f, testObject._set[1], 0.0);
        assertEquals(1, testObject._states[1]);
        index = testObject.insertKeyRehash(4.0f, 1, 2, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyAt() {
        testObject._set = new float[]{1.0f, 0.0f, 3.0f};
        testObject._states = new byte[]{1, 0, 2};
        testObject.insertKeyAt(1, 2.0f);
        assertEquals(2.0f, testObject._set[1], 0.0);
        assertEquals(1, testObject._states[1]);
    }

    @Test
    public void testXinsertKey() {
        testObject._set = new float[]{1.0f, 0.0f, 3.0f};
        testObject._states = new byte[]{1, 0, 2};
        int index = testObject.XinsertKey(2.0f);
        assertEquals(-2, index);
        assertEquals(2.0f, testObject._set[1], 0.0);
        assertEquals(1, testObject._states[1]);
        index = testObject.XinsertKey(4.0f);
        assertEquals(1, index);
        assertEquals(4.0f, testObject._set[1], 0.0);
        assertEquals(1, testObject._states[1]);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ObjectOutput out = null;
        try {
            out.writeByte(0);
            testObject.writeExternal(out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ObjectInput in = null;
        try {
            in.readByte();
            testObject.readExternal(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

}