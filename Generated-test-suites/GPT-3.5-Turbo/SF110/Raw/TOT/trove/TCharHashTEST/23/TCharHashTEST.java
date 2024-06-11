import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

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
        TCharHash testObj2 = new TCharHash(10);
        assertEquals(10, testObj2.size());
    }

    @Test
    public void testConstructor3() {
        TCharHash testObj3 = new TCharHash(10, 0.5f);
        assertEquals(10, testObj3.size());
        assertEquals(0.5f, testObj3.getLoadFactor(), 0.001);
    }

    @Test
    public void testConstructor4() {
        TCharHash testObj4 = new TCharHash(10, 0.5f, 'X');
        assertEquals(10, testObj4.size());
        assertEquals(0.5f, testObj4.getLoadFactor(), 0.001);
        assertEquals('X', testObj4.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int capacity = testObj.setUp(15);
        assertEquals(15, capacity);
        assertEquals(15, testObj.size());
        assertEquals(15, testObj._set.length);
    }

    @Test
    public void testContains() {
        assertFalse(testObj.contains('A'));
        testObj.insertKey('A');
        assertTrue(testObj.contains('A'));
    }

    @Test
    public void testForEach() {
        StringBuilder stringBuilder = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                stringBuilder.append(value);
                return true;
            }
        };
        testObj.insertKey('A');
        testObj.insertKey('B');
        testObj.insertKey('C');
        testObj.forEach(procedure);
        assertEquals("ABC", stringBuilder.toString());
    }

    @Test
    public void testRemoveAt() {
        testObj.insertKey('A');
        testObj.insertKey('B');
        testObj.insertKey('C');
        testObj.removeAt(1);
        assertFalse(testObj.contains('B'));
        assertEquals(testObj.getNoEntryValue(), testObj._set[1]);
    }

    @Test
    public void testIndex() {
        testObj.insertKey('A');
        testObj.insertKey('B');
        testObj.insertKey('C');
        assertEquals(1, testObj.index('B'));
        assertEquals(-1, testObj.index('D'));
    }

    @Test
    public void testIndexRehashed() {
        testObj.insertKey('A');
        testObj.insertKey('B');
        testObj.insertKey('C');
        assertEquals(2, testObj.indexRehashed('C', 1, 12345, (byte)1));
        assertEquals(-1, testObj.indexRehashed('D', 0, 123, (byte)0));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, testObj.insertKey('A'));
        assertEquals(-1, testObj.insertKey('A'));
        testObj.removeAt(0);
        assertEquals(0, testObj.insertKey('A'));
        assertEquals(1, testObj.insertKey('B'));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        testObj.insertKeyRehash('A', 0, 12345, (byte)1);
    }

    @Test
    public void testInsertKeyAt() {
        testObj.insertKeyAt(0, 'A');
        assertEquals('A', testObj._set[0]);
        assertEquals((byte)1, testObj._states[0]);
    }
}