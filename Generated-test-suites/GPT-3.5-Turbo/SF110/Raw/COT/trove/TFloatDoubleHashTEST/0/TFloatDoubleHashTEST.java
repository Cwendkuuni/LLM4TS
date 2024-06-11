import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHashStub();
    }

    // Test contains method
    @Test
    public void testContains() {
        assertFalse(tFloatDoubleHash.contains(1.0f));
        tFloatDoubleHash.insertKey(1.0f);
        assertTrue(tFloatDoubleHash.contains(1.0f));
    }

    // Test forEach method
    @Test
    public void testForEach() {
        double sum = 0.0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };

        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);
        tFloatDoubleHash.forEach(procedure);

        assertEquals(3.0, sum, 0.001);
    }

    // Test removing an element
    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.insertKey(1.0f);
        assertTrue(tFloatDoubleHash.contains(1.0f));
        tFloatDoubleHash.removeAt(0);
        assertFalse(tFloatDoubleHash.contains(1.0f));
    }

    // Test inserting and getting an element
    @Test
    public void testInsertAndGet() {
        tFloatDoubleHash.insertKey(1.0f);
        assertEquals(0, tFloatDoubleHash.index(1.0f));
        assertEquals(1.0, tFloatDoubleHash.get(0), 0.001);
    }

    // Test indexRehashed method
    @Test
    public void testIndexRehashed() {
        tFloatDoubleHash.insertKey(1.0f);
        assertEquals(0, tFloatDoubleHash.indexRehashed(1.0f, 0, 0, (byte)1));
    }

    // Test insertKey method
    @Test
    public void testInsertKey() {
        assertEquals(0, tFloatDoubleHash.insertKey(1.0f));
        assertEquals(-1, tFloatDoubleHash.insertKey(1.0f));
    }

    // Test XinsertKey method
    @Test
    public void testXinsertKey() {
        assertEquals(0, tFloatDoubleHash.XinsertKey(1.0f));
        assertEquals(-1, tFloatDoubleHash.XinsertKey(1.0f));
    }

    // Test writeExternal and readExternal methods
    @Test
    public void testExternalMethods() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        tFloatDoubleHash.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        tFloatDoubleHash.readExternal(objectInputStream);

        assertEquals(0.0, tFloatDoubleHash.getNoEntryKey(), 0.001);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.001);
    }

    // Custom test case for edge case of initialCapacity
    @Test
    public void testInitialCapacityEdgeCase() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHashStub(0);
        tFloatDoubleHash.insertKey(1.0f);
        assertEquals(1.0, tFloatDoubleHash.get(0), 0.001);
        assertEquals(0, tFloatDoubleHash.index(1.0f));
    }

    // Custom test case for edge case of loadFactor
    @Test
    public void testLoadFactorEdgeCase() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHashStub(5, 0.5f);
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);
        tFloatDoubleHash.insertKey(3.0f);
        tFloatDoubleHash.insertKey(4.0f);
        tFloatDoubleHash.insertKey(5.0f);
        tFloatDoubleHash.insertKey(6.0f); // Load factor will be exceeded after this insert
        tFloatDoubleHash.insertKey(7.0f);
        tFloatDoubleHash.insertKey(8.0f);
        assertEquals(8.0, tFloatDoubleHash.get(0), 0.001);
        assertEquals(0, tFloatDoubleHash.index(1.0f));
    }

    // Custom test case for edge case of no_entry_key
    @Test
    public void testNoEntryKeyEdgeCase() {
        TFloatDoubleHashStub tFloatDoubleHash = new TFloatDoubleHashStub();
        assertEquals(0.0, tFloatDoubleHash.getNoEntryKey(), 0.001);
        tFloatDoubleHash.setNoEntryKey(10.0f);
        tFloatDoubleHash.insertKey(10.0f);
        assertEquals(10.0, tFloatDoubleHash.get(0), 0.001);
        assertEquals(0, tFloatDoubleHash.index(0.0f));
    }

    // Custom test case for edge case of no_entry_value
    @Test
    public void testNoEntryValueEdgeCase() {
        TFloatDoubleHashStub tFloatDoubleHash = new TFloatDoubleHashStub();
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.001);
        tFloatDoubleHash.setNoEntryValue(10.0);
        tFloatDoubleHash.insertKey(1.0f);
        assertEquals(10.0, tFloatDoubleHash.get(0), 0.001);
        assertEquals(0, tFloatDoubleHash.index(0.0f));
    }

    // Custom test case for exception handling when inserting more values than initialCapacity
    @Test(expected = IllegalStateException.class)
    public void testExceptionHandlingInsertKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHashStub(2);
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);
        tFloatDoubleHash.insertKey(3.0f); // Exception expected here
    }
}