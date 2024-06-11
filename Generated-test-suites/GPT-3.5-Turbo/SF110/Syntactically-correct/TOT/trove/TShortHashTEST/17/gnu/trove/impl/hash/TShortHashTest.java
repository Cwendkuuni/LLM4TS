import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testTShortHash_noArgsConstructor() {
        TShortHash hash = new TShortHash() {
            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing - mocking the method for testing purposes
            }
        };
        
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertFalse(hash.contains((short) 0));
    }
    
    @Test
    public void testTShortHash_oneArgsConstructor() {
        int initialCapacity = 10;
        
        TShortHash hash = new TShortHash(initialCapacity) {
            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing - mocking the method for testing purposes
            }
        };
        
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertFalse(hash.contains((short) 0));
    }
    
    @Test
    public void testTShortHash_twoArgsConstructor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        
        TShortHash hash = new TShortHash(initialCapacity, loadFactor) {
            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing - mocking the method for testing purposes
            }
        };
        
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertFalse(hash.contains((short) 0));
    }
    
    @Test
    public void testTShortHash_threeArgsConstructor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short noEntryValue = -1;
        
        TShortHash hash = new TShortHash(initialCapacity, loadFactor, noEntryValue) {
            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing - mocking the method for testing purposes
            }
        };
        
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertFalse(hash.contains(noEntryValue));
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        
        TShortHash hash = new TShortHash() {
            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing - mocking the method for testing purposes
            }
        };
        
        int capacity = hash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(initialCapacity, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash() {
            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing - mocking the method for testing purposes
            }
        };
        
        hash.insertKey((short) 10);
        assertTrue(hash.contains((short) 10));
        assertFalse(hash.contains((short) 20));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash() {
            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing - mocking the method for testing purposes
            }
        };
        
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);
        
        final StringBuilder sb = new StringBuilder();
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                sb.append(value).append(",");
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals("10,20,30,", sb.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash() {
            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing - mocking the method for testing purposes
            }
        };
        
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);
        
        hash.removeAt(1);
        
        assertFalse(hash.contains((short) 20));
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash() {
            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing - mocking the method for testing purposes
            }
        };
        
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);
        
        assertEquals(0, hash.index((short) 10));
        assertEquals(1, hash.index((short) 20));
        assertEquals(2, hash.index((short) 30));
        assertEquals(-1, hash.index((short) 40));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash() {
            @Override
            protected void insertKeyAt(int index, short val) {
                // do nothing - mocking the method for testing purposes
            }
        };
        
        int index1 = hash.insertKey((short) 10);
        int index2 = hash.insertKey((short) 20);
        int index3 = hash.insertKey((short) 30);
        
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
    }

}