package gnu.trove.impl.hash;

import org.junit.Test;

import static org.junit.Assert.*;

public class TShortHashTest {

    // Test method for getNoEntryValue()
    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            public boolean contains(short val) {
                return false;
            }

            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }

            @Override
            protected void removeAt(int index) {

            }

            @Override
            protected int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {

            }
        };

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    // Test method for contains(short val)
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            public boolean contains(short val) {
                if (val == 5) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }

            @Override
            protected void removeAt(int index) {

            }

            @Override
            protected int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {

            }
        };

        assertTrue(tShortHash.contains((short) 5));
        assertFalse(tShortHash.contains((short) 10));
    }

    // Test method for forEach(TShortProcedure procedure)
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            public boolean contains(short val) {
                return false;
            }

            @Override
            public boolean forEach(TShortProcedure procedure) {
                return procedure.execute((short) 5);
            }

            @Override
            protected void removeAt(int index) {

            }

            @Override
            protected int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {

            }
        };

        assertTrue(tShortHash.forEach((value) -> value == 5));
        assertFalse(tShortHash.forEach((value) -> value == 10));
    }

    // Test method for removeAt(int index)
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            public boolean contains(short val) {
                return false;
            }

            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }

            @Override
            protected void removeAt(int index) {
                assertEquals(0, index);
            }

            @Override
            protected int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {

            }
        };

        tShortHash.removeAt(0);
    }

    // Test method for index(short val)
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            public boolean contains(short val) {
                return false;
            }

            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }

            @Override
            protected void removeAt(int index) {

            }

            @Override
            protected int index(short val) {
                if (val == 5) {
                    return 0;
                }
                return -1;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {

            }
        };

        assertEquals(0, tShortHash.index((short) 5));
        assertEquals(-1, tShortHash.index((short) 10));
    }

    // Test method for indexRehashed(short key, int index, int hash, byte state)
    @Test
    public void testIndexRehashed() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            public boolean contains(short val) {
                return false;
            }

            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }

            @Override
            protected void removeAt(int index) {

            }

            @Override
            protected int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                if (key == 5 && state == 1) {
                    return index;
                }
                return -1;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {

            }
        };

        assertEquals(0, tShortHash.indexRehashed((short) 5, 0, 1, (byte) 1));
        assertEquals(-1, tShortHash.indexRehashed((short) 10, 0, 1, (byte) 0));
    }

    // Test method for insertKey(short val)
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            public boolean contains(short val) {
                return false;
            }

            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }

            @Override
            protected void removeAt(int index) {

            }

            @Override
            protected int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                if (val == 5) {
                    return 0;
                }
                return -1;
            }

            @Override
            int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {

            }
        };

        assertEquals(0, tShortHash.insertKey((short) 5));
        assertEquals(-1, tShortHash.insertKey((short) 10));
    }

    // Test method for insertKeyRehash(short val, int index, int hash, byte state)
    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            public boolean contains(short val) {
                return false;
            }

            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }

            @Override
            protected void removeAt(int index) {

            }

            @Override
            protected int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int insertKeyRehash(short val, int index, int hash, byte state) {
                if (val == 5 && state == 2) {
                    return index;
                }
                return -1;
            }

            @Override
            void insertKeyAt(int index, short val) {

            }
        };

        assertEquals(0, tShortHash.insertKeyRehash((short) 5, 0, 1, (byte) 2));
        assertEquals(-1, tShortHash.insertKeyRehash((short) 10, 0, 1, (byte) 0));
    }

    // Test method for insertKeyAt(int index, short val)
    @Test
    public void testInsertKeyAt() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            public boolean contains(short val) {
                return false;
            }

            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }

            @Override
            protected void removeAt(int index) {

            }

            @Override
            protected int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                assertEquals(0, index);
                assertEquals(5, val);
            }
        };

        tShortHash.insertKeyAt(0, (short) 5);
    }
}