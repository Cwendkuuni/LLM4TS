import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {

    private TCharHash testObj;

    @Before
    public void setUp() {
        testObj = new TCharHash();
    }

    @Test
    public void testConstructor1() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, testObj.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        testObj = new TCharHash(initialCapacity);
        assertEquals(initialCapacity, testObj._set.length);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, testObj.getNoEntryValue());
    }

    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        testObj = new TCharHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, testObj._set.length);
        assertEquals(loadFactor, testObj.loadFactor, 0);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, testObj.getNoEntryValue());
    }

    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        char no_entry_value = 'X';
        testObj = new TCharHash(initialCapacity, loadFactor, no_entry_value);
        assertEquals(initialCapacity, testObj._set.length);
        assertEquals(loadFactor, testObj.loadFactor, 0);
        assertEquals(no_entry_value, testObj.getNoEntryValue());
    }

    @Test
    public void testContains_Positive() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        assertTrue(testObj.contains('a'));
    }

    @Test
    public void testContains_Negative() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        assertFalse(testObj.contains('x'));
    }

    @Test
    public void testForEach() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        TCharProcedure procedure = (char value) -> {
            System.out.println(value);
            return true;
        };
        assertTrue(testObj.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        testObj._states = new byte[]{ 1, 1, 1 };
        int indexToRemove = 1;
        testObj.removeAt(indexToRemove);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, testObj._set[indexToRemove]);
        assertEquals(0, testObj._states[indexToRemove]);
    }

    @Test
    public void testIndex_Positive() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        testObj._states = new byte[]{ 1, 1, 1 };
        int expectedIndex = 1;
        assertEquals(expectedIndex, testObj.index('b'));
    }

    @Test
    public void testIndex_Negative() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        testObj._states = new byte[]{ 1, 1, 1 };
        int expectedIndex = -1;
        assertEquals(expectedIndex, testObj.index('x'));
    }

    @Test
    public void testIndexRehashed_Positive() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        testObj._states = new byte[]{ 1, 1, 1 };
        int expectedIndex = 1;
        assertEquals(expectedIndex, testObj.indexRehashed('b', 0, 0, (byte)1));
    }

    @Test
    public void testIndexRehashed_Negative() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        testObj._states = new byte[]{ 1, 1, 1 };
        int expectedIndex = -1;
        assertEquals(expectedIndex, testObj.indexRehashed('x', 0, 0, (byte)1));
    }

    @Test
    public void testInsertKey_Positive() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        testObj._states = new byte[]{ 1, 1, 1 };
        char key = 'd';
        int expectedIndex = 3;
        assertEquals(expectedIndex, testObj.insertKey(key));
        assertEquals(key, testObj._set[expectedIndex]);
        assertEquals((byte)1, testObj._states[expectedIndex]);
    }

    @Test
    public void testInsertKey_Negative() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        testObj._states = new byte[]{ 1, 1, 1 };
        char key = 'b';
        int expectedIndex = -2;
        assertEquals(expectedIndex, testObj.insertKey(key));
    }

    @Test
    public void testInsertKeyRehash_Positive() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        testObj._states = new byte[]{ 1, 1, 1 };
        char key = 'd';
        int expectedIndex = 0;
        assertEquals(expectedIndex, testObj.insertKeyRehash(key, 1, 2, (byte)1));
        assertEquals(key, testObj._set[expectedIndex]);
        assertEquals((byte)1, testObj._states[expectedIndex]);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash_Negative() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        testObj._states = new byte[]{ 1, 1, 1 };
        char key = 'd';
        testObj.insertKeyRehash(key, 0, 0, (byte)1);
    }

    @Test
    public void testInsertKeyAt() {
        testObj._set = new char[]{ 'a', 'b', 'c' };
        testObj._states = new byte[]{ 1, 0, 1 };
        int index = 1;
        char val = 'x';
        testObj.insertKeyAt(index, val);
        assertEquals(val, testObj._set[index]);
        assertEquals((byte)1, testObj._states[index]);
    }
}