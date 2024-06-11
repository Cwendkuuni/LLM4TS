import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.5f);
        assertTrue(hash.contains(10.5f));
        assertFalse(hash.contains(20.5f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.5f);
        hash.insertKey(20.5f);
        
        StringBuilder result = new StringBuilder();
        TFloatProcedure procedure = value -> {
            result.append(value);
            return true;
        };
        assertTrue(hash.forEach(procedure));
        assertEquals("10.520.5", result.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.5f);
        hash.removeAt(0);
        assertFalse(hash.contains(10.5f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.5f);
        assertEquals(0, hash.insertKey(10.5f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.5f);
        hash.insertKey(15.5f);
        assertEquals(1, hash.insertKey(15.5f));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashException() {
        TFloatCharHash hash = new TFloatCharHash(1, 0.5f);
        hash.insertKey(10.5f);
        hash.insertKey(15.5f);
        hash.insertKey(20.5f);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.XinsertKey(10.5f);
        assertEquals(0, hash.XinsertKey(10.5f));
    }
    
    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.5f);
        hash.insertKey(20.5f);
        
        // Not testing the actual writeExternal functionality, just checking for exceptions
        assertDoesNotThrow(() -> hash.writeExternal(null));
    }
    
    @Test
    public void testReadExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        
        // Not testing the actual readExternal functionality, just checking for exceptions
        assertDoesNotThrow(() -> hash.readExternal(null));
    }
}