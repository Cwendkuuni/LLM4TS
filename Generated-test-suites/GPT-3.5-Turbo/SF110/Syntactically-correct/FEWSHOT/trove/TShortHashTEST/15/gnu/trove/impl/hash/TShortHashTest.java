package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testConstructor1() {
        TShortHash hash = new TShortHash() {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                return;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                return;
            }
        };

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TShortHash hash = new TShortHash(initialCapacity) {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                return;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                return;
            }
        };

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(initialCapacity, hash._set.length);
    }

    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash hash = new TShortHash(initialCapacity, loadFactor) {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                return;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                return;
            }
        };

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(initialCapacity, hash._set.length);
    }

    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short no_entry_value = 0;
        TShortHash hash = new TShortHash(initialCapacity, loadFactor, no_entry_value) {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                return;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                return;
            }
        };

        assertEquals(no_entry_value, hash.getNoEntryValue());
        assertEquals(initialCapacity, hash._set.length);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash() {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                return;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                return;
            }
        };

        hash._set = new short[]{1, 2, 3};
        assertTrue(hash.contains((short) 1));
        assertFalse(hash.contains((short) 4));
    }

    @Test
    public void testForEach() {
        final boolean[] procedureExecuted = {false};

        TShortHash hash = new TShortHash() {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                return;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                return;
            }
        };

        hash._set = new short[]{1, 2, 3};
        hash._states = new byte[]{1, 1, 1};
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                procedureExecuted[0] = true;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertTrue(procedureExecuted[0]);
    }

    @Test
    public void testRemoveAt() {
        boolean[] removeAtCalled = {false};

        TShortHash hash = new TShortHash() {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                removeAtCalled[0] = true;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                return;
            }
        };

        hash.removeAt(0);
        assertTrue(removeAtCalled[0]);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash() {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                return;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                return;
            }
        };

        hash._states = new byte[]{1, 0, 1};
        hash._set = new short[]{1, 0, 3};
        assertEquals(0, hash.index((short) 1));
        assertEquals(2, hash.index((short) 3));
        assertEquals(-1, hash.index((short) 2));
    }

    @Test
    public void testInsertKey() {
        boolean[] insertKeyAtCalled = {false};

        TShortHash hash = new TShortHash() {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                return;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                insertKeyAtCalled[0] = true;
            }
        };

        hash.insertKey((short) 1);
        assertTrue(insertKeyAtCalled[0]);
    }

    @Test
    public void testInsertKeyRehash() {
        boolean[] insertKeyAtCalled = {false};

        TShortHash hash = new TShortHash() {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                return;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                insertKeyAtCalled[0] = true;
            }
        };

        hash.insertKeyRehash((short) 1, 0, 20, (byte) 1);
        assertTrue(insertKeyAtCalled[0]);
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash() {
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            public void removeAt(int index) {
                return;
            }

            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            public void insertKeyAt(int index, short val) {
                return;
            }
        };

        short val = 1;
        int index = 0;
        hash.insertKeyAt(index, val);
        assertEquals(val, hash._set[index]);
        assertEquals(1, hash._states[index]);
    }
}