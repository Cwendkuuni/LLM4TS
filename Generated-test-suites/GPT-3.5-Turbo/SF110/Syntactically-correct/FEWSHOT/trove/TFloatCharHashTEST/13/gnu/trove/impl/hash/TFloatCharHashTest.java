package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TFloatCharHashTest {
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assertEquals(true, hash.contains(1.0f));
        assertEquals(true, hash.contains(2.0f));
        assertEquals(true, hash.contains(3.0f));
        assertEquals(false, hash.contains(4.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index1 = hash.insertKey(1.0f);
        int index2 = hash.insertKey(2.0f);
        int index3 = hash.insertKey(3.0f);
        int index4 = hash.insertKey(4.0f);
        
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
        assertEquals(3, index4);
    }
    
    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float noEntryKey = hash.getNoEntryKey();
        
        assertEquals(0.0f, noEntryKey, 0.0f);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        char noEntryValue = hash.getNoEntryValue();
        
        assertEquals('\0', noEntryValue);
    }
}