import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash testInstance;
    private static final float NO_ENTRY_KEY = 0.0f;
    private static final double NO_ENTRY_VALUE = 0.0;

    @Before
    public void setUp() {
        testInstance = new TFloatDoubleHash();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(NO_ENTRY_KEY, testInstance.getNoEntryKey(), 0.0001);
        assertEquals(NO_ENTRY_VALUE, testInstance.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        testInstance = new TFloatDoubleHash(5);
        assertEquals(NO_ENTRY_KEY, testInstance.getNoEntryKey(), 0.0001);
        assertEquals(NO_ENTRY_VALUE, testInstance.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        testInstance = new TFloatDoubleHash(5, 0.5f);
        assertEquals(NO_ENTRY_KEY, testInstance.getNoEntryKey(), 0.0001);
        assertEquals(NO_ENTRY_VALUE, testInstance.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyAndNoEntryValue() {
        testInstance = new TFloatDoubleHash(5, 0.5f, 1.0f, 2.0);
        assertEquals(1.0f, testInstance.getNoEntryKey(), 0.0001);
        assertEquals(2.0, testInstance.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 5;
        int capacity = testInstance.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(new float[capacity].length, testInstance._set.length);
    }

    @Test
    public void testContains() {
        testInstance.setUp(5);
        testInstance.insertKey(1.0f);
        assertTrue(testInstance.contains(1.0f));
        assertFalse(testInstance.contains(2.0f));
    }

    @Test
    public void testForEach() {
        testInstance.setUp(5);
        testInstance.insertKey(1.0f);
        testInstance.insertKey(2.0f);
        testInstance.insertKey(3.0f);

        TFloatProcedure procedure = (float val) -> {
            System.out.println(val);
            return true;
        };

        assertTrue(testInstance.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        testInstance.setUp(5);
        testInstance.insertKey(1.0f);
        testInstance.insertKey(2.0f);
        testInstance.removeAt(0);

        assertFalse(testInstance.contains(1.0f));
        assertTrue(testInstance.contains(2.0f));
    }

    @Test
    public void testIndex() {
        testInstance.setUp(5);
        testInstance.insertKey(1.0f);
        testInstance.insertKey(2.0f);

        int index = testInstance.index(2.0f);
        assertEquals(1, index);

        index = testInstance.indexRehashed(1.0f, 0, 0, (byte) 1);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKey() {
        testInstance.setUp(5);
        int index = testInstance.insertKey(1.0f);
        assertEquals(0, index);

        index = testInstance.insertKey(1.0f);
        assertTrue(index < 0);
    }

    @Test
    public void testInsertKeyRehash() {
        testInstance.setUp(5);
        testInstance.insertKey(1.0f);
        testInstance.insertKey(2.0f);
        testInstance.insertKey(3.0f);
        testInstance.insertKey(4.0f);
        testInstance.insertKey(5.0f);
        testInstance.insertKey(6.0f);

        assertTrue(testInstance.contains(6.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFullKeySet() {
        testInstance.setUp(1);
        testInstance.insertKey(1.0f);
        testInstance.insertKey(1.0f);
    }

    @Test
    public void testXinsertKey() {
        testInstance.setUp(5);
        int index = testInstance.XinsertKey(1.0f);
        assertEquals(0, index);

        index = testInstance.XinsertKey(1.0f);
        assertTrue(index < 0);
    }

    @Test
    public void testWriteExternal() {
        // Not possible to test as the method requires ObjectOutput
        // and we cannot verify contents in this case
    }

    @Test
    public void testReadExternal() {
        // Not possible to test as the method requires ObjectInput
        // and we cannot verify contents in this case
    }
}