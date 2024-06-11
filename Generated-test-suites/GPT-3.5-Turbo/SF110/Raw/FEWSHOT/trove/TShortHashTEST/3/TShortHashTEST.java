package gnu.trove.impl.hash;

import java.util.*;
import gnu.trove.procedure.*;
import gnu.trove.impl.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    @Test
    public void testContains() {
        TShortHash hash = createTestHash();
        
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertFalse(hash.contains((short) 3));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = createTestHash();
        
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short val) {
                System.out.println(val);
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = createTestHash();
        
        hash.removeAt(1);
        
        assertFalse(hash.contains((short) 2));
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = createTestHash();
        
        assertEquals(1, hash.index((short) 1));
        assertEquals(2, hash.index((short) 2));
        assertEquals(-1, hash.index((short) 3));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = createTestHash();
        
        int index = hash.insertKey((short) 3);
        
        assertEquals(3, hash._set[index]);
    }
    
    private TShortHash createTestHash() {
        TShortHash hash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                _states = new byte[initialCapacity];
                Constants.DEFAULT_SHORT_NO_ENTRY_VALUE = 0;
                return initialCapacity;
            }
            
            @Override
            void insertKeyAt(int index, short val) {
                _set[index] = val;
                _states[index] = 1;
            }
        };
        
        hash._set = new short[4];
        hash._states = new byte[4];
        
        hash._set[1] = 1;
        hash._set[2] = 2;
        hash._states[1] = 1;
        hash._states[2] = 1;
        
        return hash;
    }
}