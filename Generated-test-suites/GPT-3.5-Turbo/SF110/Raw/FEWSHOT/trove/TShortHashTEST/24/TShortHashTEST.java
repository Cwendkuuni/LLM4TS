package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    @Test
    public void testConstructor1() {
        TShortHash hash = new TShortHash();
        short[] set = hash._set;
        short no_entry_value = hash.getNoEntryValue();
        
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, no_entry_value);
        assertNotNull(set);
        assertEquals(set.length, 0);
    }
    
    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        
        TShortHash hash = new TShortHash(initialCapacity);
        short[] set = hash._set;
        short no_entry_value = hash.getNoEntryValue();
        
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, no_entry_value);
        assertNotNull(set);
        assertEquals(set.length, initialCapacity);
    }
    
    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        
        TShortHash hash = new TShortHash(initialCapacity, loadFactor);
        short[] set = hash._set;
        short no_entry_value = hash.getNoEntryValue();
        
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, no_entry_value);
        assertNotNull(set);
        assertEquals(set.length, initialCapacity);
    }
    
    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short no_entry_value = 0;
        
        TShortHash hash = new TShortHash(initialCapacity, loadFactor, no_entry_value);
        short[] set = hash._set;
        
        assertEquals(no_entry_value, hash.getNoEntryValue());
        assertNotNull(set);
        assertEquals(set.length, initialCapacity);
        for (short value : set) {
            assertEquals(no_entry_value, value);
        }
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        
        TShortHash hash = new TShortHash();
        int capacity = hash.setUp(initialCapacity);
        short[] set = hash._set;
        short no_entry_value = hash.getNoEntryValue();
        
        assertEquals(initialCapacity, capacity);
        assertNotNull(set);
        assertEquals(set.length, initialCapacity);
        for (short value : set) {
            assertEquals(no_entry_value, value);
        }
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        short[] set = hash._set;
        short no_entry_value = hash.getNoEntryValue();
        
        Arrays.fill(set, (short)1);
        
        assertTrue(hash.contains((short)1));
        assertFalse(hash.contains((short)0));
        assertFalse(hash.contains(no_entry_value));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        short[] set = hash._set;
        byte[] states = hash._states;
        
        set[0] = 1;
        set[1] = 2;
        set[2] = 3;
        states[0] = 1;
        states[1] = 1;
        states[2] = 1;
        
        TShortProcedure procedure = new TShortProcedure() {
            int sum = 0;
            
            public boolean execute(short value) {
                sum += value;
                return true;
            }
            
            public int getSum() {
                return sum;
            }
        };
        
        hash.forEach(procedure);
        
        assertEquals(6, procedure.getSum());
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        short[] set = hash._set;
        byte[] states = hash._states;
        short no_entry_value = hash.getNoEntryValue();
        
        set[0] = 1;
        states[0] = 1;
        
        hash.removeAt(0);
        assertEquals(no_entry_value, set[0]);
        assertEquals(0, states[0]);
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        short[] set = hash._set;
        byte[] states = hash._states;
        short no_entry_value = hash.getNoEntryValue();
        
        set[0] = 1;
        states[0] = 1;
        
        int index = hash.index((short)1);
        assertEquals(0, index);
        
        index = hash.index((short)0);
        assertEquals(-1, index);
        
        index = hash.index(no_entry_value);
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        short[] set = hash._set;
        byte[] states = hash._states;
        short no_entry_value = hash.getNoEntryValue();
        
        set[0] = 1;
        set[1] = 2;
        set[2] = 3;
        states[0] = 1;
        states[1] = 1;
        states[2] = 1;
        
        int index = hash.indexRehashed((short)1, 0, 0, (byte)0);
        assertEquals(-1, index);
        
        index = hash.indexRehashed((short)1, 0, 0, (byte)1);
        assertEquals(0, index);
        
        index = hash.indexRehashed((short)2, 1, 1, (byte)1);
        assertEquals(1, index);
        
        index = hash.indexRehashed((short)3, 2, 2, (byte)1);
        assertEquals(2, index);
        
        index = hash.indexRehashed((short)0, 2, 2, (byte)0);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        
        int index = hash.insertKey((short)1);
        assertEquals(0, index);
        
        index = hash.insertKey((short)1);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        short[] set = hash._set;
        byte[] states = hash._states;
        short no_entry_value = hash.getNoEntryValue();
        
        set[0] = 1;
        states[0] = 1;
        set[1] = 2;
        states[1] = 1;
        set[2] = 3;
        states[2] = 1;
        set[3] = 4;
        states[3] = 0;
        
        int index = hash.insertKeyRehash((short)1, 0, 0, (byte)1);
        assertEquals(0, index);
        
        index = hash.insertKeyRehash((short)3, 0, 0, (byte)1);
        assertEquals(3, index);
        
        index = hash.insertKeyRehash((short)4, 0, 0, (byte)0);
        assertEquals(3, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        short[] set = hash._set;
        byte[] states = hash._states;
        
        hash.insertKeyAt(0, (short)1);
        assertEquals(1, set[0]);
        assertEquals(1, states[0]);
    }
}