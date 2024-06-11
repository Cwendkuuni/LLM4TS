package gnu.trove.impl.hash;

import gnu.trove.procedure.TShortProcedure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash() {
            @Override
            protected void removeAt(int index) {
                // do nothing
            }

            @Override
            protected int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing
            }
        };
    }

    @Test
    public void testContains() {
        assertFalse(tShortHash.contains((short) 0));
        assertFalse(tShortHash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return false;
            }
        };

        assertTrue(tShortHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tShortHash.removeAt(0);
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tShortHash.insertKey((short) 0));
        assertEquals(0, tShortHash.insertKey((short) 1));
    }
}