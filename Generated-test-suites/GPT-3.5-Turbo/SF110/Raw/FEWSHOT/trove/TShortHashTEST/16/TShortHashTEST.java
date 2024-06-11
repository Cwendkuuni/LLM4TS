package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash() {

            @Override
            protected void removeAt(int index) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected int insertKeyRehash(short val, int index, int hash, byte state) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            protected void insertKeyAt(int index, short val) {
                // TODO Auto-generated method stub
                
            }
        };
    }

    @Test
    public void testContains() {
        assertTrue(tShortHash.contains((short) 0));
        assertFalse(tShortHash.contains((short) 5));
    }

    @Test
    public void testForEach() {
        TShortProcedure procedure = new TShortProcedure() {

            @Override
            public boolean execute(short value) {
                // TODO Auto-generated method stub
                return true;
            }
        };
        assertTrue(tShortHash.forEach(procedure));
    }

    @Test
    public void testIndex() {
        assertEquals(-1, tShortHash.index((short) 3));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tShortHash.insertKey((short) 5));
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(-1, tShortHash.getNoEntryValue());
    }
}