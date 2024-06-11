package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TShortHashTest {
    
    @Test
    public void testContains() {
        TShortHash testHash = new TestTShortHash();
        testHash.insertKey((short) 1);
        testHash.insertKey((short) 2);
        
        assertTrue(testHash.contains((short) 1));
        assertTrue(testHash.contains((short) 2));
        assertFalse(testHash.contains((short) 3));
    }
    
    @Test
    public void testForEach() {
        TShortHash testHash = new TestTShortHash();
        testHash.insertKey((short) 1);
        testHash.insertKey((short) 2);
        
        TestTShortProcedure procedure = new TestTShortProcedure();
        assertTrue(testHash.forEach(procedure));
        assertTrue(procedure.executed[0]);
        assertTrue(procedure.executed[1]);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash testHash = new TestTShortHash();
        testHash.insertKey((short) 1);
        testHash.insertKey((short) 2);
        
        testHash.removeAt(0);
        
        assertFalse(testHash.contains((short) 1));
        assertTrue(testHash.contains((short) 2));
    }
    
    @Test
    public void testIndex() {
        TShortHash testHash = new TestTShortHash();
        testHash.insertKey((short) 1);
        testHash.insertKey((short) 2);
        
        assertEquals(0, testHash.index((short) 1));
        assertEquals(1, testHash.index((short) 2));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash testHash = new TestTShortHash();
        assertEquals(0, testHash.insertKey((short) 1));
        assertEquals(1, testHash.insertKey((short) 2));
    }
    
    private class TestTShortHash extends TShortHash {
        public TestTShortHash() {
            super();
        }
        
        @Override
        protected int setUp(int initialCapacity) {
            return 2;
        }
    }
    
    private class TestTShortProcedure implements TShortProcedure {
        boolean[] executed = new boolean[2];
        
        @Override
        public boolean execute(short value) {
            if (value == 1) {
                executed[0] = true;
            } else if (value == 2) {
                executed[1] = true;
            }
            
            return true;
        }
    }
}