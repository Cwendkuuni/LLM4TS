package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {
    
    private TShortHash tShortHash;
    
    @Before
    public void setUp() {
        tShortHash = new TShortHash() {
            @Override
            public void removeAt(int index) {
                
            }
            
            @Override
            public int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }
            
            @Override
            public int insertKey(short val) {
                return 0;
            }
            
            @Override
            public int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }
            
            @Override
            protected void insertKeyAt(int index, short val) {
                
            }
            
            @Override
            protected int index(short val) {
                return 0;
            }
            
            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }
        };
    }
    
    @Test
    public void testContains() {
        // Test when value is present in the set
        tShortHash._set = new short[]{1, 2, 3};
        assertTrue(tShortHash.contains((short) 2));
        
        // Test when value is not present in the set
        tShortHash._set = new short[]{1, 2, 3};
        assertFalse(tShortHash.contains((short) 4));
    }
    
    @Test
    public void testForEach() {
        // Test when procedure.execute() returns true for all elements
        tShortHash._states = new byte[]{1, 1, 1};
        tShortHash._set = new short[]{1, 2, 3};
        assertFalse(tShortHash.forEach((val) -> true));
        
        // Test when procedure.execute() returns false for one of the elements
        tShortHash._states = new byte[]{1, 1, 1};
        tShortHash._set = new short[]{1, 2, 3};
        assertTrue(tShortHash.forEach((val) -> val != 2));
    }
}