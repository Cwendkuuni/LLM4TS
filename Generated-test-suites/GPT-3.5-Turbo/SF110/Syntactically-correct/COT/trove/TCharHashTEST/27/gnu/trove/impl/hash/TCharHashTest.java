
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash hash = new MockTCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        assert(hash.contains('a'));
        assert(hash.contains('b'));
        assert(hash.contains('c'));
        assert(!hash.contains('d'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new MockTCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        StringBuilder result = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                result.append(value);
                return true;
            }
        };
        
        hash.forEach(procedure);
        
        assert(result.toString().equals("abc"));
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new MockTCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        hash.removeAt(0);
        assert(!hash.contains('a'));
        assert(hash.contains('b'));
        assert(hash.contains('c'));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new MockTCharHash();
        
        int index = hash.insertKey('a');
        assert(index == 0);
        assert(hash.contains('a'));
        
        index = hash.insertKey('b');
        assert(index == 1);
        assert(hash.contains('b'));
        
        index = hash.insertKey('a');
        assert(index == -1);
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testSetUp() {
        TCharHash hash = new MockTCharHash() {
            protected int setUp(final int initialCapacity) {
                return 5; // Force smaller capacity
            }
        };
        
        assert(hash._set.length == 5);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TCharHash hash = new MockTCharHash();
        hash.insertKeyRehash('a', 0, 0, (byte)0);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash hash = new MockTCharHash();
        char noEntryValue = hash.getNoEntryValue();
        
        assert(noEntryValue == Constants.DEFAULT_CHAR_NO_ENTRY_VALUE);
    }

    // MockTCharHash class for testing

    private class MockTCharHash extends TCharHash {
        protected void removeAt(final int index) {
            // Do nothing in mock implementation
        }

        int insertKeyRehash(final char val, int index, final int hash, byte state) {
            // Stub implementation
            return index;
        }

        void insertKeyAt(final int index, final char val) {
            // Do nothing in mock implementation
        }

        protected int setUp(final int initialCapacity) {
            return super.setUp(initialCapacity);
        }

        public boolean contains(final char val) {
            // Stub implementation
            return true;
        }

        public boolean forEach(final TCharProcedure procedure) {
            // Stub implementation
            return true;
        }

        protected int index(final char val) {
            // Stub implementation
            return 0;
        }

        protected int indexRehashed(final char key, int index, final int hash, byte state) {
            // Stub implementation
            return 0;
        }

        protected int insertKey(final char val) {
            // Stub implementation
            return 0;
        }
    }
}
