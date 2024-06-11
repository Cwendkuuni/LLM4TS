import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testNoEntryValue() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }
        };

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 2;
            }
        };
        hash._set = new short[]{1, 2};
        hash._states = new byte[]{1, 1};

        assertTrue(hash.contains((short) 1));
        assertFalse(hash.contains((short) 3));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 3;
            }
        };
        hash._states = new byte[]{1, 1, 0};
        hash._set = new short[]{1, 2, 0};

        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value != 0;
            }
        };

        assertTrue(hash.forEach(procedure));
        procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value != 1;
            }
        };
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 3;
            }

            @Override
            protected void removeAt(int index) {
                _states[index] = 0;
            }
        };
        hash._states = new byte[]{1, 1, 1};
        hash._set = new short[]{1, 2, 3};

        hash.removeAt(1);

        assertEquals(0, hash._states[1]);
        assertEquals(0, hash._set[1]);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 3;
            }

            @Override
            protected int indexRehashed(short key, int index, int hash, byte state) {
                return 1;
            }
        };
        hash._states = new byte[]{1, 2, 1};
        hash._set = new short[]{1, 2, 3};

        assertEquals(1, hash.index((short) 2));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 3;
            }

            @Override
            protected void insertKeyAt(int index, short val) {
                _states[index] = 1;
                _set[index] = val;
            }
        };
        hash._states = new byte[]{1, 1, 0};
        hash._set = new short[]{1, 2, 0};

        int index = hash.insertKey((short) 3);

        assertEquals(2, index);
        assertEquals(1, hash._states[2]);
        assertEquals(3, hash._set[2]);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 4;
            }

            @Override
            protected void insertKeyAt(int index, short val) {
                _states[index] = 1;
                _set[index] = val;
            }
        };
        hash._states = new byte[]{1, 2, 1, 0};
        hash._set = new short[]{1, 2, 3, 0};

        int index = hash.insertKeyRehash((short) 4, 1, 1, (byte) 2);

        assertEquals(3, index);
        assertEquals(1, hash._states[3]);
        assertEquals(4, hash._set[3]);
    }
}