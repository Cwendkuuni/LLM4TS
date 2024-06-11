import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        // Test when value is present in the hash
        hash.insertKey(1.0f);
        assert(hash.contains(1.0f));
        // Test when value is not present in the hash
        assert(!hash.contains(2.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        // Test when hash is empty
        assert(hash.forEach((float val) -> false));
        // Test when hash is not empty
        hash.insertKey(1.0f);
        assert(hash.forEach((float val) -> val == 1.0f));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        // Test when removing at an index
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assert(!hash.contains(1.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        // Test when inserting a new key that doesn't exist in the hash
        int index = hash.insertKey(1.0f);
        assert(hash.contains(1.0f));
        assert(index >= 0);
        // Test when inserting a key that already exists in the hash
        index = hash.insertKey(1.0f);
        assert(index < 0);
    }
    
    @Test
    public void testXInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        // Test when inserting a new key that doesn't exist in the hash
        int index = hash.XinsertKey(1.0f);
        assert(hash.contains(1.0f));
        assert(index >= 0);
        // Test when inserting a key that already exists in the hash
        index = hash.XinsertKey(1.0f);
        assert(index < 0);
    }
    
    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        // Test writing to an output stream
        // ...
    }
    
    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        // Test reading from an input stream
        // ...
    }
}