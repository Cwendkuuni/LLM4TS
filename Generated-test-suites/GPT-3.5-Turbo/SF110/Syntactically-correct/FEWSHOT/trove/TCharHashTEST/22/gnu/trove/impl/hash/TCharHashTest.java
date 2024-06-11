package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.*;

public class TCharHashTest {
    @Test
    public void testConstructor_WithoutArguments() {
        TCharHash hash = new TCharHash();
        char[] expectedSet = new char[10];
        char expectedNoEntryValue = '\0';
        boolean expectedConsumeFreeSlot = false;
        
        assertArrayEquals(expectedSet, hash._set);
        assertEquals(expectedNoEntryValue, hash.no_entry_value);
        assertEquals(expectedConsumeFreeSlot, hash.consumeFreeSlot);
    }
    
    @Test
    public void testConstructor_WithInitialCapacity() {
        int initialCapacity = 20;
        TCharHash hash = new TCharHash(initialCapacity);
        char[] expectedSet = new char[initialCapacity];
        char expectedNoEntryValue = '\0';
        boolean expectedConsumeFreeSlot = false;
        
        assertArrayEquals(expectedSet, hash._set);
        assertEquals(expectedNoEntryValue, hash.no_entry_value);
        assertEquals(expectedConsumeFreeSlot, hash.consumeFreeSlot);
    }
    
    @Test
    public void testConstructor_WithInitialCapacityAndLoadFactor() {
        int initialCapacity = 20;
        float loadFactor = 0.75f;
        TCharHash hash = new TCharHash(initialCapacity, loadFactor);
        char[] expectedSet = new char[initialCapacity];
        char expectedNoEntryValue = '\0';
        boolean expectedConsumeFreeSlot = false;
        
        assertArrayEquals(expectedSet, hash._set);
        assertEquals(expectedNoEntryValue, hash.no_entry_value);
        assertEquals(expectedConsumeFreeSlot, hash.consumeFreeSlot);
    }
    
    @Test
    public void testConstructor_WithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 20;
        float loadFactor = 0.75f;
        char noEntryValue = '#';
        TCharHash hash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        char[] expectedSet = new char[initialCapacity];
        char expectedNoEntryValue = noEntryValue;
        boolean expectedConsumeFreeSlot = false;
        
