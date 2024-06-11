package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testConstructorDefault() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(0, tCharHash.size());
        assertEquals(0, tCharHash.capacity());
        assertEquals(Character.MIN_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        TCharHash tCharHash = new TCharHash(5);
        assertEquals(0, tCharHash.size());
        assertEquals(5, tCharHash.capacity());
        assertEquals(Character.MIN_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TCharHash tCharHash = new TCharHash(5, 0.75f);
        assertEquals(0, tCharHash.size());
        assertEquals(5, tCharHash.capacity());
        assertEquals(Character.MIN_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        TCharHash tCharHash = new TCharHash(5, 0.75f, '-');
        assertEquals(0, tCharHash.size());
        assertEquals(5, tCharHash.capacity());
        assertEquals('-', tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        int newCapacity = tCharHash.setUp(10);
        assertEquals(0, tCharHash.size());
        assertEquals(10, newCapacity);
        assertEquals(Character.MIN_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('*');
        assertTrue(tCharHash.contains('*'));
        assertFalse(tCharHash.contains('-'));
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        
        assertTrue(tCharHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.removeAt(0);
        assertEquals(0, tCharHash.size());
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        assertEquals(0, tCharHash.index('a'));
        assertEquals(-1, tCharHash.index('b'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        int index = tCharHash.insertKey('a');
        assertEquals(0, index);
        index = tCharHash.insertKey('a');
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash(5, 0.75f, '-');
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        tCharHash.insertKey('d');
        int index = tCharHash.insertKeyRehash('e', 0, 1, (byte)0);
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKeyAt(0, 'a');
        assertEquals(1, tCharHash.size());
        assertEquals('a', tCharHash._set[0]);
        assertEquals(1, tCharHash._states[0]);
    }

}