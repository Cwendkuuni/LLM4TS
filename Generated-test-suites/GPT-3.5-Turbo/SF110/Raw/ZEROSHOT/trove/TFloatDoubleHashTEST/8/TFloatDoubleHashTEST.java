import org.junit.*;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    private TFloatDoubleHash testHash;

    @Before
    public void setUp() {
        testHash = new TFloatDoubleHash();
    }

    @Test
    public void testGetNoEntryKey() {
        testHash.no_entry_key = 1.0f;
        assertEquals(1.0f, testHash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        testHash.no_entry_value = 2.0;
        assertEquals(2.0, testHash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testContains() {
        testHash._set = new float[] {1.0f, 2.0f};
        assertTrue(testHash.contains(1.0f));
        assertFalse(testHash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        testHash._states = new byte[] {1, 1};
        testHash._set = new float[] {1.0f, 2.0f};
        
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                return true;
            }
        };

        assertTrue(testHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        testHash._set = new float[] {1.0f, 2.0f};
        testHash._states = new byte[] {1, 1};
        
        testHash.removeAt(1);
        assertEquals(0.0f, testHash._set[1], 0.0001);
        assertEquals(0, testHash._states[1]);
    }

    @Test
    public void testIndex() {
        testHash._states = new byte[] {1, 0, 1};
        testHash._set = new float[] {1.0f, 0.0f, 2.0f};
        
        assertEquals(0, testHash.index(1.0f));
        assertEquals(-1, testHash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        testHash._states = new byte[] {1, 0};
        testHash._set = new float[] {1.0f, 0.0f};
        testHash.consumeFreeSlot = false;
        
        assertEquals(1, testHash.insertKey(2.0f));
        assertEquals(2.0f, testHash._set[1], 0.0001);
        assertEquals(1, testHash._states[1]);

        testHash.consumeFreeSlot = true;
        assertEquals(0, testHash.insertKey(3.0f));
        assertEquals(3.0f, testHash._set[0], 0.0001);
        assertEquals(1, testHash._states[0]);
    }

    @Test
    public void testIndexRehashed() {
        testHash._states = new byte[] {1, 0, 1};
        testHash._set = new float[] {1.0f, 0.0f, 2.0f};
        
        assertEquals(0, testHash.indexRehashed(1.0f, 0, 1, (byte)1));
        assertEquals(-1, testHash.indexRehashed(3.0f, 0, 1, (byte)1));
    }

    @Test
    public void testInsertKeyRehash() {
        testHash._states = new byte[] {1, 0, 1};
        testHash._set = new float[] {1.0f, 0.0f, 2.0f};
        testHash.consumeFreeSlot = false;

        assertEquals(2, testHash.insertKeyRehash(3.0f, 0, 1, (byte)1));
        assertEquals(3.0f, testHash._set[2], 0.0001);
        assertEquals(1, testHash._states[2]);

        testHash.consumeFreeSlot = true;
        assertEquals(0, testHash.insertKeyRehash(4.0f, 2, 1, (byte)2));
        assertEquals(4.0f, testHash._set[0], 0.0001);
        assertEquals(1, testHash._states[0]);
    }

    @Test
    public void testInsertKeyAt() {
        testHash._set = new float[3];
        testHash._states = new byte[3];
        
        testHash.insertKeyAt(1, 2.0f);
        assertEquals(2.0f, testHash._set[1], 0.0001);
        assertEquals(1, testHash._states[1]);
    }

    @Test
    public void testXinsertKey() {
        testHash._states = new byte[] {1, 0};
        testHash._set = new float[] {1.0f, 0.0f};
        testHash.consumeFreeSlot = false;
        
        assertEquals(1, testHash.XinsertKey(2.0f));
        assertEquals(2.0f, testHash._set[1], 0.0001);
        assertEquals(1, testHash._states[1]);

        testHash.consumeFreeSlot = true;
        assertEquals(0, testHash.XinsertKey(3.0f));
        assertEquals(3.0f, testHash._set[0], 0.0001);
        assertEquals(1, testHash._states[0]);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutStream = new ObjectOutputStream(byteOutStream);

        testHash.writeExternal(objectOutStream);
        objectOutStream.flush();

        byte[] bytes = byteOutStream.toByteArray();
        assertEquals(8, bytes[0]);
        // Verify other bytes
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(new byte[] {8, 0, 0, 0, 0, 32, 0, 0});
        ObjectInputStream objectInStream = new ObjectInputStream(byteInStream);

        testHash.readExternal(objectInStream);
        assertEquals(1.0f, testHash.no_entry_key, 0.0001);
        assertEquals(2.0, testHash.no_entry_value, 0.0001);
    }
}