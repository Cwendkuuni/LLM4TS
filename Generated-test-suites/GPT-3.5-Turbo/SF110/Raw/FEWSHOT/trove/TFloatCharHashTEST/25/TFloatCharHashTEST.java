package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    private TFloatCharHash tFloatCharHash;

    @Before
    public void setUp() {
        tFloatCharHash = new TFloatCharHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatCharHash.getNoEntryKey(), 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', tFloatCharHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        assertFalse(tFloatCharHash.contains(1.0f));

        tFloatCharHash.insertKey(1.0f);

        assertTrue(tFloatCharHash.contains(1.0f));
        assertFalse(tFloatCharHash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;

            @Override
            public boolean execute(float val) {
                sum += val;
                return true;
            }

            public float getSum() {
                return sum;
            }
        };

        assertTrue(tFloatCharHash.forEach(procedure));
        assertEquals(0.0f, procedure.getSum(), 0.0f);

        tFloatCharHash.insertKey(1.0f);
        tFloatCharHash.insertKey(2.0f);
        tFloatCharHash.insertKey(3.0f);

        assertTrue(tFloatCharHash.forEach(procedure));
        assertEquals(6.0f, procedure.getSum(), 0.0f);
    }

    @Test
    public void testRemoveAt() {
        tFloatCharHash.insertKey(1.0f);
        tFloatCharHash.insertKey(2.0f);
        tFloatCharHash.insertKey(3.0f);

        assertEquals(3, tFloatCharHash.size());

        tFloatCharHash.removeAt(1);

        assertEquals(2, tFloatCharHash.size());
        assertEquals(0.0f, tFloatCharHash._set[1], 0.0f);
        assertEquals('\0', tFloatCharHash._states[1]);
    }

    @Test
    public void testInsertKey() {
        int index = tFloatCharHash.insertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1, tFloatCharHash.size());
        
        index = tFloatCharHash.insertKey(2.0f);
        assertEquals(1, index);
        assertEquals(2, tFloatCharHash.size());
        
        index = tFloatCharHash.insertKey(3.0f);
        assertEquals(2, index);
        assertEquals(3, tFloatCharHash.size());
        
        index = tFloatCharHash.insertKey(2.0f);
        assertEquals(-2, index);
        assertEquals(3, tFloatCharHash.size());
    }

    @Test
    public void testInsertKeyAt() {
        tFloatCharHash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, tFloatCharHash._set[0], 0.0f);
        assertEquals(1, tFloatCharHash._states[0]);
    }

    @Test
    public void testIndex() {
        assertEquals(-1, tFloatCharHash.index(1.0f));

        tFloatCharHash.insertKey(1.0f);

        assertEquals(0, tFloatCharHash.index(1.0f));
        assertEquals(-1, tFloatCharHash.index(2.0f));
        
    }

    @Test
    public void testIndexRehashed() {
        tFloatCharHash.insertKey(1.0f);
        tFloatCharHash.insertKey(2.0f);
        tFloatCharHash.insertKey(3.0f);

        int index = tFloatCharHash.indexRehashed(2.0f, 0, 0, (byte) 1);
        assertEquals(1, index);
        
        index = tFloatCharHash.indexRehashed(2.0f, 1, 1, (byte) 1);
        assertEquals(-2, index);
    }

    @Test
    public void testSetUp() {
        int capacity = tFloatCharHash.setUp(10);
        assertEquals(10, capacity);

        assertEquals(10, tFloatCharHash._set.length);
        assertEquals(10, tFloatCharHash._states.length);
        assertEquals(0.0f, tFloatCharHash.no_entry_key, 0.0f);
        assertEquals('\0', tFloatCharHash.no_entry_value);
    }

    @Test
    public void testXinsertKey() {
        int index = tFloatCharHash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1, tFloatCharHash.size());
        
        index = tFloatCharHash.XinsertKey(2.0f);
        assertEquals(1, index);
        assertEquals(2, tFloatCharHash.size());
        
        index = tFloatCharHash.XinsertKey(3.0f);
        assertEquals(2, index);
        assertEquals(3, tFloatCharHash.size());
        
        index = tFloatCharHash.XinsertKey(2.0f);
        assertEquals(-2, index);
        assertEquals(3, tFloatCharHash.size());
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        tFloatCharHash.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        String expectedOutput = "aced05sr0%5Cgnu%2Ftrove%2Fimpl%2Fhash%2FTFloatCharHash%582X2%E5%A9E%3DC5%2A%A6%2A%E5%02%00%02%46%00%09no_entry_key%46%00%0Bno_entry_value%4C%00%0FconsumeFreeSlott%00%0A_keyInfo%46%4A%54%67%90%25%4D%49%03%00%00xpw%07%00%00%00%01%01%00%05_sizeI%00%0CmaxSizeI%00%0A_setlengthI%00%0A_stateljava%2Flang%2FByte%3B%4C%00%0EconsumeFreeSlotZ%00%0B_no_entry_keyF%00%0D_no_entry_valueC%00%05_tlocI%00%12lastPositionU%00%00%00%00%00%00%00%00%00%FF%FF%FF%FF%FF%FF%FF%FFx";
        assertEquals(expectedOutput, byteArrayOutputStream.toString());
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        String input = "aced05sr0%5Cgnu%2Ftrove%2Fimpl%2Fhash%2FTFloatCharHash%582X2%E5%A9E%3DC5%2A%A6%2A%E5%02%00%02%46%00%09no_entry_key%46%00%0Bno_entry_value%4C%00%0FconsumeFreeSlott%00%0A_keyInfo%46%4A%54%67%90%25%4D%49%03%00%00xpw%07%00%00%00%01%01%00%05_sizeI%00%0CmaxSizeI%00%0A_setlengthI%00%0A_stateljava%2Flang%2FByte%3B%4C%00%0EconsumeFreeSlotZ%00%0B_no_entry_keyF%00%0D_no_entry_valueC%00%05_tlocI%00%12lastPositionU%00%00%00%00%00%00%00%00%00%FF%FF%FF%FF%FF%FF%FF%FFx";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        tFloatCharHash.readExternal(objectInputStream);

        assertEquals(0.0f, tFloatCharHash.no_entry_key, 0.0f);
        assertEquals('\0', tFloatCharHash.no_entry_value);
        assertEquals(0, tFloatCharHash.size());
    }
}