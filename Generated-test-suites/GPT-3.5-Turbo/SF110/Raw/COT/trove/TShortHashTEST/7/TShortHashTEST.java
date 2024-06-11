import static org.junit.Assert.*;
import org.junit.Test;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }
            @Override
            protected int insertKey(short val) {
                return 0;
            }
            @Override
            protected void removeAt(int index) {}
        };
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }
            @Override
            protected int insertKey(short val) {
                return 0;
            }
            @Override
            protected void removeAt(int index) {}
        };
        // Test containing value
        hash._states = new byte[]{1, 0};
        hash._set = new short[]{5, 0};
        assertTrue(hash.contains((short) 5));

        // Test not containing value
        hash._states = new byte[]{0, 0};
        hash._set = new short[]{0, 0};
        assertFalse(hash.contains((short) 5));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }
            @Override
            protected int insertKey(short val) {
                return 0;
            }
            @Override
            protected void removeAt(int index) {}
        };
        // Test forEach procedure returns true
        hash._states = new byte[]{1, 1};
        hash._set = new short[]{1, 2};
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));

        // Test forEach procedure returns false
        hash._states = new byte[]{1, 1};
        hash._set = new short[]{1, 2};
        procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return false;
            }
        };
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }
            @Override
            protected int insertKey(short val) {
                return 0;
            }
        };
        // Test removeAt
        hash._states = new byte[]{1, 1};
        hash._set = new short[]{1, 2};
        hash.removeAt(0);
        assertEquals(0, hash._states[0]);
        assertEquals(0, hash._set[0]);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }
            @Override
            protected int insertKey(short val) {
                return 0;
            }
        };
        // Test index valid val
        hash._states = new byte[]{1, 0};
        hash._set = new short[]{5, 0};
        assertEquals(0, hash.index((short) 5));

        // Test index invalid val
        hash._states = new byte[]{1, 0};
        hash._set = new short[]{5, 0};
        assertEquals(-1, hash.index((short) 10));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }
        };
        // Test insertKey with free slot
        hash._states = new byte[]{0};
        hash._set = new short[]{0};
        assertEquals(0, hash.insertKey((short) 5));

        // Test insertKey with no free slot
        hash._states = new byte[]{2, 2};
        hash._set = new short[]{5, 6};
        assertEquals(0, hash.insertKey((short) 10));
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }
        };
        hash._set = new short[1];
        hash._states = new byte[1];

        // Test insertKeyAt
        hash.insertKeyAt(0, (short) 5);
        assertEquals(5, hash._set[0]);
        assertEquals(1, hash._states[0]);
    }

    @Test(expected=IllegalStateException.class)
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }
        };
        hash._set = new short[1];
        hash._states = new byte[1];

        // Test insertKeyRehash with no free or removed slots
        hash._states[0] = 1;
        hash._set[0] = 5;
        hash.insertKeyRehash((short) 10, 0, 0, (byte) 1);
    }

}