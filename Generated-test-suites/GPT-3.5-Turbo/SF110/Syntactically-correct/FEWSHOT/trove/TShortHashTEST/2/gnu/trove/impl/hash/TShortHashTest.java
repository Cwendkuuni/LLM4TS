package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHashStub();
    }

    @Test
    public void testContains_shouldReturnTrueWhenValueExists() {
        tShortHash._set[0] = 10;
        assertTrue(tShortHash.contains((short) 10));
    }

    @Test
    public void testContains_shouldReturnFalseWhenValueDoesNotExist() {
        assertFalse(tShortHash.contains((short) 10));
    }

    @Test
    public void testForEach_shouldReturnTrueWhenProcedureExecutesOnAllValues() {
        tShortHash._set[0] = 10;
        tShortHash._set[1] = 20;
        TShortProcedureStub procedure = new TShortProcedureStub();
        assertTrue(tShortHash.forEach(procedure));
        assertTrue(procedure.executed1);
        assertTrue(procedure.executed2);
    }

    @Test
    public void testForEach_shouldReturnFalseWhenProcedureDoesNotExecuteOnAllValues() {
        tShortHash._set[0] = 10;
        tShortHash._set[1] = 20;
        TShortProcedureStub procedure = new TShortProcedureStub(false);
        assertFalse(tShortHash.forEach(procedure));
        assertTrue(procedure.executed1);
        assertFalse(procedure.executed2);
    }

    @Test
    public void testRemoveAt_shouldSetNoEntryValueAndRemoveElementAtIndex() {
        tShortHash._set[0] = 10;
        tShortHash._states[0] = 1;
        tShortHash.removeAt(0);
        assertEquals(tShortHash.no_entry_value, tShortHash._set[0]);
        assertEquals(0, tShortHash._states[0]);
    }

    @Test
    public void testIndex_shouldReturnIndexWhenValueExists() {
        tShortHash._set[0] = 10;
        tShortHash._states[0] = 1;
        assertEquals(0, tShortHash.index((short) 10));
    }

    @Test
    public void testIndex_shouldReturnNegativeWhenValueDoesNotExist() {
        assertEquals(-1, tShortHash.index((short) 10));
    }

    @Test
    public void testInsertKey_shouldReturnIndexWhenKeyIsInserted() {
        int index = tShortHash.insertKey((short) 10);
        assertEquals(0, index);
        assertEquals(10, tShortHash._set[0]);
        assertEquals(1, tShortHash._states[0]);
    }

    @Test
    public void testInsertKey_shouldReturnNegativeIndexWhenKeyAlreadyExists() {
        tShortHash._set[0] = 10;
        tShortHash._states[0] = 1;
        int index = tShortHash.insertKey((short) 10);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash_shouldReturnIndexWhenKeyIsInsertedAfterRehash() {
        tShortHash._set = new short[3];
        tShortHash._states = new byte[3];
        tShortHash._states[1] = 2;
        tShortHash._states[2] = 1;
        int index = tShortHash.insertKeyRehash((short) 10, 1, 1, (byte) 1);
        assertEquals(0, index);
        assertEquals(10, tShortHash._set[0]);
        assertEquals(1, tShortHash._states[0]);
    }

    @Test
    public void testInsertKeyRehash_shouldReturnNegativeIndexWhenKeyIsAlreadyPresentAfterRehash() {
        tShortHash._set = new short[3];
        tShortHash._states = new byte[3];
        tShortHash._set[1] = 10;
        tShortHash._states[1] = 1;
        int index = tShortHash.insertKeyRehash((short) 10, 1, 1, (byte) 1);
        assertEquals(-2, index);
    }

    @Test
    public void testInsertKeyRehash_shouldThrowExceptionWhenNoFreeSlotsAvailableAfterRehash() {
        tShortHash._set = new short[3];
        tShortHash._states = new byte[3];
        tShortHash._set[0] = 10;
        tShortHash._states[0] = 1;
        tShortHash._set[1] = 20;
        tShortHash._states[1] = 1;
        tShortHash._set[2] = 30;
        tShortHash._states[2] = 1;
        
        try {
            tShortHash.insertKeyRehash((short) 40, 1, 1, (byte) 1);
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }

    private class TShortHashStub extends TShortHash {

        @Override
        public boolean forEach(TShortProcedure procedure) {
            procedure.execute(_set[0]);
            return procedure.execute(_set[1]);
        }

        @Override
        protected int indexRehashed(short key, int index, int hash, byte state) {
            return 0;
        }

        @Override
        protected int removeAt(int index) {
            return 0;
        }

        @Override
        protected int insertKeyRehash(short val, int index, int hash, byte state) {
            return 0;
        }

        @Override
        protected int insertKeyAt(int index, short val) {
            return 0;
        }
    }

    private class TShortProcedureStub implements TShortProcedure {
        public boolean executed1;
        public boolean executed2;

        public TShortProcedureStub() {
            this.executed1 = false;
            this.executed2 = false;
        }

        public TShortProcedureStub(boolean executed2) {
            this.executed1 = false;
            this.executed2 = executed2;
        }

        @Override
        public boolean execute(short value) {
            if (!executed1 && value == 10) {
                executed1 = true;
                return true;
            }
            if (!executed2 && value == 20) {
                executed2 = true;
                return true;
            }
            return false;
        }
    }
}