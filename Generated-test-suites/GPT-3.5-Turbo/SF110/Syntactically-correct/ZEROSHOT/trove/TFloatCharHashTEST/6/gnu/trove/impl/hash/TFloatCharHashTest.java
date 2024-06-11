import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001f);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;

            public boolean execute(float value) {
                sum += value;
                return true;
            }

            public float getSum() {
                return sum;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(6.0f, procedure.getSum(), 0.0001f);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(1);
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(2.0f));
    }

    @Test
    public void testInsertKey() {
        int index1 = hash.insertKey(1.0f);
        assertEquals(0, index1);
        assertEquals(1.0f, hash._set[index1], 0.0001f);

        int index2 = hash.insertKey(2.0f);
        assertEquals(1, index2);
        assertEquals(2.0f, hash._set[index2], 0.0001f);

        int index3 = hash.insertKey(1.0f);
        assertEquals(-1, index3);
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.0001f);

        hash.insertKeyAt(1, 2.0f);
        assertEquals(2.0f, hash._set[1], 0.0001f);
    }

    @Test
    public void testXinsertKey() {
        int index1 = hash.XinsertKey(1.0f);
        assertEquals(0, index1);
        assertEquals(1.0f, hash._set[index1], 0.0001f);

        int index2 = hash.XinsertKey(2.0f);
        assertEquals(1, index2);
        assertEquals(2.0f, hash._set[index2], 0.0001f);

        int index3 = hash.XinsertKey(1.0f);
        assertEquals(-1, index3);
    }

    @Test
    public void testWriteExternal() throws IOException {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(output);
        hash.writeExternal(out);
        out.flush();
        out.close();

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ObjectInput in = new ObjectInputStream(input);
        TFloatCharHash deserializedHash = new TFloatCharHash();
        deserializedHash.readExternal(in);

        assertEquals(1.0f, deserializedHash._set[0], 0.0001f);
        assertEquals(2.0f, deserializedHash._set[1], 0.0001f);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(output);
        hash.writeExternal(out);
        out.flush();
        out.close();

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ObjectInput in = new ObjectInputStream(input);
        hash.readExternal(in);

        assertEquals(1.0f, hash._set[0], 0.0001f);
        assertEquals(2.0f, hash._set[1], 0.0001f);
    }

}