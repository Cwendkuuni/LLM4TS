import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    
    @Test
    public void testConstructorDefault() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.no_entry_value);
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TShortHash hash = new TShortHash(initialCapacity);
        assertEquals(initialCapacity, hash.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        TShortHash hash = new TShortHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, hash.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        short noEntryValue = -1;
        TShortHash hash = new TShortHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        short val = 5;
        assertFalse(hash.contains(val));
        hash.insertKey(val);
        assertTrue(hash.contains(val));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        TShortProcedure procedure = value -> {
            System.out.println(value);
            return true;
        };
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        short val = 10;
        hash.insertKey(val);
        int index = hash.index(val);
        hash.removeAt(index);
        assertFalse(hash.contains(val));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        short val = 5;
        hash.insertKey(val);
        assertTrue(hash.contains(val));
        int index = hash.index(val);
        assertEquals(index, hash.insertKey(val));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        short val = 5;
        hash.insertKey(val);
        assertTrue(hash.contains(val));
        int index = hash.index(val);
        assertEquals(index, hash.insertKeyRehash(val, index, 5, (byte) 1));
    }
}