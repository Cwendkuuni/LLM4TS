import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        
        // Test with no elements in the hash
        boolean contains = hash.contains(5.0f);
        assertFalse(contains);
        
        // Test with elements in the hash
        hash.insertKey(5.0f);
        contains = hash.contains(5.0f);
        assertTrue(contains);
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        
        // Test with no elements in the hash
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return false;
            }
        };
        boolean result = hash.forEach(procedure);
        assertTrue(result);
        
        // Test with elements in the hash
        hash.insertKey(5.0f);
        result = hash.forEach(procedure);
        assertFalse(result);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        
        // Test with no elements in the hash
        hash.removeAt(0);
        
        // Test with elements in the hash
        hash.insertKey(5.0f);
        hash.removeAt(0);
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        
        // Test with no elements in the hash
        int index = hash.insertKey(5.0f);
        assertEquals(0, index);
        
        // Test with elements in the hash
        index = hash.insertKey(10.0f);
        assertEquals(1, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        
        // Test with no elements in the hash
        int index = hash.insertKeyRehash(5.0f, 0, 0, (byte)0);
        assertEquals(0, index);
        
        // Test with elements in the hash
        index = hash.insertKeyRehash(10.0f, 1, 1, (byte)0);
        assertEquals(1, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        
        // Test with no elements in the hash
        hash.insertKeyAt(0, 5.0f);
        
        // Test with elements in the hash
        hash.insertKeyAt(1, 10.0f);
    }

}