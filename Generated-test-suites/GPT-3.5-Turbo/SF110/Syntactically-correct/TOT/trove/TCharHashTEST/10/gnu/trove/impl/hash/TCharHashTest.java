import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    private TCharHash tCharHash;

    @Before
    public void setUp() {
        tCharHash = new TestTCharHash();
    }

    @Test
    public void testConstructor1() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
        assertEquals(10, tCharHash._set.length);
    }

    @Test
    public void testConstructor2() {
        TCharHash customTCharHash = new TestTCharHash(20);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, customTCharHash.getNoEntryValue());
        assertEquals(20, customTCharHash._set.length);
    }

    @Test
    public void testConstructor3() {
        TCharHash customTCharHash = new TestTCharHash(30, 0.75f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, customTCharHash.getNoEntryValue());
        assertEquals(30, customTCharHash._set.length);
    }

    @Test
    public void testConstructor4() {
        char noEntryValue = '!';
        TCharHash customTCharHash = new TestTCharHash(40, 0.6f, noEntryValue);
        assertEquals(noEntryValue, customTCharHash.getNoEntryValue());
        assertEquals(40, customTCharHash._set.length);
    }

    @Test
    public void testContains() {
        tCharHash.insertKey('A');
        assertTrue(tCharHash.contains('A'));
        assertFalse(tCharHash.contains('B'));
    }

    @Test
    public void testForEach() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        TestTCharProcedure tCharProcedure = new TestTCharProcedure();
        assertTrue(tCharHash.forEach(tCharProcedure));
        assertFalse(tCharProcedure.procedureCalled);
    }

    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        tCharHash.removeAt(1);
        assertFalse(tCharHash.contains('B'));
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash._set[1]);
    }

    @Test
    public void testIndex() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        assertEquals(1, tCharHash.index('B'));
        assertEquals(-1, tCharHash.index('D'));
    }

    @Test
    public void testIndexRehashed() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        int index = tCharHash.index('B');
        assertEquals(1, tCharHash.indexRehashed('B', index, 0, (byte) 0));
        assertEquals(-1, tCharHash.indexRehashed('D', index, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('A'));
        assertEquals(-1, tCharHash.insertKey('A'));
        assertEquals(1, tCharHash.insertKey('B'));
    }

    @Test
    public void testInsertKeyRehash() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        assertEquals(1, tCharHash.insertKeyRehash('A', 0, 0, (byte) 2));
        assertEquals(3, tCharHash.insertKeyRehash('D', 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKeyAt() {
        tCharHash.insertKeyAt(0, 'A');
        assertEquals('A', tCharHash._set[0]);
        assertEquals((byte) 1, tCharHash._states[0]);
    }

    private class TestTCharHash extends TCharHash {

        public TestTCharHash() {
            super();
        }

        public TestTCharHash(int initialCapacity) {
            super(initialCapacity);
        }

        public TestTCharHash(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }

        public TestTCharHash(int initialCapacity, float loadFactor, char no_entry_value) {
            super(initialCapacity, loadFactor, no_entry_value);
        }
    }

    private class TestTCharProcedure implements TCharProcedure {

        public boolean procedureCalled = false;

        public boolean execute(char value) {
            procedureCalled = true;
            return false;
        }
    }
}