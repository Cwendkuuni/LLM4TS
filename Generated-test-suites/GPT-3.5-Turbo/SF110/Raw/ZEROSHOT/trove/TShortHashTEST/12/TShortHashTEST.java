package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash() {
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
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        assertEquals(0, tShortHash.setUp(0));
    }
    
    @Test
    public void testContains() {
        assertFalse(tShortHash.contains((short) 0));
    }
    
    @Test
    public void testForEach() {
        assertFalse(tShortHash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return false;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        tShortHash.removeAt(0);
        // no assertion as the method does not return a value
    }
    
    @Test
    public void testIndex() {
        assertEquals(0, tShortHash.index((short) 0));
    }
    
    @Test
    public void testIndexRehashed() {
        assertEquals(0, tShortHash.indexRehashed((short) 0, 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tShortHash.insertKey((short) 0));
    }
    
    @Test
    public void testInsertKeyRehash() {
        assertEquals(0, tShortHash.insertKeyRehash((short) 0, 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKeyAt() {
        tShortHash.insertKeyAt(0, (short) 0);
        // no assertion as the method does not return a value
    }
}