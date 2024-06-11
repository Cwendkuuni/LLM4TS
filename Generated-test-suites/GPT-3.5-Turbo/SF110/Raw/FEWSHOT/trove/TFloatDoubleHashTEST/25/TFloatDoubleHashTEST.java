package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setup() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }

    @Test
    public void testContains() {
        assertFalse(tFloatDoubleHash.contains(0.0f));
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                return true;
            }
        };

        assertTrue(tFloatDoubleHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tFloatDoubleHash._set = new float[2];
        tFloatDoubleHash._set[0] = 1.0f;
        tFloatDoubleHash._set[1] = 2.0f;
        tFloatDoubleHash._states = new byte[2];
        tFloatDoubleHash._states[0] = 1;
        tFloatDoubleHash._states[1] = 1;

        tFloatDoubleHash.removeAt(1);
        assertEquals(0.0f, tFloatDoubleHash._set[1], 0.00001);
        assertEquals(2, tFloatDoubleHash._states[1]);
    }

    @Test
    public void testIndex() {
        tFloatDoubleHash._set = new float[2];
        tFloatDoubleHash._set[0] = 1.0f;
        tFloatDoubleHash._set[1] = 2.0f;
        tFloatDoubleHash._states = new byte[2];
        tFloatDoubleHash._states[0] = 1;
        tFloatDoubleHash._states[1] = 1;

        assertEquals(1, tFloatDoubleHash.index(2.0f));
    }

    @Test
    public void testIndexRehashed() {
        tFloatDoubleHash._set = new float[3];
        tFloatDoubleHash._set[0] = 1.0f;
        tFloatDoubleHash._set[1] = 2.0f;
        tFloatDoubleHash._set[2] = 3.0f;
        tFloatDoubleHash._states = new byte[3];
        tFloatDoubleHash._states[0] = 1;
        tFloatDoubleHash._states[1] = 1;
        tFloatDoubleHash._states[2] = 1;

        assertEquals(-1, tFloatDoubleHash.indexRehashed(4.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        tFloatDoubleHash._states = new byte[2];
        tFloatDoubleHash._states[0] = 1;
        tFloatDoubleHash._states[1] = 1;

        tFloatDoubleHash.insertKey(3.0f);
        assertEquals(0.0f, tFloatDoubleHash._set[2], 0.00001);
        assertEquals(1, tFloatDoubleHash._states[2]);
    }

    @Test
    public void testInsertKeyRehash() {
        tFloatDoubleHash._set = new float[3];
        tFloatDoubleHash._set[0] = 1.0f;
        tFloatDoubleHash._set[1] = 2.0f;
        tFloatDoubleHash._set[2] = 3.0f;
        tFloatDoubleHash._states = new byte[3];
        tFloatDoubleHash._states[0] = 1;
        tFloatDoubleHash._states[1] = 1;
        tFloatDoubleHash._states[2] = 1;

        assertEquals(2, tFloatDoubleHash.insertKeyRehash(4.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKeyAt() {
        tFloatDoubleHash._set = new float[2];
        tFloatDoubleHash._states = new byte[2];

        tFloatDoubleHash.insertKeyAt(0, 5.0f);
        assertEquals(5.0f, tFloatDoubleHash._set[0], 0.00001);
        assertEquals(1, tFloatDoubleHash._states[0]);
    }
    
    @Test
    public void testXinsertKey() {
        tFloatDoubleHash._states = new byte[2];
        tFloatDoubleHash._states[0] = 1;
        tFloatDoubleHash._states[1] = 1;
        tFloatDoubleHash._set = new float[2];
        tFloatDoubleHash._set[0] = 1.0f;
        tFloatDoubleHash._set[1] = 2.0f;

        assertEquals(-1, tFloatDoubleHash.XinsertKey(1.0f));
    }

    @Test
    public void testWriteExternal() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);

        tFloatDoubleHash.writeExternal(objectOutput);
        objectOutput.flush();

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        int expectedByte = 0;
        for (byte expected : byteArray) {
            assertEquals(expectedByte, expected);
            expectedByte++;
        }
    }

    @Test
    public void testReadExternal() throws Exception {
        byte[] byteArray = new byte[]{0, -84, -19, 0, 5, 115, 114, 0, 25, 103, 110, 117, 46,
                116, 114, 111, 118, 101, 46, 105, 109, 112, 108, 46, 104, 97, 115, 104, 46, 84, 70, 108,
                111, 97, 116, 68, 111, 117, 98, 108, 101, 72, 97, 115, 104, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, -1, -1, -1, -1, -1, -1, -93, 92, 0, 0, 0, 0, 0, 0, 110, 111, -94, -24, 63, 28, 0,
                0, 0, 0, 0, 0, 0, 0};

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);

        tFloatDoubleHash.readExternal(objectInput);

        assertEquals(0.0f, tFloatDoubleHash.no_entry_key, 0.00001);
        assertEquals(0.0, tFloatDoubleHash.no_entry_value, 0.00001);
    }
}