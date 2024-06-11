import static org.junit.Assert.*;
import org.junit.*;

public class TCharHashTest {

    private TCharHash testObject;

    @Before
    public void setUp() {
        testObject = new TCharHash();
    }

    @After
    public void tearDown() {
        testObject = null;
    }

    @Test
    public void testConstructor1() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, testObject.getNoEntryValue());
        assertEquals(10, testObject._states.length);
        assertEquals(10, testObject._set.length);
        assertEquals(10, testObject.capacity());
        assertEquals(0, testObject.size());
        assertFalse(testObject.contains((char) 0));
    }

    @Test
    public void testConstructor2() {
        testObject = new TCharHash(20);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, testObject.getNoEntryValue());
        assertEquals(20, testObject._states.length);
        assertEquals(20, testObject._set.length);
        assertEquals(20, testObject.capacity());
        assertEquals(0, testObject.size());
        assertFalse(testObject.contains((char) 0));
    }

    @Test
    public void testConstructor3() {
        testObject = new TCharHash(20, 0.5f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, testObject.getNoEntryValue());
        assertEquals(20, testObject._states.length);
        assertEquals(20, testObject._set.length);
        assertEquals(20, testObject.capacity());
        assertEquals(0, testObject.size());
        assertFalse(testObject.contains((char) 0));
    }

    @Test
    public void testConstructor4() {
        testObject = new TCharHash(20, 0.5f, (char) 1);
        assertEquals(1, testObject.getNoEntryValue());
        assertEquals(20, testObject._states.length);
        assertEquals(20, testObject._set.length);
        assertEquals(20, testObject.capacity());
        assertEquals(0, testObject.size());
        assertFalse(testObject.contains((char) 0));
    }

    @Test
    public void testGetNoEntryValue() {
        char expected = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        assertEquals(expected, testObject.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int expectedCapacity = 20;
        assertEquals(expectedCapacity, testObject.setUp(expectedCapacity));
        assertEquals(expectedCapacity, testObject._set.length);
        assertEquals(expectedCapacity, testObject.capacity());
    }

    @Test
    public void testContains() {
        assertFalse(testObject.contains((char) 1));
        testObject.insertKey((char) 1);
        assertTrue(testObject.contains((char) 1));
    }

    @Test
    public void testForEach() {
        testObject.insertKey((char) 1);
        testObject.insertKey((char) 2);
        testObject.insertKey((char) 3);

        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.print(value + " ");
                return true;
            }
        };

        assertTrue(testObject.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        testObject.insertKey((char) 1);
        testObject.insertKey((char) 2);
        testObject.insertKey((char) 3);

        int expectedSize = testObject.size() - 1;
        testObject.removeAt(1);
        assertEquals(expectedSize, testObject.size());
        assertFalse(testObject.contains((char) 2));
    }

    @Test
    public void testIndex() {
        testObject.insertKey((char) 1);
        assertEquals(0, testObject.index((char) 1));
        assertEquals(-1, testObject.index((char) 2));
    }

    @Test
    public void testInsertKey() {
        int expectedSize = testObject.size() + 1;
        int index = testObject.insertKey((char) 1);
        assertEquals(expectedSize, testObject.size());
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehash() {
        testObject = new TCharHash(5);
        testObject.insertKey((char) 1);
        testObject.insertKey((char) 2);
        testObject.insertKey((char) 3);
        testObject.insertKey((char) 4);
        int expectedSize = testObject.size() + 1;
        int index = testObject.insertKeyRehash((char) 5, 0, HashFunctions.hash((char) 5) & Integer.MAX_VALUE, (byte) 2);
        assertEquals(expectedSize, testObject.size());
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyAt() {
        testObject.insertKeyAt(0, (char) 1);
        assertEquals((char) 1, testObject._set[0]);
        assertEquals((byte) 1, testObject._states[0]);
    }

}