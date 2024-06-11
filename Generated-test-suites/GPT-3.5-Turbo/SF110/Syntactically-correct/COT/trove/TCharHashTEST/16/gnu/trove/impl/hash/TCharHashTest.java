
import org.junit.Assert;
import org.junit.Test;

public class TCharHashTest {
    
    // Test Setup
    private void setup(TCharHash hash) {
        hash.setUp(10);
        hash.insertKey((char) 65);
        hash.insertKey((char) 66);
    }
    
    // Test Method 1
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        setup(hash);
        Assert.assertTrue(hash.contains((char) 65));
        Assert.assertFalse(hash.contains((char) 67));
    }
    
    // Test Method 2
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        setup(hash);
        
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return value > 65;
            }
        };
        
        Assert.assertFalse(hash.forEach(procedure));
    }
    
    // Test Method 3
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        setup(hash);
        
        int index = hash.index((char) 65);
        Assert.assertEquals(0, index);
        hash.removeAt(index);
        Assert.assertFalse(hash.contains((char) 65));
    }
    
    // Test Method 4
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        setup(hash);
        
        int index = hash.insertKey((char) 67);
        Assert.assertTrue(index >= 0);
        Assert.assertTrue(hash.contains((char) 67));
    }
    
    // Test Method 5
    @Test
    public void testGetNoEntryValue() {
        TCharHash hash = new TCharHash();
        setup(hash);
        
        char noEntryValue = hash.getNoEntryValue();
        Assert.assertEquals('\0', noEntryValue);
    }
    
    // Test Method 6
    @Test
    public void testSetUp() {
        TCharHash hash = new TCharHash();
        int capacity = hash.setUp(10);
        Assert.assertEquals(10, capacity);
        Assert.assertEquals(10, hash._set.length);
    }
    
    // Test Method 7
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        setup(hash);

        int index = hash.index((char) 65);
        Assert.assertEquals(0, index);
        
        index = hash.index((char) 67);
        Assert.assertEquals(-1, index);
    }
    
    // Test Method 8
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        setup(hash);
        
        int index = hash.indexRehashed((char) 65, 0, 1, (byte) 1);
        Assert.assertEquals(0, index);
        
        index = hash.indexRehashed((char) 67, 0, 1, (byte) 1);
        Assert.assertEquals(-1, index);
    }
    
    // Test Method 9
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        setup(hash);
        
        hash.insertKeyAt(0, (char) 67);
        Assert.assertTrue(hash.contains((char) 67));
    }
    
    // Test Method 10
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        setup(hash);
        
        int index = hash.insertKeyRehash((char) 67, 0, 1, (byte) 1);
        Assert.assertTrue(index >= 0);
        Assert.assertTrue(hash.contains((char) 67));
    }
}

