package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import gnu.trove.procedure.TFloatProcedure;
import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {

    private TFloatCharHash hashTable;

    @Before
    public void setUp() {
        hashTable = new TFloatCharHash();
    }

    @Test
    public void testContains() {
        assertFalse(hashTable.contains(1.0f));
        hashTable.insertKey(1.0f);
        assertTrue(hashTable.contains(1.0f));
    }

    @Test
    public void testForEach() {
        hashTable.insertKey(1.0f);
        hashTable.insertKey(2.0f);
        hashTable.insertKey(3.0f);

        final float[] result = new float[3];
        hashTable.forEach(new TFloatProcedure() {
            private int index = 0;

            @Override
            public boolean execute(float value) {
                result[index++] = value;
                return true;
            }
        });

        assertEquals(1.0f, result[0], 0.0f);
        assertEquals(2.0f, result[1], 0.0f);
        assertEquals(3.0f, result[2], 0.0f);
    }

    @Test
    public void testRemoveAt() {
        hashTable.insertKey(1.0f);
        hashTable.insertKey(2.0f);
        hashTable.insertKey(3.0f);
        hashTable.removeAt(1);

        assertFalse(hashTable.contains(2.0f));
    }

    @Test
    public void testSetUp() {
        hashTable.setUp(10);
        assertEquals(10, hashTable._set.length);
    }

    @Test
    public void testIndex() {
        hashTable.insertKey(1.0f);
        hashTable.insertKey(2.0f);

        assertEquals(0, hashTable.index(1.0f));
        assertEquals(1, hashTable.index(2.0f));
        assertEquals(-1, hashTable.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        int index = hashTable.insertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hashTable.contains(1.0f));

        index = hashTable.insertKey(1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash() {
        hashTable.insertKey(1.0f);
        hashTable.insertKey(2.0f);
        hashTable.insertKey(3.0f);

        int index = hashTable.insertKeyRehash(1.0f, 0, 0, (byte) 1);
        assertEquals(0, index);
        assertTrue(hashTable.contains(1.0f));

        index = hashTable.insertKeyRehash(4.0f, 0, 0, (byte) 1);
        assertEquals(3, index);
        assertTrue(hashTable.contains(4.0f));
    }

    @Test
    public void testXinsertKey() {
        int index = hashTable.XinsertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hashTable.contains(1.0f));

        index = hashTable.XinsertKey(1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testWriteExternal() {
        // Not testable as external write requires ObjectOutput
    }

    @Test
    public void testReadExternal() {
        // Not testable as external read requires ObjectInput
    }
}