        assertArrayEquals(expectedSet, hash._set);
        assertEquals(expectedNoEntryValue, hash.no_entry_value);
        assertEquals(expectedConsumeFreeSlot, hash.consumeFreeSlot);
    }
    
    @Test
    public void testGetNoEntryValue() {
        char noEntryValue = '#';
        TCharHash hash = new TCharHash();
        hash.no_entry_value = noEntryValue;
        
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 20;
        TCharHash hash = new TCharHash();
        int result = hash.setUp(initialCapacity);
        char[] expectedSet = new char[initialCapacity];
        
        assertArrayEquals(expectedSet, hash._set);
        assertEquals(initialCapacity, result);
    }
    
    @Test
    public void testContains_WhenValueExists() {
        TCharHash hash = new TCharHash();
        char[] set = {'a', 'b', 'c'};
        hash._set = set;
        
        assertTrue(hash.contains('b'));
    }
    
    @Test
    public void testContains_WhenValueDoesNotExist() {
        TCharHash hash = new TCharHash();
        char[] set = {'a', 'b', 'c'};
        hash._set = set;
        
        assertFalse(hash.contains('d'));
    }
    
    @Test
    public void testForEach_WhenProcedureReturnsTrue() {
        TCharHash hash = new TCharHash();
        char[] set = {'a', 'b', 'c'};
        hash._set = set;
        
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testForEach_WhenProcedureReturnsFalse() {
        TCharHash hash = new TCharHash();
        char[] set = {'a', 'b', 'c'};
        hash._set = set;
        
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return false;
            }
        };
        
        assertFalse(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        char[] set = {'a', 'b', 'c'};
        byte[] states = {1, 1, 1};
        hash._set = set;
        hash._states = states;
        
        int index = 1;
        hash.removeAt(index);
        
        assertEquals('\0', hash._set[index]);
        assertEquals(0, hash._states[index]);
    }
    
    @Test
    public void testIndex_WhenValueExists() {
        TCharHash hash = new TCharHash();
        char[] set = {'a', 'b', 'c'};
        byte[] states = {1, 1, 1};
        hash._set = set;
        hash._states = states;
        
        char val = 'b';
        int expectedIndex = 1;
        int result = hash.index(val);
        
        assertEquals(expectedIndex, result);
    }
    
    @Test
    public void testIndex_WhenValueDoesNotExist() {
        TCharHash hash = new TCharHash();
        char[] set = {'a', 'b', 'c'};
        byte[] states = {1, 1, 1};
        hash._set = set;
        hash._states = states;
        
        char val = 'd';
        int expectedIndex = -1;
        int result = hash.index(val);
        
        assertEquals(expectedIndex, result);
    }
    
    @Test
    public void testIndexRehashed_WhenValueExists() {
        TCharHash hash = new TCharHash();
        char[] set = {'a', 'b', 'c'};
        byte[] states = {1, 1, 1};
        hash._set = set;
        hash._states = states;
        
        char key = 'b';
        int index = 2;
        int hashVal = 12;
        byte state = 1;
        int expectedIndex = 1;
        int result = hash.indexRehashed(key, index, hashVal, state);
        
        assertEquals(expectedIndex, result);
    }
    
    @Test
    public void testIndexRehashed_WhenValueDoesNotExist() {
        TCharHash hash = new TCharHash();
        char[] set = {'a', 'b', 'c'};
        byte[] states = {1, 1, 1};
        hash._set = set;
        hash._states = states;
        
        char key = 'd';
        int index = 2;
        int hashVal = 12;
        byte state = 1;
        int expectedIndex = -1;
        int result = hash.indexRehashed(key, index, hashVal, state);
        
        assertEquals(expectedIndex, result);
    }
    
    @Test
    public void testInsertKey_WhenSlotIsFree() {
        TCharHash hash = new TCharHash();
        int hashVal = 12;
        hash._states = new byte[10];
        
        char val = 'a';
        int expectedIndex = 2;
        int result = hash.insertKey(val);
        
        assertEquals(expectedIndex, result);
        assertEquals(val, hash._set[expectedIndex]);
        assertEquals(1, hash._states[expectedIndex]);
    }
    
    @Test
    public void testInsertKey_WhenSlotIsOccupiedBySameValue() {
        TCharHash hash = new TCharHash();
        int hashVal = 12;
        hash._states = new byte[10];
        hash._set = new char[10];
        hash._set[2] = 'a';
        hash._states[2] = 1;
        
        char val = 'a';
        int expectedIndex = -3;
        int result = hash.insertKey(val);
        
        assertEquals(expectedIndex, result);
    }
    
    @Test
    public void testInsertKey_WhenSlotIsOccupiedByDifferentValue() {
        TCharHash hash = new TCharHash();
        int hashVal = 12;
        hash._states = new byte[10];
        hash._set = new char[10];
        hash._set[2] = 'b';
        hash._states[2] = 1;
        
        char val = 'a';
        int expectedIndex = -2;
        int result = hash.insertKey(val);
        
        assertEquals(expectedIndex, result);
    }
    
    @Test
    public void testInsertKeyRehash_WhenSlotIsFree() {
        TCharHash hash = new TCharHash();
        int hashVal = 12;
        hash._states = new byte[10];
        
        char val = 'a';
        int index = 2;
        byte state = 2;
        int expectedIndex = 3;
        int result = hash.insertKeyRehash(val, index, hashVal, state);
        
        assertEquals(expectedIndex, result);
        assertEquals(val, hash._set[expectedIndex]);
        assertEquals(1, hash._states[expectedIndex]);
    }
    
    @Test
    public void testInsertKeyRehash_WhenSlotIsOccupiedBySameValue() {
        TCharHash hash = new TCharHash();
        int hashVal = 12;
        hash._states = new byte[10];
        hash._set = new char[10];
        hash._set[2] = 'a';
        hash._states[2] = 1;
        
        char val = 'a';
        int index = 2;
        byte state = 2;
        int expectedIndex = -3;
        int result = hash.insertKeyRehash(val, index, hashVal, state);
        
        assertEquals(expectedIndex, result);
    }
    
    @Test
    public void testInsertKeyRehash_WhenSlotIsOccupiedByDifferentValue() {
        TCharHash hash = new TCharHash();
        int hashVal = 12;
        hash._states = new byte[10];
        hash._set = new char[10];
        hash._set[2] = 'b';
        hash._states[2] = 1;
        
        char val = 'a';
        int index = 2;
        byte state = 2;
        int expectedIndex = -3;
        int result = hash.insertKeyRehash(val, index, hashVal, state);
        
        assertEquals(expectedIndex, result);
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        int hashVal = 12;
        hash._states = new byte[10];
        
        char val = 'a';
        int index = 2;
        hash.insertKeyAt(index, val);
        
        assertEquals(val, hash._set[index]);
        assertEquals(1, hash._states[index]);
    }
}