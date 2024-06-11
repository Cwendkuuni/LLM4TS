package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHashImpl();
    }

    @Test
    public void testConstructor() {
        assertNotNull(tShortHash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithCapacity() {
        tShortHash = new TShortHashImpl(10);
        assertNotNull(tShortHash);
        assertEquals(10, tShortHash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithCapacityAndLoadFactor() {
        tShortHash = new TShortHashImpl(10, 0.5f);
        assertNotNull(tShortHash);
        assertEquals(10, tShortHash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithCapacityLoadFactorAndNoEntryValue() {
        short noEntryValue = 0;
        tShortHash = new TShortHashImpl(10, 0.5f, noEntryValue);
        assertNotNull(tShortHash);
        assertEquals(10, tShortHash.size());
        assertEquals(noEntryValue, tShortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        tShortHash.setUp(10);
        assertEquals(10, tShortHash.size());
    }

    @Test
    public void testContains() {
        tShortHash.put((short)1);
        assertTrue(tShortHash.contains((short)1));
        assertFalse(tShortHash.contains((short)2));
    }

    @Test
    public void testForEach() {
        tShortHash.put((short)1);
        tShortHash.put((short)2);
        TShortProcedure procedure = new TShortProcedureImpl();
        assertTrue(tShortHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tShortHash.put((short)1);
        tShortHash.removeAt(0);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.get(0));
        assertEquals(0, tShortHash.size());
    }

    @Test
    public void testIndex() {
        tShortHash.put((short)1);
        assertEquals(0, tShortHash.index((short)1));
        assertEquals(-1, tShortHash.index((short)2));
    }

    @Test
    public void testInsertKey() {
        tShortHash.insertKey((short)1);
        assertEquals(1, tShortHash.size());
        assertEquals(0, tShortHash.index((short)1));
    }

    private class TShortHashImpl extends TShortHash {
        private static final long serialVersionUID = 1L;

        public TShortHashImpl() {
            super();
        }

        public TShortHashImpl(int initialCapacity) {
            super(initialCapacity);
        }

        public TShortHashImpl(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }

        public TShortHashImpl(int initialCapacity, float loadFactor, short no_entry_value) {
            super(initialCapacity, loadFactor, no_entry_value);
        }

        @Override
        protected void rehash(int newCapacity) {
            // Do nothing
        }
    }

    private class TShortProcedureImpl implements TShortProcedure {
        @Override
        public boolean execute(short value) {
            return true;
        }
    }
}