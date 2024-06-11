import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int capacity = hash.setUp(5);
        assertEquals(5, capacity);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float key = 1.5f;
        hash.insertKey(key);
        
        assertTrue(hash.contains(key));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float key1 = 1.0f;
        float key2 = 2.0f;
        float key3 = 3.0f;
        hash.insertKey(key1);
        hash.insertKey(key2);
        hash.insertKey(key3);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.println(value);
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float key = 1.5f;
        hash.insertKey(key);
        
        hash.removeAt(0);
        
        assertFalse(hash.contains(key));
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float key = 1.5f;
        hash.insertKey(key);
        
        assertEquals(0, hash.index(key));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float key = 1.5f;
        int index = hash.insertKey(key);
        
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float key1 = 1.0f;
        float key2 = 2.0f;
        float key3 = 3.0f;
        hash.insertKey(key1);
        hash.insertKey(key2);
        hash.insertKey(key3);
        
        float key4 = 4.0f;
        int index = hash.insertKeyRehash(key4, 0, 0, (byte) 0);
        
        assertEquals(0, index);
        assertTrue(hash.contains(key4));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFullKeySet() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        float key1 = 1.0f;
        float key2 = 2.0f;
        float key3 = 3.0f;
        hash.insertKey(key1);
        hash.insertKey(key2);
        hash.insertKey(key3);
        
        float key4 = 4.0f;
        hash.insertKeyRehash(key4, 0, 0, (byte) 0);
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float key = 1.5f;
        hash.insertKeyAt(0, key);
        
        assertTrue(hash.contains(key));
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float key = 1.5f;
        int index = hash.XinsertKey(key);
        
        assertEquals(0, index);
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        
        hash.writeExternal(objectOutputStream);
        
        objectOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        
        assertTrue(bytes.length > 0);
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        
        hash.writeExternal(objectOutputStream);
        
        objectOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        
        hash.readExternal(objectInputStream);
        
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }
    
}